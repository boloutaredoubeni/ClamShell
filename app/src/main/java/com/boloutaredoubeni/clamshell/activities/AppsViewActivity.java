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
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.adapters.AppListAdapter;
import com.boloutaredoubeni.clamshell.fragments.AppListFragment;
import com.boloutaredoubeni.clamshell.fragments.DashboardFragment;
import com.boloutaredoubeni.clamshell.fragments.HomeScreenFragment;
import com.boloutaredoubeni.clamshell.models.App;

import java.io.IOException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

public final class AppsViewActivity
    extends Activity implements AppListAdapter.AppActionListener {

  // FIXME: get runtime permissions for 6.0

  private static final int DASHBOARD_FRAG = 0;
  private static final int APP_LIST_FRAG = 1;
  private static final int SETTINGS_FRAG = 2;
  private static final int NUM_OF_TABS = 3;
  private static final int APP_DELETED = 100;

  @Bind(R.id.app_view_container) ViewPager pager;
  private SwipePageAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_app_view);
    ButterKnife.bind(this);
    setupView();
  }

  private void setupView() {
    adapter = new SwipePageAdapter(getFragmentManager());
    pager.setAdapter(adapter);
    pager.setCurrentItem(APP_LIST_FRAG);
  }

  @Override
  public void onAppAction(final App app) {

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
                             startActivityForResult(i, APP_DELETED);
                           })
        .setCancelable(true)
        .show();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    //    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      switch (requestCode) {
      case HomeScreenFragment.SELECT_WALLPAPER:
        Uri imageUri = data.getData();
        changeWallpaper(imageUri);
        break;
      case APP_DELETED:
        Timber.e("You should notify the adapter or list here");
        break;
      case AppListFragment.RESULT_SPEECH:
        if (data != null) {
          try {
            List<String> query =
                data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            OnVoiceSearchListener listener =
                (OnVoiceSearchListener)adapter.currentFragment;
            listener.executeVoiceSearch(query.get(0));
          } catch (Exception e) {
            Timber.e(e.getMessage());
          }
        }
      }
    }
  }

  private void changeWallpaper(Uri imgSrc) {
    try {
      Bitmap bitmap =
          MediaStore.Images.Media.getBitmap(getContentResolver(), imgSrc);
      WallpaperManager.getInstance(this).setBitmap(bitmap);
      Timber.i("The wallpaper has been changed");
    } catch (IOException e) {
      Timber.e(e.getMessage());
    }
  }

  public interface OnVoiceSearchListener {
    void executeVoiceSearch(CharSequence query);
  }

  private final class SwipePageAdapter extends FragmentPagerAdapter {

    Fragment currentFragment;

    public SwipePageAdapter(@NonNull FragmentManager fm) { super(fm); }

    @Override
    public Fragment getItem(int position) {
      switch (position) {
      case DASHBOARD_FRAG:
        Timber.d("Moving to dashboard");
        return new DashboardFragment();
      case APP_LIST_FRAG:
        Timber.d("Moving to app list");
        return new AppListFragment();
      case SETTINGS_FRAG:
        Timber.d("Moving to settings");
        return new HomeScreenFragment();
      }
      return null;
    }

    @Override
    public int getCount() {
      return NUM_OF_TABS;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position,
                               Object object) {
      if (currentFragment != object) {
        currentFragment = (Fragment)object;
      }
      super.setPrimaryItem(container, position, object);
    }
  }
}
