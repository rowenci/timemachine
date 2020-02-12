package com.rowenci.timemachine.entity;

/**
 * <p>
 *
 * </p>
 *
 * @author rowenci
 * @since 2020/2/12 17:24
 */
public class OrderSend {

    private String orderId;

    private String trade_no;

    private String userId;

    private String time;

    private Integer money;

    private Integer vipMode;

    public OrderSend(String orderId, String trade_no, String userId, String time, Integer money, Integer vipMode) {
        this.orderId = orderId;
        this.trade_no = trade_no;
        this.userId = userId;
        this.time = time;
        this.money = money;
        this.vipMode = vipMode;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTrade_no() {
        return trade_no;
    }

    public void setTrade_no(String trade_no) {
        this.trade_no = trade_no;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }

    public Integer getVipMode() {
        return vipMode;
    }

    public void setVipMode(Integer vipMode) {
        this.vipMode = vipMode;
    }
}
