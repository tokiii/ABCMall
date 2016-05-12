package com.cbn.abcmall.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.utils.AppManager;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

/**
 * 设置界面
 * Created by Administrator on 2015/10/8.
 */
public class SettingActivity extends BaseActivity {
    private ImageView left;
    private ImageView right;
    private TextView title;
    private RelativeLayout rl_address;// 收货地址
    private RelativeLayout rl_check_update;// 更新检测
    private Button btn_exit;
    private ProgressDialog progressDialog;
    private TextView tv_version;// 当期版本号显示

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        title = (TextView) findViewById(R.id.title);
        rl_address = (RelativeLayout) findViewById(R.id.rl_address);
        rl_check_update = (RelativeLayout) findViewById(R.id.rl_check_update);
        rl_check_update.setOnClickListener(this);
        btn_exit = (Button) findViewById(R.id.btn_exit);
        rl_address.setOnClickListener(this);
        btn_exit.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setVisibility(View.GONE);
        title.setText("设置");
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在检测...");
        tv_version = (TextView) findViewById(R.id.tv_version);
        tv_version.setText("当前版本 V" + Config.getVersion(this));
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.rl_address:
                Intent addressIntent = new Intent(SettingActivity.this, AddressActivity.class);
                startActivity(addressIntent);
                break;
            case R.id.btn_exit:
                Intent exitIntent = new Intent(SettingActivity.this, LoginActivity.class);
                exitIntent.putExtra("exit", "exit");// 发送退出消息
                new SharePreferenceUtil(this, Config.ACCOUNT).clearStr();
                new SharePreferenceUtil(this, Config.ACCOUNT).saveStr("isLogin", "0");//清空缓存数据
                startActivity(exitIntent);
                AppManager.getAppManager().finishActivity(MainActivity.class);// 销毁主页
                finish();
                break;
            // 检查更新按钮
            case R.id.rl_check_update:
                progressDialog.show();
                UmengUpdateAgent.forceUpdate(this);
                UmengUpdateAgent.setUpdateAutoPopup(false);
                UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                    @Override
                    public void onUpdateReturned(int i, UpdateResponse updateResponse) {
                        switch (i) {
                            case UpdateStatus.Yes:
                                progressDialog.dismiss();
                                UmengUpdateAgent.showUpdateDialog(SettingActivity.this, updateResponse);
                                break;
                            case UpdateStatus.No: // has no update
                                progressDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "当前为最新版本，无需更新！", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.NoneWifi: // none wifi
                                progressDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                                break;
                            case UpdateStatus.Timeout: // time out
                                progressDialog.dismiss();
                                Toast.makeText(SettingActivity.this, "网络无法连接", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                });
                break;
        }

    }

}
