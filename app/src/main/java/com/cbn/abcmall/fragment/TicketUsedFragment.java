package com.cbn.abcmall.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.LogUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 使用记录
 * Created by lost on 2016/5/11.
 */
public class TicketUsedFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_ticketused, null);
        getUsedTicket();
        return view;
    }


    /**
     * 获取使用记录
     */
    private void getUsedTicket() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        LogUtils.i("我的礼券------->" + msg.obj);
                        String json = (String) msg.obj;
                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("token", Config.getAccountToken(getActivity()));
            jsonObject.put("sign", Config.getSign(getActivity()));
            jsonObject.put("page", "");
            jsonObject.put("status", "2");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils.httpPostResult(getActivity(), jsonObject, handler, Config.URL_GET_MYCOUPON);

            }
        }).start();


    }
}
