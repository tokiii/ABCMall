package com.cbn.abcmall.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.bean.CartGood;
import com.cbn.abcmall.bean.CartShop;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;
import com.cbn.abcmall.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2015/9/29.
 */
public class WaitReceiptAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CartShop> groupList;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private LayoutInflater inflater;

    public WaitReceiptAdapter(Context context, List<CartShop> groupList) {
        this.context = context;
        this.groupList = groupList;
        requestQueue = MyApplication.getInstance().getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new BitMapCache());
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupList.get(groupPosition).getShopList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).getShopList().get(childPosition);
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

        final CartShop group = (CartShop) getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_waitreceipt_item, null);
        }
        TextView groupText = (TextView) convertView.findViewById(R.id.groupText);
//        TextView tv_status = (TextView) convertView.findViewById(R.id.tv_status);
        groupText.setText(group.getCompany());
//        tv_status.setText("等待买家收货");
        convertView.setClickable(true);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final CartGood cartGood = groupList.get(groupPosition).getShopList().get(childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_waitreceipt_item, null);
        }
        TextView childText = (TextView) convertView.findViewById(R.id.childText);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_count);
        NetworkImageView niv_image = (NetworkImageView) convertView.findViewById(R.id.niv_item);
        TextView tv_color = (TextView) convertView.findViewById(R.id.tv_color);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
        Button btn_check = (Button) convertView.findViewById(R.id.btn_check);
        Button btn_confirm = (Button) convertView.findViewById(R.id.btn_confirm);
        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.ll_tip);
        TextView tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
        TextView tv_total_price = (TextView) convertView.findViewById(R.id.tv_total_price);
        childText.setText(cartGood.getName());
        tv_count.setText("X" + cartGood.getCount());
        tv_color.setText(cartGood.getColor());
        tv_price.setText("单价：" + cartGood.getPrice());
        niv_image.setImageUrl(cartGood.getImage(), imageLoader);
        if (isLastChild) {
            linearLayout.setVisibility(View.VISIBLE);
            tv_amount.setText("共" + groupList.get(groupPosition).getShopList().size() + "件商品");
            tv_total_price.setText("总价：" + groupList.get(groupPosition).getSumprice());
            btn_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

            // 确认收货
            btn_confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new AlertDialog.Builder(context)
                                .setTitle("确认收货")
                                .setMessage("是否确认收货")
                                .setPositiveButton("确定",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialoginterface, int i) {
                                                String orderId = groupList.get(groupPosition).getOrderId();
                                                confirmOrder(orderId);
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        }).show();

                    }
            });

        }else {
            linearLayout.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }



    private void confirmOrder(String orderId) {

        String token = Config.getAccountToken(context);
        String sign = Config.getSign(context);

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sign", sign);
            jsonObject.put("token", token);
            jsonObject.put("orderid", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.i("info", "发送的数据为------->" + jsonObject.toString());

     /*   new Thread(new Runnable() {
            @Override
            public void run() {
              String str = HttpUtils.JsonPost(Config.URL_CONFIRM_RECEIPT, jsonObject);
                String json = JsonUtils.decodeUnicode(str);
                LogUtils.i("info", "确认收货得到数据为------>" + json);
            }
        }).start();*/

    }
}
