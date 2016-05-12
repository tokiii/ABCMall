package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 订单list文件
 * Created by Administrator on 2015/9/24.
 */
public class Order {

    private String id;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getConsignee_address() {
        return consignee_address;
    }

    public void setConsignee_address(String consignee_address) {
        this.consignee_address = consignee_address;
    }

    public String getConsignee_tel() {
        return consignee_tel;
    }

    public void setConsignee_tel(String consignee_tel) {
        this.consignee_tel = consignee_tel;
    }

    public String getConsignee_mobile() {
        return consignee_mobile;
    }

    public void setConsignee_mobile(String consignee_mobile) {
        this.consignee_mobile = consignee_mobile;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public String getLogistics_type() {
        return logistics_type;
    }

    public void setLogistics_type(String logistics_type) {
        this.logistics_type = logistics_type;
    }

    public String getLogistics_name() {
        return logistics_name;
    }

    public void setLogistics_name(String logistics_name) {
        this.logistics_name = logistics_name;
    }

    public String getInvoice_no() {
        return invoice_no;
    }

    public void setInvoice_no(String invoice_no) {
        this.invoice_no = invoice_no;
    }

    public String getInvoice_title() {
        return invoice_title;
    }

    public void setInvoice_title(String invoice_title) {
        this.invoice_title = invoice_title;
    }

    public String getPayment_name() {
        return payment_name;
    }

    public void setPayment_name(String payment_name) {
        this.payment_name = payment_name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReturn_status() {
        return return_status;
    }

    public void setReturn_status(String return_status) {
        this.return_status = return_status;
    }

    public String getBuyer_comment() {
        return buyer_comment;
    }

    public void setBuyer_comment(String buyer_comment) {
        this.buyer_comment = buyer_comment;
    }

    public String getSeller_comment() {
        return seller_comment;
    }

    public void setSeller_comment(String seller_comment) {
        this.seller_comment = seller_comment;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getPayment_time() {
        return payment_time;
    }

    public void setPayment_time(String payment_time) {
        this.payment_time = payment_time;
    }

    public String getDeliver_time() {
        return deliver_time;
    }

    public void setDeliver_time(String deliver_time) {
        this.deliver_time = deliver_time;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getTime_expand() {
        return time_expand;
    }

    public void setTime_expand(String time_expand) {
        this.time_expand = time_expand;
    }

    public String getVoucher_price() {
        return voucher_price;
    }

    public void setVoucher_price(String voucher_price) {
        this.voucher_price = voucher_price;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getIs_virtual() {
        return is_virtual;
    }

    public void setIs_virtual(String is_virtual) {
        this.is_virtual = is_virtual;
    }

    public String getIs_fenxiao() {
        return is_fenxiao;
    }

    public void setIs_fenxiao(String is_fenxiao) {
        this.is_fenxiao = is_fenxiao;
    }

    public String getUorder_id() {
        return uorder_id;
    }

    public void setUorder_id(String uorder_id) {
        this.uorder_id = uorder_id;
    }

    public String getRemind_time() {
        return remind_time;
    }

    public void setRemind_time(String remind_time) {
        this.remind_time = remind_time;
    }

    public String getRemind_sms() {
        return remind_sms;
    }

    public void setRemind_sms(String remind_sms) {
        this.remind_sms = remind_sms;
    }

    public String getSign_time() {
        return sign_time;
    }

    public void setSign_time(String sign_time) {
        this.sign_time = sign_time;
    }

    public String getSign_state() {
        return sign_state;
    }

    public void setSign_state(String sign_state) {
        this.sign_state = sign_state;
    }

    public String getPostage_relief() {
        return postage_relief;
    }

    public void setPostage_relief(String postage_relief) {
        this.postage_relief = postage_relief;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public List<OrderProduct> getProduct() {
        return product;
    }

    public void setProduct(List<OrderProduct> product) {
        this.product = product;
    }

    private String userid;
    private String order_id;
    private String out_trade_no;
    private String buyer_id;
    private String seller_id;
    private String consignee;
    private String consignee_address;
    private String consignee_tel;
    private String consignee_mobile;
    private String product_price;
    private String logistics_type;
    private String logistics_name;
    private String invoice_no;
    private String invoice_title;
    private String payment_name;
    private String status;
    private String return_status;
    private String buyer_comment;
    private String seller_comment;
    private String des;
    private String create_time;
    private String payment_time;
    private String deliver_time;
    private String uptime;
    private String time_expand;
    private String voucher_price;
    private String discounts;
    private String is_virtual;
    private String is_fenxiao;
    private String uorder_id;
    private String remind_time;
    private String remind_sms;
    private String sign_time;
    private String sign_state;
    private String postage_relief;
    private String company;
    private List<OrderProduct> product;
    private String statu_text;

    public String getStatu_text() {
        return statu_text;
    }

    public void setStatu_text(String statu_text) {
        this.statu_text = statu_text;
    }
}
