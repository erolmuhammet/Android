package com.infoline.android.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.MediaController;
import android.widget.VideoView;

import com.infoline.android.R;

public class PlayerActivity extends AppCompatActivity {

   private VideoView videoView;
   private MediaController mediaController;
   private Uri uri;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_player);
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      videoView = (VideoView) findViewById(R.id.activity_player_videoview);
      mediaController = new MediaController(this);
      mediaController.setAnchorView(videoView);

      if (getIntent() != null && getIntent().getExtras() != null) {
         String path = getIntent().getStringExtra("filePath");
         uri = Uri.parse(path);
         videoView.setMediaController(mediaController);
         videoView.setVideoURI(uri);
         videoView.requestFocus();
         videoView.start();
      }
   }

   @Override
   protected void onResume() {
      super.onResume();
      videoView.setVideoURI(uri);
      videoView.requestFocus();
      videoView.start();
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      if (item.getItemId() == android.R.id.home) {
         onBackPressed();
      }
      return super.onOptionsItemSelected(item);
   }
}
