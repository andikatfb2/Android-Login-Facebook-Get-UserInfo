package com.example.clay.loginfacebook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

/**
 * Created by CLAY on 11/6/2016.
 */

public class Profil extends Activity {
 private EditText e_nama,e_email,e_jender,e_birthday;
 private ProfilePictureView profilePictureView;

    private Button b_logout;

    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.profile);

        // deklarasi nilai
        e_nama=(EditText)findViewById(R.id.edit_name);
        e_email=(EditText)findViewById(R.id.edit_email);
        e_jender=(EditText)findViewById(R.id.edit_jender);
        e_birthday=(EditText)findViewById(R.id.edit_birthday);
        profilePictureView=(ProfilePictureView)findViewById(R.id.fb_image);
        b_logout=(Button)findViewById(R.id.btn_loout);
        // ambil nilai extras
        Bundle bundle = getIntent().getExtras();

        if (AccessToken.getCurrentAccessToken() == null) {
            Intent intent = new Intent(Profil.this, Login_fb.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }else{
            // get data from login
            SharedPreferences pref =getSharedPreferences("UserDetails", Context.MODE_PRIVATE);
                String name =pref.getString("name","");
                String s_email =pref.getString("email","");
                String gender =pref.getString("gender","");
                String age_range =pref.getString("age_range","");
                String id =pref.getString("id","");

            // set value
                e_nama.setText(name.toString());
                e_email.setText(s_email.toString());
                e_jender.setText(gender.toString());
                e_birthday.setText(age_range.toString());
                profilePictureView.setPresetSize(ProfilePictureView.LARGE);
                profilePictureView.setProfileId(id.toString());

            // action logout
            b_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LoginManager.getInstance().logOut();
                    Intent intent = new Intent(Profil.this, Login_fb.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            });
        }
        }

}
