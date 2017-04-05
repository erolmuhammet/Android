package com.infoline.android.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.infoline.android.R;
import com.infoline.android.realm.UserObject;
import com.infoline.android.util.Util;

import io.realm.Realm;

public class LoginActivity extends AppCompatActivity {

   private EditText editTextUsername;
   private EditText editTextPassword;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_login);
      editTextUsername = (EditText) findViewById(R.id.activity_login_edittext_username);
      editTextPassword = (EditText) findViewById(R.id.activity_login_edittext_password);

      Number dateNumber = Realm.getDefaultInstance().where(UserObject.class).max("lastLoginDate");
      long lastLoginDate = 0;

      if (dateNumber != null) {
         lastLoginDate = dateNumber.longValue();
      }

      UserObject userObject = Realm.getDefaultInstance().where(UserObject.class).equalTo("lastLoginDate", lastLoginDate).findFirst();

      if (userObject != null && (System.currentTimeMillis() - userObject.getLastLoginDate()) < 300000) {
         toListActivity(userObject.getId());
      } else if (userObject != null) {
         editTextUsername.setText(userObject.getUsername());
         editTextPassword.requestFocus();
      }
   }

   public void onButtonLoginClick(View view) {
      if (isUsernameValid(editTextUsername.getText().toString()) && isPasswordValid(editTextPassword.getText().toString())) {
         Realm realm = Realm.getDefaultInstance();
         final UserObject userObject = realm.where(UserObject.class).equalTo("passwordHash", Util.md5(editTextPassword.getText().toString())).findFirst();

         if (userObject != null) {
            realm.beginTransaction();
            userObject.setLastLoginDate(System.currentTimeMillis());
            realm.copyToRealmOrUpdate(userObject);
            realm.commitTransaction();
            toListActivity(userObject.getId());
         } else {
            Toast.makeText(this, R.string.toast_activity_login_unauthorized_attempt, Toast.LENGTH_SHORT).show();
         }
      }
   }

   private boolean isUsernameValid(String username) {
      if (username == null || username.equals("")) {
         Toast.makeText(this, R.string.toast_activity_login_invalid_username, Toast.LENGTH_SHORT).show();
         return false;
      }
      return true;
   }

   private boolean isPasswordValid(String password) {
      if (password == null || password.equals("") || password.length() > 8) {
         Toast.makeText(this, R.string.toast_activity_login_invalid_passsword, Toast.LENGTH_SHORT).show();
         return false;
      }
      return true;
   }

   private void toListActivity(long userId) {
      Intent intent = new Intent(this, ListActivity.class);
      intent.putExtra("userId", userId);
      startActivity(intent);
      finish();
   }

}
