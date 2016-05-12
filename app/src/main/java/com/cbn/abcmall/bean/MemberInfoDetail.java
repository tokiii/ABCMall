package com.cbn.abcmall.bean;

/**
 * 个人信息详细
 * Created by Administrator on 2015/9/28.
 */
public class MemberInfoDetail {

    private String userid;
    private String user;
    private String mobile;
    private String pay_id;
    private String pay_email;
    private String cash;
    private String unreachable;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }


    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPay_id() {
        return pay_id;
    }

    public void setPay_id(String pay_id) {
        this.pay_id = pay_id;
    }

    public String getPay_email() {
        return pay_email;
    }

    public void setPay_email(String pay_email) {
        this.pay_email = pay_email;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getUnreachable() {
        return unreachable;
    }

    public void setUnreachable(String unreachable) {
        this.unreachable = unreachable;
    }
}
