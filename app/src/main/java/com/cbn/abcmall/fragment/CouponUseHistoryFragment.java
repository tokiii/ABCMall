package com.cbn.abcmall.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 优惠券使用记录
 * Created by lost on 2016/5/8.
 */
public class CouponUseHistoryFragment extends Fragment {

    private ListView lv_history;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_couponusehistory, null);
        lv_history = (ListView) v.findViewById(R.id.lv_history);

        getHistory();

        return v;
    }


    /**
     * 获得已使用优惠券数据列表
     */
    private void getHistory() {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        LogUtils.i("得到的优惠券数据为------->" + msg.obj);
                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("token", Config.getAccountToken(getActivity()));
            jsonObject.put("sign", Config.getSign(getActivity()));
            jsonObject.put("page", "1");
            jsonObject.put("status", "2"); // “2”代表已使用
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils.httpPostResult(getActivity(), jsonObject, handler, Config.URL_COUPON);

            }
        }).start();

    }
}
