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

  // FIXME This is so wrong its not even funny, the question is who is to blame
  public static Day convertFromUnixMill(long unix) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(unix);
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    switch (day) {
    case 1:
      return Sunday;
    case 2:
      return Monday;
    case 3:
      return Tuesday;
    case 4:
      return Wednesday;
    case 5:
      return Thursday;
    case 6:
      return Friday;
    case 7:
      return Saturday;
    default:
      return null;
    }
  }
}
