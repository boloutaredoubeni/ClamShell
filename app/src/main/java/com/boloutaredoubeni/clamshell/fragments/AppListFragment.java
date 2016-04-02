package com.boloutaredoubeni.clamshell.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.adapters.AppListAdapter;
import com.boloutaredoubeni.clamshell.models.UserApplicationInfo;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class AppListFragment extends Fragment {

  private static final int APP_NUM_WIDTH = 4;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_app_list, container, false);
    loadView(root);
    return root;
  }

  private void loadView(View view) {

    RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.app_list);
    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), APP_NUM_WIDTH));
    List<UserApplicationInfo> apps = listUserApps();
//  FIXME:  UserApplicationInfo.sortApps(apps);
    AppListAdapter adapter = new AppListAdapter(getActivity(), apps);
    recyclerView.setAdapter(adapter);
  }

  private List<UserApplicationInfo> listUserApps() {
    PackageManager manager = getActivity().getPackageManager();
    List<UserApplicationInfo> apps = new ArrayList<>();
    Intent i = new Intent(Intent.ACTION_MAIN, null);
    i.addCategory(Intent.CATEGORY_LAUNCHER);

    List<ResolveInfo> launchableApps = manager.queryIntentActivities(i, 0);
    for (ResolveInfo info : launchableApps) {
      UserApplicationInfo app = UserApplicationInfo.createFrom(getActivity(), info);
      apps.add(app);
    }
    Timber.i("Found %d apps", apps.size());
    return apps;
  }
}
