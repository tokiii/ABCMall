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
import com.cbn.abcmall.bean.GetRefundListData;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;
import com.cbn.abcmall.utils.DateUtil;
import com.cbn.abcmall.utils.LogUtils;

import java.util.List;

/**
 * 所有退单适配器
 * Created by Administrator on 2015/10/10.
 */
public class AllRefundAdapter extends BaseAdapter{

    private Context context;
    private List<GetRefundListData> lists;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;


    public AllRefundAdapter(Context context, List<GetRefundListData> lists) {
        this.context = context;
        this.lists = lists;
        requestQueue = MyApplication.getInstance().getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new BitMapCache());
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

        ViewHolder holder;
        final GetRefundListData getRefundListData = lists.get(position);
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_refund_list, null);
            holder.niv_product = (NetworkImageView) convertView.findViewById(R.id.niv_product);
            holder.tv_order_id = (TextView) convertView.findViewById(R.id.tv_order_id);
            holder.tv_product_name = (TextView) convertView.findViewById(R.id.tv_product_name);
            holder.tv_refund_id = (TextView) convertView.findViewById(R.id.tv_refund_id);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_shop_name = (TextView) convertView.findViewById(R.id.tv_shop_name);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
            holder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            holder.tv_reason = (TextView) convertView.findViewById(R.id.tv_reason);


            convertView.setTag(holder);
        }


        holder = (ViewHolder) convertView.getTag();
        holder.tv_order_id.setText(getRefundListData.getOrder_id());
        holder.tv_product_name.setText(getRefundListData.getName());
        holder.tv_refund_id.setText(getRefundListData.getRefund_id());
        LogUtils.i("info", "获得的地址------>" + getRefundListData.getPpic());
        if (getRefundListData.getPpic().contains("http")){
            holder.niv_product.setImageUrl(getRefundListData.getPpic(), imageLoader);
        }

        holder.tv_shop_name.setText(getRefundListData.getStor_name());
        holder.tv_price.setText(getRefundListData.getRefund_price());
        holder.tv_reason.setText(getRefundListData.getReason());
        holder.tv_type.setText(getRefundListData.getSpec_value());
        Long time = Long.valueOf(getRefundListData.getCreate_time()) * 1000;
        holder.tv_time.setText(DateUtil.getDateToString(time));
        switch (getRefundListData.getStatus()) {
            case "1":
                holder.tv_status.setText("退货中");
                break;
            case "2":
                holder.tv_status.setText("卖家同意发送退货地址");
                break;
            case "3":
                holder.tv_status.setText("买家发货 卖家确认");
                break;
            case "4":
                holder.tv_status.setText("卖家拒绝退货");
                break;

            case "5":
                holder.tv_status.setText("退货成功");
                break;

            case "0" :
                holder.tv_status.setText("关闭");
                break;
        }

        return convertView;
    }


    class ViewHolder{
        private TextView tv_order_id;
        private TextView tv_refund_id;
        private TextView tv_shop_name;
        private TextView tv_product_name;
        private TextView tv_status;
        private NetworkImageView niv_product;
        private TextView tv_price;
        private TextView tv_time;
        TextView tv_reason;
        TextView tv_type;
    }
}
