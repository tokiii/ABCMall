package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 获取商品收藏
 * Created by Administrator on 2015/10/14.
 */
public class GetCollectProduct {

    private String token;
    private int result;
    private String msg;

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

    public List<GetCollectProductDetail> getList() {
        return list;
    }

    public void setList(List<GetCollectProductDetail> list) {
        this.list = list;
    }

    private List<GetCollectProductDetail> list;
}
