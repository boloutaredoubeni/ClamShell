package com.boloutaredoubeni.clamshell.viewmodels;

import android.location.Location;

import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class WeatherDashCard {

  // Fixme: bind to view and card using rx-observers

  public final double latitude;
  public final double longitude;

  public static WeatherDashCard create(Location location) {
    return new WeatherDashCard(location);
  }

  private void bind(Object object) {}

  private void bind(List list) {}

  private WeatherDashCard(Location location) {
    latitude = location.getLatitude();
    longitude = location.getLongitude();
  }
}
