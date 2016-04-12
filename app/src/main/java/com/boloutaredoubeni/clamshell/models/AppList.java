package com.boloutaredoubeni.clamshell.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public final class AppList {

  private List<App> apps = new ArrayList<>();
  private List<App> originalApps = new ArrayList<>();

  public AppList() {
  }

  public void add(App app) {
    apps.add(app);
    originalApps.add(app);
  }

  public void addAll(List<App> apps) {
    this.apps = apps;
  }

  public App get(int position) {
    return apps.get(position);
  }

  public boolean contains(App app) {
    if (apps != null && originalApps != null) {
      for (App app_ : originalApps) {
        if (app.equals(app_)) {
          return true;
        }
      }
    }
    return false;
  }

  public void clear() {
    apps.clear();
    originalApps.clear();
  }

  public void reinit(AppList appList) {
    Collections.sort(appList.apps);
    Collections.sort(appList.originalApps);
    originalApps = appList.apps;
    this.apps = originalApps;
  }

  public void sort() {
    Collections.sort(apps);
    originalApps = apps;
  }

  public int size() {
    return apps.size();
  }

  public List<App> originals() {
    return originalApps;
  }
}
