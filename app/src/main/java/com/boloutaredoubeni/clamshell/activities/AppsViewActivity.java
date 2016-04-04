package com.boloutaredoubeni.clamshell.activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.fragments.AppListFragment;
import com.boloutaredoubeni.clamshell.fragments.DashboardFragment;
import com.boloutaredoubeni.clamshell.fragments.SettingsFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public final class AppsViewActivity extends Activity {

  // FIXME: get location permissions

  private static final int DASHBOARD_FRAG = 0;
  private static final int APPLIST_FRAG = 1;
  private static final int SETTINGS_FRAG = 2;
  private static final int NUM_OF_TABS = 3;

  @Bind(R.id.app_view_container) ViewPager pager;

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
    pager.setCurrentItem(APPLIST_FRAG);
  }

  private final class SwipePageAdapter extends FragmentPagerAdapter {

    public SwipePageAdapter(@NonNull FragmentManager fm) { super(fm); }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
      case DASHBOARD_FRAG:
        Timber.d("Moving to dashboard");
        return new DashboardFragment();
      case APPLIST_FRAG:
        Timber.d("Moving to app list");
        return new AppListFragment();
      case SETTINGS_FRAG:
        Timber.d("Moving to settings");
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
