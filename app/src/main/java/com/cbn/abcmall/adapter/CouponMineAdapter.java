package com.cbn.abcmall.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.MainActivity;
import com.cbn.abcmall.bean.Coupon;
import com.cbn.abcmall.utils.AppManager;
import com.cbn.abcmall.utils.DateUtil;

import java.util.List;

/**
 * 我的优惠券适配器
 * Created by lost on 2016/5/6.
 */
public class CouponMineAdapter extends BaseAdapter {


    private List<Coupon> list;
    private Context context;


    public CouponMineAdapter(Context context, List<Coupon> list) {
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

        ViewHolder holder = null;
        Coupon coupon = list.get(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_couponmine, null);
            holder = new ViewHolder();
            holder.tv_condition = (TextView) convertView.findViewById(R.id.tv_condition);
            holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
            holder.tv_number = (TextView) convertView.findViewById(R.id.tv_number);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.rl_youhuiquan = (RelativeLayout) convertView.findViewById(R.id.rl_youhuiquan);
            holder.btn_use = (Button) convertView.findViewById(R.id.btn_use);// 立即使用
            convertView.setTag(holder);

        }
        holder = (ViewHolder) convertView.getTag();
        holder.tv_condition.setText(coupon.getDesc());
        holder.tv_price.setText(coupon.getPrice());
        holder.tv_number.setText("编号：" + coupon.getSerial());
        holder.tv_date.setText("截止日期：" + DateUtil.getInviteDate(Long.valueOf(coupon.getEnd_time())));
        if (coupon.getPrice().equals("50")) {
            holder.rl_youhuiquan.setBackgroundResource(R.mipmap.wushi);
        }else if (coupon.getPrice().equals("30")){
            holder.rl_youhuiquan.setBackgroundResource(R.mipmap.sanshi);
        }else {
            holder.rl_youhuiquan.setBackgroundResource(R.mipmap.ershi);
        }

        holder.btn_use.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent homeIntent = new Intent(context, MainActivity.class);
                homeIntent.putExtra("page", 0);// 跳转到首页
                context.startActivity(homeIntent);
                AppManager.getAppManager().finishOthersActivity(MainActivity.class);

            }
        });

        return convertView;
    }


    class ViewHolder {
        private TextView tv_price;
        private TextView tv_number;
        private TextView tv_condition;
        private TextView tv_date;
        private RelativeLayout rl_youhuiquan;
        private Button btn_use;
    }
}
