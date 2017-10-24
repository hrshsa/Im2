package com.example.hrsoym.improject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.hrsoym.improject.im.ConversationActivity;
import com.example.hrsoym.improject.im.ConversationListActivity;
import com.example.hrsoym.improject.im.OnConnectListener;
import com.example.hrsoym.improject.im.RongImManager;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "RongIm";
    //id=10086
    public static String token1 = "THjWgg4K7Y2pqsJviriYV5MuG5bM9yH3xLlBgGob+A6LkCRBzunrcpDJET2+aDJHZltaFJa1fTnvlH/kRTR53Q==";
    //id=10010
    public static String token2 = "vH9m2IpQBpJ0TBvbzoAbmnYgFZYwEKy7U6sWxTjYw1M0r68xzPGgE8QFk8Uc4WreIG4g4mL1vjt2xppohHMNdA==";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void connect(View view) {
        OnConnectListener listener = new OnConnectListener() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "on success " + s);
                startActivity(new Intent(MainActivity.this, ConversationListActivity.class));
            }

            @Override
            public void onFail() {
                Log.e(TAG, "on fail");
            }
        };
        RongImManager.connect(getApplicationContext(), token1, listener);
    }

    public void connect2(View view) {
        OnConnectListener listener = new OnConnectListener() {
            @Override
            public void onSuccess(String s) {
                Log.e(TAG, "on success " + s);
                startActivity(new Intent(MainActivity.this, ConversationListActivity.class));
            }

            @Override
            public void onFail() {
                Log.e(TAG, "on fail");
            }
        };
        RongImManager.connect(getApplicationContext(), token2, listener);
    }
}
