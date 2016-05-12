package com.cbn.abcmall.bean;

import java.util.List;

/**
 * 个人信息实体类
 * Created by Administrator on 2015/9/28.
 */
public class MemberInfo {

    private String token;
    private String jbuid;
    private int statu;
    private MemberInfoDetail memberinfo;

    public List<OrderCount> getOrder_count() {
        return order_count;
    }

    public void setOrder_count(List<OrderCount> order_count) {
        this.order_count = order_count;
    }

    private List<OrderCount> order_count;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getJbuid() {
        return jbuid;
    }

    public void setJbuid(String jbuid) {
        this.jbuid = jbuid;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }

    public MemberInfoDetail getMemberinfo() {
        return memberinfo;
    }

    public void setMemberinfo(MemberInfoDetail memberinfo) {
        this.memberinfo = memberinfo;
    }
}
