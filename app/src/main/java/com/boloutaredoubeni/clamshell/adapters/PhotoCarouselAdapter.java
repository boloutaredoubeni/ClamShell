package com.boloutaredoubeni.clamshell.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.boloutaredoubeni.clamshell.models.UserPhoto;

import java.util.List;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class PhotoCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

  // TODO: make sure this is actually a carousel

  private Context mContext;
  private List<UserPhoto> mPhotos;

  public PhotoCarouselAdapter(Context context, @NonNull List<UserPhoto> photos) {
    mContext = context;
    mPhotos = photos;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    return null;
  }

  @Override
  public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

  }

  @Override
  public int getItemCount() {
    return 0;
  }

  public void clearThenAddAll(List<UserPhoto> newPhotos) {
    mPhotos.clear();
    mPhotos.addAll(newPhotos);
    notifyDataSetChanged();
  }

  private static class ViewHolder extends RecyclerView.ViewHolder {

    public ViewHolder(View itemView) {
      super(itemView);
    }
  }
}
