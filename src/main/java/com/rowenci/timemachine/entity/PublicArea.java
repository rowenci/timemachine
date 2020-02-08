package com.rowenci.timemachine.entity;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/8 14:10
 */
public class PublicArea {

    private int id;

    private String messageId;

    private String userId;

    private String title;

    private String context;

    private String writeTime;

    private String sendTime;

    private int good_number;

    private int favorite_number;

    private int isSend;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getGood_number() {
        return good_number;
    }

    public void setGood_number(int good_number) {
        this.good_number = good_number;
    }

    public int getFavorite_number() {
        return favorite_number;
    }

    public void setFavorite_number(int favorite_number) {
        this.favorite_number = favorite_number;
    }

    public int getIsSend() {
        return isSend;
    }

    public void setIsSend(int isSend) {
        this.isSend = isSend;
    }
}
