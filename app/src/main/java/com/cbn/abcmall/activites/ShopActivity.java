package com.cbn.abcmall.activites;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.StringUtil;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 店铺界面
 * Created by Administrator on 2015/10/12.
 */
public class ShopActivity extends BaseActivity{

    private TextView right;
    private ImageView left;
    private TextView title;
    private WebView wv_shop;

    private String id;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_shop);
        initView();
        Intent intent = getIntent();
        String url;
        if(intent.hasExtra("uid"))
        {
            url = "http://www.cbnmall.com/shop.php?uid=" + intent.getStringExtra("uid");
            id = intent.getStringExtra("uid");
        }else {
            url = getIntent().getStringExtra("url");
            id = StringUtil.getNumberFromString(url);
        }
        LogUtils.i("ShopActivity", "得到的链接网址------>" + url);
        checkProductIsCollected(id);

        wv_shop.loadUrl(url);
        wv_shop.addJavascriptInterface(new JsObject(), "contact");
        /**
         * 设置webView内链接点击自刷新
         */
        wv_shop.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                LogUtils.i("ShopActivity", "从店铺点击商品获得的链接------>" + url);

                if (url.contains("product-detail") || url.contains("s=detail")) {
                    Intent productDetailIntent = new Intent(ShopActivity.this, ProductDetailActivity.class);
                    productDetailIntent.putExtra("productUrl", url);
                    startActivity(productDetailIntent);
                } else {
                    wv_shop.loadUrl(url);
                    wv_shop.addJavascriptInterface(new JsObject(), "contact");
                }

                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (!wv_shop.getSettings().getLoadsImagesAutomatically()) {
                    wv_shop.getSettings().setLoadsImagesAutomatically(true);
                }
                super.onPageFinished(view, url);
            }
        });

        wv_shop.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && wv_shop.canGoBack()) {
                        wv_shop.goBack();
                        return true;
                    }
                }
                return false;
            }
        });



    }


    private void initView() {
        right = (TextView) findViewById(R.id.right);
        left = (ImageView) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        title.setText("店铺");
        left.setOnClickListener(this);
        right.setOnClickListener(this);
        wv_shop = (WebView) findViewById(R.id.wv_shop);
        wv_shop.getSettings().setJavaScriptEnabled(true);
        wv_shop.getSettings().setLoadsImagesAutomatically(false);
        wv_shop.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            // 点击收藏
            case R.id.right:
                if (right.getText().toString().equals("已收藏")){
                    cancelCollect(id);
                }else {
                    collectProduct(id);
                }
                break;
        }

    }

    class JsObject {
        @JavascriptInterface
        public String getcontact() {
            // webview.loadUrl("javascript:headerandfooterhiden();");

            return "headerandfooterhiden";
        }
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
                        LogUtils.i("收藏店铺获得的数据为---------->" +msg.obj);
                        if (((String) msg.obj).contains("收藏店铺成功")){
                            right.setText("已收藏");
                            ToastUtils.TextToast(ShopActivity.this, "收藏店铺成功！", Toast.LENGTH_SHORT);
                        }else {
                            ToastUtils.TextToast(ShopActivity.this, "店铺已收藏,勿需重复收藏！", Toast.LENGTH_SHORT);
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
            jsonObject.put("type", "shop");
            jsonObject.put("act", "add");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.httpPostResult(ShopActivity.this, jsonObject, collectHandler, Config.URL_COLLECT);
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

                        LogUtils.i("ShopActivity---", "查询是否收藏过获取到的数据------>" + msg.obj);

                        if (((String)msg.obj).contains("店铺未收藏")) {
                            right.setText("收藏");
                        }else {
                            right.setText("已收藏");
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
            jsonObject.put("type", "shop");
            jsonObject.put("act", "qurey");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpUtils.httpPostResult(ShopActivity.this, jsonObject, checkHandler, Config.URL_COLLECT);
            }
        }).start();

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
                        LogUtils.i("info", "取消收藏接收的数据为----->" + msg.obj);
                        right.setText("收藏");
                        ToastUtils.TextToast(ShopActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT);
                        break;
                }
            }
        };

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("type", "shop");
            jsonObject.put("cid", cid);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.httpPostResult(this, jsonObject, cancelHander, Config.URL_COLLECT_DETELE);
    }

}
