package com.boloutaredoubeni.clamshell.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.adapters.AppListAdapter;
import com.boloutaredoubeni.clamshell.models.UserApplicationInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class AppListFragment extends Fragment {

  private static final int APP_NUM_WIDTH = 4;

  private EditText mSearchInput;

  @Bind(R.id.app_list) RecyclerView mRecyclerView;

  private AppListAdapter mAdapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_app_list, container, false);
    loadView(root);
    return root;
  }

  @Override
  public void onResume() {
    super.onResume();

    new GetUserAppsTask().execute();
  }

  private void loadView(View view) {

    mRecyclerView = (RecyclerView)view.findViewById(R.id.app_list);
    mRecyclerView.setHasFixedSize(true);
    mRecyclerView.setLayoutManager(
        new GridLayoutManager(getActivity(), APP_NUM_WIDTH));
    List<UserApplicationInfo> apps = new ArrayList<>();
    //  FIXME:  UserApplicationInfo.sortApps(apps);
    mAdapter = new AppListAdapter(getActivity(), apps);
    mRecyclerView.setAdapter(mAdapter);

    mSearchInput = (EditText)view.findViewById(R.id.search_edit_text);
    mSearchInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count,
                                    int after) {}

      @Override
      public void onTextChanged(CharSequence s, int start, int before,
                                int count) {
        mAdapter.getFilter().filter(s.toString());
      }

      @Override
      public void afterTextChanged(Editable s) {}
    });
  }

  /**
   *
   * @return A list of applications that can be opened via a launcher
   */
  private List<UserApplicationInfo> listUserApps() {
    PackageManager manager = getActivity().getPackageManager();
    List<UserApplicationInfo> apps = new ArrayList<>();
    Intent i = new Intent(Intent.ACTION_MAIN, null);
    i.addCategory(Intent.CATEGORY_LAUNCHER);

    List<ResolveInfo> launchableApps = manager.queryIntentActivities(i, 0);
    for (ResolveInfo info : launchableApps) {
      UserApplicationInfo app =
          UserApplicationInfo.createFrom(getActivity(), info);
      apps.add(app);
    }
    Timber.i("Found %d apps", apps.size());
    return apps;
  }

  private final class GetUserAppsTask
      extends AsyncTask<Void, Void, List<UserApplicationInfo>> {

    @Override
    protected List<UserApplicationInfo> doInBackground(Void... params) {
      return listUserApps();
    }

    @Override
    protected void
    onPostExecute(List<UserApplicationInfo> userApplicationInfos) {
      super.onPostExecute(userApplicationInfos);

      mAdapter.clearThenAddAll(userApplicationInfos);
      Timber.d("Adding all apps to the view");
    }
  }
}
