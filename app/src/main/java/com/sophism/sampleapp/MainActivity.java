package com.sophism.sampleapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sophism.sampleapp.fragments.FragmentRetrofitSample;

/**
 * Created by D.H.KIM on 2016. 1. 13.
 */
public class MainActivity extends Activity implements View.OnClickListener{

    private Fragment mFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView menu_item_retrofit = (TextView) findViewById(R.id.menu_item_retrofit);
        menu_item_retrofit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        switch(v.getId()) {
            case R.id.menu_item_retrofit:
                mFragment = new FragmentRetrofitSample();
                ft.replace(R.id.content_frame, mFragment);
                ft.commit();
                break;
        }
    }
}
