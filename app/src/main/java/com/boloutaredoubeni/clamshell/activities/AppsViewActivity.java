package com.boloutaredoubeni.clamshell.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.fragments.AppListFragment;
import com.boloutaredoubeni.clamshell.fragments.DashboardFragment;
import com.boloutaredoubeni.clamshell.fragments.SettingsFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public final class AppsViewActivity
    extends Activity implements GoogleApiClient.ConnectionCallbacks,
                                GoogleApiClient.OnConnectionFailedListener {

  // FIXME: get location permissions

  private static final int DASHBOARD_FRAG = 0;
  private static final int APPLIST_FRAG = 1;
  private static final int SETTINGS_FRAG = 2;
  private static final int NUM_OF_TABS = 3;

  @Bind(R.id.app_view_container) ViewPager pager;

  private GoogleApiClient mClient;
  private Location mLastLocation;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_view);
    ButterKnife.bind(this);
    setupView();
    getUserLocation();
  }

  @Override
  protected void onStart() {
    mClient.connect();
    super.onStart();
  }

  @Override
  protected void onStop() {
    mClient.disconnect();
    super.onStop();
  }

  @Override
  public void onConnected(Bundle bundle) {
    mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mClient);
    if (mLastLocation != null) {
      Timber.d("Got the user location");
      // TODO: update the weather ui
    }
  }

  @Override
  public void onConnectionSuspended(int i) {
    Timber.d("The connection was suspended\t%d", i);
  }

  private void getUserLocation() {
    if (mClient == null) {
      mClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
      Timber.i("Got the google api client");
    }
  }

  private void setupView() {
    SwipePageAdapter adapter = new SwipePageAdapter(getFragmentManager());
    pager.setAdapter(adapter);
    pager.setCurrentItem(APPLIST_FRAG);
  }

  @Override
  public void onConnectionFailed(ConnectionResult connectionResult) {
    Timber.e("The location connection failed:\t%d\t%s",
             connectionResult.getErrorCode(),
             connectionResult.getErrorMessage())
  }

  private final class SwipePageAdapter extends FragmentPagerAdapter {

    public SwipePageAdapter(FragmentManager fm) { super(fm); }

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
