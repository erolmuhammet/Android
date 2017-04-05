package com.infoline.android.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.infoline.android.InfolineApp;
import com.infoline.android.R;
import com.infoline.android.activity.ListActivity;
import com.infoline.android.activity.PlayerActivity;
import com.infoline.android.realm.VideoObject;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

/**
 * Created by muhammet on 4/4/17.
 */

public class VideoRealmRecyclerViewAdapter extends RealmRecyclerViewAdapter<VideoObject, RecyclerView.ViewHolder> {

   private Context context;

   public VideoRealmRecyclerViewAdapter(Context context, @Nullable OrderedRealmCollection<VideoObject> data) {
      super(data, true);
      this.context = context;
   }

   @Override
   public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      return new VideoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false));
   }

   @Override
   public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
      if (holder instanceof VideoViewHolder) {
         if ((position) > -1) {
            VideoObject videoObject = getData().get(position);
            ((VideoViewHolder) holder).videoObject = videoObject;

            if (videoObject.getVideoUrl() != null) {
               String urlString = videoObject.getVideoUrl();
               String videoFileName = urlString.substring(urlString.lastIndexOf('/') + 1);
               ((VideoViewHolder) holder).textViewVideoName.setText(videoFileName);
            } else {
               ((VideoViewHolder) holder).textViewVideoName.setText("");
            }

            Glide.with(context).load(videoObject.getImageUrl()).into(((VideoViewHolder) holder).imageViewVideoImage);
         }
      }
   }

   private class VideoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

      private VideoObject videoObject;
      private ImageView imageViewVideoImage;
      private TextView textViewVideoName;

      public VideoViewHolder(View view) {
         super(view);
         imageViewVideoImage = (ImageView) view.findViewById(R.id.video_list_item_imageview);
         textViewVideoName = (TextView) view.findViewById(R.id.video_list_item_textview);
         view.setOnClickListener(this);
      }

      @Override
      public void onClick(View view) {
         if (!InfolineApp.isDownloading(videoObject.getId())) {
            Intent intent = new Intent(context, PlayerActivity.class);
            intent.putExtra("filePath", videoObject.getFilePath());
            context.startActivity(intent);
         } else {
            Toast.makeText(context, R.string.toast_activity_list_downloading_still, Toast.LENGTH_SHORT).show();
         }
      }
   }

}
