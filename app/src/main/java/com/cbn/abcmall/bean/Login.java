package com.cbn.abcmall.bean;

/**
 * 登录实体类
 * Created by Administrator on 2015/9/8.
 */
public class Login {

    private String password;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    private String user;
    private String sign;
}
