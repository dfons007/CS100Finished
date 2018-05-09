package com.CS100MessagingApp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupChat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollview;
    DatabaseReference groupRef;

    /*
     Maybe this line isn't needed? I can't tell. There is an error with it and it doesn't seem to
     affect anything else without it.


     ListView 11;
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollview = (ScrollView)findViewById(R.id.scrollView);

        ListView listView = (ListView) findViewById(R.id.listview);

        final List<UserModel> users = new ArrayList<>();
        users.add(new UserModel(false, "Do"));
        users.add(new UserModel(false, "Re"));
        users.add(new UserModel(false, "Me"));
        users.add(new UserModel(false, "Fa"));
        users.add(new UserModel(false, "So"));
        users.add(new UserModel(false, "La"));
        users.add(new UserModel(false, "Ti"));

        final CustomAdapter adapter = new CustomAdapter(this, users);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long l) {

                UserModel model = users.get(i);

                if(model.isSelected())
                    model.setSelected(false);

                else
                    model.setSelected(true);

                users.set(i, model);

                // Update adapter below
                adapter.updateRecords(users);
            }
        });

        // Reference to GroupChat Message Data
       // groupRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/messages/"+UserDetails.CurrentGroupChat);

    }

}
