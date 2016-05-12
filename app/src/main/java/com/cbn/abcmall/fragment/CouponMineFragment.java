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
import com.cbn.abcmall.adapter.CouponMineAdapter;
import com.cbn.abcmall.bean.Coupon;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 我的优惠券
 * Created by lost on 2016/5/6.
 */
public class CouponMineFragment extends Fragment {

    private ListView lv_youhuiquan;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_couponmine, null);
        lv_youhuiquan = (ListView) view.findViewById(R.id.lv_youhuiquan);
        getMineCoupon();

        return view;
    }


    private void getMineCoupon() {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        LogUtils.i("得到的优惠券数据为------->" + msg.obj);

                        String json = (String) msg.obj;
                        try {
                            JSONObject jsonObject = new JSONObject(json);
                            JSONObject coupon_list = jsonObject.getJSONObject("coupon_list");
                            JSONArray array = coupon_list.getJSONArray("list");

                            List<Coupon> coupons = JsonUtils.jsonToList(array.toString(), new TypeToken<List<Coupon>>(){});

                            if (coupons.size() != 0) {
                                CouponMineAdapter adapter = new CouponMineAdapter(getActivity(), coupons);
                                lv_youhuiquan.setAdapter(adapter);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("token", Config.getAccountToken(getActivity()));
            jsonObject.put("sign", Config.getSign(getActivity()));
            jsonObject.put("page", "");
            jsonObject.put("status", "1");
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
