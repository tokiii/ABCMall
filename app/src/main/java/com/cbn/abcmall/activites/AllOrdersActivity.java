package com.cbn.abcmall.activites;

import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
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
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有订单界面
 * Created by Administrator on 2015/10/8.
 */
public class AllOrdersActivity extends BaseActivity {

    private ExpandableListView expandableListView;
    private Handler handler;
    private AllOrderAdapter adapter;
    private List<CartShop> groupList;
    private ProgressDialog progressDialog;
    private ImageView right;
    private ImageView left;
    private TextView title;
    private int status;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_waitpay);
        right = (ImageView) findViewById(R.id.right);
        right.setVisibility(View.GONE);
        title = (TextView) findViewById(R.id.title);
        left = (ImageView) findViewById(R.id.left);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        groupList = new ArrayList<>();
        expandableListView = (android.widget.ExpandableListView) findViewById(R.id.expandableListView);
        expandableListView.setGroupIndicator(null);
        progressDialog = new ProgressDialog(this);
        status = getIntent().getIntExtra("status", 0);
        progressDialog.setMessage("正在加载....");
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        progressDialog.dismiss();
                        ToastUtils.TextToast(AllOrdersActivity.this, "没有此类订单", Toast.LENGTH_SHORT);
                        break;
                    case 1:
                        progressDialog.dismiss();
                        DebugUtil.put((String)msg.obj, "订单");
                        setWaitAdapter((String) msg.obj, msg.arg1);
                        break;
                }
            }
        };
        switch (status) {
            case 1:
                title.setText("待付款");
                break;
            case 2:
                title.setText("待发货");
                break;

            case 3:
                title.setText("待收货");
                break;

            case 4:
                title.setText("已收货");
                break;

            case 0:
                title.setText("所有订单");
                break;
        }

        getData(status);

    }

    @Override
    public void widgetClick(View v) {

    }


    private void getData(final int status) {
        progressDialog.show();

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("orderStatus", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = HttpUtils.JsonPost(AllOrdersActivity.this, Config.URL_GET_ORDER_STATUS, jsonObject);
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

                        LogUtils.i("info", "得到的OrderList值------->" + resultJsonObject.get("orderList"));
                        message.what = 0;
                    } else {
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
        adapter = new AllOrderAdapter(this, groupList);
        expandableListView.setAdapter(adapter);

        // 默认展开expandableListView
        for (int i = 0; i < groupList.size(); i++) {
            expandableListView.expandGroup(i);
        }

    }


}
