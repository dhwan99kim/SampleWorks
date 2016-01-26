package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.sophism.sampleapp.AppDefine;
import com.sophism.sampleapp.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;

/**
 * Created by D.H.KIM on 2016. 1. 25.
 */
public class FragmentChatFriendList extends Fragment{

    private final String TAG = "FragmentChatFriendList";
    private ArrayList<String> mFriendList;
    private FriendListAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_chat_friend_list, container, false);
        mFriendList = new ArrayList<>();
        ListView listview_friend = (ListView) rootView.findViewById(R.id.listview_friend);
        mAdapter = new FriendListAdapter(getActivity());
        listview_friend.setAdapter(mAdapter);

        getFriendList();
        return rootView;
    }

    Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .registerTypeAdapter(Date.class, new DateTypeAdapter())
            .create();

    private void getFriendList(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(AppDefine.CHAT_SERVER_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        try {
            restAdapter.create(GetFriendListService.class).friendsItems(new Callback<List<Friend>>() {

                @Override
                public void success(List<Friend> friends, Response response) {
                    for (Friend item:friends){
                        mFriendList.add(item.friend);
                    }
                    mAdapter.notifyDataSetChanged();
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

    public class Friend {
        public String id;
        public String friend;
    }
    public interface GetFriendListService {

        @GET("/friends/sophism")
        void friendsItems(
                Callback <List<Friend>> callback
        );
    }
    public class FriendListAdapter extends BaseAdapter

    {
        private LayoutInflater mInflater;

        public FriendListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

        @Override
        public int getCount() {
        return mFriendList.size();
    }

        @Override
        public Object getItem(int position) {
        return mFriendList.get(position);
    }

        @Override
        public long getItemId(int position) {
        return position;
    }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.list_item_chat_friend, parent, false);
            holder.chat_friend_name = (TextView) convertView.findViewById(R.id.chat_friend_name);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.chat_friend_name.setText(mFriendList.get(position));
        return convertView;
    }

        class ViewHolder {
            TextView chat_friend_name;
        }
    }
}
