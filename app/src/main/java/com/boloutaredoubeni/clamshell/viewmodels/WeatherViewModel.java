package com.boloutaredoubeni.clamshell.viewmodels;

import android.location.Location;
import android.support.annotation.NonNull;

import com.boloutaredoubeni.clamshell.apis.owm.models.Forecast;
import com.boloutaredoubeni.clamshell.models.Weather;
import com.boloutaredoubeni.clamshell.utils.Day;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class WeatherViewModel {

  private final String DEGREES = "°F";

  public final double latitude;
  public final double longitude;

  private String city;
  private Integer currentTemp;

  private Weather currentWeather;
  private int lo;
  private int hi;
  private String description;
  private String icon;
  private Day day;

  private OnWeatherChangeListener mListener;

  public static WeatherViewModel create(@NonNull Location location) {
    return new WeatherViewModel(location);
  }

  private WeatherViewModel(Location location) {
    latitude = location.getLatitude();
    longitude = location.getLongitude();
  }

  public void setOnWeatherChangeListener(OnWeatherChangeListener listener) {
    mListener = listener;
  }

  public void bind(Weather weather) {
    currentWeather = weather;
    city = weather.city;
    lo = Weather.convertToFahrenheit(weather.lo);
    hi = Weather.convertToFahrenheit(weather.hi);
    description = weather.description;
    currentTemp = weather.getCurrentTemp() != null
                      ? Weather.convertToFahrenheit(weather.getCurrentTemp())
                      : null;
    icon = weather.icon;
    day = weather.day;
    if (mListener != null) {
      mListener.weatherChanged(this);
    }
  }

  public static List<WeatherViewModel> getCards(Forecast list,
                                                Location location) {
    List<WeatherViewModel> cards = new ArrayList<>();
    List<Weather> forecast = Weather.getWeeklyForecastFrom(list);
    for (Weather weather : forecast) {
      WeatherViewModel card = WeatherViewModel.create(location);
      card.bind(weather);
      cards.add(card);
    }
    return cards;
  }

  public String getCity() { return city; }

  public String getCurrentTemp() { return Integer.toString(currentTemp) + DEGREES; }

  public String getLo() { return Integer.toString(lo) + DEGREES; }

  public String getHi() { return Integer.toString(hi) + DEGREES; }

  public String getDescription() { return description; }

  public String getIcon() { return icon; }

  public String getDay() { return day != null ? day.name() : "Anyday now"; }

  public interface OnWeatherChangeListener {
    void weatherChanged(WeatherViewModel viewmodel);
    void forecastChanged(List<WeatherViewModel> viewModels);
  }
}
