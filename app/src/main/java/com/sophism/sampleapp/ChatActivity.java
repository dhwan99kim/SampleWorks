package com.sophism.sampleapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.sophism.sampleapp.fragments.FragmentChatFriendList;

/**
 * Created by D.H.KIM on 2016. 1. 25.
 */
public class ChatActivity extends Activity{

    FrameLayout fragment_holder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        //fragment_holder = (FrameLayout) findViewById(R.id.fragment_holder);
        changeFragment(new FragmentChatFriendList());

    }

    public void changeFragment(Fragment fragment){
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_holder, fragment);
        ft.commit();
    }
}
