package com.cbn.abcmall.services;

import android.content.Context;
import android.content.Intent;

/**
 * 服务启动工具类
 * Created by lost on 2015/11/10.
 */
public class ServiceTool {

    public static void startService(Context context) {
        context.startService(new Intent(context, MyService.class));
    }
}
