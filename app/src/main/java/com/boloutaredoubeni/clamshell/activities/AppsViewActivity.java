package com.boloutaredoubeni.clamshell.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;

import com.boloutaredoubeni.clamshell.R;

import java.util.ArrayList;
import java.util.List;

public class AppsViewActivity extends Activity {


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_view);

    listUserApps();
  }

  /**
   *
   * @return A list of applications that can be opened via a launcher
   */
  private List listUserApps() {
    PackageManager manager = getPackageManager();
    List apps = new ArrayList();
    Intent i = new Intent(Intent.ACTION_MAIN, null);
    i.addCategory(Intent.CATEGORY_LAUNCHER);

    List<ResolveInfo> launchableApps = manager.queryIntentActivities(i, 0);
    for (ResolveInfo info : launchableApps) {
      System.out.println("");
    }
    return apps;
  }
}
