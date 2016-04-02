package com.boloutaredoubeni.clamshell.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.adapters.AppListAdapter;
import com.boloutaredoubeni.clamshell.models.UserApplicationInfo;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public final class AppsViewActivity extends Activity {

  // FIXME: Don't let me rotate
  // FIXME: 4/2/16 add a search bar
  // FIXME: 4/2/16 Make sure the are in some kind of order


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_view);

    loadView();
  }


  private void loadView() {

    RecyclerView recyclerView = (RecyclerView) findViewById(R.id.app_list);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new LinearLayoutManager(this));
    AppListAdapter adapter = new AppListAdapter(this, listUserApps());
    recyclerView.setAdapter(adapter);
  }

  /**
   *
   * @return A list of applications that can be opened via a launcher
   */
  private List<UserApplicationInfo> listUserApps() {
    PackageManager manager = getPackageManager();
    List<UserApplicationInfo> apps = new ArrayList<>();
    Intent i = new Intent(Intent.ACTION_MAIN, null);
    i.addCategory(Intent.CATEGORY_LAUNCHER);

    List<ResolveInfo> launchableApps = manager.queryIntentActivities(i, 0);
    for (ResolveInfo info : launchableApps) {
      UserApplicationInfo app = UserApplicationInfo.createFrom(this, info);
      apps.add(app);
    }
    Timber.i("Found %d apps", apps.size());
    return apps;
  }
}
