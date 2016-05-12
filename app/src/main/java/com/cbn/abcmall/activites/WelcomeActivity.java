package com.cbn.abcmall.activites;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;

import com.cbn.abcmall.R;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.umeng.update.UmengUpdateAgent;

/**
 * 欢迎页
 * Created by Administrator on 2015/10/16.
 */
public class WelcomeActivity extends BaseActivity {
    private Handler handler;
    private SharePreferenceUtil sharePreferenceUtil;
    @Override
    public void initWidget() {

        // 欢迎页全屏显示
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        openTecentXpush();// 开启信鸽推送

        // 去掉更新检测弹窗提示
        UmengUpdateAgent.setUpdateCheckConfig(false);
        //调用友盟自动更新
        UmengUpdateAgent.update(this);

        sharePreferenceUtil = new SharePreferenceUtil(this, Config.ACCOUNT);

        /*// 开启服务进程
        ServiceTool.startService(this);*/

        if (!sharePreferenceUtil.hasStr("isLogin")) {
            sharePreferenceUtil.saveStr("isLogin", "3");// 表示第一次登录应用
        }
        handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hasAccount();
            }
        }, 1000);
    }

    @Override
    public void widgetClick(View v) {

    }


    /**
     * 判断是否登录过，登录过就不用再登录
     */
    private void hasAccount() {
        if (sharePreferenceUtil.getStr("isLogin").equals("1")) { // 表示登录
            Intent mainIntent = new Intent(WelcomeActivity.this, MainActivity.class);
            startActivity(mainIntent);
            finish();
        } else {
            Intent loginIntent = new Intent(WelcomeActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }


    /**
     * 开启腾讯信鸽推送服务
     */
    private void openTecentXpush() {
        // 开启logcat输出，方便debug，发布时请关闭
//         XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext
        XGPushManager.registerPush(getApplicationContext());
//        XGPushConfig.getToken(this);
        LogUtils.i("获取到的设备token---->" + XGPushConfig.getToken(this));
        // 2.36（不包括）之前的版本需要调用以下2行代码
        // Intent service = new Intent(getApplicationContext(), XGPushService.class);
        // getApplicationContext().startService(service);
    }

}
