package com.cbn.abcmall.bean;

/**
 * 获取地址信息数据
 * Created by Administrator on 2015/9/23.
 */
public class GetAddressPost {

    private String token;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    private String sign;
    private String act;
}
