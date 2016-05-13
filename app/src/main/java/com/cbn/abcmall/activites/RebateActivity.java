package com.cbn.abcmall.activites;

import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.cbn.abcmall.R;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 我的返利界面
 * Created by lost on 2016/5/8.
 */
public class RebateActivity extends BaseActivity {
    @Override
    public void initWidget() {
        setContentView(R.layout.activity_rebate);
        getMyRebate();
    }

    @Override
    public void widgetClick(View v) {

    }



  private void getMyRebate() {
      final Handler handler = new Handler() {
          @Override
          public void handleMessage(Message msg) {
              super.handleMessage(msg);

              switch (msg.what) {
                  case 1:
                      LogUtils.i("我的返利------->" + msg.obj);
                      String json = (String) msg.obj;
                      break;
              }
          }
      };

      final JSONObject jsonObject = new JSONObject();

      try {
          jsonObject.put("token", Config.getAccountToken(this));
          jsonObject.put("sign", Config.getSign(this));
          jsonObject.put("page", "");
          jsonObject.put("status", "1");
      } catch (JSONException e) {
          e.printStackTrace();
      }


      new Thread(new Runnable() {
          @Override
          public void run() {

              HttpUtils.httpPostResult(RebateActivity.this, jsonObject, handler, Config.URL_GET_MYCOUPON);

          }
      }).start();


  }

}
