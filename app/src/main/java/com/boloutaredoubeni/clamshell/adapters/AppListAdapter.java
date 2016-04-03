package com.boloutaredoubeni.clamshell.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.models.UserApplicationInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public final class AppListAdapter
    extends RecyclerView.Adapter<AppListAdapter.ViewHolder>
    implements Filterable {

  private List<UserApplicationInfo> mOriginalApps;
  private List<UserApplicationInfo> mApps;
  private Context mContext;

  // Fixme: icons need to be the same size
  // Todo: databinding

  public AppListAdapter(Context context, List<UserApplicationInfo> apps) {
    mApps = apps;
    mOriginalApps = apps;
    mContext = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_item, parent, false);
    return new ViewHolder(itemView);
  }

  public void clearThenAddAll(List<UserApplicationInfo> apps) {
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
      PackageManager pm = mContext.getPackageManager();
      Intent i = pm.getLaunchIntentForPackage(app.getPackage());
      Timber.i("Starting %s", app.getPackage());
      // Todo: save to database!! Content Provider?
      mContext.startActivity(i);
    });
  }

  @Override
  public int getItemCount() {
    return mApps.size();
  }

  @Override
  public Filter getFilter() {
    return new AppInfoFilter(this, mApps);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.app_icon) ImageView icon;
    @Bind(R.id.app_name) TextView appName;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private static final class AppInfoFilter extends Filter {

    private AppListAdapter mAdapter;
    private final List<UserApplicationInfo> mOriginalList;
    private final List<UserApplicationInfo> mFilteredList;

    private AppInfoFilter(AppListAdapter adapter,
                          List<UserApplicationInfo> orginalList) {
      super();
      mAdapter = adapter;
      mOriginalList = orginalList;
      mFilteredList = new ArrayList<>();
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      mFilteredList.clear();
      final FilterResults results = new FilterResults();
      if (constraint == null || constraint.length() == 0) {
        mFilteredList.addAll(mAdapter.mOriginalApps);
      } else {
        final String filterPattern = constraint.toString().toLowerCase();

        // TODO: refine me
        for (UserApplicationInfo app : mOriginalList) {
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
      mAdapter.mApps.clear();
      mAdapter.mApps.addAll(mFilteredList);
      mAdapter.notifyDataSetChanged();
    }
  }
}
