package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.sophism.sampleapp.R;

import org.json.JSONObject;

/**
 * Created by D.H.KIM on 2016. 1. 15.
 */
public class FragmentMain extends Fragment implements View.OnClickListener {

    final String TAG = "FragmentMain";

    public FragmentMain() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_main, container, false);

        Button parse_push_all = (Button) rootView.findViewById(R.id.parse_push_all);
        parse_push_all.setOnClickListener(this);
        Button parse_push_target = (Button) rootView.findViewById(R.id.parse_push_target);
        parse_push_target.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.parse_push_all:
                sendParsePush();
                break;
            case R.id.parse_push_target:
                sendParsePush("sophism");
                break;
        }
    }

    private void sendParsePush(){
        sendParsePush(null);
    }

    private void sendParsePush(String targetId){
        ParsePush push = new ParsePush();
        if (targetId != null) {
            ParseQuery pQuery = ParseInstallation.getQuery();
            pQuery.whereEqualTo("device_id", "sophism");
            push.setQuery(pQuery);
            push.setMessage("PUSH Message Target sophism");
        }else
            push.setMessage("PUSH Message All");
        push.sendInBackground();
    }
}