package com.CS100MessagingApp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserProfilePage extends AppCompatActivity {

    Button ChangePassword;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);

        AlphaAnimation fadein = new AlphaAnimation(0.0f,1.0f);

        ChangePassword = (Button)findViewById(R.id.button_changepw);

        fadein.setDuration(1500);
        fadein.setFillAfter(true);
        ChangePassword.startAnimation(fadein);

        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfilePage.this,ResetPassword.class));
            }
        });
    }
}
