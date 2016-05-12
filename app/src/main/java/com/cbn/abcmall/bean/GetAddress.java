package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 获取到的地址解析
 * Created by Administrator on 2015/9/23.
 */
public class GetAddress {

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<MyAddress> getConsignee_list() {
        return consignee_list;
    }

    public void setConsignee_list(List<MyAddress> consignee_list) {
        this.consignee_list = consignee_list;
    }

    private  String token;
    private List<MyAddress> consignee_list;
}
