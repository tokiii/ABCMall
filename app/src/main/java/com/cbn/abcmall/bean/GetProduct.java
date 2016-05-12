package com.cbn.abcmall.bean;

/**
 * 得到的Json字符串
 * Created by Administrator on 2015/9/11.
 */
public class GetProduct{

    private String token; //用户的token数据

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    private int result; //返回的结果数据
    private String sql; //不明参数
    private Product product; //商品具体详情

}