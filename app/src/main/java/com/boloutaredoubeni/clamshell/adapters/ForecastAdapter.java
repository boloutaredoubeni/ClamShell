package com.boloutaredoubeni.clamshell.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.viewmodels.WeatherViewModel;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class ForecastAdapter
    extends RecyclerView.Adapter<ForecastAdapter.ViewHolder>
    implements WeatherViewModel.OnWeatherChangeListener {
  private List<WeatherViewModel> mForecast;

  public ForecastAdapter(List<WeatherViewModel> forecast) {
    mForecast = forecast;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.weather_item, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    final WeatherViewModel model = mForecast.get(position);
//    holder.day.setText(model.getDay());
    holder.description.setText(model.getDescription());
    holder.hi.setText(model.getHi());
//    holder.lo.setText(model.getLo());
  }

  @Override
  public int getItemCount() {
    return mForecast.size();
  }

  public void clearAndAddAll(List<WeatherViewModel> viewModels) {
    mForecast.clear();
    mForecast.addAll(viewModels);
    notifyDataSetChanged();
  }

  @Override
  public void weatherChanged(WeatherViewModel viewmodel) {
    Timber.e("Not implemented");
  }

  @Override
  public void forecastChanged(List<WeatherViewModel> viewModels) {
    clearAndAddAll(viewModels);
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {
    @Bind(R.id.forecast_day_of_week) TextView day;
    @Bind(R.id.forecast_weather_description) TextView description;
    @Bind(R.id.forecast_hi_temp) TextView hi;
//    @Bind(R.id.forecast_lo_temp) TextView lo;
    @Bind(R.id.forecast_weather_icon) ImageView icon;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
