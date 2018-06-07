package com.CS100MessagingApp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

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
    ArrayList<AllKindsOfChat> al = new ArrayList<>();
    int totalUsers = 0;
    ProgressDialog pd;
    String tempDate,key;


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

        // If the array list is not empty clear
        if(!al.isEmpty())
        {
            al.clear();
        }
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
                UserDetails.CurrentGroup = al.get(position).getGroupName();


                if (al.get(position).getStartdate().equals("")) {

                    startActivity(new Intent(getActivity(), GroupChat.class));
                }else if (Integer.parseInt(al.get(position).getStartdate())<=Integer.parseInt(getCurrentDate())
                        && Integer.parseInt(al.get(position).getEndDate())>=Integer.parseInt(getCurrentDate())){

                    Toast.makeText(getActivity(),"You Can Chat!\nStartDate: "+al.get(position).getStartdate()+"\nEndDate: "
                            +al.get(position).getEndDate()+"\nCurrent Date:"+getCurrentDate(),Toast.LENGTH_LONG).show();

                    startActivity(new Intent(getActivity(), GroupChat.class));
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                    builder.setMessage("You can chat!\nStartDate: "+al.get(position).getStartdate()+"\nEndDate: "+al.get(position).getEndDate())
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    startActivity(new Intent(getActivity(), GroupChat.class));
//                                }
//                            });
                }else{

                    Toast.makeText(getActivity(),"Cannot Chat Now!\nStartDate: "+al.get(position).getStartdate()+"\nEndDate: "
                            +al.get(position).getEndDate()+"\nCurrent Date:"+getCurrentDate(),Toast.LENGTH_LONG).show();
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getSupportFragmentManager().this);
//                    builder.setMessage("You CANNOT chat!\nStartDate: "+al.get(position).getStartdate()+"\nEndDate: "+al.get(position).getEndDate())
//                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            });

                }
            }

        });

        return v;
    }

    public String getCurrentDate(){
        Calendar cl = Calendar.getInstance();
        int year = cl.get(Calendar.YEAR);
        int month = cl.get(Calendar.MONTH);
        int day = cl.get(Calendar.DAY_OF_MONTH);

        String days;
        if (month + 1 < 10) {
            if (day < 10) {
                days = new StringBuffer().append(year).append("0").
                        append(month + 1).append("0").append(day).toString();
            } else {
                days = new StringBuffer().append(year).append("0").
                        append(month + 1).append(day).toString();
            }

        } else {
            if (day < 10) {
                days = new StringBuffer().append(year).
                        append(month + 1).append("0").append(day).toString();
            } else {
                days = new StringBuffer().append(year).
                        append(month + 1).append(day).toString();
            }

        }
        return days;
    }

    public void doOnSuccess(String s){
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
//            final String key = "";

            ArrayList<String> dateList = new ArrayList<>();
            String startDate,endDate;
            DatabaseReference ref = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/users");
            while(i.hasNext()){
                key = i.next().toString();

                tempDate = obj.getString(key);
//                tempDate = ref.child(UserDetails.username).child("groups").child(key).getKey();
                if (tempDate.equals("true")){
                    AllKindsOfChat acht = new AllKindsOfChat();
                    acht.setGroupName(key);
                    al.add(acht);
                    totalUsers++;
                }
                else{
                    // Date Chat
                    String[] buff = tempDate.split(";");
                    Log.v("tempDate",tempDate);
                    startDate = buff[0];
                    endDate = buff[1];
                    AllKindsOfChat acht = new AllKindsOfChat();
                    acht.setGroupName(key);
                    acht.setStartdate(startDate);
                    acht.setEndDate(endDate);

                    if (Integer.parseInt(startDate) > Integer.parseInt(getCurrentDate())
                            || Integer.parseInt(endDate) < Integer.parseInt(getCurrentDate())){
                        // Flag = 0 : unexpired
                        // Flag = 1 : expired
                        acht.setFlag(1);
                    }

                    al.add(acht);
                    totalUsers++;
                }

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
            ArrayList<String> tempAl = new ArrayList<>();
            for (int i=0;i<al.size();i++){
//                tempAl.add(al.get(i).getGroupName());
                if (al.get(i).getFlag()==0){
                    // This Chat is not expired
                    tempAl.add(al.get(i).getGroupName());
                }else {
                    tempAl.add(al.get(i).getGroupName()+"---------Expired");
                }

            }
            UserAreaAdapter adapter = new UserAreaAdapter(getActivity(), tempAl);
            usersList.setAdapter(adapter);
        }

        pd.dismiss();
    }


}
