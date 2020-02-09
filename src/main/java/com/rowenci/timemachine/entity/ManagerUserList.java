package com.rowenci.timemachine.entity;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/9 18:36
 */
public class ManagerUserList {

    private String id;

    private String account;

    private String isVIP;

    private int messageNumber;

    private int publicMessageNumber;

    private String logupTime;

    public ManagerUserList(String id, String account, String isVIP, int messageNumber, int publicMessageNumber, String logupTime) {
        this.id = id;
        this.account = account;
        this.isVIP = isVIP;
        this.messageNumber = messageNumber;
        this.publicMessageNumber = publicMessageNumber;
        this.logupTime = logupTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getIsVIP() {
        return isVIP;
    }

    public void setIsVIP(String isVIP) {
        this.isVIP = isVIP;
    }

    public int getMessageNumber() {
        return messageNumber;
    }

    public void setMessageNumber(int messageNumber) {
        this.messageNumber = messageNumber;
    }

    public int getPublicMessageNumber() {
        return publicMessageNumber;
    }

    public void setPublicMessageNumber(int publicMessageNumber) {
        this.publicMessageNumber = publicMessageNumber;
    }

    public String getLogupTime() {
        return logupTime;
    }

    public void setLogupTime(String logupTime) {
        this.logupTime = logupTime;
    }
}
