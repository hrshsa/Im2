package com.example.hrsoym.improject.im;

/**
 * Created by HuangRuiShu on 2017/10/13.
 * 列表显示的内容
 */

public class ListUserBean {
    private String content;
    private String targetId;
    private String senderId;
    private String face;

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}
