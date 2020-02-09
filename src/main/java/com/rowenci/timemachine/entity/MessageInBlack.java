package com.rowenci.timemachine.entity;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/9 17:44
 */
public class MessageInBlack {

    private String messageId;

    private String title;

    private String context;

    private String writeTime;

    private String sendTime;

    private int isSend;

    private String time;

    private int reason;

    private String account;

    public MessageInBlack(String messageId, String title, String context, String writeTime, String sendTime, int isSend, String time, int reason, String account) {
        this.messageId = messageId;
        this.title = title;
        this.context = context;
        this.writeTime = writeTime;
        this.sendTime = sendTime;
        this.isSend = isSend;
        this.time = time;
        this.reason = reason;
        this.account = account;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getWriteTime() {
        return writeTime;
    }

    public void setWriteTime(String writeTime) {
        this.writeTime = writeTime;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
