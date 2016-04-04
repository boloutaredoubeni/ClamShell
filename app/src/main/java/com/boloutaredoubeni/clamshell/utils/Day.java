package com.boloutaredoubeni.clamshell.utils;

import java.util.Calendar;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public enum Day {

  Sunday,
  Monday,
  Tuesday,
  Wednesday,
  Thursday,
  Friday,
  Saturday;

  public static Day convertFromUnixMill(long unix) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(unix);
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    switch (day) {
    case 0:
      return Sunday;
    case 1:
      return Monday;
    case 2:
      return Tuesday;
    case 3:
      return Wednesday;
    case 4:
      return Thursday;
    case 5:
      return Friday;
    case 6:
      return Saturday;
    default:
      return null;
    }
  }
}
