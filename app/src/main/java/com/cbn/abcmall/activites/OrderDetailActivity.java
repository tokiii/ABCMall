package com.cbn.abcmall.activites;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.OrderInfoAdapter;
import com.cbn.abcmall.bean.GetOrderDetail;
import com.cbn.abcmall.bean.GetOrderDetailInfo;
import com.cbn.abcmall.utils.DateUtil;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.views.ScrollDisabledListView;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 订单详情页
 * Created by Administrator on 2015/10/13.
 */
public class OrderDetailActivity extends BaseActivity {

    private String orderId;// 订单id
    private Handler handler;
    private ImageView left;
    private TextView title;
    private TextView tv_address;// 地址
    private TextView tv_order_id;// 订单编号
    private TextView tv_create_time;// 创建时间
    private ScrollDisabledListView lv_product;// 商品适配器
    private OrderInfoAdapter adapter;// 商品列表适配器
    private TextView groupText;// 订单商铺标题
    private TextView tv_status;// 订单状态
    private TextView tv_phone;// 联系方式
    private TextView tv_name;// 联系人

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_orderdetail);
        // 加载组件
        bindViews();
        // handler接收消息
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        String json = (String) msg.obj;
                        LogUtils.i("info", "获得的商品详情json----------->" + msg.obj);
                        GetOrderDetail getOrderDetail = (GetOrderDetail) JsonUtils.jsonToBean(json, GetOrderDetail.class);
                        GetOrderDetailInfo info = getOrderDetail.getOrderdetail();
                        tv_address.setText(info.getAddress());
                        LogUtils.i("info", "获得的订单详情的地址为--------》" + info.getAddress());
                        tv_order_id.setText("订单编号：" + info.getOrderId());
                        tv_create_time.setText("订单时间：" + DateUtil.getDateToString(Long.valueOf(info.getCreateTime()) * 1000));
                        adapter = new OrderInfoAdapter(OrderDetailActivity.this, info.getList());
                        tv_phone.setText(info.getMobilephone());
                        tv_name.setText(info.getConnectName());
                        groupText.setText(info.getShopName());

                        switch (Integer.valueOf(info.getOrderStatus())){
                            case 1:
                                tv_status.setText("待付款");
                                break;
                            case 2:
                                tv_status.setText("待发货");
                                break;

                            case 3:
                                tv_status.setText("待收货");
                                break;
                            case 4:
                                tv_status.setText("已收货");
                                break;
                        }
                        lv_product.setAdapter(adapter);

                        break;
                }
            }
        };


        getData(orderId);
    }


    /**
     * 初始化组件
     */
    private void bindViews() {
        orderId = getIntent().getStringExtra("orderId");
        title = (TextView) findViewById(R.id.title);
        left = (ImageView) findViewById(R.id.left);
        left.setOnClickListener(this);
        title.setText("订单详情");
        tv_address = (TextView) findViewById(R.id.tv_address);
        tv_order_id = (TextView) findViewById(R.id.tv_order_id);
        tv_create_time = (TextView) findViewById(R.id.tv_create_time);
        lv_product = (ScrollDisabledListView) findViewById(R.id.lv_product);
        groupText = (TextView) findViewById(R.id.groupText);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_name = (TextView) findViewById(R.id.tv_name);
    }


    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
        }
    }

    /**
     * 获得订单详情界面
     *
     * @param orderId
     */
    private void getData(String orderId) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.httpPostResult(this, jsonObject, handler, Config.URL_ORDER_DETAIL);
    }


}
