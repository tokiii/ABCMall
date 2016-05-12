package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 物流信息列表
 * Created by Administrator on 2015/10/13.
 */
public class GetExpress {

    private String message;
    private String nu;

    public String getIscheck() {
        return ischeck;
    }

    public void setIscheck(String ischeck) {
        this.ischeck = ischeck;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getNu() {
        return nu;
    }

    public void setNu(String nu) {
        this.nu = nu;
    }

    public String getCom() {
        return com;
    }

    public void setCom(String com) {
        this.com = com;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public List<LogisticData> getData() {
        return data;
    }

    public void setData(List<LogisticData> data) {
        this.data = data;
    }

    private String ischeck;
    private String com;
    private String status;
    private String condition;
    private String state;
    private List<LogisticData> data;


}
