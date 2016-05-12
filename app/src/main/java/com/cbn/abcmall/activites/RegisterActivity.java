package com.cbn.abcmall.activites;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.CountDownTimer;
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
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 用户注册界面
 * Created by Administrator on 2015/9/8.
 */
public class RegisterActivity extends BaseActivity {

    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private ImageView left;
    private ImageView right;
    private TextView title;
    private EditText et_phone;
    private EditText et_password;
    private EditText et_code;
    private Button btn_send;
    private Button btn_register;
    private String mobile;
    private String code;
    private String password;
    private String token;
    private String type;
    private MyTimeCount timeCount;// 定时器
    private MsgReceiver msgReceiver;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_register);
        et_phone = (EditText) findViewById(R.id.et_phone);
        et_code = (EditText) findViewById(R.id.et_code);
        et_password = (EditText) findViewById(R.id.et_password);
        requestQueue = MyApplication.getInstance().getRequestQueue();
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        title = (TextView) findViewById(R.id.title);
        right.setVisibility(View.GONE);
        left.setOnClickListener(this);
        title.setText("注册");
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(this);
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter(msgReceiver.SMS_RECEIVED_ACTION);
        intentFilter.setPriority(Integer.MAX_VALUE);
        this.registerReceiver(msgReceiver, intentFilter);

        msgReceiver.setOnReceivedMessageListener(new MsgReceiver.MessageListener() {
            @Override
            public void onReceive(String message) {
                et_code.setText(message);
            }
        });
    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {

            // 注册并登陆按钮
            case R.id.btn_register:
                code = et_code.getText().toString().trim();
                password = et_password.getText().toString().trim();
                JSONObject registJsonObject = new JSONObject();
                try {
                    registJsonObject.put("mobile", mobile);
                    registJsonObject.put("password", password);
                    registJsonObject.put("code", code);
                    registJsonObject.put("token", token);
                    registJsonObject.put("sign", EncryptUtils.getMD5(code + mobile + password + token + Config.APP_KEY));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LogUtils.i("info", "发送的注册数据为----->" + registJsonObject.toString());

                jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_MOBILE_REGIST, registJsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtils.i("info", "注册返回的json----->" + jsonObject.toString());
                        try {
                            if (jsonObject.getInt("statu") == 6) {
                                loginToHome();
                            }else {
                                ToastUtils.TextToast(RegisterActivity.this, "注册失败,请检查是否该手机已经注册或者是验证码错误！", Toast.LENGTH_SHORT);
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

                break;

            case R.id.left:
                finish();
                break;

            // 发送验证码按钮
            case R.id.btn_send:
                mobile = et_phone.getText().toString().trim();
                password = et_password.getText().toString().trim();
                code = et_code.getText().toString().trim();
                timeCount = new MyTimeCount(60000, 1000);
                timeCount.start();
                JSONObject codeJsonObject = new JSONObject();
                try {

                    codeJsonObject.put("mobile", mobile);
                    codeJsonObject.put("type", "mregister");
                    codeJsonObject.put("token", "");
                    codeJsonObject.put("sign", EncryptUtils.getMD5(mobile + "mregister" + Config.APP_KEY));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                LogUtils.i("info", "发送验证码的数据----->" + codeJsonObject.toString());

                jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_MOBILE_CODE, codeJsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        LogUtils.i("info", "发送验证码返回的数据----->" + jsonObject.toString());
                        try {

                            int status = 0;
                            status = jsonObject.getInt("statu");
                            switch (status) {
                                // 未注册保存token
                                case 1:
                                    token = jsonObject.getString("token");
                                    break;
                                // 已经注册
                                case -5:
                                    ToastUtils.TextToast(RegisterActivity.this, "该手机号已经注册！", Toast.LENGTH_SHORT);
                                    break;
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
                break;
        }

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
            btn_send.setBackgroundColor(Color.parseColor("#FF4081"));
            btn_send.setClickable(true);
        }
    }

    /**
     * 跳转到主页
     */
    private void loginToHome() {
        ToastUtils.TextToast(this, "登录成功！", Toast.LENGTH_SHORT);
        SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(RegisterActivity.this, Config.ACCOUNT);
        sharePreferenceUtil.saveStr(Config.ACCOUNT_NAME, mobile);
        sharePreferenceUtil.saveStr(Config.ACCOUNT_PASSWORD, password);
        sharePreferenceUtil.saveStr(Config.ACCOUNT_TOKEN, token);
        sharePreferenceUtil.saveStr("isLogin", "1");
        Intent loginSuccessIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(loginSuccessIntent);
        finish();
    }

}
