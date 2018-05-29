package com.CS100MessagingApp;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONException;
import org.json.JSONObject;


public class ResetPassword extends AppCompatActivity {
    //TextView registerUser, LoginText;
    EditText oldText, newText;
    Button submit_button,cancel_button;
    String old_pass, new_pass,user,pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        AlphaAnimation fadein = new AlphaAnimation(0.0f,1.0f);

        oldText = (EditText)findViewById(R.id.editText4);
        newText = (EditText)findViewById(R.id.editText2);
        submit_button = (Button)findViewById(R.id.button);
        cancel_button = (Button) findViewById(R.id.button2);

        //Fade-in Animation
        fadein.setDuration(1500);
        fadein.setFillAfter(true);

        oldText.startAnimation(fadein);
        newText.startAnimation(fadein);
        submit_button.startAnimation(fadein);
        cancel_button.startAnimation(fadein);
        //End Animation
        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = UserDetails.username;
                pass = UserDetails.password;

                old_pass = oldText.getText().toString();
                new_pass = newText.getText().toString();

                if(old_pass.equals("")){
                    oldText.setError("can't be blank");
                }
                else if(new_pass.equals("")){
                    newText.setError("can't be blank");
                }
                //Checks if username fits criteria
                else if(!old_pass.matches("[A-Za-z0-9]+")){
                    oldText.setError("Remember only the Alphabet or numbers allowed");
                }
                //Checks if username and pw is under 5 letters
                else if(old_pass.length()<5){
                    oldText.setError("Remember needs to be at least 5 characters long");
                }
                else if(newText.length()<5){
                    newText.setError("Needs to be at least 5 characters long");
                }
                else{
                    String url = "https://messaging-app-cs100.firebaseio.com/users.json";
                    final ProgressDialog pd = new ProgressDialog(ResetPassword.this);
                    pd.setMessage("Loading...");
                    pd.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            if(s.equals("null")){
                                Toast.makeText(ResetPassword.this, "user not found", Toast.LENGTH_LONG).show();
                            }
                            else{
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    if(obj.getJSONObject(user).getString("password").equals(old_pass)){
                                        DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/users");
                                        reference.child(user).child("password").setValue(new_pass);
                                        Toast.makeText(ResetPassword.this, "Password change successful", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(ResetPassword.this, MainActivity.class));
                                    }
                                    else {
                                        Toast.makeText(ResetPassword.this, "Current password incorrect", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
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
                    RequestQueue rQueue = Volley.newRequestQueue(ResetPassword.this);
                    rQueue.add(request);
                }

            }
        });

        cancel_button.setOnClickListener(new View.OnClickListener() {
                                             @Override
                                             public void onClick(View v) {
                                                 startActivity(new Intent(ResetPassword.this, MainActivity.class));
                                             }
                                         }
        );
    }
}
