package com.cbn.abcmall.bean;

/**
 * 购物车数据第一层
 * Created by Administrator on 2015/9/17.
 */
public class GetCart {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public CartData getData() {
        return data;
    }

    public void setData(CartData data) {
        this.data = data;
    }

    private String token;
    private CartData data;

}
