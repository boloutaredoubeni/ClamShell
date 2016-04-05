package com.boloutaredoubeni.clamshell.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.adapters.AppListAdapter;
import com.boloutaredoubeni.clamshell.fragments.AppListFragment;
import com.boloutaredoubeni.clamshell.fragments.DashboardFragment;
import com.boloutaredoubeni.clamshell.fragments.SettingsFragment;
import com.boloutaredoubeni.clamshell.models.UserApplicationInfo;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public final class AppsViewActivity
    extends Activity implements AppListAdapter.AppActionListener {

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

  @Override
  public void onAppAction(final UserApplicationInfo app) {

    new AlertDialog.Builder(this)
        .setPositiveButton(
            "Show info",
            (dialog, which) -> {
              Intent i = new Intent();
              i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
              i.setData(Uri.parse("package:" + app.getPackage()));
              startActivity(i);
            })
        .setNegativeButton("Uninstall",
                           (dialog, which) -> {
                             Intent i = new Intent(Intent.ACTION_DELETE);
                             i.setData(
                                 Uri.parse("package:" + app.getPackage()));
                             startActivity(i);
                           })
        .setCancelable(true)
        .show();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case SettingsFragment.SELECT_WALLPAPER: {
        if (resultCode == Activity.RESULT_OK) {
          Uri imageUri = data.getData();
          changeWallpaper(imageUri);
        }
        break;
      }
    }
  }

  private void changeWallpaper(Uri imgSrc) {
    try {
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgSrc);
      WallpaperManager.getInstance(this).setBitmap(bitmap);
    } catch (IOException e) {
      Timber.e(e.getMessage());
    }
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
