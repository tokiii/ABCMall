package com.cbn.abcmall.utils;

/*
 * abstract 处理网络json的http类
 * author
 * */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebView;

import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.activites.LoginActivity;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpUtils {

    public static String getRequest(String url) throws Exception {
        HttpGet get = new HttpGet(url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpResponse httpresponse = httpClient.execute(get);
        if (httpresponse.getStatusLine().getStatusCode() == 200) {
            String result = EntityUtils.toString(httpresponse.getEntity());
            get.abort();
            return result;
        }
        return null;
    }

    // post
    public static String postRequest(String url, Map<String, String> params)
            throws Exception {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        if (params != null) {
            for (String key : params.keySet()) {
                param.add(new BasicNameValuePair(key, params.get(key)));
            }
            post.setEntity(new UrlEncodedFormEntity(param, HTTP.UTF_8));
        }
        HttpResponse httpResponse = httpClient.execute(post);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String result = EntityUtils.toString(httpResponse.getEntity());
            return result;
        }
        return null;
    }

    // 通过post方式向服务器发送json数据包
    public static String JsonPost(final Context context, final String url, final JSONObject json) {

        if (!NetUtil.isNetworkAvaiable(context)) {
            return  "";
        }

        HttpPost request = new HttpPost(url);
        // 封装json
        String retSrc = null;
        try {
            if (json != null) {
                // 绑定到请求Entity
                StringEntity se = new StringEntity(json.toString(), HTTP.UTF_8);
                request.setEntity(se);
                // 发送请求
                HttpResponse httpResponse = new DefaultHttpClient()
                        .execute(request);
                // 得到应答字符串
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    retSrc = EntityUtils.toString(httpResponse.getEntity());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (JsonUtils.decodeUnicode(retSrc).contains("登录失效")) {
            Log.i("info", "获得的登录失效------>" + retSrc);
            return "登录失效";
        } else {
            return retSrc;
        }
    }

    /**
     * 封装之后的网络请求
     * @param jsonObject
     * @param handler
     * @param url
     */
    public static void httpPostResult(final Context context, final JSONObject jsonObject, final Handler handler, final String url) {


        if (!NetUtil.isNetworkAvaiable(context)){

            new AlertDialog.Builder(context)
                    .setTitle("当前无网络连接")
                    .setMessage("是否重试")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                   httpPostResult(context, jsonObject, handler, url);

                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();

            return;

        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = JsonPost(context, url, jsonObject);

                LogUtils.i("info", "从网上得到的数据为------------>" + str);
                if (str == null) {
                    return;
                }
                String json = JsonUtils.decodeUnicode(str);
                Message message = new Message();
                message.obj = json;
                if (json.contains("登录失效")){

                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(loginIntent);

                }else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        }).start();


    }


    /**
     * 封装之后的网络请求
     * @param jsonObject
     * @param handler
     * @param url
     */
    public static void httpPostResult(final WebView webView, final Context context, final JSONObject jsonObject, final Handler handler, final String url) {

        if (!NetUtil.isNetworkAvaiable(context)){
            new AlertDialog.Builder(context)
                    .setTitle("当前无网络连接")
                    .setMessage("是否重试")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    httpPostResult(webView, context, jsonObject, handler, url);
                                    webView.reload();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            }).show();

            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = JsonPost(context, url, jsonObject);
                if (str == null) {
                    return;
                }
                String json = JsonUtils.decodeUnicode(str);
                Message message = new Message();
                message.obj = json;
                if (json.contains("登录失效")){
                    Config.Login(context);
                    String token = Config.getAccountToken(context);
                    String sign = Config.getSign(context);
                    try {
                        jsonObject.put("token", token);
                        jsonObject.put("sign", sign);
                        Log.i("info", "重新获得的登录json------------>" + jsonObject.toString() );
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    httpPostResult(context, jsonObject, handler, url);
                }else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        }).start();


    }

  /*  public static void httpPostResult( final JSONObject jsonObject, final Handler handler, final String url) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = JsonPost(url, jsonObject);
                String json = JsonUtils.decodeUnicode(str);
                Message message = new Message();
                message.obj = json;
                if (json.contains("登录失效")){
                   message.what = 10;
                }else {
                    message.what = 1;
                }
                handler.sendMessage(message);
            }
        }).start();
*/

//    }

}
