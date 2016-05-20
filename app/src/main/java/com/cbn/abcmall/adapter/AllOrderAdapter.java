package com.cbn.abcmall.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.activites.LogisticActivity;
import com.cbn.abcmall.activites.OrderDetailActivity;
import com.cbn.abcmall.activites.PayActivity;
import com.cbn.abcmall.activites.RefundActivity;
import com.cbn.abcmall.activites.RefundDetailActivity;
import com.cbn.abcmall.bean.CartGood;
import com.cbn.abcmall.bean.CartShop;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;
import com.cbn.abcmall.utils.DateUtil;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 所有订单列表适配器
 * Created by Administrator on 2015/10/10.
 */
public class AllOrderAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<CartShop> groupLists;
    private ImageLoader imageLoader;
    private String orderId;
    private String status;
    private String refundId;


    public AllOrderAdapter(Context context, List<CartShop> groupLists) {
        this.groupLists = groupLists;
        this.context = context;
        imageLoader = new ImageLoader(MyApplication.getInstance().getRequestQueue(), new BitMapCache());
    }


    @Override
    public int getGroupCount() {
        return groupLists.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupLists.get(groupPosition).getShopList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupLists.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupLists.get(groupPosition).getShopList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {



        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.group_allorder_item, null);
        }
        TextView groupText = (TextView) convertView.findViewById(R.id.groupText);
        groupText.setText(groupLists.get(groupPosition).getCompany());
        TextView tv_order_time = (TextView) convertView.findViewById(R.id.tv_order_time);
        TextView tv_order_id = (TextView) convertView.findViewById(R.id.tv_order_id);
        TextView tv_status = (TextView) convertView.findViewById(R.id.tv_status);
        switch (groupLists.get(groupPosition).getStatus()) {
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
        Long time = Long.valueOf(groupLists.get(groupPosition).getOrderTime());
        String t = DateUtil.getDateToString(time * 1000);
        tv_order_time.setText("时间：" + t);
        tv_order_id.setText("编号：" + groupLists.get(groupPosition).getOrderId());
        convertView.setClickable(true);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final CartGood cartGood = groupLists.get(groupPosition).getShopList().get(childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.child_allorder_item, null);
        }

        Button btn_left = (Button) convertView.findViewById(R.id.btn_left);
        Button btn_right = (Button) convertView.findViewById(R.id.btn_right);
        Button btn_apply_refund = (Button) convertView.findViewById(R.id.btn_apply_refund);
        LinearLayout ll_child = (LinearLayout) convertView.findViewById(R.id.ll_child);
        LinearLayout ll_tip = (LinearLayout) convertView.findViewById(R.id.ll_tip);
        TextView tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
        TextView tv_total_price = (TextView) convertView.findViewById(R.id.tv_total_price);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_count);
        TextView childText = (TextView) convertView.findViewById(R.id.childText);
        TextView tv_color = (TextView) convertView.findViewById(R.id.tv_color);
        TextView tv_express = (TextView) convertView.findViewById(R.id.tv_express);
        NetworkImageView niv_image = (NetworkImageView) convertView.findViewById(R.id.niv_item);

        childText.setText(cartGood.getName());
        tv_count.setText("X" + cartGood.getCount());
        tv_color.setText(cartGood.getColor());
        tv_price.setText("¥" + cartGood.getPrice());
        niv_image.setImageUrl(cartGood.getImage(), imageLoader);
        tv_express.setText("邮费：¥" + cartGood.getExpress());

        // 子项目点击查看详情
        ll_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent orderDetailIntent = new Intent(context, OrderDetailActivity.class);
                orderDetailIntent.putExtra("orderId", groupLists.get(groupPosition).getOrderId());
                orderDetailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(orderDetailIntent);
            }
        });

        switch (Integer.valueOf(cartGood.getOrderStatus())) {
            case 1:
                // 待付款
                isWaitPay(btn_left, btn_right, btn_apply_refund, groupPosition, childPosition);
                break;
            case 2:
                // 待发货
                isWaitDeliver(btn_left, btn_right, btn_apply_refund, groupPosition, childPosition, cartGood.getReimburseStatus() + "");
                break;
            case 3:
                // 待收货
                isWaitReceipt(btn_left, btn_right, btn_apply_refund, groupPosition, childPosition, cartGood.getReimburseStatus() + "");
                break;
            case 4:
                // 已收货
                isReceipted(btn_left, btn_right, btn_apply_refund, groupPosition, childPosition, cartGood.getReimburseStatus() + "");
                break;
        }

        if (isLastChild) {
            ll_tip.setVisibility(View.VISIBLE);
            tv_amount.setText("共" + groupLists.get(groupPosition).getShopList().size() + "件商品");
            tv_total_price.setText("¥" + groupLists.get(groupPosition).getSumprice());
        } else {
            ll_tip.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


    /**
     * 待付款状态
     */
    private void isWaitPay(Button btn_left, Button btn_right, Button btn_apply_refund, final int groupPosition, final int childPosition) {
        btn_left.setText("取消订单");
        btn_right.setText("付款");
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.VISIBLE);
        btn_apply_refund.setVisibility(View.GONE);

        // 取消订单
        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(context)
                        .setTitle("取消订单")
                        .setMessage("是否取消订单")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        cancelOrder(groupLists.get(groupPosition).getOrderId(), groupPosition);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

            }
        });

        // 付款
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                pay(groupLists, groupPosition);

            }
        });
    }

    /**
     * 待发货状态
     */
    private void isWaitDeliver(Button btn_left, Button btn_right, Button btn_apply_refund, final int groupPostion, final int childPosition, String r) {
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.VISIBLE);
        btn_left.setText("取消订单");
        btn_right.setText("提醒发货");
        btn_apply_refund.setVisibility(View.VISIBLE);
        btn_left.setVisibility(View.INVISIBLE);


        // 提醒发货
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ToastUtils.TextToast(context, "提醒成功", Toast.LENGTH_SHORT);

            }
        });
        orderId = groupLists.get(groupPostion).getOrderId();
        status = groupLists.get(groupPostion).getShopList().get(childPosition).getRefund_status();
        refundId = groupLists.get(groupPostion).getShopList().get(childPosition).getRefund_id();

        setOrderStatus(btn_apply_refund, r, "1", "0", groupPostion, childPosition);

    }

    /**
     * 待收货状态
     */
    private void isWaitReceipt(Button btn_left, Button btn_right, final Button btn_apply_refund, final int groupPosition, final int childPosition, final String r) {
        btn_left.setVisibility(View.VISIBLE);
        btn_right.setVisibility(View.VISIBLE);
        btn_left.setText("查看物流");
        btn_right.setText("确认收货");
        btn_apply_refund.setVisibility(View.VISIBLE);

        btn_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logistic(groupLists.get(groupPosition).getOrderId());
            }
        });

        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmOrder(groupLists.get(groupPosition).getOrderId(), groupPosition);
            }
        });

        btn_apply_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setOrderStatus(btn_apply_refund, r, "2", "1", groupPosition, childPosition);
            }
        });
    }

    /**
     * 已收货状态
     */
    private void isReceipted(final Button btn_left, final Button btn_right, final Button btn_apply_refund, final int groupPosition, final int childPosition, String r) {
        btn_left.setVisibility(View.INVISIBLE);
        btn_right.setVisibility(View.VISIBLE);
        btn_right.setText("查看物流");
        btn_apply_refund.setVisibility(View.VISIBLE);

        // 查看物流
        btn_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logistic(groupLists.get(groupPosition).getOrderId());
            }
        });



        setOrderStatus(btn_apply_refund, r, "2", "1", groupPosition, childPosition);
    }

    /**
     * 退款操作
     *
     * @param groupLists
     * @param groupPosition
     * @param childPosition
     */
    private void refund(List<CartShop> groupLists, int groupPosition, int childPosition, String status, String goodStatus) {
        final CartGood cartGood = groupLists.get(groupPosition).getShopList().get(childPosition);
        Intent refundIntent = new Intent(context, RefundActivity.class);
        refundIntent.putExtra("orderId", groupLists.get(groupPosition).getOrderId());
        refundIntent.putExtra("id", cartGood.getId());
        refundIntent.putExtra("sellerId", cartGood.getSellerid());
        refundIntent.putExtra("status", status);
        refundIntent.putExtra("goodStatus", goodStatus);
        refundIntent.putExtra("price", cartGood.getPrice());
        refundIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 重新开启一个新任务
        context.startActivity(refundIntent);
    }


    /**
     * 查看物流
     *
     * @param orderId
     */
    private void logistic(String orderId) {
        Intent logisticIntent = new Intent(context, LogisticActivity.class);
        logisticIntent.putExtra("orderId", orderId);
        logisticIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(logisticIntent);
    }


    private int cangroupPosition;
    Handler cancelHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    if (msg.obj.toString().contains("取消订单成功")) {
                        ToastUtils.TextToast(context, "取消订单成功", Toast.LENGTH_SHORT);
                        groupLists.remove(groupLists.get(cangroupPosition));
                        notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    ;

    /**
     * 取消订单
     *
     * @param orderId
     */
    private void cancelOrder(String orderId, final int groupPosition) {

        cangroupPosition = groupPosition;

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(context));
            jsonObject.put("sign", Config.getSign(context));
            jsonObject.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.httpPostResult(context, jsonObject, cancelHandler, Config.URL_DELETE_ORDER);

    }


    Handler confirmHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String json = (String) msg.obj;
                    if (json.contains("确认收货成功")) {
                        ToastUtils.TextToast(context, "确认收货成功！", Toast.LENGTH_SHORT);
                        groupLists.remove(groupLists.get(confirmPosition));
                        notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    ;
    /**
     * 确认收货
     *
     * @param orderId
     */

    int confirmPosition;

    private void confirmOrder(String orderId, final int groupPosition) {

        confirmPosition = groupPosition;

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(context));
            jsonObject.put("sign", Config.getSign(context));
            jsonObject.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.httpPostResult(context, jsonObject, confirmHandler, Config.URL_CONFIRM_RECEIPT);
    }


    /**
     * 支付
     *
     * @param groupList
     * @param groupPosition
     */
    private void pay(List<CartShop> groupList, int groupPosition) {
        Intent payIntent = new Intent(context, PayActivity.class);
        payIntent.putExtra("orderId", groupList.get(groupPosition).getOrderId());
        payIntent.putExtra("fromWait", "wait");
        payIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(payIntent);

    }


    /**
     * 退款状态的商品设置
     *
     * @param b
     * @param r
     */
    private void setOrderStatus(Button b, String r, final String arg1, final String arg2, final int group, final int child) {

        if (r.equals("4") || r.equals("5")) {
            b.setBackgroundColor(Color.GRAY);
            if (r.equals("4")) {
                b.setText("退款中");
            } else if (r.equals("5")) {
                b.setText("退款成功");
            }
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    orderId = groupLists.get(group).getOrderId();
                    status = groupLists.get(group).getShopList().get(child).getRefund_status();
                    refundId = groupLists.get(group).getShopList().get(child).getRefund_id();

                    Intent refundDetailIntent = new Intent(context, RefundDetailActivity.class);
                    refundDetailIntent.putExtra("orderId", orderId);
                    refundDetailIntent.putExtra("refundId", refundId);
                    refundDetailIntent.putExtra("status", status);
                    refundDetailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//
                    LogUtils.i("info", "从订单列表到退单详情传输的数据有----->" + orderId + "   " + refundId + "  " + status);
                    context.startActivity(refundDetailIntent);
                }
            });

        }else {
            b.setBackgroundColor(context.getResources().getColor(R.color.app_button_color));
            b.setText("申请退款");
            b.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    refund(groupLists, group, child, arg1, arg2);
                }
            });
        }

    }


}
