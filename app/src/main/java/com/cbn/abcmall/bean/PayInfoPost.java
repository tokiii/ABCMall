package com.cbn.abcmall.bean;

/**
 * 发送的支付信息地址
 * Created by Administrator on 2015/9/24.
 */
public class PayInfoPost {

    private String token;
    private String sign;
    private String tradeNo;
    private String payment_type;

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

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }
}
