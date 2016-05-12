package com.cbn.abcmall.activites;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.Login;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.AppManager;
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.ToastUtils;
import com.gc.materialdesign.views.ButtonRectangle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * 登录界面
 * Created by WuChangHe on 2015/9/8.
 */
public class LoginActivity extends BaseActivity {

    private EditText et_account; //登录账号
    private EditText et_password; //登录密码
    private ButtonRectangle btn_login; //登录
    private Button btn_register; //注册
    private Button btn_login_nopass;// 免密登陆
    private RequestQueue requestQueue = MyApplication.getInstance().getRequestQueue();
    private JsonObjectRequest jsonObjectRequest;
    private JSONObject jsonObject;
    private ImageView right;
    private ImageView left;
    private TextView title;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_login);
        et_account = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (ButtonRectangle) findViewById(R.id.btn_login);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_login.setOnClickListener(this);
        btn_login_nopass = (Button) findViewById(R.id.btn_login_nopass);
        btn_login_nopass.setOnClickListener(this);
        btn_register.setOnClickListener(this);
        requestQueue = MyApplication.getInstance().getRequestQueue();
        right = (ImageView) findViewById(R.id.right);
        left = (ImageView) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        right.setVisibility(View.GONE);
        left.setVisibility(View.GONE);
        title.setText("登录");
        if (getIntent().hasExtra("exit")) {
            et_password.setText("");
            et_account.setText("");
        }

        if (new SharePreferenceUtil(LoginActivity.this, Config.ACCOUNT).hasStr(Config.ACCOUNT_NAME)) {
            et_account.setText(new SharePreferenceUtil(LoginActivity.this, Config.ACCOUNT).getStr(Config.ACCOUNT_NAME));
            et_password.setText(new SharePreferenceUtil(LoginActivity.this, Config.ACCOUNT).getStr(Config.ACCOUNT_PASSWORD));
        }
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                final String account = et_account.getText().toString();
                final String password = et_password.getText().toString();
                Login login = new Login();
                login.setUser(account);
                login.setPassword(password);
                login.setSign(EncryptUtils.getMD5(password + account + Config.APP_KEY));
                String json = JsonUtils.objectToJson(login);
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_LOGIN, jsonObject, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {

                        LogUtils.i("info", "登录成功之后返回的数据-------->" + jsonObject.toString());

                        try {
                            int statu = jsonObject.getInt("statu");
                            LogUtils.i("info", "登录获得的status---->" + statu);
                            switch (statu) {
                                case 1:
                                    ToastUtils.TextToast(LoginActivity.this, "用户已被锁定", Toast.LENGTH_SHORT);
                                    break;
                                case 2:
                                    ToastUtils.TextToast(LoginActivity.this, "账号或者密码不正确", Toast.LENGTH_SHORT);
                                    break;
                                case 3:
                                    ToastUtils.TextToast(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT);
                                    break;
                                case 4:
                                    ToastUtils.TextToast(LoginActivity.this, "用户或密码不能为空", Toast.LENGTH_SHORT);
                                    break;
                                case 6:
                                    String token = jsonObject.getString("token");
                                    ToastUtils.TextToast(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT);
                                    SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(LoginActivity.this, Config.ACCOUNT);
                                    sharePreferenceUtil.saveStr(Config.ACCOUNT_NAME, account);
                                    sharePreferenceUtil.saveStr(Config.ACCOUNT_PASSWORD, password);
                                    sharePreferenceUtil.saveStr(Config.ACCOUNT_TOKEN, token);
                                    sharePreferenceUtil.saveStr("isLogin", "1");
                                    Intent loginSuccessIntent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(loginSuccessIntent);
                                    finish();
                                    break;

                                default:
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
                })
                {

                    @Override
                    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                        try {
                            Map<String, String> responseHeaders = response.headers;
                            String rawCookies = responseHeaders.get("Set-Cookie");
                            String dataString = new String(response.data, "UTF-8");
                            SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(getApplicationContext(), "cookie");
                            sharePreferenceUtil.saveStr("cookie", rawCookies);
                            LogUtils.i("得到的cookie值----->" + rawCookies + "   dataString----->" + dataString);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return super.parseNetworkResponse(response);

                    }
                }
                ;
                requestQueue.add(jsonObjectRequest);
                break;
            case R.id.btn_register:
                //跳转到注册界面
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(registerIntent);
                break;

            // 启动免密登录界面
            case R.id.btn_login_nopass:
                Intent loginNoPassIntent = new Intent(LoginActivity.this, LoginNoPassActivity.class);
                startActivity(loginNoPassIntent);
                break;
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AppManager.getAppManager().AppExit(this);
    }




    private void sendCode(String mobile) {
        final Handler msgHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        LogUtils.i("info", "验证码发送之后得到的信息---->" + msg.obj);
                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("mobile", mobile);
            jsonObject.put("sign", EncryptUtils.getMD5(mobile + Config.APP_KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.httpPostResult(this, jsonObject,msgHandler, "http://www.cbnmall.com/appapi/msg.php");

    }
}
