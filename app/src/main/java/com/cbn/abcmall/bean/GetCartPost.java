package com.cbn.abcmall.bean;

/**
 * 获得购物车列表需要的数据
 * Created by Administrator on 2015/9/17.
 */
public class GetCartPost {

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

    private String sign;
}
