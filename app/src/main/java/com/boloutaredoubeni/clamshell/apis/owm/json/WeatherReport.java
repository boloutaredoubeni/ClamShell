package com.boloutaredoubeni.clamshell.apis.owm.json;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class WeatherReport {
  public long dt;
  public String dt_txt;
  public WeatherMain main;
  public WeatherDescription[] weather;
  public Wind wind;
  public Clouds clouds;
  public Rain rain;
  public Snow snow;
}
