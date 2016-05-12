package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 获取
 * Created by Administrator on 2015/10/10.
 */
public class GetRefundList {

    private String token;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public String[] getRefund_status() {
        return refund_status;
    }

    public void setRefund_status(String[] refund_status) {
        this.refund_status = refund_status;
    }

    public List<GetRefundListData> getRefund_list() {
        return refund_list;
    }

    public void setRefund_list(List<GetRefundListData> refund_list) {
        this.refund_list = refund_list;
    }

    private int pageNo;
    private int pageCount;
    private int rowCount;
    private String[] refund_status;
    private List<GetRefundListData> refund_list;

}
