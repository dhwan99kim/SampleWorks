package com.sophism.sampleapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sophism.sampleapp.fragments.FragmentChatFriendList;
import com.sophism.sampleapp.fragments.FragmentChatMessagingList;

/**
 * Created by D.H.KIM on 2016. 1. 25.
 */
public class ChatActivity extends Activity implements View.OnClickListener{

    FrameLayout fragment_holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        TextView tab_friend = (TextView) findViewById(R.id.tab_friend);
        tab_friend.setOnClickListener(this);
        TextView tab_chat = (TextView) findViewById(R.id.tab_chat);
        tab_chat.setOnClickListener(this);
        //fragment_holder = (FrameLayout) findViewById(R.id.fragment_holder);
        changeFragment(new FragmentChatFriendList());

    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.tab_chat:
                changeFragment(new FragmentChatMessagingList());
                break;
            case R.id.tab_friend:
                changeFragment(new FragmentChatFriendList());
                break;
        }
    }

    public void changeFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_holder, fragment);
        ft.commit();
    }
}
