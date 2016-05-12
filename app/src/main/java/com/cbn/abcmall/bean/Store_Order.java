package com.cbn.abcmall.bean;

/**
 * 每个店铺包含的订单
 * Created by Administrator on 2015/9/17.
 */
public class Store_Order {
    private String id;
    private String buyer_id;
    private String seller_id;
    private String price;
    private String quantity;
    private String create_time;
    private String spec_id;
    private String is_tg;
    private String discounts;

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getSpec_id() {
        return spec_id;
    }

    public void setSpec_id(String spec_id) {
        this.spec_id = spec_id;
    }

    public String getIs_tg() {
        return is_tg;
    }

    public void setIs_tg(String is_tg) {
        this.is_tg = is_tg;
    }

    public String getDiscounts() {
        return discounts;
    }

    public void setDiscounts(String discounts) {
        this.discounts = discounts;
    }

    public String getIs_fenxiao() {
        return is_fenxiao;
    }

    public void setIs_fenxiao(String is_fenxiao) {
        this.is_fenxiao = is_fenxiao;
    }

    public String getSumprice() {
        return sumprice;
    }

    public void setSumprice(String sumprice) {
        this.sumprice = sumprice;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getCubage() {
        return cubage;
    }

    public void setCubage(String cubage) {
        this.cubage = cubage;
    }

    public String getSubhead() {
        return subhead;
    }

    public void setSubhead(String subhead) {
        this.subhead = subhead;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIs_shelves() {
        return is_shelves;
    }

    public void setIs_shelves(String is_shelves) {
        this.is_shelves = is_shelves;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMarket_price() {
        return market_price;
    }

    public void setMarket_price(String market_price) {
        this.market_price = market_price;
    }

    public String getIncome_price() {
        return income_price;
    }

    public void setIncome_price(String income_price) {
        this.income_price = income_price;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getFreight_type() {
        return freight_type;
    }

    public void setFreight_type(String freight_type) {
        this.freight_type = freight_type;
    }

    public String getSetmealname() {
        return setmealname;
    }

    public void setSetmealname(String setmealname) {
        this.setmealname = setmealname;
    }

    public String getSpec_name() {
        return spec_name;
    }

    public void setSpec_name(String spec_name) {
        this.spec_name = spec_name;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public String[] getSpec() {
        return spec;
    }

    public void setSpec(String[] spec) {
        this.spec = spec;
    }

    private String tradeno;
    private String is_fenxiao;
    private String sumprice;
    private String num;
    private String weight;
    private String cubage;
    private String subhead;
    private String brand;
    private String type;
    private String is_shelves;
    private String status;
    private String market_price;
    private String income_price;
    private String pname;
    private String pic;
    private String catid;
    private String pid;
    private String freight;
    private String freight_type;

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    private String product_id;
    private String setmealname;
    private String spec_name;
    private String stock;
    private String[] spec;
}
