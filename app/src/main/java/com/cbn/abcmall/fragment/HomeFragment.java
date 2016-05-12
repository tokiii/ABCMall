package com.cbn.abcmall.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.activites.LoginActivity;
import com.cbn.abcmall.activites.MainActivity;
import com.cbn.abcmall.activites.ProductDetailActivity;
import com.cbn.abcmall.activites.RegisterActivity;
import com.cbn.abcmall.activites.SearchActivity;
import com.cbn.abcmall.activites.ShopActivity;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.ToastUtils;
import com.cbn.abcmall.utils.WebViewUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 购物首页fragment
 * Created by Administrator on 2015/9/10.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private WebView webView;
    private Handler handler;
    private SwipeRefreshLayout refreshLayout;// 下拉刷新当前界面
    private ImageView iv_search;
    private EditText et_key;
    private ImageView left;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        webView = (WebView) v.findViewById(R.id.wv_home);
        iv_search = (ImageView) v.findViewById(R.id.iv_search);
        iv_search.setOnClickListener(this);
        et_key = (EditText) v.findViewById(R.id.et_key);
        left = (ImageView) v.findViewById(R.id.left);
        left.setOnClickListener(this);
        left.setVisibility(View.GONE);
        et_key.clearFocus();
//        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.refresh);
//        // 设置refreshLayout的变换颜色
//        refreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
//        // 设置刷新监听器
//        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                webView.reloagd();
//            }
//        });

        et_key.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    if (TextUtils.isEmpty(et_key.getText())) {
                        ToastUtils.TextToast(getActivity(), "请输入搜索内容！", Toast.LENGTH_SHORT);
                    } else {
                        goToSearch(et_key.getText().toString().trim());
                    }
                }
                return false;
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadsImagesAutomatically(false);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        // 开启 DOM storage API 功能
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        handler = new Handler() {
        };

        isTokenLate();
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        webView.loadUrl(Config.URL_HOME);

        webView.addJavascriptInterface(new JsObject(), "contact");
        WebViewUtils.synCookies(getActivity(), Config.URL_HOME);
        /**
         * 设置webView内链接点击自刷新
         */
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                url = url.replace("temp=default", "temp=wap");
                LogUtils.i("info", "得到的链接网址------>" + url);

                if (url.contains("shop.php") || url.contains("shop-")) {
                    Intent shopIntent = new Intent(getActivity(), ShopActivity.class);
                    shopIntent.putExtra("url", url);
                    startActivity(shopIntent);
                } else if (url.contains("product-detail")) {
                    Intent productDetailIntent = new Intent(getActivity(), ProductDetailActivity.class);
                    productDetailIntent.putExtra("productUrl", url);
                    startActivity(productDetailIntent);
                } else if (url.equals("http://www.cbnmall.com/main.php")) {
                    Intent mainIntent = new Intent(getActivity(), MainActivity.class);
                    mainIntent.putExtra("page", 2);
                    startActivity(mainIntent);
                } else if (url.equals("http://www.cbnmall.com/login.php?forward=%2Fm.php%3Fop%3Dstore")) {

                } else {
                    left.setVisibility(View.VISIBLE);
                    webView.loadUrl(url);
                    isTokenLate();
                    webView.addJavascriptInterface(new JsObject(), "contact");

                }
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                if (!webView.getSettings().getLoadsImagesAutomatically()) {
                    webView.getSettings().setLoadsImagesAutomatically(true);
                }
                super.onPageFinished(view, url);
//                refreshLayout.setRefreshing(false);
            }
        });

//        webView.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event.getAction() == KeyEvent.ACTION_DOWN) {
//                    if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
//                        webView.goBack();
//                        isTokenLate();
//                        return true;
//                    }
//                }
//                return false;
//            }
//        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_register:
                Intent registerIntent = new Intent(getActivity(), RegisterActivity.class);
                startActivity(registerIntent);
                break;

            case R.id.btn_login:
                Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                startActivity(loginIntent);
                break;


            case R.id.iv_search:
                if (!TextUtils.isEmpty(et_key.getText())) {
                    goToSearch(et_key.getText().toString().trim());
                } else {
                    ToastUtils.TextToast(getActivity(), "请输入搜索内容", Toast.LENGTH_SHORT);
                }
                break;

            case R.id.left:
                webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
                webView.loadUrl(Config.URL_HOME);
                left.setVisibility(View.GONE);
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
     * 在首页判断登录是否过期
     */
    public void isTokenLate() {

        String token = Config.getAccountToken(getActivity());
        String sign = Config.getSign(getActivity());

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("sign", sign);
            jsonObject.put("type", "product");
            jsonObject.put("page", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.httpPostResult(webView, getActivity(), jsonObject, handler, Config.URL_COLLECT_LIST);

    }


    /**
     * 跳转到搜索界面
     *
     * @param key
     */
    private void goToSearch(String key) {
        Intent searchIntent = new Intent(getActivity(), SearchActivity.class);
        searchIntent.putExtra("key", key);
        startActivity(searchIntent);
    }

}
