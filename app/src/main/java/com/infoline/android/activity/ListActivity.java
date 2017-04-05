package com.infoline.android.activity;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.infoline.android.InfolineApp;
import com.infoline.android.R;
import com.infoline.android.adapter.VideoRealmRecyclerViewAdapter;
import com.infoline.android.component.DividerItemDecoration;
import com.infoline.android.realm.VideoObject;

import java.io.File;
import java.security.SecureRandom;

import io.realm.Realm;

public class ListActivity extends AppCompatActivity {

   private RecyclerView recyclerViewVideoList;
   private VideoRealmRecyclerViewAdapter videoAdapter;
   private long userId;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_list);

      recyclerViewVideoList = (RecyclerView) findViewById(R.id.activity_list_recyclerview_video_list);
      recyclerViewVideoList.setLayoutManager(new LinearLayoutManager(this));
      recyclerViewVideoList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));

      Intent intent = getIntent();
      if (userId == 0 && intent != null && intent.getExtras() != null) {
         userId = intent.getLongExtra("userId", 0);
      }
      videoAdapter = new VideoRealmRecyclerViewAdapter(this, Realm.getDefaultInstance().where(VideoObject.class).equalTo("userId", userId).findAll());
      recyclerViewVideoList.setAdapter(videoAdapter);
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.menu_activity_list, menu);
      return super.onCreateOptionsMenu(menu);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      switch (item.getItemId()) {
         case R.id.action_add:
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
               ActivityCompat.requestPermissions(ListActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 13);
            } else {
               DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
               String urlString = getRandomVideoUri();
               String fileName = urlString.substring(urlString.lastIndexOf('/') + 1);
               Uri downloadUri = Uri.parse(urlString);

               String storagePath = getExternalFilesDir(Environment.DIRECTORY_MOVIES).toString();
               File f = new File(storagePath, fileName);

               Realm realm = Realm.getDefaultInstance();
               realm.beginTransaction();
               long id = 1;
               Number maxId = realm.where(VideoObject.class).max("id");
               if (maxId != null) {
                  id = maxId.longValue() + 1;
               }
               VideoObject videoObject = realm.createObject(VideoObject.class, id);
               videoObject.setVideoUrl(urlString);
               videoObject.setImageUrl(getRandomImageUri());
               videoObject.setUserId(userId);
               videoObject.setFilePath(f.getAbsolutePath());
               realm.commitTransaction();

               DownloadManager.Request request = new DownloadManager.Request(downloadUri);
               request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
               request.setAllowedOverRoaming(false);
               request.setTitle(getString(R.string.activity_list_downloading) + " " + fileName);
               request.setDescription(getString(R.string.activity_list_downloading) + " " + fileName);
               request.setDestinationInExternalFilesDir(this, Environment.DIRECTORY_MOVIES, fileName);

               long downloadReference = downloadManager.enqueue(request);
               InfolineApp.addDownloading(downloadReference, videoObject.getId());
               Toast.makeText(this, R.string.activity_list_downloading, Toast.LENGTH_SHORT).show();
            }
            return true;
      }
      return false;
   }

   @Override
   public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
      switch (requestCode) {
         case 13: {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
               Toast.makeText(this, R.string.toast_activity_list_permission_granted, Toast.LENGTH_SHORT).show();
            }
         }
      }
   }

   private String getRandomVideoUri() {
      String randomVideoUri;
      int i = new SecureRandom().nextInt(5);
      switch (i) {
         case 0:
            randomVideoUri = "http://techslides.com/demos/sample-videos/small.mp4";
            break;
         case 1:
            randomVideoUri = "https://0.s3.envato.com/h264-video-previews/c52ce7be-e555-4fe3-9785-9991807f01d9/902711.mp4";
            break;
         case 2:
            randomVideoUri = "https://0.s3.envato.com/h264-video-previews/91924193-933a-4388-b19c-99dd54ad8b25/1387326.mp4";
            break;
         case 3:
            randomVideoUri = "https://0.s3.envato.com/h264-video-previews/f9abc5cf-82a3-4982-b226-8f6cf4ffe286/1359675.mp4";
            break;
         default:
            randomVideoUri = "https://0.s3.envato.com/h264-video-previews/2b4b43dd-c7fd-4541-9cb4-c8d7dd18499e/14507047.mp4";
            break;

      }
      return randomVideoUri;
   }

   private String getRandomImageUri() {
      String randomImageUri;
      int i = new SecureRandom().nextInt(5);
      switch (i) {
         case 0:
            randomImageUri = "http://g-ecx.images-amazon.com/images/G/01/img15/pet-products/small-tiles/23695_pets_vertical_store_dogs_small_tile_8._CB312176604_.jpg";
            break;
         case 1:
            randomImageUri = "http://www.hickerphoto.com/images/1024/endangered-animal-list_5406.jpg";
            break;
         case 2:
            randomImageUri = "http://cdn.earthporm.com/wp-content/uploads/2014/10/animal-family-portraits-2__880.jpg";
            break;
         case 3:
            randomImageUri = "https://elifyaaman.files.wordpress.com/2014/08/animal-hd-collection-329357.jpg";
            break;
         default:
            randomImageUri = "https://pbs.twimg.com/profile_images/378800000532546226/dbe5f0727b69487016ffd67a6689e75a.jpeg";
            break;

      }
      return randomImageUri;
   }

}
