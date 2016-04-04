package com.boloutaredoubeni.clamshell.fragments;

import android.app.Fragment;
import android.database.Cursor;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.adapters.PhotoCarouselAdapter;
import com.boloutaredoubeni.clamshell.apis.owm.OpenWeatherMap;
import com.boloutaredoubeni.clamshell.apis.owm.WeatherService;
import com.boloutaredoubeni.clamshell.apis.owm.json.CurrentWeather;
import com.boloutaredoubeni.clamshell.apis.owm.json.Forecast;
import com.boloutaredoubeni.clamshell.models.UserPhoto;
import com.boloutaredoubeni.clamshell.secret.AppKeys;
import com.boloutaredoubeni.clamshell.viewmodels.WeatherDashCard;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class DashboardFragment
    extends Fragment implements GoogleApiClient.ConnectionCallbacks,
                                GoogleApiClient.OnConnectionFailedListener,
                                OpenWeatherMap.DataReceiver {

  private static final int MAX_NUM_PHOTOS = 10;

  private WeatherDashCard mWeatherCard;
  private PhotoCarouselAdapter mAdapter;

  @Bind(R.id.weather_data) TextView currentWeather;
  @Bind(R.id.photo_recycler)
  RecyclerView photoCarousel;

  private GoogleApiClient mClient;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    initGoogleApiClient();
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
    ButterKnife.bind(this, view);
    setupPhotoCarousel();
    return view;
  }

  @Override
  public void onStart() {
    mClient.connect();
    super.onStart();
  }

  @Override
  public void onStop() {
    mClient.disconnect();
    super.onStop();
  }

  @Override
  public void onResume() {
    super.onResume();
    getRecentPhotos();
  }

  @Override
  public void onConnected(Bundle bundle) {
    Location location =
        LocationServices.FusedLocationApi.getLastLocation(mClient);
    if (location != null) {
      Timber.d("Got the user location");
      mWeatherCard = WeatherDashCard.create(location);
      new GetWeatherTask(this).execute(mWeatherCard);
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
    Timber.d("The connection was suspended\t%d", i);
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Timber.e("The location connection failed:\t%d\t%s",
        connectionResult.getErrorCode(),
        connectionResult.getErrorMessage());
  }

  @Override
  public void onDataReceived(OpenWeatherMap.Payload payload) {
    Timber.d("Received payload");
    currentWeather.setText(payload.currentWeather.toString());
  }

  private void initGoogleApiClient() {
    if (mClient == null) {
      mClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
      Timber.i("Got the google api client");
    }
  }

  private void getRecentPhotos() {
    Cursor cursor = getActivity().getContentResolver().query(
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null,
        null);

    List<UserPhoto> photos  = new ArrayList<>();

    if (cursor != null) {
      cursor.moveToFirst();
      for (int i = 0; i < MAX_NUM_PHOTOS; ++i) {
        cursor.moveToPosition(i);
        String url = cursor.getString(UserPhoto.COL_URL);
        String name = cursor.getString(UserPhoto.COL_NAME);

        photos.add(UserPhoto.create(url, name));
      }
    }

    mAdapter.clearThenAddAll(photos);
  }

  private void setupPhotoCarousel() {
    photoCarousel.setLayoutManager(new LinearLayoutManager(getActivity()));
    mAdapter = new PhotoCarouselAdapter(getActivity(), new ArrayList<>());
    photoCarousel.setAdapter(mAdapter);
  }

  private static class GetWeatherTask
      extends AsyncTask<WeatherDashCard, Void, OpenWeatherMap.Payload> {

    private final OpenWeatherMap.DataReceiver mReceiver;

    private GetWeatherTask(OpenWeatherMap.DataReceiver receiver) {
      mReceiver = receiver;
    }

    @Override
    protected OpenWeatherMap.Payload doInBackground(WeatherDashCard... params) {
      Timber.i("Retrieving weather data");
      final OpenWeatherMap.Payload payload = new OpenWeatherMap.Payload();
      WeatherDashCard card = params[0];
      double latitude = card.latitude;
      double longitude = card.longitude;
      Retrofit retrofit =
          new Retrofit.Builder()
              .baseUrl(OpenWeatherMap.ENDPOINT)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
      WeatherService service = retrofit.create(WeatherService.class);
      Call<CurrentWeather> currentWeatherCall =
          service.getCurrentWeather(latitude, longitude, AppKeys.OWM_KEY);
      Call<Forecast> forecastCall =
          service.getWeeklyForcecast(latitude, longitude, AppKeys.OWM_KEY);

      Timber.d(currentWeatherCall.request().url().toString());
      Timber.d(forecastCall.request().url().toString());
      try {
        retrofit2.Response<CurrentWeather> currentWeatherResponse =
            currentWeatherCall.execute();
        payload.currentWeather = currentWeatherResponse.body();
        retrofit2.Response<Forecast> forecastResponse = forecastCall.execute();
        payload.forecast = forecastResponse.body();
        return payload;
      } catch (IOException e) {
        Timber.e(e.getMessage());
      }
      return null;
    }

    @Override
    protected void onPostExecute(OpenWeatherMap.Payload payload) {
      super.onPostExecute(payload);
      mReceiver.onDataReceived(payload);
    }
  }
}
