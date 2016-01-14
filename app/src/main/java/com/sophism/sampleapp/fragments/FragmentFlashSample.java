package com.sophism.sampleapp.fragments;

import android.app.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.sophism.sampleapp.R;

/**
 * Created by D.H.KIM on 2016. 1. 14.
 */
public class FragmentFlashSample extends Fragment implements View.OnClickListener{

    final String TAG = "FragmentModuleSample";
    final int TURN_ON = 1;
    final int TURN_OFF = 0;
    private Context mContext;
    private Camera mCamera;
    Camera.Parameters mParams;
    private Button flash_on;
    private Button flash_off;
    private Button flash_blink_on;
    private Button flash_blink_off;

    boolean isFlashon;
    Handler mHandler;
    public FragmentFlashSample(){

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_flash_sample, container, false);

        flash_on = (Button) rootView.findViewById(R.id.flash_on);
        flash_on.setOnClickListener(this);
        flash_off = (Button) rootView.findViewById(R.id.flash_off);
        flash_off.setOnClickListener(this);

        flash_off.setClickable(false);

        flash_blink_on = (Button) rootView.findViewById(R.id.flash_blink_on);
        flash_blink_on.setOnClickListener(this);
        flash_blink_off = (Button) rootView.findViewById(R.id.flash_blink_off);
        flash_blink_off.setOnClickListener(this);

        mContext = getActivity();
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.flash_on:
                turnOnFlash();
                flash_on.setClickable(false);
                flash_off.setClickable(true);
                break;
            case R.id.flash_off:
                turnOffFlash();
                flash_on.setClickable(true);
                flash_off.setClickable(false);
                break;

            case R.id.flash_blink_on:
                blinkFlash();
                break;
            case R.id.flash_blink_off:
                blinkFlashOff();
                break;
        }
    }

    private void turnOnFlash() {
        if (checkFlashAvailable()) {
            mCamera = Camera.open();
            mParams = mCamera.getParameters();
            mParams.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            mCamera.setParameters(mParams);
            mCamera.startPreview();
        }
    }

    private void turnOffFlash(){
        if (checkFlashAvailable()) {
            mCamera.stopPreview();
            mCamera.release();
        }
    }



    private void blinkFlashOff(){
        if (mHandler != null) {
            mHandler.removeCallbacks(toggleFlash);
            mHandler = null;
        }
    }
    private void blinkFlash(){
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == TURN_OFF){
                    turnOffFlash();
                }else if (msg.what == TURN_ON){
                    turnOnFlash();
                }

            }
        };
        mHandler.postDelayed(toggleFlash, 200);
    }

    private Runnable toggleFlash = new Runnable() {
        public void run() {
            if(isFlashon)
            {
                mHandler.sendEmptyMessage(TURN_OFF);
                isFlashon=false;
            }
            else
            {
                mHandler.sendEmptyMessage(TURN_ON);
                isFlashon=true;
            }
            mHandler.postDelayed(this, 200);
        }
    };
    private boolean checkFlashAvailable(){
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH))
            return true;
        else{
            Toast.makeText(mContext, "Flash is not available", Toast.LENGTH_SHORT);
            return false;
        }
    }


}
