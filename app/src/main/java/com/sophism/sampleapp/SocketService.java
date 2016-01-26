package com.sophism.sampleapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
 * Created by D.H.KIM on 2016. 1. 26.
 */
public class SocketService extends Service{
    static public Socket mSocket;
    private Context mContext;
    private Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHandler = new Handler();
        Toast.makeText(this,"Create Socket Service",Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Toast.makeText(this,"Socket Service Started",Toast.LENGTH_SHORT).show();
        try {
            mSocket = IO.socket(AppDefine.CHAT_SERVER_URL);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on("new message", onNewMessage);
        return START_STICKY;

    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mContext,"New Message",Toast.LENGTH_SHORT).show();
                }
            };
            mHandler.post(runnable);


        }
    };
    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void runOnUiThread(Runnable runnable) {
        mHandler.post(runnable);
    }
}
