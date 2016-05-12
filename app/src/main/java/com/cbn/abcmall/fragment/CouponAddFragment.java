package com.cbn.abcmall.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 添加优惠券界面
 * Created by lost on 2016/5/6.
 */
public class CouponAddFragment extends Fragment implements View.OnClickListener{


    private EditText et_card;
    private EditText et_password;
    private Button btn_active;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_couponadd, null);
        et_card = (EditText) view.findViewById(R.id.et_card);
        et_password = (EditText) view.findViewById(R.id.et_password);
        btn_active = (Button) view.findViewById(R.id.btn_active);
        btn_active.setOnClickListener(this);

        return view;
    }





    /**
     * 添加激活码
     */
    private void addCoupon(String card, String password) {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        LogUtils.i("添加优惠券返回的数据------->" + msg.obj);

                        String json = (String) msg.obj;

                        try {
                            JSONObject jsonObject = new JSONObject(json);

                            ToastUtils.TextToast(getActivity(), jsonObject.getString("msg"), Toast.LENGTH_SHORT);

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
            jsonObject.put("action", "activi");
            jsonObject.put("coupon_card", card);
            jsonObject.put("coupon_pswd", password);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_active:

                if (TextUtils.isEmpty(et_card.getText()) || TextUtils.isEmpty(et_password.getText())) {
                    ToastUtils.TextToast(getActivity(), "输入内容不能为空！", Toast.LENGTH_SHORT);
                    return;
                }
                String card = et_card.getText().toString();
                String password = et_password.getText().toString();

                addCoupon(card, password);
                break;
        }
    }
}
