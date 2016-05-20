package com.cbn.abcmall.activites;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.InviteAdapter;
import com.cbn.abcmall.bean.InviteFriend;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 我的邀请
 * Created by lost on 2016/5/8.
 */
public class InviteActivity extends BaseActivity {

    private Button btn_instantinvite;

    private ListView lv_friends;
    private ImageView left;
    private TextView title;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_invite);
        btn_instantinvite = (Button) findViewById(R.id.btn_instantinvite);
        btn_instantinvite.setOnClickListener(this);
        lv_friends = (ListView) findViewById(R.id.lv_friends);

        left = (ImageView) findViewById(R.id.left);
        left.setOnClickListener(this);
        title = (TextView) findViewById(R.id.title);
        title.setText("我的邀请");
        getFriendList();

    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.btn_instantinvite:
                Intent intent = new Intent(InviteActivity.this, InviteQrcodeActivity.class);
                startActivity(intent);
                break;

            case R.id.left:
                finish();
                break;
        }

    }


    private void getFriendList() {

        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        LogUtils.i("获得的好友信息为------->" + msg.obj);

                        String json = (String) msg.obj;

                        try {
                            JSONObject jsonObject = new JSONObject(json);

                            JSONArray array = jsonObject.getJSONArray("list");

                            List<InviteFriend> list = JsonUtils.jsonToList(array.toString(), new TypeToken<List<InviteFriend>>(){});

                            if (list != null && list.size()!= 0) {
                                InviteAdapter adapter = new InviteAdapter(InviteActivity.this, list);
                                lv_friends.setAdapter(adapter);
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
            jsonObject.put("token", Config.getAccountToken(InviteActivity.this));
            jsonObject.put("sign", Config.getSign(InviteActivity.this));
            jsonObject.put("page", "1");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        new Thread(new Runnable() {
            @Override
            public void run() {

                HttpUtils.httpPostResult(InviteActivity.this, jsonObject, handler, Config.URL_INVITE);

            }
        }).start();


    }
}
