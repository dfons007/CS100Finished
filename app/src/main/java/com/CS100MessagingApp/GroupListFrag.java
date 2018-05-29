package com.CS100MessagingApp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import Adapters.UserAreaAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GroupListFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GroupListFrag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GroupListFrag extends Fragment {
    ListView usersList;
    Button ChatTab, ProfileTab, PlusButton;
    TextView noUsersText;
    StorageReference storage;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;


    public GroupListFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.userfrag_activity_layout, container, false);
        //Connecting to Firebase Storage
        storage = FirebaseStorage.getInstance().getReference();
        //User List
        usersList = (ListView)v.findViewById(R.id.usersList);
        noUsersText = (TextView)v.findViewById(R.id.noUsersText);

        pd = new ProgressDialog(getActivity());
        pd.setMessage("Loading...");
        pd.show();

        String url = "https://messaging-app-cs100.firebaseio.com/users/"+ UserDetails.username+"/groups.json";

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

        RequestQueue rQueue = Volley.newRequestQueue(getActivity());
        rQueue.add(request);

        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.CurrentGroup = al.get(position);
                startActivity(new Intent(getActivity(), GroupChat.class));
            }
        });

        return v;
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
            UserAreaAdapter adapter = new UserAreaAdapter(getActivity(), al);
            usersList.setAdapter(adapter);
        }

        pd.dismiss();
    }


}
