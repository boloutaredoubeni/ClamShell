package com.boloutaredoubeni.clamshell.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.adapters.AppListAdapter;
import com.boloutaredoubeni.clamshell.models.UserApplicationInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class AppListFragment extends Fragment  {

  private static final int APP_NUM_WIDTH = 4;
  private static final int RESULT_SPEECH = 1;

  @Bind(R.id.app_list) RecyclerView recyclerView;

  @Bind(R.id.search_edit_text) EditText searchBox;
  @Bind(R.id.voice_search)
  ImageButton voiceSearch;

  private AppListAdapter adapter;

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View root = inflater.inflate(R.layout.fragment_app_list, container, false);
    ButterKnife.bind(this, root);
    loadView();
    return root;
  }

  @Override
  public void onResume() {
    super.onResume();
    new GetUserAppsTask().execute();
  }

  private void loadView() {

    //    recyclerView.setHasFixedSize(true);
    recyclerView.setLayoutManager(
        new GridLayoutManager(getActivity(), APP_NUM_WIDTH));
    //    recyclerView.addItemDecoration(new ItemOffsetDecoration(1));

    List<UserApplicationInfo> apps = new ArrayList<>();

    adapter = new AppListAdapter(getActivity(), apps);
    recyclerView.setAdapter(adapter);
  }

  @OnTextChanged(R.id.search_edit_text)
  void executeSearch(CharSequence query) {
    adapter.getFilter().filter(query.toString());
  }

  @OnClick(R.id.voice_search)
  void voiceSearch(View v) {
    searchBox.setText("");
    //TODO: execute search
    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search ...");
    startActivityForResult(i, RESULT_SPEECH);
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



  // TODO: enhancement, consider putting a loading screen or load apps one by
  // one
  private final class GetUserAppsTask
      extends AsyncTask<Void, Void, List<UserApplicationInfo>> {
    @Override
    protected List<UserApplicationInfo> doInBackground(Void... params) {
      return listUserApps();
    }

    @Override
    protected void onPostExecute(List<UserApplicationInfo> apps) {
      super.onPostExecute(apps);
      adapter.clearThenAddAll(apps);
      Timber.d("Adding all apps to the view");
    }
  }
}
