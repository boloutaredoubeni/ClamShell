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
import com.boloutaredoubeni.clamshell.models.App;
import com.boloutaredoubeni.clamshell.models.AppList;

import java.util.ArrayList;
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

  private AppList appList = new AppList();
  private Context context;
  private AppFilter filter;

  // TODO: custom scroll bar

  public AppListAdapter(@NonNull Context context, @NonNull AppList appList) {
    this.appList = appList;
    this.context = context;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.app_item, parent, false);
    return new ViewHolder(itemView);
  }

  public void setAppList(@NonNull AppList apps) {
    appList.reinit(apps);
    notifyDataSetChanged();
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final App app = appList.get(position);
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
    return appList.size();
  }

  @Override
  public Filter getFilter() {
    if (filter == null) {
      filter = new AppFilter();
    }
    return filter;
  }

  static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.app_icon) ImageView icon;
    @Bind(R.id.app_name) TextView appName;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }

  private class AppFilter extends Filter {

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
      FilterResults results = new FilterResults();
      if (constraint == null || constraint.length() == 0) {
        results.values = appList.originals();
        results.count = appList.originals().size();
      } else {
        List<App> filteredApps = new ArrayList<>();
        for (App app : appList.originals()) {
          String name = app.getAppName().toLowerCase();
          final String query = constraint.toString().toLowerCase();
          if (name.startsWith(query)) {
            filteredApps.add(app);
          }
          results.values = filteredApps;
          results.count = filteredApps.size();
        }
      }
      return results;
    }

    @Override
    protected void publishResults(CharSequence constraint,
                                  FilterResults results) {
      appList.addAll((List<App>)results.values);
      notifyDataSetChanged();
    }
  }

  public interface AppActionListener { void onAppAction(App app); }
}
