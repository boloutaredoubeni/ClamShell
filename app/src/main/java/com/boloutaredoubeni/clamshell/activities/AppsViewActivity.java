package com.boloutaredoubeni.clamshell.activities;

import android.app.Activity;
import android.os.Bundle;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.fragments.AppListFragment;

public final class AppsViewActivity extends Activity {


  // FIXME: 4/2/16 add a search bar
  // FIXME: 4/2/16 Make sure the are in some kind of order


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_view);

    getFragmentManager().beginTransaction().add(R.id.app_view_container, new AppListFragment()).commit();
  }


  /**
   *
   * @return A list of applications that can be opened via a launcher
   */

}
