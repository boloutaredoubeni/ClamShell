package com.boloutaredoubeni.clamshell.viewmodels;

import android.location.Location;
import android.os.AsyncTask;

import com.boloutaredoubeni.clamshell.apis.owm.OpenWeatherMap;
import com.boloutaredoubeni.clamshell.apis.owm.WeatherService;
import com.boloutaredoubeni.clamshell.secret.AppKeys;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class WeatherDashCard {

  private Location mLocation;

  public static WeatherDashCard create(Location location) {
    return new WeatherDashCard(location);
  }

  private WeatherDashCard(Location location) { mLocation = location; }

  public void requestWeatherData() {
    new GetWeatherTask(this).execute(mLocation);
  }

  public static class GetWeatherTask extends AsyncTask<Location, Void, List> {

    private WeatherDashCard mCard;

    private GetWeatherTask(WeatherDashCard card) {
      mCard = card;
    }

    @Override
    protected List doInBackground(Location... params) {
      Location userLocation = params[0];
      double latitude = userLocation.getLatitude();
      double longitude = userLocation.getLongitude();
      Retrofit retrofit = new Retrofit.Builder().baseUrl(OpenWeatherMap.ENDPOINT).build();
      WeatherService service = retrofit.create(WeatherService.class);
      Call currentWeatherResponse = service.getCurrentWeather(latitude, longitude, AppKeys.OWM_KEY);
      Call forecastResponse = service.getWeeklyForcecast(latitude, longitude, AppKeys.OWM_KEY);
      try {
        currentWeatherResponse.execute();
        forecastResponse.execute();
      } catch (IOException e) {
        Timber.e(e.getMessage());
      }
      return null;
    }
  }
}
