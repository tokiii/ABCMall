package com.cbn.abcmall.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.cbn.abcmall.activites.LogisticActivity;
import com.cbn.abcmall.activites.RefundActivity;
import com.cbn.abcmall.bean.CartGood;
import com.cbn.abcmall.bean.CartShop;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;

import java.util.List;

/**
 * 已收货适配器
 * Created by Administrator on 2015/10/8.
 */
public class ReceiptedAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<CartShop> groupLists;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    public ReceiptedAdapter(Context context, List<CartShop> groupLists) {
        this.groupLists = groupLists;
        this.context = context;
        requestQueue = MyApplication.getInstance().getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new BitMapCache());
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
            convertView = LayoutInflater.from(context).inflate(R.layout.group_receipted_item, null);
        }
        TextView groupText = (TextView) convertView.findViewById(R.id.groupText);
        groupText.setText(groupLists.get(groupPosition).getCompany());
        convertView.setClickable(true);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final CartGood cartGood = groupLists.get(groupPosition).getShopList().get(childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.child_receipted_item, null);
        }

        LinearLayout ll_tip = (LinearLayout) convertView.findViewById(R.id.ll_tip);
        Button btn_express = (Button) convertView.findViewById(R.id.btn_express);
        TextView tv_amount = (TextView) convertView.findViewById(R.id.tv_amount);
        TextView tv_total_price = (TextView) convertView.findViewById(R.id.tv_total_price);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_count);
        TextView childText = (TextView) convertView.findViewById(R.id.childText);
        TextView tv_color = (TextView) convertView.findViewById(R.id.tv_color);
        Button btn_apply_refund = (Button) convertView.findViewById(R.id.btn_apply_refund);
        NetworkImageView niv_image = (NetworkImageView) convertView.findViewById(R.id.niv_item);

        childText.setText(cartGood.getName());
        tv_count.setText("X" + cartGood.getCount());
        tv_color.setText(cartGood.getColor());
        tv_price.setText("单价：" + cartGood.getPrice());
        niv_image.setImageUrl(cartGood.getImage(), imageLoader);


        if (isLastChild) {
            ll_tip.setVisibility(View.VISIBLE);
            tv_amount.setText("共" + groupLists.get(groupPosition).getShopList().size() + "件商品");
            tv_total_price.setText("" + groupLists.get(groupPosition).getSumprice());
        } else {
            ll_tip.setVisibility(View.GONE);
        }

        // 申请退款
        btn_apply_refund.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent refundIntent = new Intent(context, RefundActivity.class);
                refundIntent.putExtra("orderId", groupLists.get(groupPosition).getOrderId());
                refundIntent.putExtra("id", cartGood.getId());
                refundIntent.putExtra("sellerId", cartGood.getSellerid());
                refundIntent.putExtra("status", "2");
                refundIntent.putExtra("goodStatus", "1");
                refundIntent.putExtra("price", cartGood.getPrice());
                context.startActivity(refundIntent);
            }
        });

        // 查看物流
        btn_express.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logisticIntent = new Intent(context, LogisticActivity.class);
                logisticIntent.putExtra("orderId", groupLists.get(groupPosition).getOrderId());
                context.startActivity(logisticIntent);
            }
        });

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
