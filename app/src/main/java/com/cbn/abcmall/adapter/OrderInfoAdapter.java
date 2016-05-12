package com.cbn.abcmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.OrderInfoList;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;

import java.util.List;

/**
 * 订单详情适配器
 * Created by Administrator on 2015/10/26.
 */
public class OrderInfoAdapter extends BaseAdapter {

    private Context context;
    private List<OrderInfoList> list;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    public OrderInfoAdapter(Context context, List<OrderInfoList> list) {
        this.context = context;
        this.list = list;
        requestQueue = MyApplication.getInstance().getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new BitMapCache());
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        final OrderInfoList orderInfoList = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_order_info, null);
            holder.niv_item = (NetworkImageView) convertView.findViewById(R.id.niv_item);
            holder.childText = (TextView) convertView.findViewById(R.id.childText);
            holder.tv_color = (TextView) convertView.findViewById(R.id.tv_color);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.niv_item.setImageUrl(orderInfoList.getPicUrl(), imageLoader);
        holder.tv_color.setText(orderInfoList.getComboName());
        holder.tv_count.setText("X" + orderInfoList.getProductVolume());
        holder.tv_price.setText(orderInfoList.getPrice());
        holder.childText.setText(orderInfoList.getProductName());

        return convertView;
    }

    class ViewHolder {
        NetworkImageView niv_item;
        TextView childText;
        TextView tv_color;
        TextView tv_price;
        TextView tv_count;
    }
}
