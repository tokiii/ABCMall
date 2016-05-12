package com.cbn.abcmall.activites;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.AllRefundAdapter;
import com.cbn.abcmall.adapter.RefundItemAdapter;
import com.cbn.abcmall.bean.GetRefundList;
import com.cbn.abcmall.bean.GetRefundListData;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 所有退单界面
 * Created by Administrator on 2015/10/8.
 */
public class AllRefundActivity extends BaseActivity {
    private ImageView right;
    private ImageView left;
    private TextView title;
    private ListView lv_all_refund; //所有退单list
    private Handler handler;
    private AllRefundAdapter adapter;
    private PopupWindow popupWindow;
    private ListView lv_group;
    private View view;
    private List<String> titles;
    private RefundItemAdapter refundItemAdapter;
    private RelativeLayout rl_title;
    private ImageView iv_title;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_all_refund);
        initView();
        getData(6);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String json = (String) msg.obj;
                        GetRefundList getRefundList = (GetRefundList) JsonUtils.jsonToBean(json, GetRefundList.class);
                        final List<GetRefundListData> lists = getRefundList.getRefund_list();
                        adapter = new AllRefundAdapter(AllRefundActivity.this, lists);
                        lv_all_refund.setAdapter(adapter);
                        lv_all_refund.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Intent refundDetailIntent = new Intent(AllRefundActivity.this, RefundDetailActivity.class);
                                refundDetailIntent.putExtra("orderId", lists.get(position).getOrder_id());
                                refundDetailIntent.putExtra("refundId", lists.get(position).getRefund_id());
                                refundDetailIntent.putExtra("status", lists.get(position).getStatus());
                                startActivity(refundDetailIntent);
                            }
                        });

                        break;
                }
            }
        };

    }


    private void initView() {
        right = (ImageView) findViewById(R.id.right);
        left = (ImageView) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        right.setVisibility(View.GONE);
        left.setOnClickListener(this);
        title.setText("所有退单");
        lv_all_refund = (ListView) findViewById(R.id.lv_all_refund);
        rl_title = (RelativeLayout) findViewById(R.id.rl_title);
        rl_title.setOnClickListener(this);
        iv_title = (ImageView) findViewById(R.id.iv_title);

    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.rl_title:
                showWindow(v);
                iv_title.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.up));
                break;
        }
    }

    /**
     * 获取退单列表数据
     */
    private void getData(int status) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("status", status);
            jsonObject.put("page", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = HttpUtils.JsonPost(AllRefundActivity.this, Config.URL_ALL_REFUND_LIST, jsonObject);
                String json = JsonUtils.decodeUnicode(str);
                Message message = new Message();
                message.what = 1;
                message.obj = json;
                handler.sendMessage(message);
                LogUtils.i("info", "获得的退单列表数据为======>" + json);
            }
        }).start();
    }


    /**
     * 显示
     *
     * @param parent
     */
    private void showWindow(View parent) {

        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = layoutInflater.inflate(R.layout.pop_refund_list, null);
            lv_group = (ListView) view.findViewById(R.id.lv_item);
            // 加载数据
            titles = new ArrayList<>();
            titles.add("退货关闭");
            titles.add("审核中");
            titles.add("审核成功");
            titles.add("买家发货");
            titles.add("拒绝退货");
            titles.add("退货成功");
            titles.add("所有退单");
            refundItemAdapter = new RefundItemAdapter(this, titles);
            lv_group.setAdapter(refundItemAdapter);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, 300, 580);
        }
        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv_title.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.down));
            }
        });
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;// 屏幕的宽dp
        int xPos = width / 2
                - popupWindow.getWidth() / 2;
        LogUtils.i("coder", "popupWindow.getWidth()/2:" + popupWindow.getWidth() / 2);
        LogUtils.i("coder", "xPos:" + xPos);
        popupWindow.showAsDropDown(parent, 0, 0);
        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                getData(position);
                title.setText(titles.get(position));
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    iv_title.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.mipmap.down));
                }
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            getData(6);
            adapter.notifyDataSetChanged();
        }

    }
}
