package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.sophism.sampleapp.AppDefine;
import com.sophism.sampleapp.R;
import com.sophism.sampleapp.dialogs.DialogInputText;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.POST;
import retrofit.mime.TypedByteArray;
import retrofit.mime.TypedInput;

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

        Button btn_add = (Button) rootView.findViewById(R.id.btn_add);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DialogInputText dialogInputText = new DialogInputText(getActivity(),"추가할 친구 아이디를 입력하세요");
                dialogInputText.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialogInputText.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        String input = dialogInputText.getValue();
                        if (input != null && input.length() != 0)
                            addFriendList("sophism", input);
                    }
                });
                dialogInputText.show();
            }
        });
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
                    mFriendList = new ArrayList<>();
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

    private void addFriendList(String myId, String targetId){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(AppDefine.CHAT_SERVER_URL)
                .setConverter(new GsonConverter(gson))
                .build();
        JSONObject object = new JSONObject();
        try {
            object.put("myId", myId);
            object.put("targetId", targetId);
        }catch (Exception e){
            e.printStackTrace();
        }
        String json = object.toString();
        try {
            TypedInput in = new TypedByteArray("application/json", json.getBytes("UTF-8"));
            restAdapter.create(AddFriendListService.class).addFriend(in, new Callback<Friend>() {

                @Override
                public void success(Friend friend, Response response) {
                    Log.d(TAG,"Success");
                    getFriendList();
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.e(TAG, error.toString());
                }
            });
        }catch(Exception e){
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

    public interface AddFriendListService {

        @POST("/friends/sophism")
        @Headers({"Content-Type: application/json;charset=UTF-8"})
        void addFriend(
                @Body TypedInput object,
                Callback<Friend> callback
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
