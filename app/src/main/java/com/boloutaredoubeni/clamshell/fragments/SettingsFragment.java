package com.boloutaredoubeni.clamshell.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boloutaredoubeni.clamshell.R;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class SettingsFragment extends Fragment {

  public static final int SELECT_WALLPAPER = 1;

  @Bind(R.id.dummy_layout) View homeScreen;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_dummy, container, false);
    ButterKnife.bind(this, view);
    return view;
  }

  @OnLongClick(R.id.dummy_layout)
  boolean pickWallpaper(View v) {
    Intent i = new Intent(
        Intent.ACTION_PICK,
        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    getActivity().startActivityForResult(i, SELECT_WALLPAPER);
    return true;
  }

//  @Override
//  public void onActivityResult(int requestCode, int resultCode, Intent data) {
//    //    super.onActivityResult(requestCode, resultCode, data);
//    switch (requestCode) {
//    case SELECT_WALLPAPER: {
//      getActivity();
//      if (resultCode == Activity.RESULT_OK) {
//        Uri imageUri = data.getData();
////        changeWallpaper(imageUri);
//      }
//      break;
//    }
//    }
//  }

}
