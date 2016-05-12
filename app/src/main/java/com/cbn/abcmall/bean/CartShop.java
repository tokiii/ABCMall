package com.cbn.abcmall.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 *
 */
public class CartShop extends Observable implements Observer{
	private String name;
	private boolean isChecked;
    private String seller_id;
	private String orderId;
	private String orderTime;
	private int status;

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(String orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getLogistics_price() {
        return logistics_price;
    }

    public void setLogistics_price(String logistics_price) {
        this.logistics_price = logistics_price;
    }

    public String getLogistics_type() {
        return logistics_type;
    }

    public void setLogistics_type(String logistics_type) {
        this.logistics_type = logistics_type;
    }

    private String msg;
    private String logistics_price;
    private String logistics_type;

    public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	private String company;


	public String getSumprice() {
		return sumprice;
	}
	public void setSumprice(String sumprice) {
		this.sumprice = sumprice;
	}

	private String sumprice;
	private List<CartGood> shopList = new ArrayList<CartGood>();
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
	public List<CartGood> getShopList() {
		return shopList;
	}
	public void setShopList(List<CartGood> shopList) {
		this.shopList = shopList;
	}
	public void changeChecked(){
		isChecked = !isChecked;
		setChanged();
		notifyObservers(isChecked);
	}

	@Override
	public void update(Observable observable, Object data) {
		boolean flag = true;
		for (CartGood city : shopList) {
			if (city.isChecked() == false) {
				flag = false;
			}
		}
		this.isChecked = flag;
	}
	
	
}
