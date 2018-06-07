package com.CS100MessagingApp;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity{
    private FragmentTabHost tabhost;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ui_for_top_menu);

        tabhost = (FragmentTabHost)findViewById(R.id.myTabHost);
        // User List where you have individual chats
        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        tabhost.addTab(tabhost.newTabSpec("Users").setIndicator("Users"), UserFragment.class,null);
        // Create a Group Chat
        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        tabhost.addTab(tabhost.newTabSpec("Create Group").setIndicator("Add  Group"), SelectUsersFrag.class,null);
        // Visit your Profile Page
        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        tabhost.addTab(tabhost.newTabSpec("Profile").setIndicator("Myfile"), UserProfilePageFrag.class,null);
        // Access your Groups
        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        tabhost.addTab(tabhost.newTabSpec("Group").setIndicator("Group"),GroupListFrag.class,null);

        // Create a special chat
        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        tabhost.addTab(tabhost.newTabSpec("*").setIndicator("*"), AdvancedOptionsFrag.class,null);


    }

}
