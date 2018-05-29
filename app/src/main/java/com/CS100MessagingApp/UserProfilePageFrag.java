package com.CS100MessagingApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link UserProfilePageFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link UserProfilePageFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserProfilePageFrag extends Fragment {
    Button ChangePassword, logoutButton;
    ImageView profilepicture,EditBioImage;
    TextView profilename;
    EditText profile_bio;
    String usersName = UserDetails.username;
    private static int GALLERY_INTENT = 2;
    private StorageReference storage;
    private ProgressDialog pd;

    public UserProfilePageFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_user_profile_page, container, false);
        storage = FirebaseStorage.getInstance().getReference();

        AlphaAnimation fadein = new AlphaAnimation(0.0f,1.0f);

        ChangePassword = (Button)v.findViewById(R.id.button_changepw);
        logoutButton = (Button)v.findViewById(R.id.button_logout);
        profilepicture = (ImageView)v.findViewById(R.id.ProPic);
        profilename =(TextView)v.findViewById(R.id.ProfilePicName);
        profile_bio = (EditText)v.findViewById(R.id.bio_textView);
        profilename.setText(usersName);
        EditBioImage = (ImageView)v.findViewById(R.id.Edit_imageView);

        if (!UserDetails.bio.isEmpty()){
            profile_bio.setText(UserDetails.bio);
        }

        pd = new ProgressDialog(getActivity());

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
                startActivity(new Intent(getActivity(),ResetPassword.class));
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(),Login.class);
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
                final ProgressDialog pd = new ProgressDialog(getActivity());
                pd.setMessage("Update Bio..");
                pd.show();
                // URL to firebase user data json
                String url = "https://messaging-app-cs100.firebaseio.com/users.json";
                // Request to firebase user data json
                StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String s) {
                        if(s.equals("null")){
                            Toast.makeText(getActivity(), "Fail to request from Firebase", Toast.LENGTH_LONG).show();
                        }
                        else{
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/users");
                            reference.child(usersName).child("bio").setValue(profile_bio.getText().toString());
                            Toast.makeText(getActivity(),"Update Bio successfully", Toast.LENGTH_LONG).show();
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
                RequestQueue rQueue = Volley.newRequestQueue(getActivity());
                rQueue.add(request);
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            pd.setMessage("Uploading Photo");
            pd.show();
            Uri uri = data.getData();
            StorageReference filepath = storage.child("Users").child(usersName).child("ProfilePic");

            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getActivity(), "Upload Done", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            });
        }
    }


}
