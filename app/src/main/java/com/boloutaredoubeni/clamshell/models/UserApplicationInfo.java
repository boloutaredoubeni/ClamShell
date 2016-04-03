package com.boloutaredoubeni.clamshell.models;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;

import java.text.Collator;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public final class UserApplicationInfo
    implements Comparable<UserApplicationInfo> {

  private final Drawable mIcon;
  private final String mAppName;
  private final String mPackage;

  /**
   * @param context Only needed to get the package manager. A reference to
   * context is NEVER stored here
   * @param info Provided by the intent
   * @return An object with information about a launchable application
   */
  public static UserApplicationInfo createFrom(Context context,
                                               ResolveInfo info) {
    PackageManager pm = context.getPackageManager();
    Drawable icon = info.activityInfo.loadIcon(pm);
    String appName = info.activityInfo.loadLabel(pm).toString();
    String packageName = info.activityInfo.packageName;
    return new UserApplicationInfo(icon, appName, packageName);
  }

  private UserApplicationInfo(Drawable icon, String appName,
                              String packageName) {
    mIcon = icon;
    mAppName = appName;
    mPackage = packageName;
  }

  public Drawable getIcon() { return mIcon; }

  public String getAppName() { return mAppName; }

  public String getPackage() { return mPackage; }

  @Override
  public int compareTo(@NonNull UserApplicationInfo another) {
    final Collator collator = Collator.getInstance();
    return collator.compare(this.getAppName(), another.getAppName());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    UserApplicationInfo that = (UserApplicationInfo)o;

    return mIcon.equals(that.mIcon) && mAppName.equals(that.mAppName) &&
        mPackage.equals(that.mPackage);
  }

  @Override
  public int hashCode() {
    int result = mIcon.hashCode();
    result = 31 * result + mAppName.hashCode();
    result = 31 * result + mPackage.hashCode();
    return result;
  }
}
