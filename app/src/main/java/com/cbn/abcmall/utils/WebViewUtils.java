package com.cbn.abcmall.utils;

import android.content.Context;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;

/**
 * webView缓存工具类
 * Created by lost on 2015/11/26.
 */
public class WebViewUtils {

    public static void setWebView(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
    }



    // 设置cookie
    public static void synCookies(Context context, String url) {
        CookieSyncManager.createInstance(context);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setCookie(url, new SharePreferenceUtil(context, "cookie").getStr("cookie"));
        CookieSyncManager.getInstance().sync();
    }


}
