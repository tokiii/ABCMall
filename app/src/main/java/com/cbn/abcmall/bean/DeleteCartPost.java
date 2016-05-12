package com.cbn.abcmall.bean;

/**
 * 删除购物车实体类发送的数据
 * Created by Administrator on 2015/9/22.
 */
public class DeleteCartPost {

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

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getAct() {
        return act;
    }

    public void setAct(String act) {
        this.act = act;
    }

    private String sign;
    private String cid;
    private String act;

}
