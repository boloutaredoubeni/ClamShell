package com.boloutaredoubeni.clamshell.activities;

import android.app.Activity;
import android.os.Bundle;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.fragments.AppListFragment;

public final class AppsViewActivity extends Activity {

  // FIXME: show the status bar

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_view);

    getFragmentManager()
        .beginTransaction()
        .add(R.id.app_view_container, new AppListFragment())
        .commit();
  }
}
