package com.cbn.abcmall.bean;

/**
 * 获取到的PayInfo
 * Created by Administrator on 2015/9/24.
 */
public class PayInfoGet {

    private String token;
    private double price;
    private int tradeNo;
    private String return_url;
    private String notify_url;
    private String out_trade_no;
    private String subject;
    private String statu;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(int tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getStatu() {
        return statu;
    }

    public void setStatu(String statu) {
        this.statu = statu;
    }
}
