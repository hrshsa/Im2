package com.example.hrsoym.improject.im;

import android.app.ActivityManager;
import android.content.Context;
import android.util.Log;

import io.rong.imlib.RongIMClient;

/**
 * Created by hrsoym on 2017/9/28.
 */

public class RongImManager {
    private static RongImManager instance;

    private RongImManager() {

    }

    public static RongImManager getInstance() {
        if (instance == null) {
            synchronized (RongImManager.class) {
                if (instance == null)
                    instance = new RongImManager();
            }
        }
        return instance;
    }

    public static void connect(Context context, String token, final OnConnectListener listener) {
        if (context.getApplicationInfo().packageName.equals(getCurProcessName(context.getApplicationContext()))) {
//            RongIMClient.init(context);
            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
                @Override
                public void onTokenIncorrect() {

                }

                @Override
                public void onSuccess(String s) {
                    Log.e("userId", s);
                    if (listener != null)
                        listener.onSuccess(s);
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    Log.e("on error", errorCode.getMessage());
                    if (listener != null)
                        listener.onFail();
                }
            });
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }
}
