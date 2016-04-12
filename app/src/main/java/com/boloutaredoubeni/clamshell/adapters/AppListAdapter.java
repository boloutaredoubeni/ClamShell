package com.boloutaredoubeni.clamshell.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.models.UserApplicationInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class AppListAdapter
    extends RecyclerView.Adapter<AppListAdapter.ViewHolder>
    implements Filterable {

  private List<UserApplicationInfo> mOriginalApps;
  private List<UserApplicationInfo> mApps;
  private Context context;

  // TODO: custom scroll bar
  // Fixme: allow seeing app info
  // https://stackoverflow.com/questions/11157102/how-i-can-start-application-info-screen-in-android

  public AppListAdapter(@NonNull Context context,
                        @NonNull List<UserApplicationInfo> apps) {
    mApps = apps;
    mOriginalApps = apps;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_item, parent, false);
    return new ViewHolder(itemView);
  }

  public void clearThenAddAll(@NonNull List<UserApplicationInfo> apps) {
    Collections.sort(apps);
    mApps.clear();
    mOriginalApps.clear();
    mApps.addAll(apps);
    mOriginalApps.addAll(apps);
    notifyDataSetChanged();
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final UserApplicationInfo app = mApps.get(position);
    holder.icon.setImageDrawable(app.getIcon());
    holder.appName.setText(app.getAppName());
    holder.icon.setOnClickListener(v -> {
      PackageManager pm = context.getPackageManager();
      Intent i = pm.getLaunchIntentForPackage(app.getPackage());
      Timber.i("Starting %s", app.getPackage());
      context.startActivity(i);
    });
    holder.icon.setOnLongClickListener(v -> {
      Animation shake = AnimationUtils.loadAnimation(context, R.anim.shake);
      v.startAnimation(shake);
      AppActionListener listener;
      try {
        listener = (AppActionListener)context;
        listener.onAppAction(app);
      } catch (ClassCastException e) {
        Timber.e("The context is not an AppActionListener: %s", e.getMessage());
      }
      return true;
    });
  }

  @Override
  public int getItemCount() {
    return mApps.size();
  }

  @Override
  public Filter getFilter() {
    return new AppInfoFilter();
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.app_icon) ImageView icon;
    @Bind(R.id.app_name) TextView appName;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private class AppInfoFilter extends Filter {

    private AppListAdapter mAdapter;
    //    private final List<UserApplicationInfo> mFilteredList;


    // FIXME: this isnt working properly
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      List<UserApplicationInfo> mFilteredList = new ArrayList<>();
      //      mFilteredList.clear();
      FilterResults results = new FilterResults();
      if (constraint == null || constraint.length() == 0) {
        mFilteredList.addAll(mAdapter.mOriginalApps);
      } else {
        final String filterPattern = constraint.toString().toLowerCase();
        mFilteredList.clear();
        // TODO: refine me
        for (UserApplicationInfo app : mAdapter.mOriginalApps) {
          if (app.getAppName().contains(filterPattern)) {
            mFilteredList.add(app);
          }
        }
      }

      results.values = mFilteredList;
      results.count = mFilteredList.size();
      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint,
                                  FilterResults results) {

      if (results.count > 0) {
        mAdapter.mApps.clear();
        mAdapter.mApps.addAll((ArrayList<UserApplicationInfo>)results.values);
      }
      mAdapter.notifyDataSetChanged();
    }
  }

  public interface AppActionListener {
    void onAppAction(UserApplicationInfo app);
  }
}
