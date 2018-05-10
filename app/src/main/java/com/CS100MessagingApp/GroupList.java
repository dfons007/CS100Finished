package com.CS100MessagingApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class GroupList extends AppCompatActivity {
    ListView usersList;
    Button ChatTab, ProfileTab, PlusButton;
    TextView noUsersText;
    StorageReference storage;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Setting Layout ro activity_user
        setContentView(R.layout.activity_user);
        //Connecting to Firebase Storage
        storage = FirebaseStorage.getInstance().getReference();
        //User List
        usersList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);
        //Button
        ChatTab = (Button)findViewById(R.id.ChatTab);
        ProfileTab = (Button)findViewById(R.id.ProfileTab);
        PlusButton = (Button)findViewById(R.id.PlusButton);
        ChatTab.setText("Chat");

        pd = new ProgressDialog(GroupList.this);
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://messaging-app-cs100.firebaseio.com/users/"+UserDetails.username+"/groups.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>(){
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        },new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(GroupList.this);
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.CurrentGroup = al.get(position);
                startActivity(new Intent(GroupList.this, GroupChat.class));
            }
        });

        ProfileTab.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v){
                startActivity(new Intent(GroupList.this,UserProfilePage.class));
            }
        } );

        PlusButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GroupList.this, SelectUsers.class));
            }
        });
        ChatTab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startActivity(new Intent(GroupList.this,Users.class));
            }
        });

    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while(i.hasNext()){
                key = i.next().toString();
                al.add(key);
                totalUsers++;
            }
            Log.i("Groupname:",key);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers < 1){
            //Checks to see if there are any users.
            noUsersText.setVisibility(View.VISIBLE);
            usersList.setVisibility(View.GONE);
        }
        else{
            noUsersText.setVisibility(View.GONE);
            usersList.setVisibility(View.VISIBLE);
            //Setting Adapter for Custom List View Takes in AL an ArrayList of Strings
            UserAreaAdapter adapter = new UserAreaAdapter(this, al);
            usersList.setAdapter(adapter);
        }

        pd.dismiss();
    }
}