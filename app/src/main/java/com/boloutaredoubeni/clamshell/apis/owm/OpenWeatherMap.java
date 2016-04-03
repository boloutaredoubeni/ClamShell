package com.boloutaredoubeni.clamshell.apis.owm;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class OpenWeatherMap {

  public static final String ENDPOINT = "https://openweathermap.org";

  private OpenWeatherMap() {}

  public interface DataReceiver {
    void onDataReceived(Payload payload);
  }

  public static class Payload {
    public String currentWeatherJson;
    public String forecastWeatherJson;
  }
}
