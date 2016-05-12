package com.cbn.abcmall.bean;

import com.google.gson.annotations.SerializedName;

/**
 * 还是订单状态，不知道什么作用，但是也得解析
 * Created by Administrator on 2015/9/17.
 */
public class Order_status {

    public String getClose() {
        return close;
    }

    public void setClose(String close) {
        this.close = close;
    }

    public String getWait_for_pay() {
        return wait_for_pay;
    }

    public void setWait_for_pay(String wait_for_pay) {
        this.wait_for_pay = wait_for_pay;
    }

    public String getPayed() {
        return payed;
    }

    public void setPayed(String payed) {
        this.payed = payed;
    }

    public String getDeliver() {
        return deliver;
    }

    public void setDeliver(String deliver) {
        this.deliver = deliver;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getRefunding_order() {
        return refunding_order;
    }

    public void setRefunding_order(String refunding_order) {
        this.refunding_order = refunding_order;
    }

    @SerializedName("1")
    private String close;

    @SerializedName("2")
    private String wait_for_pay;

    @SerializedName("3")
    private String payed;

    @SerializedName("4")
    private String deliver;

    @SerializedName("5")
    private String success;

    @SerializedName("6")
    private String refunding_order;

}
