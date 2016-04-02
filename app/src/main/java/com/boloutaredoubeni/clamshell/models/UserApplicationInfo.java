package com.boloutaredoubeni.clamshell.models;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public final class UserApplicationInfo {

  private final Drawable mIcon;
  private final String mAppName;
  private final String mPackage;

  /**
   * @param context Only needed to get the package manager. A reference to context is NEVER stored here
   * @param info Provided by the intent
   * @return An object with information about a launchable application
   */
  public static UserApplicationInfo createFrom(Context context, ResolveInfo info) {
    PackageManager pm = context.getPackageManager();
    Drawable icon = info.activityInfo.loadIcon(pm);
    String appName = info.activityInfo.loadLabel(pm).toString();
    String packageName = info.activityInfo.packageName;
    return new UserApplicationInfo(icon, appName, packageName);
  }

  private UserApplicationInfo(Drawable icon, String appName, String packageName) {
    mIcon = icon;
    mAppName = appName;
    mPackage = packageName;
  }

  public Drawable getIcon() { return mIcon; }

  public String getAppName() { return mAppName; }

  public String getPackage() { return mPackage; }

}
