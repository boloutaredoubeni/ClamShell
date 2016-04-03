package com.boloutaredoubeni.clamshell.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.apis.owm.OpenWeatherMap;
import com.boloutaredoubeni.clamshell.apis.owm.WeatherService;
import com.boloutaredoubeni.clamshell.secret.AppKeys;
import com.boloutaredoubeni.clamshell.viewmodels.WeatherDashCard;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class DashboardFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener, OpenWeatherMap.DataReceiver {

  private WeatherDashCard mWeatherCard;

  @Bind(R.id.weather_data)
  TextView currentWeather;

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
    View view =  inflater.inflate(R.layout.fragment_dummy, container, false);
    ButterKnife.bind(this, view);
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

  @Override
  public void onConnected(Bundle bundle) {
    Location location = LocationServices.FusedLocationApi.getLastLocation(mClient);
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
    currentWeather.setText(payload.currentWeatherJson);
  }

  private static class GetWeatherTask extends AsyncTask<WeatherDashCard, Void, OpenWeatherMap.Payload> {

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
      Retrofit retrofit = new Retrofit.Builder().baseUrl(OpenWeatherMap.ENDPOINT).build();
      WeatherService service = retrofit.create(WeatherService.class);
      Call currentWeatherResponse = service.getCurrentWeather(latitude, longitude, AppKeys.OWM_KEY);
      Call forecastResponse = service.getWeeklyForcecast(latitude, longitude, AppKeys.OWM_KEY);
      try {
        payload.currentWeatherJson = currentWeatherResponse.execute().body().toString();
        payload.forecastWeatherJson = forecastResponse.execute().body().toString();
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
