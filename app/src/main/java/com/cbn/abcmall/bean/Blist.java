package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 购物车分页数据
 * Created by Administrator on 2015/9/17.
 */
public class Blist {
    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }



    private String page;

    public List<Order> getList() {
        return list;
    }

    public void setList(List<Order> list) {
        this.list = list;
    }

    private List<Order> list;
}
