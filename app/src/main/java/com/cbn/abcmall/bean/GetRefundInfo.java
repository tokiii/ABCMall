package com.cbn.abcmall.bean;

import java.util.List;

/**
 * Created by Administrator on 2015/10/19.
 */
public class GetRefundInfo {

    private String token;
    private int stau;
    private String msg;
    private List<GetRefundTalk> talk;

    public List<Getfastmail> getFastmail() {
        return fastmail;
    }

    public void setFastmail(List<Getfastmail> fastmail) {
        this.fastmail = fastmail;
    }

    private List<Getfastmail> fastmail;

    public GetRefundInfoData getRefund_info() {
        return refund_info;
    }

    public void setRefund_info(GetRefundInfoData refund_info) {
        this.refund_info = refund_info;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }



    public int getStau() {
        return stau;
    }

    public void setStau(int stau) {
        this.stau = stau;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<GetRefundTalk> getTalk() {
        return talk;
    }

    public void setTalk(List<GetRefundTalk> talk) {
        this.talk = talk;
    }

    private GetRefundInfoData refund_info;

}
