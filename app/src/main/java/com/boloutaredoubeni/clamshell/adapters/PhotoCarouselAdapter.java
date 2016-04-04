package com.boloutaredoubeni.clamshell.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.boloutaredoubeni.clamshell.R;
import com.boloutaredoubeni.clamshell.fragments.DashboardFragment;
import com.boloutaredoubeni.clamshell.models.UserPhoto;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import timber.log.Timber;

/**
 * Copyright 2016 Boloutare Doubeni
 */
public class PhotoCarouselAdapter
    extends RecyclerView.Adapter<PhotoCarouselAdapter.ViewHolder> {

  // TODO: make sure this is actually a carousel
  // TODO: make sure photos are actually the right size

  private Context mContext;
  private List<UserPhoto> mPhotos;

  public PhotoCarouselAdapter(Context context,
                              @NonNull List<UserPhoto> photos) {
    mContext = context;
    mPhotos = photos;
  }

  @Override
  public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.user_photo_item, parent, false);
    return new ViewHolder(itemView);
  }

  @Override
  public void onBindViewHolder(ViewHolder holder, int position) {
    holder.userPhoto.setImageURI(Uri.parse(mPhotos.get(position).url));
    holder.userPhoto.setOnClickListener(v -> { Timber.d("Clicked on photo"); });
  }

  @Override
  public int getItemCount() {
    return DashboardFragment.MAX_NUM_PHOTOS;
  }

  public void clearThenAddAll(List<UserPhoto> newPhotos) {
    mPhotos.clear();
    mPhotos.addAll(newPhotos);
    notifyDataSetChanged();
  }

  public static class ViewHolder extends RecyclerView.ViewHolder {

    @Bind(R.id.user_photo) ImageView userPhoto;

    public ViewHolder(View itemView) {
      super(itemView);
      ButterKnife.bind(this, itemView);
    }
  }
}
