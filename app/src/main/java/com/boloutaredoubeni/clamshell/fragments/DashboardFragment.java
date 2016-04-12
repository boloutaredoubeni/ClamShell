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
import android.widget.ImageView;
import android.widget.TextView;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.adapters.ForecastAdapter;
import com.boloutaredoubeni.clamshell.adapters.PhotoCarouselAdapter;
import com.boloutaredoubeni.clamshell.apis.owm.OpenWeatherMap;
import com.boloutaredoubeni.clamshell.apis.owm.WeatherService;
import com.boloutaredoubeni.clamshell.apis.owm.models.CurrentWeather;
import com.boloutaredoubeni.clamshell.apis.owm.models.Forecast;
import com.boloutaredoubeni.clamshell.models.UserPhoto;
import com.boloutaredoubeni.clamshell.models.Weather;
import com.boloutaredoubeni.clamshell.secret.AppKeys;
import com.boloutaredoubeni.clamshell.viewmodels.WeatherViewModel;
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
                                OpenWeatherMap.DataReceiver,
                                WeatherViewModel.OnWeatherChangeListener {

  public static final int MAX_NUM_PHOTOS = 10;

  private WeatherViewModel mWeatherCard;
  private GoogleApiClient mClient;
  private ForecastAdapter mForecastAdapter;
  private Location mLocation;

  @Bind(R.id.forecast_list) RecyclerView forecastRecycler;
  @Bind(R.id.city_name) TextView city;
  @Bind(R.id.current_temp) TextView currentTemp;
  @Bind(R.id.current_weather_description) TextView weatherDescription;

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
    setupWeatherView();
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
  }

  @Override
  public void onConnected(Bundle bundle) {
    mLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
    if (mLocation != null) {
      Timber.d("Got the user location");
      mWeatherCard = WeatherViewModel.create(mLocation);
      mWeatherCard.setOnWeatherChangeListener(this);
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
    // FIXME: error handle
    Timber.d("Received payload");
    if (payload == null) {
      return;
    }
    mWeatherCard.bind(Weather.createFrom(payload.currentWeather));
    //    mWeatherCard.bind(Weather.getWeeklyForecastFrom(payload.forecast));
    //    currentWeather.setText(payload.currentWeather != null
    //                               ? payload.currentWeather.toString()
    //                               : "Error!");
    List<WeatherViewModel> models =
        WeatherViewModel.getCards(payload.forecast, mLocation);
    mForecastAdapter.clearAndAddAll(models);
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

  private void setupWeatherView() {
    LinearLayoutManager llm = new LinearLayoutManager(getActivity());
    llm.setOrientation(LinearLayoutManager.HORIZONTAL);
    forecastRecycler.setLayoutManager(llm);
    mForecastAdapter = new ForecastAdapter(new ArrayList<>());
    forecastRecycler.setAdapter(mForecastAdapter);
    Timber.i("Got the forecast");
  }

  @Override
  public void weatherChanged(WeatherViewModel viewmodel) {
    city.setText(viewmodel.getCity());
    currentTemp.setText(viewmodel.getCurrentTemp());
    //    dayOfWeek.setText(viewmodel.getDay());
    weatherDescription.setText(viewmodel.getDescription());
    //    highTemperature.setText(viewmodel.getHi());
    //    lowTemperature.setText(viewmodel.getLo());
    // icon.setImageURI();
    Timber.i("The weather has changed");
  }

  @Override
  public void forecastChanged(List<WeatherViewModel> viewModels) {
    Timber.e("forecastChanged Not implemented!!");
  }

  private static class GetWeatherTask
      extends AsyncTask<WeatherViewModel, Void, OpenWeatherMap.Payload> {

    private final OpenWeatherMap.DataReceiver mReceiver;

    private GetWeatherTask(OpenWeatherMap.DataReceiver receiver) {
      mReceiver = receiver;
    }

    @Override
    protected OpenWeatherMap.Payload
        doInBackground(WeatherViewModel... params) {
      Timber.i("Retrieving weather data");
      final OpenWeatherMap.Payload payload = new OpenWeatherMap.Payload();
      WeatherViewModel card = params[0];
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
        if (payload.forecast == null || payload.currentWeather == null) {
          Timber.e(
              "There is either no internet or the server did not respond properly");
        }
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
