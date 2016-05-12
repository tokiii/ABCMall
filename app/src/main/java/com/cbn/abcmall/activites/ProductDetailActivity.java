package com.cbn.abcmall.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.GetProduct;
import com.cbn.abcmall.bean.ProductDetailPost;
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 商品详情界面
 * Created by WuChangHe on 2015/9/10.
 */
public class ProductDetailActivity extends BaseActivity {

    private Intent getIntent;
    private String productUrl = "";
    private WebView webView;
    private TextView title;
    private ImageView right;
    private ImageView left;
    private Button btn_add;
    private Handler handler;
    private String sendJson;
    private JSONObject jsonObject;
    private ProgressDialog progressDialog;
    private RelativeLayout rl_cart;
    private RelativeLayout rl_collect;// 收藏按钮
    private RelativeLayout rl_shop;// 进入店铺
    private CheckBox cb_collect;// 收藏按钮
    private String id;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_productdetail);
        title = (TextView) findViewById(R.id.title);
        right = (ImageView) findViewById(R.id.right);
        left = (ImageView) findViewById(R.id.left);
        rl_cart = (RelativeLayout) findViewById(R.id.rl_cart);
        rl_cart.setOnClickListener(this);
        rl_collect = (RelativeLayout) findViewById(R.id.rl_collect);
        rl_shop = (RelativeLayout) findViewById(R.id.rl_shop);
        rl_shop.setOnClickListener(this);
        rl_collect.setOnClickListener(this);
        cb_collect = (CheckBox) findViewById(R.id.cb_collect);
        title.setText("商品详情");
        left.setOnClickListener(this);
        right.setVisibility(View.GONE);
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        webView = (WebView) findViewById(R.id.wv_product);
        getIntent = getIntent();

        // 如果是从收藏界面传过来的，则拼接收藏商品网址
        if (getIntent.hasExtra("pid")){
            productUrl = "http://www.cbnmall.com/?m=product&s=detail&id=" + getIntent.getStringExtra("pid");
        }else {// 否则就是主页传送过来的网址
            productUrl = getIntent.getStringExtra("productUrl");
        }
        LogUtils.i("info", "得到的产品URL地址为-------》" + productUrl);
        id = getNumberFromString(productUrl);// 获取网址当中的id
        progressDialog.setMessage("正在加载....");
        progressDialog.show();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.loadUrl(productUrl);
        webView.addJavascriptInterface(new JsObject(), "contact");
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                    super.onPageFinished(view, url);
                }
                progressDialog.dismiss();
            }
        });

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                     sendJson = (String) msg.obj;
                        LogUtils.i("info", "获得的商品详情的Json数据为---->" + sendJson);
                            jsonToBean(sendJson);
                        break;
                }
            }
        };
        getProdutDetail();
        checkProductIsCollected(id);
       cb_collect.setClickable(false);
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                Intent propertyIntent = new Intent(ProductDetailActivity.this, PropertyActivity.class);
                propertyIntent.putExtra("sendJson", sendJson);
                startActivity(propertyIntent);
                break;
            case R.id.left:
                finish();
                break;
            case R.id.rl_cart:
                Intent cartIntent = new Intent(ProductDetailActivity.this, MainActivity.class);
                cartIntent.putExtra("fromProduct", "fromProduct");
                cartIntent.putExtra("pid", id);
                startActivity(cartIntent);
                break;
            case R.id.rl_collect:

                if (cb_collect.isChecked()){
                    cancelCollect(id);
                }else {
                    collectProduct(id);
                }

                break;

            case R.id.rl_shop:
                getShopId(id);
                break;

        }
    }

    private void jsonToBean(String sendJson) {
        GetProduct getProduct = (GetProduct) JsonUtils.jsonToBean(sendJson, GetProduct.class);
        String name = getProduct.getProduct().getName();
        LogUtils.i("info", "得到的商品的名字为------>" + name);
    }

    class JsObject {
        @JavascriptInterface
        public String getcontact() {
            return "headerandfooterhiden";
        }
    }

    /**
     *获取订单详情
     */
    private void getProdutDetail() {
        SharePreferenceUtil sharePreferenceUtil = new SharePreferenceUtil(ProductDetailActivity.this, Config.ACCOUNT);
        String token = sharePreferenceUtil.getStr(Config.ACCOUNT_TOKEN);
        ProductDetailPost productDetailPost = new ProductDetailPost();
        productDetailPost.setProduct_id(id);
        productDetailPost.setToken(token);
        productDetailPost.setSign(EncryptUtils.getMD5(token + Config.APP_KEY));
        final String json = JsonUtils.objectToJson(productDetailPost);

        progressDialog.show();

        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.i("info", "发送的JSON数据" + jsonObject.toString());
        HttpUtils.httpPostResult(webView, ProductDetailActivity.this, jsonObject, handler, "http://www.cbnmall.com/appapi/product_detail.php");
        progressDialog.dismiss();
    }

    @Override
    public void overridePendingTransition(int enterAnim, int exitAnim) {
        super.overridePendingTransition(enterAnim, exitAnim);
    }


    /**
     *字符串提取数字
     * @param url
     * @return
     */
    private String getNumberFromString(String url) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(url);
        return String.valueOf(m.replaceAll(""));
    }


    /**
     * 收藏商品
     * @param id
     */
    private void collectProduct(String id) {
        final Handler collectHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        LogUtils.i((String)msg.obj);
                        if (((String) msg.obj).contains("收藏产品成功")){
                            cb_collect.setChecked(true);
                            ToastUtils.TextToast(ProductDetailActivity.this, "收藏产品成功！", Toast.LENGTH_SHORT);
                        }else {
                            ToastUtils.TextToast(ProductDetailActivity.this, "产品已收藏！", Toast.LENGTH_SHORT);
                        }
                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("cid", id);
            jsonObject.put("type", "product");
            jsonObject.put("act", "add");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.httpPostResult(ProductDetailActivity.this, jsonObject, collectHandler, Config.URL_COLLECT);
            }
        }).start();
    }


    /**
     * 查询是否收藏了该商品
     * @param id
     */
    private void checkProductIsCollected(String id) {

        final Handler checkHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:

                        LogUtils.i("info", "查询是否收藏过获取到的数据------>" + msg.obj);

                        if (((String)msg.obj).contains("产品未收藏")) {
                            cb_collect.setChecked(false);
                        }else {
                            cb_collect.setChecked(true);
                        }
                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("cid", id);
            jsonObject.put("type", "product");
            jsonObject.put("act", "qurey");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.httpPostResult(ProductDetailActivity.this, jsonObject, checkHandler, Config.URL_COLLECT);
            }
        }).start();

    }

    /**
     * 获取店铺id
     * @param id
     */
    private void getShopId(String id) {

        final Handler shopHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        LogUtils.i("info", "获得到的店铺数据------>" + msg.obj);

                        try {
                            JSONObject jsonObject =new JSONObject((String)msg.obj);
                            String shopId = jsonObject.getString("shop_id");
                            Intent intent_shop = new Intent(ProductDetailActivity.this, ShopActivity.class);
                            intent_shop.putExtra("uid", shopId);
                            startActivity(intent_shop);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("productId", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
                HttpUtils.httpPostResult(ProductDetailActivity.this, jsonObject, shopHandler, Config.URL_GET_SHOP);
    }

    /**
     * 取消收藏
     *
     * @param cid
     */
    private void cancelCollect( String cid) {
        final Handler cancelHander = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        cb_collect.setChecked(false);
                        ToastUtils.TextToast(ProductDetailActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT);
                        break;
                }
            }
        };

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("type", "product");
            jsonObject.put("cid", cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.httpPostResult(this, jsonObject, cancelHander, Config.URL_COLLECT_DETELE);
    }

}
