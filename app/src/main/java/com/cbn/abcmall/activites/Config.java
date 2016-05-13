package com.cbn.abcmall.activites;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 存入一些常量、参数等。
 * Created by Administrator on 2015/9/8.
 */
public class Config {

    public static final String ACCOUNT = "account";// 保存到SharePreference账户信息的名称
    public static final String ACCOUNT_NAME = "account_name"; //账户名称
    public static final String ACCOUNT_PASSWORD = "account_password";//账户密码
    public static final String ACCOUNT_TOKEN = "account_token"; //账户的token
    public static final String APP_KEY = "ba0fb457e89f60b29e2091af899b1ae3";

    public static final String MESSAGE = "message";// 保存的留言数据名称

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

    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQ" +
            "KBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKB" +
            "oLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZw" +
            "BC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

    /**
     * 支付宝商户id
     */
    public static final String PARTNER = "2088511124117236";
    /**
     * 支付宝账号
     */
    public static final String SELLER = "984607708@qq.com";

    public static String URL_HOST = "http://www.cbnmall.com/appapi/";

    /**
     * 登录接口
     */
    public static final String URL_LOGIN = URL_HOST + "login.php";
    /**
     * 注册接口
     */
    public static final String URL_REGISTER = URL_HOST + "register.php";

    /**
     * 主页接口
     */
    public static final String URL_HOME = "http://www.cbnmall.com/?temp=wap&contact=hidden";
    /**
     * 加入购物车
     */
    public static final String URL_ADD_TO_CART = URL_HOST + "get_cart.php";

    /**
     * 获取购物车数据接口
     */
    public static final String URL_GET_CART = URL_HOST + "get_cart.php";

    /**
     * 删除购物车接口
     */
    public static final String URL_DELETE_CART = URL_HOST + "get_cart.php";

    /**
     * 获取和操作地址接口
     */
    public static final String URL_ADDRESS = URL_HOST + "get_address.php";

    /**
     * 提交订单接口
     */
    public static final String URL_CONFIRM_ORDER = URL_HOST + "order_add.php";

    /**
     * 获取订单信息的地址
     */
    public static final String URL_GET_ORDER_INFO = URL_HOST + "get_apppayinfo.php";

    /**
     * 个人信息的接口
     */
    public static final String URL_GET_MEMBER_INFO = URL_HOST + "member_all.php";

    /**
     * 余额充值
     */
    public static final String URL_CHARGE = URL_HOST + "recharge.php";

    /**
     * 订单状态
     */
    public static final String URL_GET_ORDER_STATUS = URL_HOST + "order_all.php";

    /**
     * 取消订单接口
     */
    public static final String URL_DELETE_ORDER = URL_HOST + "order_delete.php";

    /**
     * 退货订单列表
     */
    public static final String URL_REFUND_ORDER = URL_HOST + "refund.php";

    /**
     * 获取所有地址
     */
    public static final String URL_GET_ADDRESS = "http://www.cbnmall.com/appapi/get_address.php";

    /**
     * 确认收货地址
     */
    public static final String URL_CONFIRM_RECEIPT = URL_HOST + "order_confirm.php";

    /**
     * 所有退单列表
     */
    public static final String URL_ALL_REFUND_LIST = URL_HOST + "refund_list.php";

    /**
     * 查看物流信息
     */
    public static final String URL_LOGISTIC = URL_HOST + "logistic.php";

    /**
     * 获取和提交评论列表
     */
    public static final String URL_COMMENT = URL_HOST + "comment.php";

    /**
     * 收藏列表
     */
    public static final String URL_COLLECT_LIST = URL_HOST + "collection_list.php";

    /**
     * 删除收藏
     */
    public static final String URL_COLLECT_DETELE = URL_HOST + "collection_delete.php";


    /**
     * 订单详情接口
     */
    public static final String URL_ORDER_DETAIL = URL_HOST + "order_detail.php";

    /**
     * 收藏接口
     */
    public static final String URL_COLLECT = URL_HOST + "collection.php";

    /**
     * 获取店铺id接口
     */
    public static final String URL_GET_SHOP = URL_HOST + "get_shop.php";

    /**
     * 手机号注册
     */
    public static final String URL_MOBILE_REGIST = URL_HOST + "mregister.php";

    /**
     * 手机号登陆
     */
    public static final String URL_MOBILE_LOGIN = URL_HOST + "mlogin.php";

    /**
     * 手机发送验证码
     */
    public static final String URL_MOBILE_CODE = URL_HOST + "msg.php";


    /**
     * 优惠券
     */
    public static final String URL_COUPON = URL_HOST + "get_coupon.php";

    /**
     * 获取二维码图片
     */
    public static final String URL_GET_QRCODE = URL_HOST + "get_inshow.php";

    /**
     * 我的邀请接口
     */
    public static final String URL_INVITE = URL_HOST + "get_myinvitation.php";

    /**
     * 我的礼券
     */
    public static final String URL_GET_MYCOUPON = URL_HOST + "get_mycoupons.php";

    /**
     * 我的返利
     */
    public static final String URL_REBATE = URL_HOST + "get_rebateinfo.php";



    /**
     * 返回token
     *
     * @param context
     * @return
     */
    public static String getAccountToken(Context context) {

        SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(context, Config.ACCOUNT);
        String token = sharePreferenceUtil.getStr(Config.ACCOUNT_TOKEN);
        if (token != null) {
            return token;
        } else {
            return null;
        }
    }

    /**
     * 返回sign
     *
     * @param context
     * @return
     */
    public static String getSign(Context context) {
        String sign = EncryptUtils.getMD5(Config.getAccountToken(context) + Config.APP_KEY);
        return sign;
    }

    public static RequestQueue requestQueue;
    public static JSONObject jsonObject;
    public static JsonObjectRequest jsonObjectRequest;

    /**
     * 登录方法
     *
     * @param context
     */
    public static void Login(final Context context) {
        final SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(context, Config.ACCOUNT);
        String user = sharePreferenceUtil.getStr(Config.ACCOUNT_NAME);
        String password = sharePreferenceUtil.getStr(Config.ACCOUNT_PASSWORD);
        requestQueue = MyApplication.getInstance().getRequestQueue();
        jsonObject = new JSONObject();
        try {
            jsonObject.put("user", user);
            jsonObject.put("password", password);
            jsonObject.put("sign", EncryptUtils.getMD5(password + user + Config.APP_KEY));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Config.URL_LOGIN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                LogUtils.i("info", "重新登录获取的信息------>" + jsonObject.toString());
                try {
                    sharePreferenceUtil.saveStr(Config.ACCOUNT_TOKEN, jsonObject.getString("token"));
                    ToastUtils.TextToast(context, "登录失效，自动登录成功！", Toast.LENGTH_SHORT);
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
     * 获取当前应用版本号
     *
     * @return
     */
    public static String getVersion(Context context) {

        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);

            String version = info.versionName;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }

    }


}
