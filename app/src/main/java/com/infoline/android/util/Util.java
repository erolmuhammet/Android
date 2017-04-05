package com.infoline.android.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by muhammet on 4/4/17.
 */

public class Util {

   public static final String md5(final String s) {
      try {
         MessageDigest digest = MessageDigest.getInstance("MD5");
         digest.update(s.getBytes());
         byte messageDigest[] = digest.digest();

         StringBuilder hexString = new StringBuilder();
         for (byte aMessageDigest : messageDigest) {
            String h = Integer.toHexString(0xFF & aMessageDigest);
            while (h.length() < 2) {
               h = "0" + h;
            }
            hexString.append(h);
         }
         return hexString.toString();

      } catch (NoSuchAlgorithmException e) {
         return "";
      }
   }
}
