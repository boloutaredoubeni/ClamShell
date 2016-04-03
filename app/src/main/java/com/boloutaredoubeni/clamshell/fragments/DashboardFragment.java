package com.boloutaredoubeni.clamshell.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boloutaredoubeni.clamshell.R;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class DashboardFragment extends Fragment {

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_app_list, container, false);
  }
}
