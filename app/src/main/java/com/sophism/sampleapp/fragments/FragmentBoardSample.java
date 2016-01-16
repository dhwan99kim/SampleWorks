package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sophism.sampleapp.R;

/**
 * Created by D.H.KIM on 2016. 1. 16.
 */
public class FragmentBoardSample extends Fragment {

    public FragmentBoardSample(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {

        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_board_sample, container, false);

        return rootView;
    }
}
