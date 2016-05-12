package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 每一个店铺
 * Created by Administrator on 2015/9/17.
 */
public class Store {

    public List<Store_Order> getProlist() {
        return prolist;
    }

    public void setProlist(List<Store_Order> prolist) {
        this.prolist = prolist;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDicounts() {
        return dicounts;
    }

    public void setDicounts(String dicounts) {
        this.dicounts = dicounts;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getIs_invoice() {
        return is_invoice;
    }

    public void setIs_invoice(String is_invoice) {
        this.is_invoice = is_invoice;
    }

    public String getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(String spec_id) {
        this.spec_id = spec_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public int getExpress() {
        return express;
    }

    public void setExpress(int express) {
        this.express = express;
    }



    private List<Store_Order> prolist;
    private String id;

    public int getEms() {
        return ems;
    }

    public void setEms(int ems) {
        this.ems = ems;
    }

    public int getMail() {
        return mail;
    }

    public void setMail(int mail) {
        this.mail = mail;
    }

    private int mail;

    public Double getSumprice() {
        return sumprice;
    }

    public void setSumprice(Double sumprice) {
        this.sumprice = sumprice;
    }

    private Double sumprice;
    private String dicounts;
    private String company;
    private String is_invoice;
    private String spec_id;
    private String seller_id;
    private int express;
    private int ems;

}
