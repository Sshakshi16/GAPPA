package com.example.gappa.Models;

public class MessageModel {

    String uId,msg,messageId;
    long time;

    public MessageModel()
    {

    }

    public MessageModel(String uId, String msg, long time) {
        this.uId = uId;
        this.msg = msg;
        this.time = time;
    }

    public MessageModel(String uId, String msg) {
        this.uId = uId;
        this.msg = msg;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public MessageModel(String msg) {
        this.msg = msg;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
