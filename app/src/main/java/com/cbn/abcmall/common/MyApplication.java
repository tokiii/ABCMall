package com.cbn.abcmall.common;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * 自定义Application
 * Created by lost on 2015/11/12.
 */
public class MyApplication extends Application {


    public static MyApplication myApplication;
    public RequestQueue requestQueue;


    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    /**
     * 单例模式返回Application实例
     * @return
     */
    public static synchronized MyApplication getInstance() {
        return myApplication;
    }


    /**
     * 获取RequestQueue
     * @return
     */
    public RequestQueue getRequestQueue() {

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        return  requestQueue;
    }
}
