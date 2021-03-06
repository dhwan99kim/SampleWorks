package com.sophism.sampleapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sophism.sampleapp.fragments.FragmentBoardSample;
import com.sophism.sampleapp.fragments.FragmentChatSample;
import com.sophism.sampleapp.fragments.FragmentFlashSample;
import com.sophism.sampleapp.fragments.FragmentLoginSample;
import com.sophism.sampleapp.fragments.FragmentParseSample;
import com.sophism.sampleapp.fragments.FragmentRetrofitSample;

/**
 * Created by D.H.KIM on 2016. 1. 13.
 */
public class MainActivity extends Activity implements View.OnClickListener{

    private Fragment mFragment;
    private DrawerLayout mDrawerLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        ImageView menu_btn = (ImageView) findViewById(R.id.menu_btn);
        menu_btn.setOnClickListener(this);

        TextView menu_item_retrofit = (TextView) findViewById(R.id.menu_item_retrofit);
        menu_item_retrofit.setOnClickListener(this);

        TextView menu_item_login = (TextView) findViewById(R.id.menu_item_login);
        menu_item_login.setOnClickListener(this);

        TextView menu_item_flash = (TextView) findViewById(R.id.menu_item_flash);
        menu_item_flash.setOnClickListener(this);

        TextView menu_item_parse = (TextView) findViewById(R.id.menu_item_parse);
        menu_item_parse.setOnClickListener(this);

        TextView menu_item_board = (TextView) findViewById(R.id.menu_item_board);
        menu_item_board.setOnClickListener(this);

        TextView menu_item_chat = (TextView) findViewById(R.id.menu_item_chat);
        menu_item_chat.setOnClickListener(this);

        TextView menu_item_chat_ver2 = (TextView) findViewById(R.id.menu_item_chat_ver2);
        menu_item_chat_ver2.setOnClickListener(this);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (getIntent().getBooleanExtra("isFromChatNoti",false)) {
            mFragment = new FragmentChatSample();
            Bundle bundle = new Bundle();
            bundle.putInt("room",getIntent().getIntExtra("room",-1));
            mFragment.setArguments(bundle);
        }else{
            mFragment = new FragmentParseSample();
            startService(new Intent(this, SocketService.class));
        }

        ft.replace(R.id.content_frame, mFragment);
        ft.commit();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.menu_btn:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
            case R.id.menu_item_retrofit:
                changeFragmentFromSideMenu(new FragmentRetrofitSample());
                break;
            case R.id.menu_item_login:
                changeFragmentFromSideMenu(new FragmentLoginSample());
                break;
            case R.id.menu_item_flash:
                changeFragmentFromSideMenu(new FragmentFlashSample());
                break;
            case R.id.menu_item_parse:
                changeFragmentFromSideMenu(new FragmentParseSample());
                break;
            case R.id.menu_item_board:
                changeFragmentFromSideMenu(new FragmentBoardSample());
                break;
            case R.id.menu_item_chat:
                changeFragmentFromSideMenu(new FragmentChatSample());
                break;
            case R.id.menu_item_chat_ver2:
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
                break;
        }
    }

    public void changeFragmentFromSideMenu(Fragment fragment){
        mDrawerLayout.closeDrawers();
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mFragment = fragment;
        ft.replace(R.id.content_frame, mFragment);
        ft.commit();
    }
}
