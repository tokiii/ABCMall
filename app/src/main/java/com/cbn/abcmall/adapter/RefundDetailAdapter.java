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
import com.cbn.abcmall.bean.GetRefundTalk;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;
import com.cbn.abcmall.utils.DateUtil;

import java.util.List;

/**
 * 退款详情adapter
 * Created by Administrator on 2015/10/19.
 */
public class RefundDetailAdapter extends BaseAdapter {

    private Context context;
    private List<GetRefundTalk> list;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;


    public RefundDetailAdapter(Context context, List<GetRefundTalk> list) {
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
        final GetRefundTalk getRefundTalk = list.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_talk, null);
            holder.niv_head = (NetworkImageView) convertView.findViewById(R.id.niv_head);
            holder.niv_pic = (NetworkImageView) convertView.findViewById(R.id.niv_pic);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }

        Long time = Long.valueOf(getRefundTalk.getCreate_time());
        holder = (ViewHolder) convertView.getTag();
        holder.tv_name.setText(getRefundTalk.getEmail());
        holder.tv_content.setText(getRefundTalk.getContent());
        holder.niv_pic.setVisibility(View.GONE);
        holder.tv_time.setText(DateUtil.getDateToString(time * 1000));
        if (getRefundTalk.getLogo().contains("http"))
        holder.niv_head.setImageUrl(getRefundTalk.getLogo(), imageLoader);
        if (!getRefundTalk.getPic().equals("")) {
            holder.niv_pic.setVisibility(View.VISIBLE);
            holder.niv_pic.setImageUrl(getRefundTalk.getPic(), imageLoader);
        }


        return convertView;
    }


    class ViewHolder{
        NetworkImageView niv_head;
        TextView tv_time;
        TextView tv_content;
        TextView tv_name;
        NetworkImageView niv_pic;
    }
}
