package com.cbn.abcmall.activites;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.LogisticAdapter;
import com.cbn.abcmall.bean.GetExpress;
import com.cbn.abcmall.bean.GetLogistic;
import com.cbn.abcmall.bean.LogisticData;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 查看物流界面
 * Created by Administrator on 2015/10/10.
 */
public class LogisticActivity extends  BaseActivity{
    private ImageView left;
    private ImageView right;
    private TextView title;
    private String orderId;// 订单编号
    private Handler handler;// 接收物流信息handler
    private ListView lv_logistic;
    private TextView tv_name;
    private TextView tv_number;
    private LogisticAdapter adapter;
    @Override
    public void initWidget() {
        setContentView(R.layout.activity_logistic);
        initView();
        orderId = getIntent().getStringExtra("orderId");
        getLogisticData(orderId);
    }

    private void initView() {
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        title = (TextView) findViewById(R.id.title);
        left.setOnClickListener(this);
        right.setVisibility(View.GONE);
        title.setText("查看物流");
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_number = (TextView) findViewById(R.id.tv_number);
        lv_logistic = (ListView) findViewById(R.id.lv_logistic);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
        }
    }

    /**
     * 获取物流信息
     * @param orderId
     */
    private void getLogisticData(String orderId) {

        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String json = (String) msg.obj;
                       if (!json.contains("ftime")) {
                           return;
                       }
                        GetLogistic getLogistic = (GetLogistic) JsonUtils.jsonToBean(json, GetLogistic.class);
                        tv_name.setText(getLogistic.getLogistics_name());
                        tv_number.setText(getLogistic.getInvoice_no());
                        GetExpress express = getLogistic.getExpress();
                        if (express.equals("[]")){
                            return;
                        }
                        List<LogisticData> datas = express.getData();
                        adapter = new LogisticAdapter(LogisticActivity.this, datas);
                        lv_logistic.setAdapter(adapter);
                        break;
                }
            }
        };

        String token = Config.getAccountToken(this);
        String sign = Config.getSign(this);
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("sign", sign);
            jsonObject.put("orderId", orderId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = HttpUtils.JsonPost(LogisticActivity.this, Config.URL_LOGISTIC, jsonObject);
                String json = JsonUtils.decodeUnicode(str);
                LogUtils.i("info", "得到的物流信息为----->" + json);
                Message message = new Message();
                message.obj = json;
                message.what = 1;
                handler.sendMessage(message);

            }
        }).start();

    }
}
