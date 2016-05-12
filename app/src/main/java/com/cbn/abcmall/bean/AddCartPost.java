package com.cbn.abcmall.bean;

/**
 * 加入购物车发送的JSON数据
 * Created by Administrator on 2015/9/17.
 */
public class AddCartPost {

    private String add_cart;
    private String num;
    private String pid;
    private String sid;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdd_cart() {
        return add_cart;
    }

    public void setAdd_cart(String add_cart) {
        this.add_cart = add_cart;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    private String token;
    private String sign;
}
