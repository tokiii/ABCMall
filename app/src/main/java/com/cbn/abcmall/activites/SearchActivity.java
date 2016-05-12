package com.cbn.abcmall.activites;

import android.content.Intent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.utils.LogUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 搜索界面
 * Created by Administrator on 2015/10/15.
 */
public class SearchActivity extends BaseActivity {

    private ImageView right;
    private ImageView left;
    private TextView title;
    private String key;
    private WebView wv_search;
    private String url;
    @Override
    public void initWidget() {
        setContentView(R.layout.activity_search);
        initView();
        key = getIntent().getStringExtra("key");
        load(key);
    }

    /**
     * 初始化组件
     */
    private void initView() {
        right = (ImageView) findViewById(R.id.right);
        left = (ImageView) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        title.setText("搜索结果");
        left.setOnClickListener(this);
        right.setVisibility(View.GONE);
        wv_search = (WebView) findViewById(R.id.wv_search);

        wv_search.getSettings().setJavaScriptEnabled(true);// 设置js可用
        wv_search.getSettings().setLoadsImagesAutomatically(true);// 设置自动加载图片
        wv_search.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);// 设置js可以自动打开窗口
        wv_search.addJavascriptInterface(new JsObject(), "contact");
        wv_search.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                LogUtils.i("info", "得到的链接网址------>" + url);
                if (url.contains("s=detail")) {
                    Intent productDetailIntent = new Intent(SearchActivity.this, ProductDetailActivity.class);
                    productDetailIntent.putExtra("productUrl", url);
                    startActivity(productDetailIntent);
                } else if (url.contains("shop-")) {// 如果匹配的是店铺，则跳转到店铺列表
                    String uid = getNumberFromString(url);
                    Intent shopIntent = new Intent(SearchActivity.this, ShopActivity.class);
                    shopIntent.putExtra("uid", uid);
                    startActivity(shopIntent);
                } else {
                    wv_search.addJavascriptInterface(new JsObject(), "contact");
                }
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!wv_search.getSettings().getLoadsImagesAutomatically()) {
                    wv_search.getSettings().setLoadsImagesAutomatically(true);
                    super.onPageFinished(view, url);
                }
            }
        });
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                this.finish();
                break;
        }
    }

    private void load(String key) {
        url = "http://www.cbnmall.com/m.php?key=" + key + "&op=list";
        LogUtils.i("info", "搜索到的网址为-------->" + url);
        wv_search.loadUrl(url);
    }

    class JsObject {
        @JavascriptInterface
        public String getcontact() {
            return "headerandfooterhiden";
        }
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

}
