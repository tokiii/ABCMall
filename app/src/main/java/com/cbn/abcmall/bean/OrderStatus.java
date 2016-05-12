package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 订单状态
 * Created by Administrator on 2015/9/28.
 */
public class OrderStatus {
    private String token;
    private int count;
    private int pageIndex;
    private int pageTotal;
    private int result;
    private String jbuid;
    private List<OrderStatusShop> orderList;

    public List<OrderStatusShop> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderStatusShop> orderList) {
        this.orderList = orderList;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getJbuid() {
        return jbuid;
    }

    public void setJbuid(String jbuid) {
        this.jbuid = jbuid;
    }


}
