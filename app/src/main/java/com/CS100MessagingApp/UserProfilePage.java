package com.CS100MessagingApp;


=import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;


public class UserProfilePage extends AppCompatActivity {

    Button ChangePassword, logoutButton;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);

        AlphaAnimation fadein = new AlphaAnimation(0.0f,1.0f);

        ChangePassword = (Button)findViewById(R.id.button_changepw);
        logoutButton = (Button)findViewById(R.id.button_logout);


        fadein.setDuration(1500);
        fadein.setFillAfter(true);
        ChangePassword.startAnimation(fadein);
        logoutButton.startAnimation(fadein);
        ChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserProfilePage.this,ResetPassword.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Login.class);
                startActivity(i);
            }
        });
    }
}
