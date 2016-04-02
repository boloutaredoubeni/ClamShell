package com.boloutaredoubeni.clamshell;

import android.app.Application;

import timber.log.Timber;

public class ClamShellApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();

    initTimber();
  }

  private void initTimber() {
    Timber.plant(new Timber.DebugTree());
  }
}