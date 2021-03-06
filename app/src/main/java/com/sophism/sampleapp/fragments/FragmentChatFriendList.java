package com.sophism.sampleapp.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.sophism.sampleapp.AppDefine;
import com.sophism.sampleapp.ChatActivity;
import com.sophism.sampleapp.MainActivity;
import com.sophism.sampleapp.R;
import com.sophism.sampleapp.SocketService;
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
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by D.H.KIM on 2016. 1. 25.
 */
public class FragmentChatFriendList extends Fragment{

    private final String TAG = "FragmentChatFriendList";
    private ArrayList<String> mFriendList;
    private FriendListAdapter mAdapter;
    private Socket mSocket = SocketService.mSocket;
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

        mSocket.on("open room", onOpenRoom);
        return rootView;
    }
    private Emitter.Listener onOpenRoom = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            if (getActivity()!= null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int roomId = Integer.valueOf(args[0].toString());
                        Intent intent = new Intent(getActivity(), MainActivity.class);
                        intent.putExtra("isFromChatNoti",true);
                        intent.putExtra("room",roomId);
                        startActivity(intent);

                    }
                });
            }
        }
    };
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
            restAdapter.create(GetFriendListService.class).friendsItems("sophism",new Callback<List<Friend>>() {

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
        try {
            restAdapter.create(AddFriendListService.class).addFriend(myId,targetId, new Callback<Friend>() {

                @Override
                public void success(Friend friend, Response response) {
                    Log.d(TAG, "add Success");
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

    private void delFriendList(String myId, String targetId){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(AppDefine.CHAT_SERVER_URL)
                .setConverter(new GsonConverter(gson))
                .build();
        try {

            restAdapter.create(DeleteFriendListService.class).deleteFriend(myId,targetId, new Callback<Friend>() {

                @Override
                public void success(Friend friend, Response response) {
                    Log.d(TAG, "delete Success");
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
        @GET("/friends/{id}")
        void friendsItems(
                @Path("id") String id, Callback <List<Friend>> callback
        );
    }

    public interface AddFriendListService {
        @POST("/friends/{id}/{target}")
        //@Headers({"Content-Type: application/json;charset=UTF-8"})
        void addFriend(
                //@Body TypedInput object,
                @Path("id")String id, @Path("target")String targetId,
                Callback<Friend> callback
        );
    }

    public interface DeleteFriendListService {
        @DELETE("/friends/{id}/{target}")
        void deleteFriend(
                @Path("id")String id, @Path("target")String targetId,
                Callback<Friend> target
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
        public View getView(final int position, View convertView, ViewGroup parent) {
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
        holder.chat_friend_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //delFriendList("sophism",mFriendList.get(position));
                mSocket.emit("invite",mFriendList.get(position));

            }
        });
        return convertView;
    }

        class ViewHolder {
            TextView chat_friend_name;
        }
    }
}
