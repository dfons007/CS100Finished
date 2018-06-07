package com.CS100MessagingApp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

@SuppressWarnings("ResourceType")
public class AdvancedOptionsFrag extends Fragment {
    Button DateChat_Button, TopicChat_Button, ReactionChat_Button;
    private StorageReference storage;

    //Required empty public constructor
    public AdvancedOptionsFrag(){}

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_advanced_options, container, false);
        storage = FirebaseStorage.getInstance().getReference();

        AlphaAnimation fadein = new AlphaAnimation(0.0f,1.0f);

        DateChat_Button = (Button)v.findViewById(R.id.button_dateChat);
        TopicChat_Button = (Button)v.findViewById(R.id.button_topicChat);
        ReactionChat_Button = (Button)v.findViewById(R.id.button_reactionChat);


        // Animation Start
        fadein.setDuration(1500);
        fadein.setFillAfter(true);
        DateChat_Button.startAnimation(fadein);
        TopicChat_Button.startAnimation(fadein);
        ReactionChat_Button.startAnimation(fadein);
        // Animation End

        DateChat_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), DateChatFrag.class));
//                FragmentManager fm = getActivity().getSupportFragmentManager();
//                Fragment fragment = new DateChatFrag();
//                fm.beginTransaction().replace(R.layout.fragment_date_chat,fragment).commit();
                startActivity(new Intent(getActivity(),CreateDateChat.class));
            }
        });

        TopicChat_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Unavailable Now.\nPLZ Try Limit Time Chat",Toast.LENGTH_LONG).show();
            }
        });

        ReactionChat_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Unavailable Now.\nPLZ Try Limit Time Chat",Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }
}
