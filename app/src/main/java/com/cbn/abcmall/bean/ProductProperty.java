package com.cbn.abcmall.bean;

/**
 * 商品具体属性
 * Created by Administrator on 2015/9/11.
 */
public class ProductProperty {

    private String id;
    private String stock;

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getProperty_value_id() {
        return property_value_id;
    }

    public void setProperty_value_id(String property_value_id) {
        this.property_value_id = property_value_id;
    }

    public String getSetmeal() {
        return setmeal;
    }

    public void setSetmeal(String setmeal) {
        this.setmeal = setmeal;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getSpec_name() {
        return spec_name;
    }

    public void setSpec_name(String spec_name) {
        this.spec_name = spec_name;
    }

    private String price;
    private String cost_price;
    private String market_price;
    private String property_value_id;
    private String setmeal;
    private String pid;
    private String sku;
    private String spec_name;
}
