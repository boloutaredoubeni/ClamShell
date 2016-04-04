package com.boloutaredoubeni.clamshell.viewmodels;

import android.location.Location;
import android.support.annotation.NonNull;

import com.boloutaredoubeni.clamshell.models.Weather;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class WeatherDashCard {

  // Fixme: bind to view and card using rx-observers

  public final double latitude;
  public final double longitude;

  private Weather currentWeather;
  private List<Weather> forecast = new ArrayList<>();

  public static WeatherDashCard create(@NonNull Location location) {
    return new WeatherDashCard(location);
  }


  private WeatherDashCard(Location location) {
    latitude = location.getLatitude();
    longitude = location.getLongitude();
  }

  public void bind(Weather weather) {
    currentWeather = weather;
  }

  public void bind(List<Weather> list) {
    forecast.clear();
    forecast.addAll(list);
  }

  public Weather getCurrentWeather() {
    return currentWeather;
  }

  public List<Weather> getForecast() {
    return forecast;
  }
}
