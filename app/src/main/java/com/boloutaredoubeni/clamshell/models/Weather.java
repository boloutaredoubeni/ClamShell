package com.boloutaredoubeni.clamshell.models;

import android.support.annotation.NonNull;

import com.boloutaredoubeni.clamshell.apis.owm.models.CurrentWeather;
import com.boloutaredoubeni.clamshell.apis.owm.models.Forecast;
import com.boloutaredoubeni.clamshell.apis.owm.models.WeatherReport;
import com.boloutaredoubeni.clamshell.utils.Day;

import java.util.ArrayList;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class Weather {

  public final String city;
  private final Double currentTemp;
  public final double lo;
  public final double hi;
  public final String description;
  public final Day day;
  public final String icon;

  public static Weather createFrom(@NonNull CurrentWeather current) {
    return new Weather(current.name, current.main.temp, current.main.temp_min,
                       current.main.temp_max, current.weather[0].main,
                       current.dt, current.weather[0].icon);
  }

  public static List<Weather>
  getWeeklyForecastFrom(@NonNull Forecast forecast) {
    // FIXME: consolidate similar dates and calc hi/lo
    List<Weather> weeklyForecast = new ArrayList<>();
    String city = forecast.city.name;
    for (WeatherReport weather : forecast.list) {
      weeklyForecast.add(Weather.createFrom(weather, city));
    }
    return weeklyForecast;
  }

  private static Weather createFrom(@NonNull WeatherReport report,
                                    @NonNull String city) {
    return new Weather(city, null, report.main.temp_min, report.main.temp_max,
                       report.weather[0].main, report.dt,
                       report.weather[0].icon);
  }

  public static int convertToFahrenheit(double kelvin) {
    return (int)(1.8 * (kelvin - 273) + 32);
  }

  private Weather(String city, Double currentTemp, double lo, double hi,
                  String description, long unixDate, String icon) {
    this.city = city;
    this.currentTemp = currentTemp;
    this.lo = lo;
    this.hi = hi;
    this.description = description;
    this.day = Day.convertFromUnixMill(unixDate);
    this.icon = icon;
  }

  public Double getCurrentTemp() { return currentTemp; }
}
