package com.CS100MessagingApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class UserProfilePage extends AppCompatActivity {

    Button ChangePassword, logoutButton;
    ImageView profilepicture,EditBioImage;
    TextView profilename;
    EditText profile_bio;
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
        profile_bio = (EditText) findViewById(R.id.bio_textView);
        profilename.setText(usersName);
        profile_bio.setText(UserDetails.bio);
        EditBioImage = (ImageView)findViewById(R.id.Edit_imageView);

        if (!UserDetails.bio.isEmpty()){
            profile_bio.setText(UserDetails.bio);
        }

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


        EditBioImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog pd = new ProgressDialog(UserProfilePage.this);
                pd.setMessage("Update Bio..");
                pd.show();
                // URL to firebase user data json
                String url = "https://messaging-app-cs100.firebaseio.com/users.json";
                // Request to firebase user data json
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("null")){
                            Toast.makeText(UserProfilePage.this, "Fail to request from Firebase", Toast.LENGTH_LONG).show();
                        }
                        else{
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/users");
                                reference.child(usersName).child("bio").setValue(profile_bio.getText().toString());
                                Toast.makeText(UserProfilePage.this,"Update Bio successfully", Toast.LENGTH_LONG).show();
                        }
                        pd.dismiss();
                    }
                },new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        System.out.println("" + volleyError);
                        pd.dismiss();
                    }
                });
                RequestQueue rQueue = Volley.newRequestQueue(UserProfilePage.this);
                rQueue.add(request);
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
