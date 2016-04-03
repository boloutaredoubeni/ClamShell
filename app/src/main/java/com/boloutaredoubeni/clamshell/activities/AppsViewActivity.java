package com.boloutaredoubeni.clamshell.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.fragments.AppListFragment;
import com.boloutaredoubeni.clamshell.fragments.DashboardFragment;
import com.boloutaredoubeni.clamshell.fragments.SettingsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public final class AppsViewActivity extends Activity {

  private static final int NUM_OF_TABS = 3;

  @Bind(R.id.app_view_container)
  ViewPager pager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_view);
    ButterKnife.bind(this);
    setupView();
  }

  private void setupView() {
    SwipePageAdapter adapter = new SwipePageAdapter(getFragmentManager());
    pager.setAdapter(adapter);
  }

  private final class SwipePageAdapter extends FragmentPagerAdapter {


    public SwipePageAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
        case 0:
          // TODO: get the dashboard fragment
          return new DashboardFragment();
        case 1:
          return new AppListFragment();
        case 2:
          // TODO: get Settings fragment
          return new SettingsFragment();
      }
      return null;
    }

    @Override
    public int getCount() {
      return NUM_OF_TABS;
    }

  }
}
