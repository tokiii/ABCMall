package com.cbn.abcmall.activites;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.android.volley.RequestQueue;
import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.PayInfoGet;
import com.cbn.abcmall.bean.PayInfoPost;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.AppManager;
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.PayResult;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.SignUtils;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 * 支付Activity
 * Created by Administrator on 2015/9/24.
 */
public class PayActivity extends BaseActivity {


    // 商户PID
    public static final String PARTNER = "2088511124117236";
    // 商户收款账号
    public static final String SELLER = "984607708@qq.com";
    private String tradeNo = "";
    private String result;
    private TextView mResult;
    private String cookies;
    private String url;
    private ProgressDialog progressDialog;// 加载支付信息的progressDialog
    // 商户私钥，pkcs8格式

    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAME8wXaK2ynS6KgE" +
            "JwtHdj+nyXr5FPBp01RHelCTXNWuzXJnUqz2q/OhoL3xAdKatxu0e+zKPxVMlqt/" +
            "FqeIAjkcbMOZIw+G2aO1hmOjkxZHePWZSoZ1CrBO6jtlyz3u8Rkmcugwol0XxHXU" +
            "8vHm3o+g1CtFUj7ep9jxXoOouroVAgMBAAECgYAauDHwGEcxw04UXW6gqqL5LlQ3" +
            "wtVYYOmtz6xkE9xUV7VkylCuRfWE3KaDXR3pZydGl3BDT7vlUA/NaudRBwd3bI0D" +
            "j3A5JS/er/H5nWGPAaJPGUKQaLfbp+jpTnSBwUTnWdEh+bsnJFPe+sdpVHVFL6e+" +
            "SPykXEO6AB9jiy49yQJBAOxu+BkjLXZU7kbbafNtLt7NN3sYsI7YtKlf2M/X3I5G" +
            "jZB8uH+fu7ZRwRkpcl52BuFWXMONqoqCMSyl7GBbnecCQQDROqRVPEPE5KYR0fj4" +
            "xgNXrw4wrfmDpvu1rvZoIn7DhaIqUO49jLDgUVWR2nHrjF1xcGr9iFANLllLbvs2" +
            "61CjAkBxAHytrwV8iA02aooSIX4EWPsKZ3uDWonGZhv/AsHUcl4Yz7NytAqIkjyD" +
            "ZS7XfPu+2YMOp/f5qU4Nc+QtDbzDAkAmKQzp4tj1Y0KKw4ImVW8soefzpvd4NI96" +
            "HpQggBIgtRHTp/kbzro/33W86VuXu30bgIZzAVagYNC2emJlLNwDAkEAno3Qsztz" +
            "6P/JF4d3FG38ZW1ncDJLoi7FdTz6a1HeIjMQHzP/t6JNmR3BKfDDYzbu8MnxJPiC" +
            "ontmD2KyNTrKhw==";
    // 支付宝公钥
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQ" +
            "KBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKB" +
            "oLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZw" +
            "BC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
    private static final int SDK_PAY_FLAG = 1;

    private static final int SDK_CHECK_FLAG = 2;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        Toast.makeText(PayActivity.this, "支付成功",
                                Toast.LENGTH_SHORT).show();

