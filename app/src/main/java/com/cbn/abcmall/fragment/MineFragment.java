package com.cbn.abcmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.AddressActivity;
import com.cbn.abcmall.activites.AllRefundActivity;
import com.cbn.abcmall.activites.ChargeActivity;
import com.cbn.abcmall.activites.CollectActivity;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.activites.CouponActivity;
import com.cbn.abcmall.activites.InviteActivity;
import com.cbn.abcmall.activites.LoginActivity;
import com.cbn.abcmall.activites.OrderFragmentActivity;
import com.cbn.abcmall.activites.RebateActivity;
import com.cbn.abcmall.activites.SettingActivity;
import com.cbn.abcmall.activites.TicketActivity;
import com.cbn.abcmall.bean.MemberInfo;
import com.cbn.abcmall.bean.MemberInfoDetail;
import com.cbn.abcmall.bean.OrderCount;
import com.cbn.abcmall.bean.OrderStatus;
import com.cbn.abcmall.bean.OrderStatusShop;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.ToastUtils;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 我的页面展示
 * Created by Administrator on 2015/9/10.
 */
public class MineFragment extends Fragment implements View.OnClickListener{

    private TextView tv_name;
    private RelativeLayout rl_collect_shop;// 收藏的店铺
    private RelativeLayout rl_collect_product; //收藏的商品
    private RelativeLayout rl_check_update;// 更新检测
    private RelativeLayout rl_address;// 收货地址
    private TextView tv_rest;
    private FrameLayout rl_wait_deliver;
    private LinearLayout ll_charge;// 充值


    private RelativeLayout rl_daijinquan;
    private RelativeLayout rl_wodeyaoqing;
    private RelativeLayout rl_youhuiquan;
    private RelativeLayout rl_fanlixinxi;

    private FrameLayout rl_wait_pay;
    private FrameLayout rl_wait_receipt;
    private FrameLayout rl_receipted;
    private RelativeLayout rl_all_orders;
    private FrameLayout rl_all_refund_orders;
    private String token, sign;
    private Handler handler;
    private TextView tv_wait_pay_count;
    private TextView tv_wait_deliver_count;
    private TextView tv_wait_receipt_count;
    private TextView tv_receipted_count;
    private TextView right;// 个人中心设置按钮
    private TextView tv_version;// 版本号

    private android.app.ProgressDialog progressDialog;

