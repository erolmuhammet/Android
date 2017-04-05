package com.infoline.android.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by muhammet on 4/4/17.
 */

public class UserObject extends RealmObject {

   @PrimaryKey
   private long id;
   private String username;
   private String passwordHash;
   private long lastLoginDate;

   public long getId() {
      return id;
   }

   public String getUsername() {
      return username;
   }

   public void setUsername(String username) {
      this.username = username;
   }

   public void setPasswordHash(String passwordHash) {
      this.passwordHash = passwordHash;
   }

   public long getLastLoginDate() {
      return lastLoginDate;
   }

   public void setLastLoginDate(long lastLoginDate) {
      this.lastLoginDate = lastLoginDate;
   }
}
