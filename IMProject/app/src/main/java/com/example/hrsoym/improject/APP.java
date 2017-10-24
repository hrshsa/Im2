package com.example.hrsoym.improject;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import browser.android.pcauto.com.cn.manager.imageloader.ImageLoader;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;


/**
 * Created by hrsoym on 2017/9/24.
 */

public class APP extends Application implements RongIMClient.ConnectionStatusListener, RongIMClient.OnReceiveMessageListener {

    public static String CONVERSATION_ID = "conversation_id";
    @Override
    public void onCreate() {
        super.onCreate();
        RongIMClient.init(this);
        initImListener();
         ImageLoader.init(this);
    }

    private void initImListener() {
        RongIMClient.setConnectionStatusListener(this);
        RongIMClient.setOnReceiveMessageListener(this);
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        Log.e("RongIMClient", "msg: " + connectionStatus.getMessage());
        if (connectionStatus.equals(ConnectionStatus.KICKED_OFFLINE_BY_OTHER_CLIENT)) {
            Toast.makeText(this, "用户在其他地方登陆，已退出", Toast.LENGTH_LONG).show();
            RongIMClient.getInstance().logout();
        }
    }

    @Override
    public boolean onReceived(Message message, int i) {
        MessageContent messageContent = message.getContent();
        Log.e("on Receive", ((TextMessage) messageContent).getContent().toString() + " id" + message.getTargetId());
        return false;
    }
}
