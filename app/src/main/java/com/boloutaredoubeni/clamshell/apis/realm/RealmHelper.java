package com.boloutaredoubeni.clamshell.apis.realm;

import android.content.Context;

import com.boloutaredoubeni.clamshell.apis.realm.models.AppRef;
import com.boloutaredoubeni.clamshell.models.UserApplicationInfo;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class RealmHelper {

  private static RealmHelper INSTANCE;

  private final RealmConfiguration mAppCacheConfig;
  private final RealmConfiguration mWeatherConfig;

  public static RealmHelper getInstance(Context context) {
    if (INSTANCE == null) {
      INSTANCE = new RealmHelper(context);
    }
    return INSTANCE;
  }

  private RealmHelper(Context context) {
    mAppCacheConfig = new RealmConfiguration.Builder(context).name("app-cache.realm").build();
    mWeatherConfig = new RealmConfiguration.Builder(context).name("weather-cache.realm").build();
  }

  public void cache(UserApplicationInfo app) {
    Realm realm = Realm.getInstance(mAppCacheConfig);
    AppRef ref = new AppRef();
    ref.setName(app.getAppName());
    ref.setPackageName(app.getPackage());
    realm.beginTransaction();
    realm.copyToRealmOrUpdate(ref);
    realm.commitTransaction();
    realm.close();
  }
}
