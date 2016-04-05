package com.boloutaredoubeni.clamshell.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boloutaredoubeni.clamshell.R;

import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class SettingsFragment extends Fragment {

  public static final int SELECT_WALLPAPER = -1;

  @Bind(R.id.dummy_layout) View homeScreen;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dummy, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnLongClick(R.id.dummy_layout) boolean pickWallpaper(View v) {
    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    getActivity().startActivityForResult(i, SELECT_WALLPAPER);
    return true;
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    switch (requestCode) {
      case SELECT_WALLPAPER: {
        getActivity();
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
      Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imgSrc);
      WallpaperManager.getInstance(getActivity()).setBitmap(bitmap);
    } catch (IOException e) {
      Timber.e(e.getMessage());
    }
  }

}
