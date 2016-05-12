package com.cbn.abcmall.bean;

import java.util.List;

/**
 *
 * 购物车列表lists
 * Created by Administrator on 2015/9/17.
 */
public class CartList {



    public List<Store> getCart() {
        return cart;
    }

    public void setCart(List<Store> cart) {
        this.cart = cart;
    }

    public Double getSumprice() {
        return sumprice;
    }

    public void setSumprice(Double sumprice) {
        this.sumprice = sumprice;
    }

    private Double sumprice;
    private List<Store> cart;
}
