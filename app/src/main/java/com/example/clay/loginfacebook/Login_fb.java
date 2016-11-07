package com.example.clay.loginfacebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Created by CLAY on 11/5/2016.
 */

public class Login_fb extends Activity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    SharedPreferences pref;

   protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
       setContentView(R.layout.login_fb);

       // get element
       callbackManager = CallbackManager.Factory.create();
       loginButton = (LoginButton) findViewById(R.id.btn_fb_login);
       loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
       callbackManager = CallbackManager.Factory.create();
       pref =getSharedPreferences("UserDetails", Context.MODE_PRIVATE);

       loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
           @Override
           public void onSuccess(LoginResult loginResult) {
               GraphRequest request = GraphRequest.newMeRequest(
                       loginResult.getAccessToken(),
                       new GraphRequest.GraphJSONObjectCallback() {

                           @Override
                           public void onCompleted(JSONObject object, GraphResponse response) {
                               Log.v("Main", response.toString());
                               setProfileToView(object);

                           }
                       });
               Bundle parameters = new Bundle();
               parameters.putString("fields", "id,name,email,gender,age_range,locale, birthday");
               request.setParameters(parameters);
               request.executeAsync();
              // goMainScreen();

           }

           @Override
           public void onCancel() {
               Toast.makeText(getApplicationContext(),"Cancel Login", Toast.LENGTH_SHORT).show();
           }

           @Override
           public void onError(FacebookException error) {
               Toast.makeText(getApplicationContext(), "Login Error", Toast.LENGTH_SHORT).show();
           }
       });
   }
    private void goMainScreen() {
        Intent intent = new Intent(this,Profil.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    private void setProfileToView(JSONObject jsonObject) {
        try {
            String s_facebookName=jsonObject.getString("name").trim();
            String s_email=jsonObject.getString("email").trim();
            String s_gender=jsonObject.getString("gender").trim();
            String s_birthday=jsonObject.getString("age_range").trim();
            String s_profilePictureView=jsonObject.getString("id").trim();
            //String s_firstName=jsonObject.getString("locale").trim();

            // shared data
            Store(getApplicationContext(),s_facebookName,s_email,s_gender,s_profilePictureView,s_birthday);
            Intent i = new Intent(Login_fb.this, Profil.class);
            i.putExtra("name", s_facebookName);
            i.putExtra("email", s_email);
            i.putExtra("gender", s_gender);
            i.putExtra("age_range", s_birthday);
            i.putExtra("id", s_profilePictureView);
            startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void Store(Context c, String s_facebookName,String s_email,String s_gender,String s_profilePictureView,String s_birthday){
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("name",s_facebookName);
        editor.putString("email",s_email);
        editor.putString("gender",s_gender);
        editor.putString("age_range",s_birthday);
        //editor.putString("locale",store_lo);
        editor.putString("id",s_profilePictureView);
        editor.commit();
    }
}
