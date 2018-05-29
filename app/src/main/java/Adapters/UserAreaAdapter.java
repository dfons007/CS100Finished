package Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.CS100MessagingApp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserAreaAdapter extends ArrayAdapter<String> {
    DatabaseReference dispuser;

    public UserAreaAdapter(Context context, ArrayList<String> usersinfo)
    {
        super(context,0, usersinfo);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        // Get the data item for this position
        String user = getItem(position);
        // Reference to see if the user is online
        dispuser = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/users/"+user);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listviewlayout, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.Name);
        final ImageView onlinestatus = convertView.findViewById(R.id.GreenDot);

        // Checking if our user is online.

        dispuser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("online").exists())
                {
                    if(dataSnapshot.child("online").getValue().equals("1")) {
                        onlinestatus.setVisibility(View.VISIBLE);
                    } else
                    {
                        onlinestatus.setVisibility(View.GONE);
                    }
                } else{
                    onlinestatus.setVisibility(View.GONE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        // Populate the data into the template view using the data object
        tvName.setText(user);
        // Return the completed view to render on screen
        return convertView;

    }






}




