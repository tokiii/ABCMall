package com.cbn.abcmall.bean;

import android.util.Log;

import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;

/**
 * 购物车商品实体类
 */
public class CartGood extends Observable implements Observer, Serializable{
	private String name;
	private boolean isChecked;
    private int express;
    private String color;
	private String msg;
	private String sellerid;
	private String orderStatus;
	private int reimburseStatus;
	private String refund_id;
	private String refund_status;

	public String getRefund_id() {
		return refund_id;
	}

	public void setRefund_id(String refund_id) {
		this.refund_id = refund_id;
	}

	public String getRefund_status() {
		return refund_status;
	}

	public void setRefund_status(String refund_status) {
		this.refund_status = refund_status;
	}

	public int getReimburseStatus() {
		return reimburseStatus;
	}

	public void setReimburseStatus(int
										   reimburseStatus) {
		this.reimburseStatus = reimburseStatus;
	}

	public String getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getSellerid() {
		return sellerid;
	}

	public void setSellerid(String sellerid) {
		this.sellerid = sellerid;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getExpress() {
        return express;
    }

    public void setExpress(int express) {
        this.express = express;
    }

    public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	private String company;

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}


	public String getCount() {
		return count;
	}

	public void setCount(String count) {
		this.count = count;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String image;
	private String count;
	private String id;

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	private String price;

	public boolean isVisible() {
		return isVisible;
	}

	public void setIsVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}

	private boolean isVisible;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isChecked() {
		return isChecked;
	}
	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}
	public void changeChecked(){
		isChecked = !isChecked;
		setChanged();
		notifyObservers(this);
	}
	@Override
	public void update(Observable observable, Object data) {

		Log.i("info", observable.toString());

		if (data instanceof Boolean) {
			this.isChecked = (Boolean) data;
		}
	}
}
