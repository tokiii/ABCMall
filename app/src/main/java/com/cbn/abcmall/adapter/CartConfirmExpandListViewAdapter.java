package com.cbn.abcmall.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.CartGood;
import com.cbn.abcmall.bean.CartShop;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 确认收货界面适配器
 */
public class CartConfirmExpandListViewAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private List<CartShop> groupList;
    LayoutInflater inflater;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private int index = -1;
    private Map<Integer, String> map;

    public CartConfirmExpandListViewAdapter(Context ctx, List<CartShop> groupList) {
        this.ctx = ctx;
        this.groupList = groupList;
        inflater = LayoutInflater.from(ctx);
        requestQueue = MyApplication.getInstance().getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new BitMapCache());
        map = new HashMap<>();
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
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final CartShop group = (CartShop) getGroup(groupPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.group_confirm_item, null);

        }
        TextView groupText = (TextView) convertView.findViewById(R.id.groupText);
        groupText.setText(group.getCompany());
        convertView.setClickable(true);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final CartGood good = groupList.get(groupPosition).getShopList().get(childPosition);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.child_confirm_item, null);
        }
        TextView childText = (TextView) convertView.findViewById(R.id.childText);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_count);
        NetworkImageView niv_image = (NetworkImageView) convertView.findViewById(R.id.niv_item);
        TextView tv_express = (TextView) convertView.findViewById(R.id.tv_express);
        TextView tv_color = (TextView) convertView.findViewById(R.id.tv_color);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
        LinearLayout relativeLayout = (LinearLayout) convertView.findViewById(R.id.ll_tip);
        EditText editText = (EditText) convertView.findViewById(R.id.et_live);
        childText.setText(good.getName());
        tv_count.setText("X" + good.getCount());
        tv_color.setText(good.getColor());
        tv_price.setText("¥" + good.getPrice());
        niv_image.setImageUrl(good.getImage(), imageLoader);
        if (isLastChild) {
            relativeLayout.setVisibility(View.VISIBLE);
            tv_express.setText("¥" + good.getExpress() + ".00");
            editText.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        index = groupPosition;
                    }
                    return false;
                }
            });


            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    groupList.get(groupPosition).setMsg(s.toString());
                }
            });

        } else {
            relativeLayout.setVisibility(View.GONE);
        }


        editText.clearFocus();
        if (index != -1 && index == groupPosition) {
            editText.requestFocus();
            editText.setSelection(editText.getText().length());
        }
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }


}
