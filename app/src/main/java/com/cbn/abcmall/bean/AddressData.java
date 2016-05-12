package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 地址信息
 * Created by Administrator on 2015/9/30.
 */
public class AddressData {
    private String token;

    public List<AddressDetail> getConsignee_list() {
        return consignee_list;
    }

    public void setConsignee_list(List<AddressDetail> consignee_list) {
        this.consignee_list = consignee_list;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private List<AddressDetail> consignee_list;
}
