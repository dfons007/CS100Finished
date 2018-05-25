package com.CS100MessagingApp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class GroupChat extends AppCompatActivity {
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton,sendPicture;
    EditText messageArea;
    ScrollView scrollview;
    DatabaseReference groupRef;
    private StorageReference imageRef;
    private static int GALLERY_INTENT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout)findViewById(R.id.layout2);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        sendPicture = (ImageView)findViewById(R.id.sendPicture);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollview = (ScrollView)findViewById(R.id.scrollView);

        groupRef = FirebaseDatabase.getInstance().getReferenceFromUrl("https://messaging-app-cs100.firebaseio.com/messages/" + UserDetails.CurrentGroup);
        imageRef = FirebaseStorage.getInstance().getReference();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("user", UserDetails.username);
                    map.put("message", messageText);
                    map.put("type","text");

                    groupRef.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        sendPicture.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,2);

            }
        });

        groupRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map<String,Object>map = (Map<String,Object>)dataSnapshot.getValue();
                String message = map.get("message").toString();
                String userName = map.get("user").toString();
                String type;

                if(map.containsKey("type")){
                    type = map.get("type").toString();
                }else{
                    type = "text";
                }

                if(userName.equals(UserDetails.username)){
                    if(type.equals("image"))
                    {
                        //In this case if you use "addMessageBox("You:\n"+message,1,type);" It will lead to bug.
                        addMessageBox(message,1,type);
                    }else {
                        addMessageBox("You:\n" + message, 1,type);
                    }
                }
                else{
                    if (type.equals("text")){
                        addMessageBox(userName+":\n"+message, 2, type);
                    }else {
                        //The type of message is image
                        addMessageBox(userName+":\nI send an image.", 2, "text");
                        addMessageBox(message, 2, type);
                    }
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

    /**
     * @param type : type==1 means that the message is sent by you, and type==2 means that it is sent by your group chat members;
     * @param MessageType : It indicates the type of the current message;
     */
    public void addMessageBox(String message, int type, String MessageType){
        TextView textView = new TextView(GroupChat.this);
        ImageView myImage = new ImageView(GroupChat.this);
        //textView.setText(message);

        if(MessageType.equals("text")){
            textView.setText(message);
            myImage.setVisibility(View.GONE);
        }else if (MessageType.equals("image"))
        {
            textView.setVisibility(View.GONE);
            myImage.setVisibility(View.VISIBLE);
        }

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        // If the message is sent by you
        if(type == 1) {
            if(MessageType.equals("text")){
                //The message type is text
                lp2.gravity = Gravity.LEFT;
                textView.setBackgroundResource(R.drawable.bubble_out);
                textView.setLayoutParams(lp2);
                layout.addView(textView);
                scrollview.fullScroll(View.FOCUS_DOWN);
                myImage.setVisibility(View.GONE);
            }else{
                // The message type is Image
                lp2.gravity = Gravity.LEFT;
                myImage.setBackgroundResource(R.drawable.bubble_out);
                Glide.with(this)
                        .load(message)
                        .apply(new RequestOptions()
                                .override(190,300).centerCrop())
                        .into(myImage);
                myImage.setLayoutParams(lp2);
                layout.addView(myImage);
                scrollview.fullScroll(View.FOCUS_DOWN);
            }

        }
        else{
            //If the message is sent by other people
            if (MessageType.equals("text")){
                //The message type is text
                lp2.gravity = Gravity.RIGHT;
                textView.setBackgroundResource(R.drawable.bubble_in);
                textView.setLayoutParams(lp2);
                layout.addView(textView);
                scrollview.fullScroll(View.FOCUS_DOWN);
                myImage.setVisibility(View.GONE);
            }else {
                // The message type is Image
                lp2.gravity = Gravity.RIGHT;
                myImage.setBackgroundResource(R.drawable.bubble_in);
                Glide.with(this)
                        .load(message)
                        .apply(new RequestOptions()
                                .override(190,300).centerCrop())
                        .into(myImage);
                myImage.setLayoutParams(lp2);
                layout.addView(myImage);
                scrollview.fullScroll(View.FOCUS_DOWN);
            }
           }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, android.content.Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            // Get URI for image picked from gallery.
            android.net.Uri sentImageURI = data.getData();
            // Gets ID for what the image will be called.
            final Map messageMap = new HashMap();
            final String push_id = String.valueOf(System.identityHashCode(messageMap));
            //Gets the Filepath for the Firebase Storage.
            StorageReference filepath = imageRef.child("message_images").child(groupRef.getKey()).child(push_id+".jpg");
            //Puts the the image into the Firebase Storage.
            filepath.putFile(sentImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>(){
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task){
                    if(task.isSuccessful()){
                        //Gets a URL for the Image.
                        String download_url = task.getResult().getDownloadUrl().toString();
                        // Sends the Image as message into the Firebase RealTime Database.
                        messageMap.put("message",download_url);
                        messageMap.put("user", UserDetails.username);
                        messageMap.put("type","image");
                        groupRef.push().setValue(messageMap);
                    }
                }


            });


        }

    }
}
