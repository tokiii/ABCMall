package com.cbn.abcmall.bean;

/**
 * 物流信息查询
 * Created by Administrator on 2015/10/13.
 */
public class GetLogistic {

    private int result;
    private String msg;
    private String logistics_name;
    private String invoice_no;

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


    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public GetExpress getExpress() {
        return express;
    }

    public void setExpress(GetExpress express) {
        this.express = express;
    }

    private GetExpress express;
}
