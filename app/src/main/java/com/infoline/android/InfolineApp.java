package com.infoline.android;

import android.app.Application;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.infoline.android.realm.UserObject;

import java.util.HashMap;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by muhammet on 4/4/17.
 */

public class InfolineApp extends Application {

   private static HashMap<Long, Long> downloadReferenceToVideoId = new HashMap<>();

   @Override
   public void onCreate() {
      super.onCreate();
      Realm.init(this);

      RealmConfiguration defaultConfig = new RealmConfiguration.Builder().schemaVersion(1).initialData(new Realm.Transaction() {
         @Override
         public void execute(Realm realm) {
            UserObject userObject = realm.createObject(UserObject.class, 1);
            userObject.setUsername("admin");
            userObject.setPasswordHash("c9480cb226145708d2a19d8adbf8abc4");
            userObject.setLastLoginDate(0);
         }
      }).build();
      Realm.setDefaultConfiguration(defaultConfig);

      IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
      registerReceiver(downloadReceiver, filter);
   }


   private BroadcastReceiver downloadReceiver = new BroadcastReceiver() {

      @Override
      public void onReceive(Context context, Intent intent) {
         long referenceId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

         if (downloadReferenceToVideoId.containsKey(referenceId)) {
            downloadReferenceToVideoId.remove(referenceId);
         }
      }
   };

   public static boolean isDownloading(long videoId) {
      if (downloadReferenceToVideoId.containsValue(videoId)) {
         return true;
      }
      return false;
   }

   public static void addDownloading(long downloadReference, long videoId) {
      downloadReferenceToVideoId.put(downloadReference, videoId);
   }

}
