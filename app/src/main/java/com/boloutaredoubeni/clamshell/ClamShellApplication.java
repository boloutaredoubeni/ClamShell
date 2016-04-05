package com.boloutaredoubeni.clamshell;

import android.app.Application;

import butterknife.ButterKnife;
import timber.log.Timber;

public final class ClamShellApplication extends Application {


  @Override
  public void onCreate() {
    super.onCreate();

    initTimber();
    initButterKnife();
  }

  private void initTimber() { Timber.plant(new Timber.DebugTree()); }

  private void initButterKnife() { ButterKnife.setDebug(BuildConfig.DEBUG); }

}