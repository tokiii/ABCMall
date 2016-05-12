package com.cbn.abcmall.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.AddressAdapter;
import com.cbn.abcmall.bean.AddressData;
import com.cbn.abcmall.bean.AddressDetail;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 地址选择Activity
 * Created by Administrator on 2015/9/23.
 */
public class AddressActivity extends BaseActivity{

    private ListView lv_address;
    private AddressAdapter adapter;
    private ImageView left;
    private ImageView right;
    private TextView title;
    private JSONObject jsonObject;
    private Handler handler;
    private List<AddressDetail> lists;
    private Button btn_add; //添加地址按钮
    private String json;
    private String addressId;
    private ProgressDialog progressDialog;// 正在加载进度条
    @Override
    public void initWidget() {
        setContentView(R.layout.activity_address);
        initView();
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        progressDialog.dismiss();
                        json = (String) msg.obj;

                        LogUtils.i("info", "获得的地址列表为----->" + json);
                        AddressData addressData = (AddressData) JsonUtils.jsonToBean(json, AddressData.class);
                        lists = addressData.getConsignee_list();
                        adapter = new AddressAdapter(AddressActivity.this, lists);
                        lv_address.setAdapter(adapter);
                        lv_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                if (isFromCartConfirm()) {
                                    Intent confirmAddressIntent = new Intent(AddressActivity.this, CartConfirmActivity.class);
                                    addressId = lists.get(position).getId();
                                    LogUtils.i("info", "得到的地址ID为----->" + lists.get(position).getId());
                                    confirmAddressIntent.putExtra("id", addressId);
                                    setResult(RESULT_OK, confirmAddressIntent);
                                    finish();
                                } else {
                                    Intent editIntent = new Intent(AddressActivity.this, AddressEditActivity.class);
                                    addressId = lists.get(position).getId();
                                    LogUtils.i("info", "得到的地址ID为----->" + lists.get(position).getId());
                                    editIntent.putExtra("id", addressId);
                                    startActivity(editIntent);
                                }

                            }
                        });
                        break;
                }
            }
        };


        getAddressData();

    }

    private void initView() {
        lv_address = (ListView) findViewById(R.id.lv_address);
        left = (ImageView) findViewById(R.id.left);
        left.setOnClickListener(this);
        right = (ImageView) findViewById(R.id.right);
        title = (TextView) findViewById(R.id.title);
        right.setVisibility(View.GONE);
        title.setText("收货地址");
        lists = new ArrayList<>();
        btn_add = (Button) findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载....");
    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.btn_add:
                Intent addIntent = new Intent(AddressActivity.this, AddressEditActivity.class);
                addIntent.putExtra("json", json);
                startActivity(addIntent);
                break;
        }

    }

    /**
     * 获取地址信息
     */
    private void getAddressData() {
        jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("act", "consignee");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        progressDialog.show();
        HttpUtils.httpPostResult(AddressActivity.this, jsonObject, handler, Config.URL_ADDRESS);
    }

    /**
     * 是否为确认收货界面传来的数据
     * @return
     */
    private boolean isFromCartConfirm() {
        return getIntent().hasExtra("CartConfirmActivity");
    }
}
