package com.cbn.abcmall.bean;

/**
 * 获得订单详情
 * Created by Administrator on 2015/10/26.
 */
public class GetOrderDetail {

    private String token;

    public GetOrderDetailInfo getOrderdetail() {
        return orderdetail;
    }

    public void setOrderdetail(GetOrderDetailInfo orderdetail) {
        this.orderdetail = orderdetail;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    private GetOrderDetailInfo orderdetail;
}
