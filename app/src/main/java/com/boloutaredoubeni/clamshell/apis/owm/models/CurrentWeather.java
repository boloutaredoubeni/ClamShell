package com.boloutaredoubeni.clamshell.apis.owm.models;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class CurrentWeather {
  public int id;
  public String name;
  public LocationMeta sys;
  public long dt;
  public Clouds clouds;
  public Wind wind;
  public WeatherMain main;
  public String base;
  public WeatherDescription[] weather;
  public Coordinates coord;
}
