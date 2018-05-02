package com.CS100MessagingApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.net.Uri;
import android.widget.TextView;
import android.widget.Toast;
import android.app.ProgressDialog;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;


import java.io.ByteArrayOutputStream;

public class UserProfilePage extends AppCompatActivity {

    Button ChangePassword, logoutButton;
    ImageView profilepicture;
    TextView profilename;
    String usersName = UserDetails.username;
    private static int GALLERY_INTENT = 2;
    private StorageReference storage;
    private ProgressDialog pd;



    /* on create */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profilepage);
        storage = FirebaseStorage.getInstance().getReference();

        AlphaAnimation fadein = new AlphaAnimation(0.0f,1.0f);

        ChangePassword = (Button)findViewById(R.id.button_changepw);
        logoutButton = (Button)findViewById(R.id.button_logout);
        profilepicture = (ImageView)findViewById(R.id.ProPic);
        profilename =(TextView)findViewById(R.id.ProfilePicName);
        profilename.setText(usersName);

        pd = new ProgressDialog(this);

        StorageReference profpic = storage.child("Users/"+usersName+"/ProfilePic");
        Glide.with(this)
                .load(profpic)
                .into(profilepicture);

        // Animation Start
        fadein.setDuration(1500);
        fadein.setFillAfter(true);
        ChangePassword.startAnimation(fadein);
        logoutButton.startAnimation(fadein);
        // Animation End

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

        profilepicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,2);
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            pd.setMessage("Uploading Photo");
            pd.show();
            Uri uri = data.getData();
            StorageReference filepath = storage.child("Users").child(usersName).child("ProfilePic");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(UserProfilePage.this, "Upload Done", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        }
    }

}
