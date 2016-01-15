package com.sophism.sampleapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

        TextView menu_item_main = (TextView) findViewById(R.id.menu_item_main);
        menu_item_main.setOnClickListener(this);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        mFragment = new FragmentParseSample();
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
            case R.id.menu_item_main:
                changeFragmentFromSideMenu(new FragmentParseSample());
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
