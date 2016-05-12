package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 订单详细信息
 * Created by Administrator on 2015/10/26.
 */
public class GetOrderDetailInfo {

    private String orderId;
    private String orderStatus;
    private String additionWord;
    private int reimburseStatus;
    private String buyerComment;
    private String sellerComment;
    private double paidMoney;
    private String postType;
    private String postPrice;
    private String logisticsName;
    private String invoiceNo;
    private double priceTotal;
    private String connectName;
    private String address;
    private String mobilephone;
    private String createTime;
    private String shopId;
    private String shopName;
    private String username;
    private String productTotal;
    private List<GetExpress> expressinfo;


    public List<OrderInfoList> getList() {
        return list;
    }

    public void setList(List<OrderInfoList> list) {
        this.list = list;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getAdditionWord() {
        return additionWord;
    }

    public void setAdditionWord(String additionWord) {
        this.additionWord = additionWord;
    }

    public int getReimburseStatus() {
        return reimburseStatus;
    }

    public void setReimburseStatus(int reimburseStatus) {
        this.reimburseStatus = reimburseStatus;
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

    public double getPaidMoney() {
        return paidMoney;
    }

    public void setPaidMoney(double paidMoney) {
        this.paidMoney = paidMoney;
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

    public String getLogisticsName() {
        return logisticsName;
    }

    public void setLogisticsName(String logisticsName) {
        this.logisticsName = logisticsName;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public double getPriceTotal() {
        return priceTotal;
    }

    public void setPriceTotal(double priceTotal) {
        this.priceTotal = priceTotal;
    }

    public String getConnectName() {
        return connectName;
    }

    public void setConnectName(String connectName) {
        this.connectName = connectName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobilephone() {
        return mobilephone;
    }

    public void setMobilephone(String mobilephone) {
        this.mobilephone = mobilephone;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProductTotal() {
        return productTotal;
    }

    public void setProductTotal(String productTotal) {
        this.productTotal = productTotal;
    }

    public List<GetExpress> getExpressinfo() {
        return expressinfo;
    }

    public void setExpressinfo(List<GetExpress> expressinfo) {
        this.expressinfo = expressinfo;
    }

    private List<OrderInfoList> list;

}
