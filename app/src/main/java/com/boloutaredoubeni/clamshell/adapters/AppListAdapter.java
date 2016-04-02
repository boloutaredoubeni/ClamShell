package com.boloutaredoubeni.clamshell.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.models.UserApplicationInfo;

import java.util.List;

import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public final class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {

  private List<UserApplicationInfo> mApps;
  private Context mContext;

  // Fixme: icons need to be the same size


  public AppListAdapter(Context context, List<UserApplicationInfo> apps) {
    mApps = apps;
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.app_item, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final UserApplicationInfo app = mApps.get(position);
    holder.icon.setImageDrawable(app.getIcon());
    holder.appName.setText(app.getAppName());
    holder.icon.setOnClickListener(v -> {
      PackageManager pm = mContext.getPackageManager();
      Intent i = pm.getLaunchIntentForPackage(app.getPackage());
      Timber.i("Starting %s", app.getPackage());
      mContext.startActivity(i);
    });
  }

  @Override
  public int getItemCount() {
    return mApps.size();
  }

  /* package */

  public static class ViewHolder extends RecyclerView.ViewHolder {
    ImageView icon;
    TextView appName;

    public ViewHolder(View itemView) {
      super(itemView);
      icon = (ImageView) itemView.findViewById(R.id.app_icon);
      appName = (TextView) itemView.findViewById(R.id.app_name);
    }

  }
}
