package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.sophism.sampleapp.R;

/**
 * Created by D.H.KIM on 2016. 1. 15.
 */
public class FragmentParseSample extends Fragment implements View.OnClickListener {

    final String TAG = "FragmentMain";
    private EditText mInPutEditText;
    private TextView mInPutResult;
    Handler mHandler;
    ScrollView scrollView;
    int bannerId=0;
    public FragmentParseSample() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_parse_sample, container, false);

        Button parse_push_all = (Button) rootView.findViewById(R.id.parse_push_all);
        parse_push_all.setOnClickListener(this);
        Button parse_push_target = (Button) rootView.findViewById(R.id.parse_push_target);
        parse_push_target.setOnClickListener(this);

        mInPutEditText = (EditText) rootView.findViewById(R.id.input_edittext);
        mInPutEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mInPutResult.setText("");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mInPutResult = (TextView) rootView.findViewById(R.id.input_result_textview);
        Button input_submit_btn = (Button) rootView.findViewById(R.id.input_submit_btn);
        input_submit_btn.setOnClickListener(this);

        scrollView = (ScrollView) rootView.findViewById(R.id.scroll);
        scrollView.setOnTouchListener(null);
        rollBanner();
        return rootView;
    }

    private void rollBanner(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                scrollView.smoothScrollTo(0,msg.what*240);

            }
        };
        mHandler.postDelayed(rollBanner, 2000);
    }

    private Runnable rollBanner = new Runnable() {
        public void run() {
            if (bannerId >1)
                bannerId = 0;
            else
                bannerId += 1;
            mHandler.sendEmptyMessage(bannerId);
            mHandler.postDelayed(this, 2000);
        }
    };

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.parse_push_all:
                sendParsePush();
                break;
            case R.id.parse_push_target:
                sendParsePush("sophism");
                break;
            case R.id.input_submit_btn:
                saveDataInParseCore(mInPutEditText.getText().toString());
                break;
        }
    }

    private void sendParsePush(){
        sendParsePush(null);
    }

    private void sendParsePush(String targetId){
        ParsePush push = new ParsePush();
        if (targetId != null) {
            ParseQuery<ParseInstallation> pQuery = ParseInstallation.getQuery();
            pQuery.whereEqualTo("device_id", "sophism");
            push.setQuery(pQuery);
            push.setMessage("PUSH Message Target sophism");
        }else
            push.setMessage("PUSH Message All");
        push.sendInBackground();
    }

    private void saveDataInParseCore(String text){
        ParseObject object = new ParseObject("Data");
        object.put("value",text);
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                mInPutResult.setText("저장 완료");
            }
        });
    }
}