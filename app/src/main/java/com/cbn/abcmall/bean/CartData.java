package com.cbn.abcmall.bean;

/**
 * 购物车数据第二层
 * Created by Administrator on 2015/9/17.
 */
public class CartData {
    public String getJbuid() {
        return jbuid;
    }

    public void setJbuid(String jbuid) {
        this.jbuid = jbuid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Addr getAddr() {
        return addr;
    }

    public void setAddr(Addr addr) {
        this.addr = addr;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Rate getRate() {
        return rate;
    }

    public void setRate(Rate rate) {
        this.rate = rate;
    }

    public Order_status getOrder_status() {
        return order_status;
    }

    public void setOrder_status(Order_status order_status) {
        this.order_status = order_status;
    }

    public CartList getCartlist() {
        return cartlist;
    }

    public void setCartlist(CartList cartlist) {
        this.cartlist = cartlist;
    }

    private String jbuid;
    private String status;
    private Addr addr;
    private String lang;
    private Rate rate;
    private Order_status order_status;
    private CartList cartlist;
}
