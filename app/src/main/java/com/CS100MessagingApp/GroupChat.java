package com.CS100MessagingApp;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        groupRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/messages/" + UserDetails.CurrentGroup + "/msgs");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("user", UserDetails.username);
                    map.put("message", messageText);

                    groupRef.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        groupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Object>map = (Map<String,Object>)dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if(userName.equals(UserDetails.username)){
                    addMessageBox("You:\n" + message, 1);
                }
                else{
                    addMessageBox(UserDetails.CurrentGroup + ":\n" + message, 2);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(GroupChat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if(type == 1) {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        else{
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollview.fullScroll(View.FOCUS_DOWN);
    }
}
