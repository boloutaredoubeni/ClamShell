package com.boloutaredoubeni.clamshell.apis.owm;

import com.boloutaredoubeni.clamshell.apis.owm.models.CurrentWeather;
import com.boloutaredoubeni.clamshell.apis.owm.models.Forecast;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class OpenWeatherMap {

  public static final String ENDPOINT = "https://openweathermap.org";

  private OpenWeatherMap() {}

  public interface DataReceiver { void onDataReceived(Payload payload); }

  public static class Payload {
    public CurrentWeather currentWeather;
    public Forecast forecast;
  }
}
