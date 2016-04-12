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
import com.boloutaredoubeni.clamshell.activities.AppsViewActivity;
import com.boloutaredoubeni.clamshell.adapters.AppListAdapter;
import com.boloutaredoubeni.clamshell.models.App;
import com.boloutaredoubeni.clamshell.models.AppList;

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
public class AppListFragment
    extends Fragment implements AppsViewActivity.OnVoiceSearchListener {

  private static final int APP_NUM_WIDTH = 4;
  public static final int RESULT_SPEECH = 500;

  @Bind(R.id.app_list) RecyclerView recyclerView;

  @Bind(R.id.search_edit_text) EditText searchBox;
  @Bind(R.id.voice_search) ImageButton voiceSearch;

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

    AppList appList = new AppList();

    adapter = new AppListAdapter(getActivity(), appList);
    recyclerView.setAdapter(adapter);
  }

  @OnTextChanged(R.id.search_edit_text)
  void executeSearch(CharSequence query) {
    // TODO: search the users contacts
    new SearchContactsTask().execute(query.toString());
    adapter.getFilter().filter(query.toString());
  }

  @OnClick(R.id.voice_search)
  void voiceSearch(View v) {
    searchBox.setText("");
    Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
               RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
    i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
    i.putExtra(RecognizerIntent.EXTRA_PROMPT, "Search ...");
    getActivity().startActivityForResult(i, RESULT_SPEECH);
  }

  /**
   *
   * @return A list of applications that can be opened via a launcher
   */
  private AppList listUserApps() {
    PackageManager manager = getActivity().getPackageManager();
    AppList appList = new AppList();
    Intent i = new Intent(Intent.ACTION_MAIN, null);
    i.addCategory(Intent.CATEGORY_LAUNCHER);

    List<ResolveInfo> launchableApps = manager.queryIntentActivities(i, 0);
    for (ResolveInfo info : launchableApps) {
      App app = App.createFrom(getActivity(), info);
      appList.add(app);
    }
    Timber.i("Found %d apps", appList.size());
    return appList;
  }

  @Override
  public void executeVoiceSearch(CharSequence query) {
    searchBox.setText(query);
    executeSearch(query);
  }

  // TODO: enhancement, consider putting a loading screen or load apps one by
  // one
  private final class GetUserAppsTask extends AsyncTask<Void, Void, AppList> {
    @Override
    protected AppList doInBackground(Void... params) {
      return listUserApps();
    }

    @Override
    protected void onPostExecute(AppList apps) {
      super.onPostExecute(apps);
      adapter.setAppList(apps);
      Timber.d("Adding all apps to the view");
    }
  }

  private final class SearchContactsTask extends AsyncTask<String, Void, List> {

    @Override
    protected List doInBackground(String... params) {
      //      ContentResolver contentResolver =
      //      getActivity().getContentResolver();
      //      Cursor cursor =
      //      contentResolver.query(ContactsContract.Contacts.CONTENT_URI, null,
      //      null, null, null, null);
      //      if (cursor != null && cursor.getCount() > 0) {
      //        while (cursor.moveToNext()) {
      //          String id = cursor.getString(
      //              cursor.getColumnIndex(ContactsContract.Contacts._ID));
      //          String name = cursor.getString(cursor.getColumnIndex(
      //              ContactsContract.Contacts.DISPLAY_NAME));
      //
      //          if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(
      //              ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
      //            Cursor pCur = cursor.query(
      //                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
      //                null,
      //                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" =
      //                ?",
      //                new String[]{id}, null);
      //            while (pCur.moveToNext()) {
      //              String phoneNo = pCur.getString(pCur.getColumnIndex(
      //                  ContactsContract.CommonDataKinds.Phone.NUMBER));
      //              Toast.makeText(NativeContentProvider.this, "Name: " + name
      //                  + ", Phone No: " + phoneNo,
      //                  Toast.LENGTH_SHORT).show();
      //            }
      //            pCur.close();
      //        }
      //      }
      return null;
    }
  }
}
