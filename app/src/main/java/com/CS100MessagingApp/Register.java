package com.CS100MessagingApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        registerButton = (Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Gets Username from text field in registration page
                user = username.getText().toString();
                //Gets password from text field in registration page
                pass = password.getText().toString();
                //Checks if username is empty
                if(user.equals("")){
                    username.setError("can't be blank");
                }
                //Checks if pw is empty
                else if(pass.equals("")){
                    password.setError("can't be blank");
                }
                //Checks if username fits criteria
                else if(!user.matches("[A-Za-z0-9]+")){
                    username.setError("Only the Alphabet or numbers allowed");
                }
                //Checks if username and pw is under 5 letters
                else if(user.length()<5){
                    username.setError("Needs to be at least 5 characters long");
                }
                else if(pass.length()<5){
                    password.setError("Needs to be at least 5 characters long");
                }
                else {
                    //creates a progressdialog to denote that process is loading
                    final ProgressDialog pd = new ProgressDialog(Register.this);
                    pd.setMessage("Loading...");
                    pd.show();
                    // URL to firebase user data json
                    String url = "https://messaging-app-cs100.firebaseio.com/users.json";
                    // Requests to firebase user data json
                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            //Referencing user area of database
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/users");
                            // Checks if User database is null
                            if(s.equals("null")) {
                                //Creates a node USER with bio and pw
                                reference.child(user).child("password").setValue(pass);
                                reference.child(user).child("bio").setValue("This is a bio.");
                                Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                            }
                            else {
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    // Checks if Database contains username
                                    if (!obj.has(user)) {
                                        //Creates a node USER with bio and pw
                                        reference.child(user).child("password").setValue(pass);
                                        reference.child(user).child("bio").setValue("This is a bio.");
                                        Toast.makeText(Register.this, "Registration successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_LONG).show();
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
                            System.out.println("" + volleyError );
                            pd.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }
            }
        });
    }
}