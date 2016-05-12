package com.cbn.abcmall.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.cbn.abcmall.utils.NetUtil;
import com.cbn.abcmall.utils.ToastUtils;

/**
 * 监听网络状态改变时网络是否可用广播接收器
 */
public class NetReceiver extends BroadcastReceiver {
    public NetReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (!NetUtil.isNetworkAvaiable(context)) {
            ToastUtils.TextToast(context, "当前网络无法连接！", Toast.LENGTH_SHORT);
        }
    }
}
