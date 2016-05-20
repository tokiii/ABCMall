package com.cbn.abcmall.activites;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cbn.abcmall.R;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 显示我的邀请二维码界面
 * Created by lost on 2016/5/9.
 */
public class InviteQrcodeActivity extends BaseActivity {

    private NetworkImageView niv_qrcode;
    private ImageView left;
    private TextView title;



    @Override
    public void initWidget() {
        setContentView(R.layout.activity_inviteqrcode);
        niv_qrcode = (NetworkImageView) findViewById(R.id.niv_qrcode);
        left = (ImageView) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        left.setOnClickListener(this);
        title.setText("邀请二维码");
        getQrcode();

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
     * 获取邀请图片
     */
    private void getQrcode() {


        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        LogUtils.i("二维码地址------->" + msg.obj);

                        String json = (String) msg.obj;

                        try {
                            String inviterurl = (String) JsonUtils.getValueFromJson(json, "inviterurl");
                            String qrcode = (String) JsonUtils.getValueFromJson(json, "qrcode");
                            ImageLoader imageLoader = new ImageLoader(MyApplication.getInstance().getRequestQueue(), new BitMapCache());
                            niv_qrcode.setImageUrl(qrcode,imageLoader );

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("token", Config.getAccountToken(InviteQrcodeActivity.this));
            jsonObject.put("sign", Config.getSign(InviteQrcodeActivity.this));
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils.httpPostResult(InviteQrcodeActivity.this, jsonObject, handler, Config.URL_GET_QRCODE);

            }
        }).start();


    }
}
