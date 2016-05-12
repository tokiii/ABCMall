package com.cbn.abcmall.activites;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.Charge;
import com.cbn.abcmall.bean.ChargePost;
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

/**
 * 充值界面
 * Created by Administrator on 2015/9/28.
 */
public class ChargeActivity extends BaseActivity {

    private EditText et_price;
    private Button btn_confirm;
    private Handler payHandler;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_CHECK_FLAG = 2;
    private String token;
    private String sign;
    private JSONObject chargeJsonObject;
    private Handler chargeHandler;
    private ProgressDialog progressDialog;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_charge);
        et_price = (EditText) findViewById(R.id.et_price);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        initData();

        payHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case SDK_PAY_FLAG: {
                        PayResult payResult = new PayResult((String) msg.obj);

                        // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                        String resultInfo = payResult.getResult();
                        String resultStatus = payResult.getResultStatus();
                        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                        if (TextUtils.equals(resultStatus, "9000")) {
                            Toast.makeText(ChargeActivity.this, "支付成功",
                                    Toast.LENGTH_SHORT).show();

                            setResult(RESULT_OK);
                            finish();


                        } else {
                            // 判断resultStatus 为非“9000”则代表可能支付失败
                            // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                            if (TextUtils.equals(resultStatus, "8000")) {
                                Toast.makeText(ChargeActivity.this, "支付结果确认中",
                                        Toast.LENGTH_SHORT).show();

                            } else {
                                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                                Toast.makeText(ChargeActivity.this, "支付失败",
                                        Toast.LENGTH_SHORT).show();

                                setResult(RESULT_OK);
                                finish();

                            }
                        }
                        break;
                    }
                    case SDK_CHECK_FLAG: {
                        Toast.makeText(ChargeActivity.this, "检查结果为：" + msg.obj,
                                Toast.LENGTH_SHORT).show();
                        break;
                    }
                    default:
                        break;
                }

            }
        };

        chargeHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (msg.getData() != null) {
                    progressDialog.dismiss();
                    String json = msg.getData().getString("json");
                    payForCharge(json);
                }
            }
        };

    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.btn_confirm:

                if (TextUtils.isEmpty(et_price.getText())) {
                    ToastUtils.TextToast(this, "请输入要充值的金额", Toast.LENGTH_SHORT);
                    return;
                }
                String str = et_price.getText().toString();
                charge(str);
                break;
        }

    }

    /**
     * 填写token和sign
     */
    private void initData() {
        SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(this, Config.ACCOUNT);
        token = sharePreferenceUtil.getStr(Config.ACCOUNT_TOKEN);
        sign = EncryptUtils.getMD5(token + Config.APP_KEY);
    }

    /**
     * 充值
     */
    private void charge(String amount) {

        ChargePost chargePost = new ChargePost();
        chargePost.setToken(token);
        chargePost.setSign(sign);
        chargePost.setAmount(amount);
        String json = JsonUtils.objectToJson(chargePost);

        try {
            chargeJsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                String string = HttpUtils.JsonPost(ChargeActivity.this, Config.URL_CHARGE, chargeJsonObject);
                String json = JsonUtils.decodeUnicode(string);
                LogUtils.i("info", "充值返回的数据----->" + json);
                Message message = new Message();
                Bundle bundle = new Bundle();
                bundle.putString("json", json);
                message.setData(bundle);
                chargeHandler.sendMessage(message);

            }
        }).start();

    }




    private void payForCharge(String json) {
        Charge charge = (Charge) JsonUtils.jsonToBean(json, Charge.class);
        String TradeNo = String.valueOf(charge.getTradeNo());
        String subject = charge.getSubject();
        String body = "充值";
        double price = charge.getPrice();
        String notify_url = charge.getNotify_url();
        String return_url = charge.getReturn_url();
        String orderInfo = getOrderInfo(TradeNo,subject,body,price,notify_url,return_url);
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
                // 构造PayTask 对象
                PayTask alipay = new PayTask(ChargeActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo);

                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                payHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();


    }

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String TradeNo, String subject, String body, double price, String notify_url, String return_url) {

        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + Config.PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + Config.SELLER + "\"";

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
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, Config.RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }

}
