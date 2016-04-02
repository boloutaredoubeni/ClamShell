package com.boloutaredoubeni.clamshell.models;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.util.Collections;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public final class UserApplicationInfo implements Comparable<UserApplicationInfo> {

  private final Drawable mIcon;
  private final String mAppName;
  private final String mPackage;


  public static void sortApps(List<UserApplicationInfo> apps) {
    Collections.sort(apps);
  }

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

  @Override
  public int compareTo(@NonNull UserApplicationInfo another) {
    if (equals(another)){
      return 0;
    } else if ((getAppName().compareTo(another.getAppName()) < 0) || (getPackage().compareTo(another.getPackage()) < 0)) {
      return -1;
    }
    return 1;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    UserApplicationInfo that = (UserApplicationInfo) o;

    return mIcon.equals(that.mIcon) && mAppName.equals(that.mAppName) && mPackage.equals(that.mPackage);

  }

  @Override
  public int hashCode() {
    int result = mIcon.hashCode();
    result = 31 * result + mAppName.hashCode();
    result = 31 * result + mPackage.hashCode();
    return result;
  }
}
