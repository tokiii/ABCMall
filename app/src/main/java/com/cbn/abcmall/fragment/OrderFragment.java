package com.cbn.abcmall.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.adapter.AllOrderAdapter;
import com.cbn.abcmall.bean.CartGood;
import com.cbn.abcmall.bean.CartShop;
import com.cbn.abcmall.bean.OrderStatus;
import com.cbn.abcmall.bean.OrderStatusProduct;
import com.cbn.abcmall.bean.OrderStatusShop;
import com.cbn.abcmall.utils.DebugUtil;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 查看订单Fragment
 * Created by lost on 2015/12/14.
 */
public class OrderFragment extends android.support.v4.app.Fragment {


    private ExpandableListView expandableListView;
    private Handler handler;
    private AllOrderAdapter adapter;
    private List<CartShop> groupList;
    private int status;// 传入的状态
    private SwipeRefreshLayout srl_order;
    private boolean isFirst = true;
    private TextView tv_empty;// 数据为空时显示的界面

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        status = getArguments().getInt("status");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        srl_order.setRefreshing(false);
                        tv_empty.setVisibility(View.VISIBLE);
                        break;
                    case 1:
                        srl_order.setRefreshing(false);
                        tv_empty.setVisibility(View.GONE);
                        DebugUtil.put((String) msg.obj, "订单");
                        setWaitAdapter((String) msg.obj, msg.arg1);
                        break;
                }
            }
        };
    }

    private boolean isViewShown = false;// 判断界面组件是否初始化

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtils.i("info", "得到的判断是否可见----isVisibleToUser-->" + isVisibleToUser + ";-----isVisible()---->" + isVisible() + ";-----getUserVisibleHint()---->" + getUserVisibleHint());
        if (getView() != null && isFirst) {
            LogUtils.i("info", "是否进行加载数据------" + isFirst);
            isViewShown = true;
            srl_order.setRefreshing(true);
            getData(status);
        }else {
            isViewShown = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_order, container, false);
        expandableListView = (android.widget.ExpandableListView) v.findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);
        expandableListView.setEmptyView(v.findViewById(R.id.tv_empty));
        srl_order = (SwipeRefreshLayout) v.findViewById(R.id.srl_order);
        tv_empty = (TextView) v.findViewById(R.id.tv_empty);
        groupList = new ArrayList<>();
        srl_order.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srl_order.setRefreshing(true);
                getData(status);
            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void onStart() {
        super.onStart();

        LogUtils.i("info", "onStart-----------isFirst----->" + isFirst);
        if (isFirst) {
            srl_order.setRefreshing(true);
            getData(status);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }


    /**
     * 从网络获取数据
     *
     * @param status
     */
    private void getData(final int status) {


        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sign", Config.getSign(getActivity()));
            jsonObject.put("token", Config.getAccountToken(getActivity()));
            jsonObject.put("orderStatus", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = HttpUtils.JsonPost(getActivity(), Config.URL_GET_ORDER_STATUS, jsonObject);
                String json = JsonUtils.decodeUnicode(str);
                JSONObject resultJsonObject = null;
                try {
                    resultJsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Message message = new Message();
                try {
                    if (resultJsonObject.get("orderList").equals(null)) {
                        isFirst = false;
                        LogUtils.i("info", "得到的OrderList值------->" + resultJsonObject.get("orderList"));
                        message.what = 0;
                    } else {
                        isFirst = false;
                        message.obj = json;
                        message.what = 1;
                        message.arg1 = status;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                handler.sendMessage(message);
            }
        }).start();

    }

    /**
     * 对界面进行适配
     *
     * @param json
     */
    private void setWaitAdapter(String json, int status) {
        // 防止数据重复加载
        groupList.clear();

        DebugUtil.put(json, "order");
        OrderStatus orderStatus = (OrderStatus) JsonUtils.jsonToBean(json, OrderStatus.class);
        List<OrderStatusShop> orderStatusShops = orderStatus.getOrderList();
        for (int i = 0; i < orderStatusShops.size(); i++) {
            CartShop cartShop = new CartShop();
            OrderStatusShop shop = orderStatusShops.get(i);
            List<OrderStatusProduct> orderStatusProducts = shop.getProductList();
            cartShop.setCompany(shop.getShopName());
            cartShop.setSumprice(String.valueOf(shop.getPriceTotal()));
            cartShop.setOrderId(shop.getOrderId());
            cartShop.setStatus(Integer.valueOf(shop.getOrderStatus()));
            cartShop.setOrderTime(shop.getCreateTime());
            List<CartGood> cartGoods = new ArrayList<>();
            for (int j = 0; j < orderStatusProducts.size(); j++) {
                OrderStatusProduct product = orderStatusProducts.get(j);
                CartGood cartGood = new CartGood();
                cartGood.setColor(product.getComboName());
                cartGood.setName(product.getProductName());
                cartGood.setImage(product.getPicUrl());
                cartGood.setPrice(product.getPrice());
                cartGood.setOrderStatus(shop.getOrderStatus());
                cartGood.setId(product.getId());
                cartGood.setExpress(Integer.valueOf(shop.getPostPrice()));
                cartGood.setSellerid(shop.getShopId());
                cartGood.setCount(product.getProductVolume());
                cartGood.setReimburseStatus(product.getReimburseStatus());
                cartGood.setRefund_id(product.getRefund_id());
                cartGood.setRefund_status(product.getRefund_status());
                cartGoods.add(cartGood);
            }
            cartShop.setShopList(cartGoods);
            groupList.add(cartShop);
        }
        adapter = new AllOrderAdapter(getActivity(), groupList);
        expandableListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        // 默认展开expandableListView
        for (int i = 0; i < groupList.size(); i++) {
            expandableListView.expandGroup(i);
        }
    }

}