    private void bindViews(View v) {
        tv_name = (TextView) v.findViewById(R.id.tv_name);
        tv_rest = (TextView) v.findViewById(R.id.tv_rest);
        rl_wait_deliver = (FrameLayout) v.findViewById(R.id.rl_wait_deliver);
        rl_wait_pay = (FrameLayout) v.findViewById(R.id.rl_wait_pay);
        rl_wait_receipt = (FrameLayout) v.findViewById(R.id.rl_wait_receipt);
        rl_receipted = (FrameLayout) v.findViewById(R.id.rl_receipted);
        rl_all_orders = (RelativeLayout) v.findViewById(R.id.rl_all_orders);
        rl_all_refund_orders = (FrameLayout) v.findViewById(R.id.rl_all_refund_orders);
        ll_charge = (LinearLayout) v.findViewById(R.id.ll_charge);
        tv_wait_pay_count = (TextView) v.findViewById(R.id.tv_wait_pay_count);
        tv_wait_deliver_count = (TextView) v.findViewById(R.id.tv_wait_deliver_count);
        tv_wait_receipt_count = (TextView) v.findViewById(R.id.tv_wait_receipt_count);
        tv_receipted_count = (TextView) v.findViewById(R.id.tv_receipted_count);
        right = (TextView) v.findViewById(R.id.right);
        rl_address = (RelativeLayout) v.findViewById(R.id.rl_address);
        rl_collect_shop = (RelativeLayout) v.findViewById(R.id.rl_collect_shop);
        rl_collect_product = (RelativeLayout) v.findViewById(R.id.rl_collect_product);
        rl_check_update = (RelativeLayout) v.findViewById(R.id.rl_check_update);
        tv_version = (TextView) v.findViewById(R.id.tv_version);
        tv_version.setText("当前版本 V" + Config.getVersion(getActivity()));

        rl_daijinquan = (RelativeLayout) v.findViewById(R.id.rl_daijinquan);
        rl_daijinquan.setOnClickListener(this);
        rl_wodeyaoqing = (RelativeLayout) v.findViewById(R.id.rl_wodeyaoqing);
        rl_wodeyaoqing.setOnClickListener(this);
        rl_youhuiquan = (RelativeLayout) v.findViewById(R.id.rl_youhuiquan);
        rl_youhuiquan.setOnClickListener(this);
        rl_fanlixinxi = (RelativeLayout) v.findViewById(R.id.rl_falixinxi);
        rl_fanlixinxi.setOnClickListener(this);

        progressDialog = new android.app.ProgressDialog(getActivity());
        progressDialog.setMessage("正在加载....");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mine, container, false);
        bindViews(v);
        ll_charge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent chargeIntent = new Intent(getActivity(), ChargeActivity.class);
                startActivityForResult(chargeIntent, 0);
            }
        });

        rl_wait_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent waitPayIntent = new Intent(getActivity(), OrderFragmentActivity.class);
                waitPayIntent.putExtra("status", 1);
                startActivity(waitPayIntent);
            }
        });

        rl_wait_deliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent waitDeliverIntent = new Intent(getActivity(), OrderFragmentActivity.class);
                waitDeliverIntent.putExtra("status", 2);
                startActivity(waitDeliverIntent);
            }
        });

        rl_wait_receipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent waitReceiptIntent = new Intent(getActivity(), OrderFragmentActivity.class);
                waitReceiptIntent.putExtra("status", 3);
                startActivity(waitReceiptIntent);
            }
        });

        rl_receipted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent receiptedIntent = new Intent(getActivity(), OrderFragmentActivity.class);
                receiptedIntent.putExtra("status", 4);
                startActivity(receiptedIntent);
            }
        });

        rl_all_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allOrdersIntent = new Intent(getActivity(), OrderFragmentActivity.class);
                allOrdersIntent.putExtra("status", 0);
                startActivity(allOrdersIntent);
            }
        });

        rl_all_refund_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent allRefundOrdersIntent = new Intent(getActivity(), AllRefundActivity.class);
                startActivity(allRefundOrdersIntent);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settingIntent = new Intent(getActivity(), SettingActivity.class);
                settingIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(settingIntent);
            }
        });

        rl_collect_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(getActivity(), CollectActivity.class);
                productIntent.putExtra("type", "product");
                startActivity(productIntent);
            }
        });

        rl_collect_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shopIntent = new Intent(getActivity(), CollectActivity.class);
                shopIntent.putExtra("type", "shop");
                startActivity(shopIntent);
            }
        });

        // 更新检测
        rl_check_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                UmengUpdateAgent.forceUpdate(getActivity());
                UmengUpdateAgent.setUpdateAutoPopup(false);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                        switch (i) {
                            case UpdateStatus.Yes:
                                progressDialog.dismiss();
                                UmengUpdateAgent.showUpdateDialog(getActivity(), updateResponse);
                                break;
                            case UpdateStatus.No: // has no update
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "当前为最新版本，无需更新！", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.NoneWifi: // none wifi
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.Timeout: // time out
                                progressDialog.dismiss();
                                Toast.makeText(getActivity(), "网络无法连接", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });

            }
        });

        // 地址
        rl_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addressIntent = new Intent(getActivity(), AddressActivity.class);
                startActivity(addressIntent);
            }
        });


        return v;

    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        String json = (String) msg.obj;

                        if (json.contains("验证出错")) {
                            Intent LoginIntent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(LoginIntent);
                            getActivity().finish();
                            return;
                        }
                        if (json.contains("登录失效")) {
                            ToastUtils.TextToast(getActivity(), "登录失效，请重新登录", Toast.LENGTH_SHORT);
                            Config.Login(getActivity());
                            return;
                        } else {
                            LogUtils.i("info", "得到的个人信息------->" + json);
                            MemberInfo memberInfo = (MemberInfo) JsonUtils.jsonToBean(json, MemberInfo.class);
                            MemberInfoDetail memberInfoDetail = memberInfo.getMemberinfo();
                            tv_name.setText("账户：  " + memberInfoDetail.getUser());
                            tv_rest.setText("余额：  " + "¥" + memberInfoDetail.getCash());


                            List<OrderCount> orderCounts = memberInfo.getOrder_count();

                            for (int i = 0; i < orderCounts.size(); i++) {
                                switch (orderCounts.get(i).getStatus()) {

                                    case "1":
                                        tv_wait_pay_count.setVisibility(View.VISIBLE);
                                        tv_wait_pay_count.setText(orderCounts.get(i).getNum());
                                        break;

                                    case "2":
                                        tv_wait_deliver_count.setVisibility(View.VISIBLE);
                                        tv_wait_deliver_count.setText(orderCounts.get(i).getNum());

                                    case "3":
                                        tv_wait_receipt_count.setVisibility(View.VISIBLE);
                                        tv_wait_receipt_count.setText(orderCounts.get(i).getNum());
                                        break;

                                    case "4":
                                        tv_receipted_count.setVisibility(View.VISIBLE);
                                        tv_receipted_count.setText(orderCounts.get(i).getNum());
                                        break;
                                }


                            }
                        }
                        break;
                }
            }
        };
    }

    /**
     * 填写token和sign
     */
    private void initData() {
        token = Config.getAccountToken(getActivity());
        sign = Config.getSign(getActivity());
        if (TextUtils.isEmpty(token)) {
            ToastUtils.TextToast(getActivity(), "请先登录", Toast.LENGTH_SHORT);

            return;
        }
    }


    /**
     * 当fragment可见时再请求网络数据
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        if (isVisibleToUser && isVisible()) {
            // 初始化数据
            initData();
            // 获取会员信息
            getMemberInfo();
            // 获取所有订单类型的数量
            getAllCount();
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    /**
     * 从网络获取会员信息实体类
     */
    private void getMemberInfo() {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("sign", sign);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.httpPostResult(getActivity(), jsonObject, handler, Config.URL_GET_MEMBER_INFO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            getMemberInfo();
        }
    }


    /**
     * 获取待发货数量
     */
    private void getDeliverCount() {
        getData(2);
    }

    /**
     * 获取待付款数量
     */


    private void getPayCount() {
        getData(1);
    }

    /**
     * 获取待收货数量
     */
    private void getReceiptingCount() {
        getData(3);

    }


    /**
     * 获取数据
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
                if (str.equals("") || str.equals("登录失效")) {

                    return;
                }

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
                        message.what = status;
                        // 如果列表为空时，发送数量为0
                        message.obj = 0;
                    } else {
                        OrderStatus orderStatus = (OrderStatus) JsonUtils.jsonToBean(json, OrderStatus.class);
                        List<OrderStatusShop> orderStatusShops = orderStatus.getOrderList();
                        message.obj = orderStatusShops.size();
                        message.what = status;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

//                getCountHandler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 获得所有数量
     */
    private void getAllCount() {
        getDeliverCount();
        getPayCount();
        getReceiptingCount();
    }

    @Override
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.rl_daijinquan:

                Intent ticketIntent = new Intent(getActivity(), TicketActivity.class);
                startActivity(ticketIntent);
                break;

            case R.id.rl_wodeyaoqing:

                Intent inviteIntent = new Intent(getActivity(), InviteActivity.class);
                startActivity(inviteIntent);

                break;

            case R.id.rl_youhuiquan:
                Intent youhuiquanIntent = new Intent(getActivity(), CouponActivity.class);
                startActivity(youhuiquanIntent);
                break;

            case R.id.rl_falixinxi:
                Intent rebateIntent = new Intent(getActivity(), RebateActivity.class);
                startActivity(rebateIntent);
                break;
        }

    }
}
