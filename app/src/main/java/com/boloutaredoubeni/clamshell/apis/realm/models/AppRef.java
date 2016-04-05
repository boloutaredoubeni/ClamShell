package com.boloutaredoubeni.clamshell.apis.realm.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class AppRef extends RealmObject {

  private String name;
  @PrimaryKey
  private String packageName; /* I hope this works */

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AppRef appRef = (AppRef) o;

    if (!name.equals(appRef.name)) return false;
    return packageName.equals(appRef.packageName);

  }

  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + packageName.hashCode();
    return result;
  }
}
