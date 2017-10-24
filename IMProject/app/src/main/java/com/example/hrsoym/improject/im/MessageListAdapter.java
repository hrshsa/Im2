package com.example.hrsoym.improject.im;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hrsoym.improject.R;

import java.util.ArrayList;
import java.util.List;

import browser.android.pcauto.com.cn.manager.imageloader.ImageLoader;
import io.rong.imlib.RongIMClient;

/**
 * Created by HuangRuiShu on 2017/10/13.
 */

public class MessageListAdapter extends BaseAdapter {
    String userId;
    List<ListUserBean> users = new ArrayList<>();
    private Context context;
    LayoutInflater mInflater;

    public MessageListAdapter(Context context) {
        userId = RongIMClient.getInstance().getCurrentUserId();
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<ListUserBean> bean) {
        if (users != null) {
            users.addAll(bean);
        }
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int position) {
        return users.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = this.newView(this.context, position, parent);
        } else {
            view = convertView;
        }
        this.bindView(view, position, this.getItem(position));
        return view;
    }

    private void bindView(View view, int position, Object item) {
        ListUserBean userBean = (ListUserBean) item;
        TextView textView = (TextView) view.findViewById(R.id.text);
        ImageView face = (ImageView) view.findViewById(R.id.face);
        textView.setText(userBean.getContent());
        ImageLoader.load(userBean.getFace(), face, null, null);
    }

    private View newView(Context context, int position, ViewGroup parent) {
        if (getItemViewType(position) == 0) {
            return mInflater.inflate(R.layout.rc_conversation_item_left, null);
        }
        return mInflater.inflate(R.layout.rc_conversation_item_right, null);
    }

    @Override
    public int getItemViewType(int position) {
        if (users.size() > position) {
            if (users.get(position).getSenderId().equals(userId))
                return 1;
            else
                return 0;
        }
        return super.getItemViewType(position);
    }
}
