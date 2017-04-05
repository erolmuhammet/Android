package com.infoline.android.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by muhammet on 4/4/17.
 */

public class VideoObject extends RealmObject {

   @PrimaryKey
   private long id;
   private String name;
   private String videoUrl;
   private String imageUrl;
   private String filePath;
   private long userId;

   public long getId() {
      return id;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setUserId(long userId) {
      this.userId = userId;
   }

   public String getImageUrl() {
      return imageUrl;
   }

   public void setImageUrl(String imageUrl) {
      this.imageUrl = imageUrl;
   }

   public String getVideoUrl() {
      return videoUrl;
   }

   public void setVideoUrl(String videoUrl) {
      this.videoUrl = videoUrl;
   }

   public String getFilePath() {
      return filePath;
   }

   public void setFilePath(String filePath) {
      this.filePath = filePath;
   }
}
