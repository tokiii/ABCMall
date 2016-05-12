package com.cbn.abcmall.activites;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.CartConfirmExpandListViewAdapter;
import com.cbn.abcmall.bean.CartGood;
import com.cbn.abcmall.bean.CartShop;
import com.cbn.abcmall.bean.ConfirmOrderGet;
import com.cbn.abcmall.bean.GetAddress;
import com.cbn.abcmall.bean.GetAddressPost;
import com.cbn.abcmall.bean.MyAddress;
import com.cbn.abcmall.bean.SetCids;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商品确认界面
 * Created by Administrator on 2015/9/22.
 */
public class CartConfirmActivity extends BaseActivity {

    private CartGood cartGood;
    private List<String> companys;
    private List<CartShop> groupList = new ArrayList<>();
    private String totalPrice;
    private ExpandableListView expandableListView;
    private TextView tv_name;
    private TextView tv_tel;
    private TextView tv_address;
    private int express = 0;
    private TextView tv_price;
    private int totalExpress = 0;
    private Button btn_confirm;
    private String getPrice;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private int consignee_list;
    private String area;
    private String username;
    private String tel;
    private String token;
    private List<String> cids;
    private List<CartShop> shoplists;
    private List<CartGood> cartGoods;
    private CartConfirmExpandListViewAdapter adapter;
    private Handler handler;
    private String orderId;
    private String orderPrice;
    private ImageView left;
    private ImageView right;
    private TextView title;
    private LinearLayout ll_address;
    private LinearLayout ll_has_address;// 显示有收货地址的布局
    private RelativeLayout rl_no_address;// 没有收货地址显示的布局


    @Override
    public void initWidget() {
        setContentView(R.layout.activity_cart_confirm);
        initView();

        getAddress(); // 获取地址
        cartGoods = (List<CartGood>) getIntent().getSerializableExtra("goods");
        getPrice = getIntent().getStringExtra("totalPrice");

        for (int i = 0; i < cartGoods.size(); i++) {
            String company;
            cartGood = cartGoods.get(i);
            company = cartGood.getCompany();
            if (!companys.contains(company)) {
                companys.add(company);
            }
            cids.add(cartGood.getId());
        }

        for (int i = 0; i < companys.size(); i++) {
            CartShop group = new CartShop();
            group.setCompany(companys.get(i));
            List<CartGood> shopList = new ArrayList<>();
            for (int j = 0; j < cartGoods.size(); j++) {
                if (cartGoods.get(j).getCompany().equals(companys.get(i))) {
                    CartGood cartGood = cartGoods.get(j);
                    express = cartGood.getExpress();
                    group.setLogistics_price(express + "");
                    group.setLogistics_type("快递");
                    group.setSeller_id(cartGood.getSellerid());
                    shopList.add(cartGood);
                }
            }
            totalExpress = totalExpress + express;
            group.setShopList(shopList);
            groupList.add(group);
        }
        LogUtils.i("info", "确认界面获取的数据为------------>" + groupList.get(0).getShopList().size());
        adapter = new CartConfirmExpandListViewAdapter(this, groupList);
        expandableListView.setAdapter(adapter);

        for (int i = 0; i < groupList.size(); i++) {
            expandableListView.expandGroup(i);
        }

        Double total = Double.valueOf(getPrice) + Double.valueOf(totalExpress);

        tv_price.setText("" + total);

    }


    @Override
    public void setAllowFullScreen(boolean allowFullScreen) {
        super.setAllowFullScreen(false);
    }

