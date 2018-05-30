package com.CS100MessagingApp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GroupRemove extends AppCompatActivity{
    ListView groupList;
    ArrayList<MyUser> al = new ArrayList<>();
    ArrayAdapter<MyUser> listAdapter;
    Button box;
    int totalUsers = 0;
    ProgressDialog pd;
    String groupID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_group);
        // Group ID List
        groupList = (ListView)findViewById(R.id.groupList);
        box = (Button)findViewById(R.id.removegroupname);
        groupList.setTextFilterEnabled(true);


        pd = new ProgressDialog(GroupRemove.this);
        pd.setMessage("Loading...");
        pd.show();

        // Find related groups in Firebase
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
        RequestQueue rQueue = Volley.newRequestQueue(GroupRemove.this);
        rQueue.add(request);

        groupList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View item,int position, long id){
                MyUser user = listAdapter.getItem( position );
                user.toggleChecked();
                MyUserHolder viewHolder = (MyUserHolder) item.getTag();
                viewHolder.getCheckBox().setChecked(user.isChecked());
            }
        });

        box.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View view)
            {
                DatabaseReference refer = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/users");
                    for (int i = 0; i < listAdapter.getCount(); i++)
                    {
                        MyUser planet = listAdapter.getItem(i);

                        if (planet.isChecked()) {
                            Log.i(planet.getName(), "onClick: ");
                            refer.child(UserDetails.username).child("groups").child(planet.getName()).removeValue();
                        }
                    }
                    //set the currentgroup to NULL
                    UserDetails.CurrentGroup = "";
                startActivity(new Intent(GroupRemove.this,MainActivity.class));
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
                    al.add(new MyUser(key));
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
            listAdapter = new GroupArrayAdapter(this,al);
            groupList.setAdapter(listAdapter);
        }

        pd.dismiss();
    }

    /** Holds user data. */
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
    private static class GroupArrayAdapter extends ArrayAdapter<MyUser>
    {

        private LayoutInflater inflater;

        public GroupArrayAdapter(Context context, List<MyUser> users)
        {
            super(context, R.layout.groupidlistview, R.id.groupidname, users);
            // Cache the LayoutInflate to avoid asking for a new one each time.
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            // user to display
            MyUser thisuser = (MyUser) this.getItem(position);

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
                convertView.setTag(new MyUserHolder(textView, checkBox));

                // If CheckBox is toggled, update the user it is tagged with.
                checkBox.setOnClickListener(new View.OnClickListener()
                {
                    public void onClick(View v)
                    {
                        CheckBox cb = (CheckBox) v;
                        MyUser thisuser = (MyUser) cb.getTag();
                        thisuser.setChecked(cb.isChecked());
                    }
                });
            }
            // Reuse existing row view
            else
            {
                // Because we use a ViewHolder, we avoid having to call
                // findViewById().
                MyUserHolder viewHolder = (MyUserHolder) convertView
                        .getTag();
                checkBox = viewHolder.getCheckBox();
                textView = viewHolder.getTextView();
            }

            // Tag the CheckBox with the data it is displaying, so that we can
            // access the data in onClick() when the CheckBox is toggled.
            checkBox.setTag(thisuser);

            // Display data
            checkBox.setChecked(thisuser.isChecked());
            textView.setText(thisuser.getName());

            return convertView;
        }

    }

}
