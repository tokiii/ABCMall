package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 获得的收藏信息
 * Created by Administrator on 2015/10/14.
 */
public class GetCollectShop {
    private String token;
    private int result;
    private String msg;

    public List<GetCollectShopDetail> getList() {
        return list;
    }

    public void setList(List<GetCollectShopDetail> list) {
        this.list = list;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private List<GetCollectShopDetail> list;
}
