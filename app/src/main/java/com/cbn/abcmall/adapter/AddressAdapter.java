package com.cbn.abcmall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.AddressDetail;

import java.util.List;

/**
 * 收货地址列表适配
 * Created by Administrator on 2015/9/30.
 */
public class AddressAdapter extends BaseAdapter {

    private Context context;
    private List<AddressDetail> lists;

    public AddressAdapter(Context context, List<AddressDetail> lists) {
        this.context = context;
        this.lists = lists;
    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_address, null);
            viewHolder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tv_phone = (TextView) convertView.findViewById(R.id.tv_phone);
            viewHolder.tv_default = (TextView) convertView.findViewById(R.id.tv_default);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();

        }
        if (lists.get(position).getDef().equals("2")) {
            viewHolder.tv_default.setVisibility(View.VISIBLE);
        }
        viewHolder.tv_address.setText(lists.get(position).getArea() +  " " + lists.get(position).getAddress());
        viewHolder.tv_name.setText(lists.get(position).getName());
        viewHolder.tv_phone.setText(lists.get(position).getMobile());

        return convertView;
    }

    class ViewHolder{
        private TextView tv_name;
        private TextView tv_phone;
        private TextView tv_address;
        private TextView tv_default;// 默认状态
    }
}
