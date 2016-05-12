package com.cbn.abcmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.LogisticData;

import java.util.List;

/**
 * Created by Administrator on 2015/10/19.
 */
public class LogisticAdapter extends BaseAdapter {

    private Context context;
    private List<LogisticData> list;

    public LogisticAdapter(Context context, List<LogisticData> list) {
        this.context = context;
        this.list = list;
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
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_logistic, null);
            holder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        }
        holder = (ViewHolder) convertView.getTag();
        holder.tv_context.setText(list.get(position).getContext());
        holder.tv_time.setText(list.get(position).getTime());
        return convertView;
    }


    class ViewHolder{
        TextView tv_context;
        TextView tv_time;
    }
}
