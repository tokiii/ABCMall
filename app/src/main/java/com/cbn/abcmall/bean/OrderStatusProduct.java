package com.cbn.abcmall.bean;

/**
 * 订单状态商品
 * Created by Administrator on 2015/9/28.
 */
public class OrderStatusProduct {

    private String id;
    private String productId;
    private String picUrl;
    private String productName;
    private String comboId;
    private String comboName;
    private String price;
    private String productVolume;
    private int reimburseStatus;// 退货状态
    private String refund_status;
    private String refund_id;

    public String getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String refund_status) {
        this.refund_status = refund_status;
    }

    public String getRefund_id() {
        return refund_id;
    }

    public void setRefund_id(String refund_id) {
        this.refund_id = refund_id;
    }

    public int getReimburseStatus() {
        return reimburseStatus;
    }

    public void setReimburseStatus(int reimburseStatus) {
        this.reimburseStatus = reimburseStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getComboId() {
        return comboId;
    }

    public void setComboId(String comboId) {
        this.comboId = comboId;
    }

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProductVolume() {
        return productVolume;
    }

    public void setProductVolume(String productVolume) {
        this.productVolume = productVolume;
    }
}
