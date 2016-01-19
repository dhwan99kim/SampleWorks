package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.sophism.sampleapp.AppDefine;
import com.sophism.sampleapp.R;
import com.sophism.sampleapp.data.PostItemList;

import java.net.URLEncoder;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.SimpleXMLConverter;
import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by D.H.KIM on 2016. 1. 14.
 */
public class FragmentRetrofitSample extends Fragment implements View.OnClickListener{

    final String TAG = "FragmentRetrofitSample";
    EditText mSearchEditText;
    public FragmentRetrofitSample(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle instance) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_retrofit_sample, container, false);

        mSearchEditText = (EditText) rootView.findViewById(R.id.postal_search_edittext);

        Button postal_search_btn = (Button) rootView.findViewById(R.id.postal_search_btn);
        postal_search_btn.setOnClickListener(this);
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
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(PostCodeSearchService.API_URL)
                .setConverter(new SimpleXMLConverter())
                .build();

        try {
            String searchKey = URLEncoder.encode(mSearchEditText.getText().toString(), "EUC-KR");
            restAdapter.create(PostCodeSearchService.class).postItems(searchKey, new Callback<PostItemList>() {

                @Override
                public void success(PostItemList contributors, Response response) {

                    Log.d(TAG, "Success");
                }

                @Override
                public void failure(RetrofitError error) {

                    Log.d(TAG, error.toString());
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public interface PostCodeSearchService {

        public static final String API_URL = "http://biz.epost.go.kr/KpostPortal";

        @GET("/openapi?regkey="+ AppDefine.POST_API_KEY+"&target=post")
        void postItems(
                @Query(value = "query", encodeValue = false) String searchKey,
                Callback<PostItemList> callback
        );
    }

}
