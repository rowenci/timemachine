package com.rowenci.timemachine.entity;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/9 19:46
 */
public class UserInBlack {

    private String id;

    private String account;

    private String logupTime;

    private String isVIP;

    private int messageNumber;

    private int publicMessageNumber;

    private String time;

    private int reason;

    private String managerAccount;

    public UserInBlack(String id, String account, String logupTime, String isVIP, int messageNumber, int publicMessageNumber, String time, int reason, String managerAccount) {
        this.id = id;
        this.account = account;
        this.logupTime = logupTime;
        this.isVIP = isVIP;
        this.messageNumber = messageNumber;
        this.publicMessageNumber = publicMessageNumber;
        this.time = time;
        this.reason = reason;
        this.managerAccount = managerAccount;
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

    public String getLogupTime() {
        return logupTime;
    }

    public void setLogupTime(String logupTime) {
        this.logupTime = logupTime;
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

    public String getManagerAccount() {
        return managerAccount;
    }

    public void setManagerAccount(String managerAccount) {
        this.managerAccount = managerAccount;
    }
}