                        // 跳转到代付款界面
                        Intent cartIntent = new Intent(PayActivity.this, AllOrdersActivity.class);
                        cartIntent.putExtra("status", 2);
                        startActivity(cartIntent);
                        AppManager.getAppManager().finishActivity(CartConfirmActivity.class);
                        finish();



                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayActivity.this, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayActivity.this, "支付失败",
                                    Toast.LENGTH_SHORT).show();

                            if (getIntent().hasExtra("fromWait")) {
                                finish();
                                return;
                            }

                            Intent cartIntent = new Intent(PayActivity.this, AllOrdersActivity.class);
                            cartIntent.putExtra("status", 1);
                            startActivity(cartIntent);
                            AppManager.getAppManager().finishActivity(CartConfirmActivity.class);
                            finish();

                        }
                    }
                    break;
                }
                case SDK_CHECK_FLAG: {
                    Toast.makeText(PayActivity.this, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };


    private Button btn_zhifubao;
    private Button btn_rest;
    private Button btn_cancel;
    private String token;
    private String orderId;
    private JSONObject payInfoObject;
    private Handler getinfoHandler; //支付宝支付的handler
    private Handler getPayInfoHandler; //余额支付得到的handler
    private RequestQueue requestQueue;


    @Override
    public void initWidget() {
        setContentView(R.layout.activity_pay);
        initView();

        getinfoHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                            String json = (String) msg.obj;
                            PayInfoGet payInfoGet = (PayInfoGet) JsonUtils.jsonToBean(json, PayInfoGet.class);

                            String TradeNo = String.valueOf(payInfoGet.getTradeNo());
                            String subject = String.valueOf(payInfoGet.getSubject());
                            String body = String.valueOf(payInfoGet.getOut_trade_no());
                            double price = payInfoGet.getPrice();
                            String notify_url = payInfoGet.getNotify_url();
                            String return_url = payInfoGet.getReturn_url();

                            LogUtils.i("info", "得到的支付信息------>" + json + payInfoGet.getStatu());
                            if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                                    || TextUtils.isEmpty(SELLER)) {
                                new AlertDialog.Builder(PayActivity.this)
                                        .setTitle("警告")
                                        .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                                        .setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialoginterface, int i) {
                                                        //
                                                        finish();
                                                    }
                                                }).show();
                                return;
                            }

                            String orderInfo = getOrderInfo(TradeNo, subject, body, price, notify_url, return_url);

                            // 对订单做RSA 签名
                            String sign = sign(orderInfo);
                            try {
                                // 仅需对sign 做URL编码
                                sign = URLEncoder.encode(sign, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            // 完整的符合支付宝参数规范的订单信息
                            final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                                    + getSignType();


                            Runnable payRunnable = new Runnable() {

                                @Override
                                public void run() {
                                    progressDialog.dismiss();
                                    // 构造PayTask 对象
                                    PayTask alipay = new PayTask(PayActivity.this);
                                    // 调用支付接口，获取支付结果
                                    String result = alipay.pay(payInfo);
                                    Message msg = new Message();
                                    msg.what = SDK_PAY_FLAG;
                                    msg.obj = result;
                                    mHandler.sendMessage(msg);
                                }
                            };
                            // 必须异步调用
                            Thread payThread = new Thread(payRunnable);
                            payThread.start();
                        break;
                }
            }
        };


        // 获得支付信息Handler
        getPayInfoHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {

                    case 1:
                        progressDialog.dismiss();
                        String json = (String) msg.obj;
                        if (json.contains("余额不足")) {
                            ToastUtils.TextToast(PayActivity.this, "账户余额不足", Toast.LENGTH_SHORT);
                        } else {
                            ToastUtils.TextToast(PayActivity.this, "支付成功", Toast.LENGTH_SHORT);
                            Intent intent = new Intent(PayActivity.this, AllOrdersActivity.class);
                            intent.putExtra("status", 2);
                            startActivity(intent);
                            finish();
                        }
                        break;

                }
            }
        };


    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.btn__zhifubao:
                getPayInfo();
                break;

            // 余额支付
            case R.id.btn_rest:
                progressDialog.show();
                payWithRest();
                break;

            case R.id.btn_cancel:

                if (getIntent().hasExtra("fromWait")) {
                    finish();
                    return;
                }
                // 取消支付
                Intent payCancelIntent = new Intent(PayActivity.this, OrderFragmentActivity.class);
                payCancelIntent.putExtra("status", 1);
                payCancelIntent.putExtra("payCancel", true);
                startActivity(payCancelIntent);
                AppManager.getAppManager().finishActivity(CartConfirmActivity.class);
                finish();
        }

    }

    /**
     * 初始化组件
     */
    private void initView() {
        token = new SharePreferenceUtil(this, Config.ACCOUNT).getStr(Config.ACCOUNT_TOKEN);
        btn_zhifubao = (Button) findViewById(R.id.btn__zhifubao);
        btn_rest = (Button) findViewById(R.id.btn_rest);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_rest.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_zhifubao.setOnClickListener(this);
        orderId = getIntent().getStringExtra("orderId");
        requestQueue = MyApplication.getInstance().getRequestQueue();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载....");
    }


    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String TradeNo, String subject, String body, double price, String notify_url, String return_url) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + TradeNo + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notify_url
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }


    /**
     * 获取支付信息
     */
    private void getPayInfo() {

        PayInfoPost payInfoPost = new PayInfoPost();
        payInfoPost.setSign(EncryptUtils.getMD5(token + Config.APP_KEY));
        payInfoPost.setToken(token);
        payInfoPost.setPayment_type("alipay");
        payInfoPost.setTradeNo(orderId);

        final String json = JsonUtils.objectToJson(payInfoPost);
        try {
            payInfoObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.i("info", "发送的PayInfo----------》" + payInfoObject.toString());
        HttpUtils.httpPostResult(PayActivity.this, payInfoObject, getinfoHandler, Config.URL_GET_ORDER_INFO);

    }

    /**
     * 余额支付
     *
     * @return 余额支付的结果
     */
    private boolean payWithRest() {
        PayInfoPost payInfoPost = new PayInfoPost();
        payInfoPost.setSign(EncryptUtils.getMD5(token + Config.APP_KEY));
        payInfoPost.setToken(token);
        payInfoPost.setPayment_type("account");
        payInfoPost.setTradeNo(orderId);
        String json = JsonUtils.objectToJson(payInfoPost);
        LogUtils.i("info", "发送的PayInfo----------》" + json);
        try {
            payInfoObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.httpPostResult(PayActivity.this, payInfoObject, getPayInfoHandler, Config.URL_GET_ORDER_INFO);

        return false;

    }


}
