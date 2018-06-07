package com.CS100MessagingApp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

public class CreateDateChat extends AppCompatActivity {
    ListView groupList;
    ArrayList<CreateDateChat.MyUser> al = new ArrayList<>();
    ArrayAdapter<CreateDateChat.MyUser> listAdapter;
    Button box, startDateBtn, endDateBtn;
    int totalUsers = 0;
    ProgressDialog pd;
    EditText groupstring;
    String groupID = "";
    DatabaseReference groupidcheck;
    Calendar calendar;
    int year,month,day, flag=-1;
    String startDate="", endDate="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_date_chat);

        // Group ID List
        groupList = (ListView)findViewById(R.id.datechat_groupList);
        box = (Button)findViewById(R.id.datechat_getgroupname);
        startDateBtn = (Button)findViewById(R.id.StartDateBtn);
        endDateBtn = (Button)findViewById(R.id.EndDateBtn);
        groupstring = (EditText)findViewById(R.id.datechat_groupname);
        groupList.setTextFilterEnabled(true);

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);


        pd = new ProgressDialog(CreateDateChat.this);
        pd.setMessage("Loading...");
        pd.show();
        // If the array list is not empty clear
        if(!al.isEmpty())
        {
            al.clear();
        }

        // Firebase GROUPID
        String url = "https://messaging-app-cs100.firebaseio.com/users.json";
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
        RequestQueue rQueue = Volley.newRequestQueue(CreateDateChat.this);
        rQueue.add(request);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View item, int position, long id){
                CreateDateChat.MyUser user = listAdapter.getItem( position );
                user.toggleChecked();
                CreateDateChat.MyUserHolder viewHolder = (CreateDateChat.MyUserHolder) item.getTag();
                viewHolder.getCheckBox().setChecked(user.isChecked());
            }
        });

        startDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog( CreateDateChat.this,onStartDateSetListener, year, month,day).show();
            }
        });

        endDateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreateDateChat.this,onEndDateSetListener,year,month,day).show();
            }
        });


        box.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                final DatabaseReference refer = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/users");
                groupID = groupstring.getText().toString();
                Log.v("GroupID",groupID);
                if(groupID.equals("")){
                    groupstring.setError("Group Name cannot be empty");
                } else if (startDate.isEmpty() || endDate.isEmpty()){
                    Log.v("IF:::::::",startDate+";;;"+endDate);
                    groupstring.setError("Start&End date cannot be empty");
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreateDateChat.this);
                    builder.setMessage("Date Chat: "+groupID+"\n  Start: "+startDate+"\n  End:  "+endDate)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    flag = 1;

                                        for (int i = 0; i < listAdapter.getCount(); i++)
                                        {
                                            CreateDateChat.MyUser thisuser = listAdapter.getItem(i);

                                            if (thisuser.isChecked()) {
                                                Log.i(thisuser.getName(), "onClick: ");
                                                refer.child(thisuser.getName()).child("groups").child(groupID).setValue(startDate+";"+endDate);
                                            }
                                        }
                                        refer.child(UserDetails.username).child("groups").child(groupID).setValue(startDate+";"+endDate);

                                        UserDetails.CurrentGroup = groupID;
                                        groupidcheck = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/messages");
                                        // Checking if Group ID is unique
                                        groupidcheck.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                Log.i(groupID,"groupID:");
                                                if(dataSnapshot.child("/"+groupID).exists())
                                                {
                                                    groupstring.setError("This Group already exists. Please choose a different name for your group.");
                                                    return;
                                                }else {
                                                    startActivity(new Intent(CreateDateChat.this, MainActivity.class));
                                                    Toast.makeText(CreateDateChat.this,"Create Chat Successfully",Toast.LENGTH_LONG).show();

                                                }
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    flag = 0;
                                    Toast.makeText(CreateDateChat.this,"Date Chat is Canceld",Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.create().show();

                }

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
                if(!key.equals(UserDetails.username)) {
                    al.add(new CreateDateChat.MyUser(key));
                };
                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(totalUsers < 1){
            //Checks to see if there are any users.
            groupList.setVisibility(View.GONE);
        }
        else{
            groupList.setVisibility(View.VISIBLE);
            //Setting Adapter for Custom List View Takes in AL an ArrayList of Strings
            listAdapter = new CreateDateChat.GroupArrayAdapter(CreateDateChat.this,al);
            groupList.setAdapter(listAdapter);
        }

        pd.dismiss();
    }


    private DatePickerDialog.OnDateSetListener onStartDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
            year = Year;
            month = monthOfYear;
            day = dayOfMonth;
            String days;
            if (month + 1 < 10) {
                if (day < 10) {
//                    days = new StringBuffer().append(year).append("-").append("0").
//                            append(month + 1).append("-").append("0").append(day).toString();
                    days = new StringBuffer().append(year).append("0").
                            append(month + 1).append("0").append(day).toString();
                } else {
//                    days = new StringBuffer().append(year).append("-").append("0").
//                            append(month + 1).append("-").append(day).toString();
                    days = new StringBuffer().append(year).append("0").
                            append(month + 1).append(day).toString();
                }

            } else {
                if (day < 10) {
//                    days = new StringBuffer().append(year).append("-").
//                            append(month + 1).append("-").append("0").append(day).toString();
                    days = new StringBuffer().append(year).
                            append(month + 1).append("0").append(day).toString();
                } else {
//                    days = new StringBuffer().append(year).append("-").
//                            append(month + 1).append("-").append(day).toString();
                    days = new StringBuffer().append(year).
                            append(month + 1).append(day).toString();
                }

            }
            startDate = days;
            Log.v("StartDay",startDate);
        }
    };

    private DatePickerDialog.OnDateSetListener onEndDateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int Year, int monthOfYear, int dayOfMonth) {
            year = Year;
            month = monthOfYear;
            day = dayOfMonth;
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
            endDate = days;
            Log.v("EndDay:::::::",endDate);
        }
    };



    /** Holds User data. */
    private static class MyUser
    {
        private String name = "";
        private boolean checked = false;

        public MyUser()
        {
        }

        public MyUser(String name)
        {
            this.name = name;
        }

        public MyUser(String name, boolean checked)
        {
            this.name = name;
            this.checked = checked;
        }

        public String getName()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public boolean isChecked()
        {
            return checked;
        }

        public void setChecked(boolean checked)
        {
            this.checked = checked;
        }

        public String toString()
        {
            return name;
        }

        public void toggleChecked()
        {
            checked = !checked;
        }
    }

    /** Holds child views for one row. */
    private static class MyUserHolder
    {
        private CheckBox checkBox;
        private TextView textView;

        public MyUserHolder()
        {
        }

        public MyUserHolder(TextView textView, CheckBox checkBox)
        {
            this.checkBox = checkBox;
            this.textView = textView;
        }

        public CheckBox getCheckBox()
        {
            return checkBox;
        }

        public void setCheckBox(CheckBox checkBox)
        {
            this.checkBox = checkBox;
        }

        public TextView getTextView()
        {
            return textView;
        }

        public void setTextView(TextView textView)
        {
            this.textView = textView;
        }
    }

    /** Custom adapter for displaying an array of user objects. */
    private static class GroupArrayAdapter extends ArrayAdapter<CreateDateChat.MyUser>
    {

        private LayoutInflater inflater;

        public GroupArrayAdapter(Context context, List<CreateDateChat.MyUser> users)
        {
            super(context, R.layout.groupidlistview, R.id.groupidname, users);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // User to display
            CreateDateChat.MyUser thisuser = (CreateDateChat.MyUser) this.getItem(position);

            // The child views in each row.
            CheckBox checkBox;
            TextView textView;

            // Create a new row view
            if (convertView == null)
            {
                convertView = inflater.inflate(R.layout.groupidlistview, null);

                // Find the child views.
                textView = (TextView) convertView
                        .findViewById(R.id.groupidname);
                checkBox = (CheckBox) convertView.findViewById(R.id.checkbox);

                // Optimization: Tag the row with it's child views, so we don't
                // have to
                // call findViewById() later when we reuse the row.
                convertView.setTag(new CreateDateChat.MyUserHolder(textView, checkBox));

                // If CheckBox is toggled, update the user it is tagged with.
                checkBox.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        CheckBox cb = (CheckBox) v;
                        CreateDateChat.MyUser thisuser = (CreateDateChat.MyUser) cb.getTag();
                        thisuser.setChecked(cb.isChecked());
                    }
                });
            }
            // Reuse existing row view
            else
            {
                // Because we use a ViewHolder, we avoid having to call
                // findViewById().
                CreateDateChat.MyUserHolder viewHolder = (CreateDateChat.MyUserHolder) convertView
                        .getTag();
                checkBox = viewHolder.getCheckBox();
                textView = viewHolder.getTextView();
            }

            // Tag the CheckBox with the user it is displaying, so that we can
            // access the user in onClick() when the CheckBox is toggled.
            checkBox.setTag(thisuser);

            // Display User data
            checkBox.setChecked(thisuser.isChecked());
            textView.setText(thisuser.getName());

            return convertView;
        }

    }



}

