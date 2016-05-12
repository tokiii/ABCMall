package com.cbn.abcmall.activites;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cbn.abcmall.R;
import com.cbn.abcmall.broadcasts.MsgReceiver;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.AppManager;
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.StringUtil;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 免密登陆界面
 * Created by lost on 16-1-6.
 */
public class LoginNoPassActivity extends BaseActivity {

    private String mobile;
    private String code;
    private EditText et_mobile;
    private EditText et_code;
    private Button btn_login;
    private Button btn_send;
    private JsonObjectRequest jsonObjectRequest;
    private RequestQueue requestQueue;
    private String token;

    private TextView title;
    private ImageView left;

    private MyTimeCount timeCount;

    private MsgReceiver msgReceiver;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_login_nopass);
        et_mobile = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_login.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        requestQueue = MyApplication.getInstance().getRequestQueue();
        title = (TextView) findViewById(R.id.title);
        left = (ImageView) findViewById(R.id.left);
        left.setOnClickListener(this);
        title.setText("免密登录");
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter(msgReceiver.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        this.registerReceiver(msgReceiver, intentFilter);
        msgReceiver.setOnReceivedMessageListener(new MsgReceiver.MessageListener() {
            @Override
            public void onReceive(String message) {

                LogUtils.i("info", "得到的短信内容为---->" + message);
                // 转化成纯数字验证码
                message = StringUtil.getNumberFromString(message);
                et_code.setText(message);
            }
        });
    }

    @Override
    public void widgetClick(View v) {


        switch (v.getId()) {
            //点击登录
            case R.id.btn_login:
                if (TextUtils.isEmpty(et_code.getText()) || TextUtils.isEmpty(et_mobile.getText())) {
                    ToastUtils.TextToast(this, "请输入手机号和验证码！", Toast.LENGTH_SHORT);
                    return;
                }
                login();
                break;
            // 点击发送验证码
            case R.id.btn_send:
                if (TextUtils.isEmpty(et_mobile.getText())) {
                    ToastUtils.TextToast(this, "手机号不能为空！", Toast.LENGTH_SHORT);
                    return;
                }
                timeCount = new MyTimeCount(60000, 1000);
                timeCount.start();
                sendCode();
                break;

            case R.id.left:
                finish();
                break;
        }

    }


    /**
     * 发送验证码
     */
    private void sendCode() {
        mobile = et_mobile.getText().toString().trim();
        JSONObject codeJsonObject = new JSONObject();
        try {
            codeJsonObject.put("mobile", mobile);
            codeJsonObject.put("type", "mlogin");
            codeJsonObject.put("token", "");
            codeJsonObject.put("sign", EncryptUtils.getMD5(mobile + "mlogin" + Config.APP_KEY));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.i("info", "发送验证码的数据----->" + codeJsonObject.toString());

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_MOBILE_CODE, codeJsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                LogUtils.i("info", "发送验证码返回的数据----->" + jsonObject.toString());
                try {
                    if (jsonObject.get("statu").equals(1)) {

                        token = jsonObject.getString("token");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    /**
     * 登录
     *
     * 参数:
     mobile:手机号 ;
     sign:验证码 code +mobile+token +key MD5;
     token:必须带token;
     vode:动态验证码;
     */
    private void login() {

        mobile = et_mobile.getText().toString().trim();
        code = et_code.getText().toString().trim();

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mobile);
            jsonObject.put("sign", EncryptUtils.getMD5(code + mobile + token + Config.APP_KEY));
            jsonObject.put("token", token);
            jsonObject.put("code", code);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        LogUtils.i("info", "免密登录发送的数据------>" + jsonObject.toString());
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_MOBILE_LOGIN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                LogUtils.i("info", "免密登录返回的数据为---->" + jsonObject.toString());


                try {
                    if (jsonObject.getInt("statu") == 6) {
                        token = jsonObject.getString("token");
                        loginToHome();
                    }else {
                        // 打印出报错信息
                        ToastUtils.TextToast(getApplicationContext(), jsonObject.getString("msg"), Toast.LENGTH_SHORT);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });

        requestQueue.add(jsonObjectRequest);

    }


    /**
     * 定时器内部类
     */
    class MyTimeCount extends CountDownTimer {

        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyTimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            btn_send.setClickable(false);
            btn_send.setBackgroundColor(Color.GRAY);
            btn_send.setText(millisUntilFinished/1000 + "s后重新发送");
        }

        @Override
        public void onFinish() {
            btn_send.setText("重新发送");
            btn_send.setClickable(true);
            btn_send.setBackgroundColor(Color.parseColor("#FF4081"));
        }
    }

    /**
     * 跳转到主页
     */
    private void loginToHome() {
        ToastUtils.TextToast(LoginNoPassActivity.this, "登录成功！", Toast.LENGTH_SHORT);
        SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(LoginNoPassActivity.this, Config.ACCOUNT);
        sharePreferenceUtil.saveStr(Config.ACCOUNT_NAME, mobile);
        sharePreferenceUtil.saveStr(Config.ACCOUNT_TOKEN, token);
        sharePreferenceUtil.saveStr("isLogin", "1");
        Intent loginSuccessIntent = new Intent(LoginNoPassActivity.this, MainActivity.class);
        startActivity(loginSuccessIntent);
        AppManager.getAppManager().finishActivity(LoginActivity.class);
        finish();
    }

}
