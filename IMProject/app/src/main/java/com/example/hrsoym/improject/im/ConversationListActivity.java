package com.example.hrsoym.improject.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.hrsoym.improject.APP;
import com.example.hrsoym.improject.R;

import java.util.ArrayList;
import java.util.List;

import browser.android.pcauto.com.cn.manager.imageloader.ImageLoader;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

/**
 * Created by hrsoym on 2017/10/10.
 */

public class ConversationListActivity extends AppCompatActivity implements View.OnClickListener {

    private ListView listView;
    List<ListUserBean> userList = new ArrayList<>();



    EditText editText;
    EditText userText;
    Button button;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.conversation_list_ll);
        listView = (ListView) findViewById(R.id.listview);
        ListAdapter adapter = new ListAdapter();
        editText = (EditText) findViewById(R.id.edit_text);
        userText = (EditText) findViewById(R.id.edit_text2);
        button = (Button) findViewById(R.id.submit);
        button.setOnClickListener(this);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (userList.size() > position) {
                    Intent intent = new Intent(ConversationListActivity.this, ConversationActivity.class);
                    intent.putExtra(APP.CONVERSATION_ID, userList.get(position).getTargetId());
                    startActivity(intent);
                }

            }
        });
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                if (conversations != null && conversations.size() > 0)
                    for (Conversation conversation : conversations) {
                        ListUserBean userBean = new ListUserBean();
                        String content = ((TextMessage) conversation.getLatestMessage()).getContent();
                        userBean.setContent(content);
                        userBean.setSenderId(conversation.getSenderUserId());
                        userBean.setTargetId(conversation.getTargetId());
                        userBean.setFace(conversation.getPortraitUrl());
                        userList.add(userBean);
                    }

            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("getConversationList", "fail : " + errorCode);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.submit) {
            String text = editText.getText().toString();
            String userId = userText.getText().toString();
            if (TextUtils.isEmpty(userId))
                return;
            TextMessage message = TextMessage.obtain(text);
            /**
             * <p>根据会话类型，发送消息。
             * 通过 {@link io.rong.imlib.IRongCallback.ISendMessageCallback} 中的方法回调发送的消息状态及消息体。</p>
             *
             * @param type        会话类型。
             * @param targetId    目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id 或聊天室 Id。
             * @param content     消息内容，例如 {@link TextMessage}, {@link ImageMessage}。
             * @param pushContent 当下发 push 消息时，在通知栏里会显示这个字段。
             *                    如果发送的是自定义消息，该字段必须填写，否则无法收到 push 消息。
             *                    如果发送 sdk 中默认的消息类型，例如 RC:TxtMsg, RC:VcMsg, RC:ImgMsg，则不需要填写，默认已经指定。
             * @param pushData    push 附加信息。如果设置该字段，用户在收到 push 消息时，能通过 {@link io.rong.push.notification.PushNotificationMessage#getPushData()} 方法获取。
             * @param callback    发送消息的回调。参考 {@link io.rong.imlib.IRongCallback.ISendMessageCallback}。
             *                    {@link #sendMessage(Message, String, String, IRongCallback.ISendMessageCallback)}
             */
            RongIMClient.getInstance().sendMessage(Conversation.ConversationType.PRIVATE, userId, message, null,
                    null, new IRongCallback.ISendMediaMessageCallback() {
                        @Override
                        public void onProgress(Message message, int i) {

                        }

                        @Override
                        public void onCanceled(Message message) {

                        }

                        @Override
                        public void onAttached(Message message) {
                            // 消息成功存到本地数据库的回调

                        }

                        @Override
                        public void onSuccess(Message message) {
                            // 消息发送成功的回调
                            Log.e("send message", "on success " + message.toString());
                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
                            // 消息发送失败的回调
                            Log.e("send message", "on fail " + message.getContent().toString());

                        }
                    });
        }
    }

    class ListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return userList.size();
        }

        @Override
        public Object getItem(int position) {
            return userList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = View.inflate(ConversationListActivity.this, R.layout.conversation_list_item, null);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView content = (TextView) view.findViewById(R.id.content);
            ImageView face = (ImageView) view.findViewById(R.id.face);
            if (userList != null && userList.size() > position) {
                name.setText(userList.get(position).getSenderId());
                content.setText(userList.get(position).getContent());
                ImageLoader.load(userList.get(position).getFace(), face, null, null);
            }
            return view;
        }
    }
}
