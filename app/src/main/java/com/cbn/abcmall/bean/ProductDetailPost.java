package com.cbn.abcmall.bean;

/**
 * 请求数据发送的JSON字符串
 * Created by Administrator on 2015/9/10.
 */
public class ProductDetailPost {

    private String product_id;
    private String token;

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private String sign;
}
