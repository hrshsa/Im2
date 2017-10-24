package com.example.hrsoym.improject.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hrsoym.improject.APP;
import com.example.hrsoym.improject.R;

import java.util.ArrayList;
import java.util.List;

import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.message.TextMessage;

/**
 * Created by hrsoym on 2017/10/10.
 */

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    private String targetId;
    List<ListUserBean> bean = new ArrayList<>();
    private TextView mSendTv;
    private EditText mContentEt;
    private MessageListAdapter messageListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_ll);
        initData();
        initView();
        initListener();
    }

    private void initListener() {
        mSendTv.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        targetId = intent.getStringExtra(APP.CONVERSATION_ID);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.message_list);
        mSendTv = (TextView) findViewById(R.id.send_tv);
        mContentEt = (EditText) findViewById(R.id.content_et);
        messageListAdapter = new MessageListAdapter(this);
        listView.setAdapter(messageListAdapter);
        RongIMClient.getInstance().getHistoryMessages(Conversation.ConversationType.PRIVATE, targetId + "", -1, 20, new RongIMClient.ResultCallback<List<Message>>() {
            @Override
            public void onSuccess(List<Message> messages) {
                if (messages == null)
                    return;
                for (Message message : messages) {
                    ListUserBean users = new ListUserBean();
                    TextMessage content = (TextMessage) message.getContent();
                    users.setContent(content.getContent());
                    users.setSenderId(message.getSenderUserId());
                    Log.e("getHistoryMessages", content.getContent());
                    bean.add(users);
                }
                messageListAdapter.setData(bean);
                messageListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("getHistoryMessages fail", errorCode.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.send_tv) {
            sendMessage();
        }
    }

    private void sendMessage() {
        String text = mContentEt.getText().toString();
        if (text.equals(""))
            return;
        mContentEt.setText("");
        TextMessage message = TextMessage.obtain(text);
        RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, targetId, message, null, null, new IRongCallback.ISendMessageCallback() {
            @Override
            public void onAttached(Message message) {

            }

            @Override
            public void onSuccess(Message message) {
                // 消息发送成功的回调
                MessageContent messageContent = message.getContent();
                Log.e("send message", "on success " + ((TextMessage) messageContent).getContent());
                List<ListUserBean> bean = new ArrayList<>();
                TextMessage content = (TextMessage) messageContent;
                ListUserBean users = new ListUserBean();
                users.setContent(content.getContent());
                users.setSenderId(RongIMClient.getInstance().getCurrentUserId());
                bean.add(users);
                messageListAdapter.setData(bean);
                messageListAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                // 消息发送失败的回调
                Log.e("send message", "on fail " + message.getContent().toString());
            }
        });
    }
}
