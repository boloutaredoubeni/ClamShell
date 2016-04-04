package com.boloutaredoubeni.clamshell.models;

import android.support.annotation.NonNull;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class UserPhoto {
  public static final int COL_URL = 1;
  public static final int COL_NAME = 3;

  public final String url;
  public final String name;

  public static UserPhoto create(@NonNull String url, @NonNull String name) {
    return new UserPhoto(url, name);
  }

  private UserPhoto(String url, String name) {
    this.url = url;
    this.name = name;
  }
}
