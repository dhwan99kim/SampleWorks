package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sophism.sampleapp.R;

/**
 * Created by D.H.KIM on 2016. 1. 14.
 */
public class FragmentRetrofitSample extends Fragment implements View.OnClickListener{

    public FragmentRetrofitSample(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_retrofit_sample, container, false);

        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.postal_search_btn:
                searchPostalCode();
                break;
        }
    }

    private void searchPostalCode(){

    }
}