    private void initView() {
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_tel = (TextView) findViewById(R.id.tv_tel);
        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        tv_price = (TextView) findViewById(R.id.tv_price);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        title = (TextView) findViewById(R.id.title);
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        right.setVisibility(View.GONE);
        title.setText("确认订单");
        ll_has_address = (LinearLayout) findViewById(R.id.ll_has_address);
        rl_no_address = (RelativeLayout) findViewById(R.id.rl_no_address);
        btn_confirm.setOnClickListener(this);
        companys = new ArrayList<>();
        cids = new ArrayList<>();
        token = new SharePreferenceUtil(this, Config.ACCOUNT).getStr(Config.ACCOUNT_TOKEN);
        requestQueue = MyApplication.getInstance().getRequestQueue();
        ll_address = (LinearLayout) findViewById(R.id.ll_address);
        ll_address.setOnClickListener(this);
    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.btn_confirm:

                if (consignee_list == 0) {
                    ToastUtils.TextToast(this, "请添加收货地址！", Toast.LENGTH_SHORT);
                    return;
                }

                handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        String json = (String) msg.obj;
                        LogUtils.i("info", "发送的pay数据为------->" + json);
                        ConfirmOrderGet confirmOrderGet = (ConfirmOrderGet) JsonUtils.jsonToBean(json, ConfirmOrderGet.class);
                        orderId = confirmOrderGet.getOrder_id();
                        orderPrice = confirmOrderGet.getPrice();
                        Intent payIntent = new Intent(CartConfirmActivity.this, PayActivity.class);
                        payIntent.putExtra("orderId", orderId);
                        startActivity(payIntent);
                    }
                };

                adapter.notifyDataSetChanged();
                final JSONObject jsonObject = new JSONObject();
                JSONObject p = new JSONObject();
                String str = "";
                String[] strs = new String[cids.size()];
                for (int i = 0; i < cids.size(); i++) {
                    strs[i] = cids.get(i);
                }

                SetCids setConfirmOrder = new SetCids();
                setConfirmOrder.setCids(strs);
                String json = JsonUtils.objectToJson(setConfirmOrder);
                JSONObject cids = null;
                try {
                    cids = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    jsonObject.put("hidden_consignee_id", consignee_list);
                    jsonObject.put("token", token);
                    jsonObject.put("sign", EncryptUtils.getMD5(token + Config.APP_KEY));
                    jsonObject.put("cids", cids.get("cids"));
                    for (int i = 0; i < groupList.size(); i++) {
                        JSONObject item = new JSONObject();
                        CartShop cartShop = groupList.get(i);
                        item.put("logistics_price", cartShop.getLogistics_price());
                        item.put("logistics_type", cartShop.getLogistics_type());
                        item.put("msg", cartShop.getMsg());
                        p.put(cartShop.getSeller_id(), item);
                    }
                    jsonObject.put("express_info", p);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                LogUtils.i("info", "发送的数据为------>" + jsonObject.toString());

                HttpUtils.httpPostResult(CartConfirmActivity.this, jsonObject, handler, Config.URL_CONFIRM_ORDER);

//                // 点击确认订单时，销毁主界面
//                AppManager.getAppManager().finishActivity(MainActivity.class);

                break;
            // 点击返回键结束当前界面
            case R.id.left:
                finish();
                break;
            // 点击地址进入地址列表更改地址信息
            case R.id.ll_address:
                if (consignee_list == 0) {
                    Intent addressIntent = new Intent(CartConfirmActivity.this, AddressEditActivity.class);
                    addressIntent.putExtra("CartConfirmActivity", "CartConfirmActivity");
                    startActivityForResult(addressIntent, 102);
                } else {
                    Intent addressIntent = new Intent(CartConfirmActivity.this, AddressActivity.class);
                    addressIntent.putExtra("CartConfirmActivity", "CartConfirmActivity");
                    startActivityForResult(addressIntent, 101);
                }

                break;

        }

    }

    /**
     * 获取收货地址
     */
    private void getAddress() {
        JSONObject jsonObject = null;
        GetAddressPost getAddressPost = new GetAddressPost();
        SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(this, Config.ACCOUNT);
        String token = sharePreferenceUtil.getStr(Config.ACCOUNT_TOKEN);
        getAddressPost.setSign(EncryptUtils.getMD5(token + Config.APP_KEY));
        getAddressPost.setToken(token);
        getAddressPost.setAct("consignee");

        final String json = JsonUtils.objectToJson(getAddressPost);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_ADDRESS, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.i("info", "获取到的收货地址-------->" + jsonObject.toString());
                GetAddress getAddress = (GetAddress) JsonUtils.jsonToBean(jsonObject.toString(), GetAddress.class);
                List<MyAddress> lists = getAddress.getConsignee_list();
                if (lists.size() == 0) {
                    ll_has_address.setVisibility(View.GONE);
                    rl_no_address.setVisibility(View.VISIBLE);
                    return;
                }
                for (int i = 0; i < lists.size(); i++) {
                    MyAddress myAddress = lists.get(i);
                    if (myAddress.getDef().equals("2")) {
                        consignee_list = Integer.valueOf(myAddress.getId());
                        area = myAddress.getArea() + myAddress.getAddress();
                        tel = myAddress.getMobile();
                        username = myAddress.getName();
                        tv_address.setText(area);
                        tv_name.setText(username);
                        tv_tel.setText(tel);
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ) {
            LogUtils.i("info", "走了onActivityResult方法");
            switch (requestCode) {
                case 101:
                    String id = data.getStringExtra("id");
                    consignee_list = Integer.valueOf(id);
                    LogUtils.i("info", "回调地址列表得到的数据为---->" + id);
                    selectAddress(id);
                    break;

                case 102:
                    String addId = data.getStringExtra("id");
                    consignee_list = Integer.valueOf(addId);
                    LogUtils.i("info", "回调添加地址得到的数据为---->" + addId);
                    selectAddress(addId);
                    break;
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 查询地址信息
     *
     * @param id
     */
    private void selectAddress(String id) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act", "select_consignee");
            jsonObject.put("id", id);
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String json = (String) msg.obj;
                        LogUtils.i("info", "获得的地址数据为----->" + json);
                        try {
                            JSONObject addressJsonObject = new JSONObject(json);
                            JSONObject addressDetailJsonobject = addressJsonObject.getJSONObject("adder");
                            tv_address.setText(addressDetailJsonobject.getString("t") + " " + addressDetailJsonobject.getString("address"));
                            tv_name.setText(addressDetailJsonobject.getString("name"));
                            tv_tel.setText(addressDetailJsonobject.getString("mobile") != "" ? addressDetailJsonobject.getString("mobile") : addressDetailJsonobject.getString("tel"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = HttpUtils.JsonPost(CartConfirmActivity.this, Config.URL_ADDRESS, jsonObject);
                String json = JsonUtils.decodeUnicode(str);
                Message message = new Message();
                message.what = 1;
                message.obj = json;
                handler.sendMessage(message);
            }
        }).start();
    }
}
