package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 订单商铺状态
 * Created by Administrator on 2015/9/28.
 */
public class OrderStatusShop {

    private String createTime;
    private String orderStatus;
    private int remburseStatus;
    private String buyerComment;
    private String sellerComment;
    private String orderId;
    private String shopId;
    private String shopName;
    private String postType;
    private String postPrice;
    private double priceTotal;

    public List<OrderStatusProduct> getProductList() {
        return productList;
    }

    public void setProductList(List<OrderStatusProduct> productList) {
        this.productList = productList;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public int getRemburseStatus() {
        return remburseStatus;
    }

    public void setRemburseStatus(int remburseStatus) {
        this.remburseStatus = remburseStatus;
    }

    public String getBuyerComment() {
        return buyerComment;
    }

    public void setBuyerComment(String buyerComment) {
        this.buyerComment = buyerComment;
    }

    public String getSellerComment() {
        return sellerComment;
    }

    public void setSellerComment(String sellerComment) {
        this.sellerComment = sellerComment;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getShopId() {
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getPostPrice() {
        return postPrice;
    }

    public void setPostPrice(String postPrice) {
        this.postPrice = postPrice;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    private List<OrderStatusProduct> productList;
}
