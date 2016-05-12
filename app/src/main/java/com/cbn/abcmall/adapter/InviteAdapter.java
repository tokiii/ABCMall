package com.cbn.abcmall.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.bean.InviteFriend;
import com.cbn.abcmall.utils.DateUtil;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.ToastUtils;
import com.zhy.autolayout.utils.AutoUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 我的邀请好友列表适配器
 * Created by lost on 2016/5/10.
 */
public class InviteAdapter extends BaseAdapter{


    private List<InviteFriend> list;
    private Context context;

    public InviteAdapter(Context context, List<InviteFriend> list) {
        this.list = list;
        this.context = context;
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
    public View getView(final int position, View convertView, ViewGroup parent) {


        final InviteFriend friend = list.get(position);

        ViewHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_friends, null);
            holder = new ViewHolder();
            holder.btn_delete = (Button) convertView.findViewById(R.id.btn_delete);
            holder.tv_id = (TextView) convertView.findViewById(R.id.tv_id);
            holder.tv_income = (TextView) convertView.findViewById(R.id.tv_income);
            holder.tv_status = (TextView) convertView.findViewById(R.id.tv_status);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
            AutoUtils.autoSize(convertView);
        }
        holder = (ViewHolder) convertView.getTag();

        holder.tv_id.setText((position + 1) + ".");
        holder.tv_name.setText(friend.getUser());
        holder.tv_time.setText(DateUtil.getInviteDate(Long.valueOf(friend.getCreate_time())));
        if (friend.getPay().equals("Y")) {
            holder.tv_status.setText("已到账");
        }else if (friend.getPay().equals("C")){
            holder.tv_status.setText("已取消");
        }else if (friend.getPay().equals("N")) {
            holder.tv_status.setText("未到账");
        }
        holder.tv_income.setText(friend.getCredit());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFriend(friend.getId());
                list.remove(position);
                ToastUtils.TextToast(context, "删除成功！",Toast.LENGTH_SHORT);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }


    class ViewHolder {
        private TextView tv_id;
        private TextView tv_name;
        private TextView tv_time;
        private TextView tv_income;
        private TextView tv_status;
        private Button btn_delete;
    }

    /**
     * 删除好友
     */
    private void deleteFriend(String id) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        LogUtils.i("获得的好友信息为------->" + msg.obj);
                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("token", Config.getAccountToken(context));
            jsonObject.put("sign", Config.getSign(context));
            jsonObject.put("deid", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils.httpPostResult(context, jsonObject, handler, Config.URL_INVITE);

            }
        }).start();


    }
}
