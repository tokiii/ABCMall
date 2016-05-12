package com.cbn.abcmall.activites;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import com.cbn.abcmall.R;

/**
 * 模拟友盟活跃度界面
 */
public class EmptyActivity extends BaseActivity {

    @Override
    public void initWidget() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty_activty);

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... params) {

                try {
                    Thread.sleep(500);// 设置activity从开始到结束间隔时间
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                finish();
            }
        }.execute();

    }

}
