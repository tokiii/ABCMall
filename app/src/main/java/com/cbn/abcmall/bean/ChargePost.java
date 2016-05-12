package com.cbn.abcmall.bean;

/**
 * 充值发送的实体类
 * Created by Administrator on 2015/9/28.
 */
public class ChargePost {

    private String token;
    private String sign;
    private String amount;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
