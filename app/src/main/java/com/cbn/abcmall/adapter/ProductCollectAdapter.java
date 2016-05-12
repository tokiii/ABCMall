package com.cbn.abcmall.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.activites.ProductDetailActivity;
import com.cbn.abcmall.bean.GetCollectProductDetail;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 收藏界面适配器
 * Created by Administrator on 2015/10/14.
 */
public class ProductCollectAdapter extends RecyclerView.Adapter<ProductCollectAdapter.MyViewHolder> {
    private Context context;
    private List<GetCollectProductDetail> list;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;
    private Handler cancelHander;
    private int position;
    private String type;


    public ProductCollectAdapter(final Context context, List<GetCollectProductDetail> list, String type) {
        this.context = context;
        this.list = list;
        requestQueue = MyApplication.getInstance().getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new BitMapCache());
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        position = i;
        MyViewHolder holder = new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_collect, viewGroup, false));
        holder.tv_name.setText(list.get(i).getPname());
        holder.tv_color.setText(list.get(i).getPrice());
        holder.niv_item.setImageUrl(list.get(i).getImage(), imageLoader);
        cancelHander = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        ToastUtils.TextToast(context, "取消收藏成功", Toast.LENGTH_SHORT);
                        list.remove(list.get(position));
                        notifyDataSetChanged();
                        break;
                }
            }
        };
        holder.iv_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(context)
                        .setTitle("取消收藏")
                        .setMessage("是否取消收藏")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                       cancelCollect(type, list.get(position).getPid());
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();

            }
        });

        holder.ll_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent productIntent = new Intent(context, ProductDetailActivity.class);
                productIntent.putExtra("pid", list.get(position).getPid());
                context.startActivity(productIntent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        NetworkImageView niv_item;
        TextView tv_color;
        ImageView iv_more;
        LinearLayout ll_item;

        public MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            niv_item = (NetworkImageView) itemView.findViewById(R.id.niv_item);
            tv_color = (TextView) itemView.findViewById(R.id.tv_color);
            iv_more = (ImageView) itemView.findViewById(R.id.iv_more);
            ll_item = (LinearLayout) itemView.findViewById(R.id.ll_item);

        }
    }


    /**
     * 取消收藏
     *
     * @param type
     * @param cid
     */
    private void cancelCollect(String type, String cid) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(context));
            jsonObject.put("sign", Config.getSign(context));
            jsonObject.put("type", type);
            jsonObject.put("cid", cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.httpPostResult(context, jsonObject, cancelHander, Config.URL_COLLECT_DETELE);
    }
}
