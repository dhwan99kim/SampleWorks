package com.sophism.sampleapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

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
                    //Toast.makeText(mContext,"New Message",Toast.LENGTH_SHORT).show();
                    JSONObject data = (JSONObject) args[0];
                    /*String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }*/
                    generateChatNotification(mContext,data);
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

    private void generateChatNotification(Context context, JSONObject object) {
        try {
            Intent intent = new Intent(context, MainActivity.class);
            intent.putExtra("isFromChatNoti",true);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);

            NotificationManager mNotifM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(object.getString("username")).setContentText(object.getString("message")).setAutoCancel(true);

            mBuilder.setContentIntent(contentIntent);
            mNotifM.notify(1, mBuilder.build());
        }catch(Exception e){
            e.printStackTrace();
        }

    }
}
