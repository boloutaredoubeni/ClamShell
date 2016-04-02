package com.boloutaredoubeni.clamshell.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.boloutaredoubeni.clamshell.R;

import timber.log.Timber;

public class HomeScreenActivity extends Activity {

  private Button mShowApplicationsButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mShowApplicationsButton = (Button) findViewById(R.id.show_apps_button);
    mShowApplicationsButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent i  = new Intent(HomeScreenActivity.this, AppsViewActivity.class);
        Timber.i("Show app list");
        startActivity(i);
      }
    });
  }
}
