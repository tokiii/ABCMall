package com.cbn.abcmall.activites;

import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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

    private ImageView left;
    private TextView title;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_rebate);
        left = (ImageView) findViewById(R.id.left);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText("返利信息");
        getMyRebate();
    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.left:

                finish();
                break;
        }

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
