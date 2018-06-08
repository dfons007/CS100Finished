package com.CS100MessagingApp;

import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabWidget;

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
        tabhost.addTab(tabhost.newTabSpec("Profile").setIndicator("Profile"), UserProfilePageFrag.class,null);
        // Access your Groups
        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        tabhost.addTab(tabhost.newTabSpec("Group").setIndicator("Group"),GroupListFrag.class,null);
        // Remove Group
        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        tabhost.addTab(tabhost.newTabSpec("Remove Group").setIndicator("Remove Group"),GroupRemove.class,null );

        // Create a special chat
        tabhost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        tabhost.addTab(tabhost.newTabSpec("*").setIndicator("*"), AdvancedOptionsFrag.class,null);

        TabWidget tw = (TabWidget)findViewById(android.R.id.tabs);
        LinearLayout ll = (LinearLayout)tw.getParent();
        HorizontalScrollView hs = new HorizontalScrollView(this);
        hs.setLayoutParams(new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
        ));

        ll.addView(hs,0);
        ll.removeView(tw);
        hs.addView(tw);
        hs.setHorizontalScrollBarEnabled(false);


    }

}
