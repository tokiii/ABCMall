package com.cbn.abcmall.activites;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.utils.AppManager;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.ToastUtils;
import com.cbn.abcmall.wheel.ArrayWheelAdapter;
import com.cbn.abcmall.wheel.OnWheelChangedListener;
import com.cbn.abcmall.wheel.WheelView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * 地址修改界面
 * Created by Administrator on 2015/9/23.
 */
public class AddressEditActivity extends BaseActivity implements OnWheelChangedListener {

    private TextView right;
    private ImageView left;
    private TextView title;
    private LinearLayout ll_province;
    private Spinner sp_default;// 是否默认
    private final String[] strings = {"否", "是"};
    private ArrayAdapter<String> adapter;// 加载视频
    private Button btn_delete;// 删除地址
    /**
     * 把全国的省市区的信息以json的格式保存，解析完成后赋值为null
     */
    private JSONObject mJsonObj;
    /**
     * 省的WheelView控件
     */
    private WheelView mProvince;
    /**
     * 市的WheelView控件
     */
    private WheelView mCity;
    /**
     * 区的WheelView控件
     */
    private WheelView mArea;
    /**
     * 所有省
     */
    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市s
     */
    private Map<String, String[]> mCitisDatasMap = new HashMap<String, String[]>();
    /**
     * key - 市 values - 区s
     */
    private Map<String, String[]> mAreaDatasMap = new HashMap<String, String[]>();
    /**
     * 当前省的名称
     */
    private String mCurrentProviceName;
    /**
     * 当前市的名称
     */
    private String mCurrentCityName;
    /**
     * 当前区的名称
     */
    private String mCurrentAreaName = "";
    private JSONArray allJson;
    private String address;
    private EditText et_name;
    private EditText et_tel;
    private EditText et_mobile;
    private EditText et_address;
    private EditText et_address_detail;
    private String def = "1"; //默认状态
    private String id; //地址id
    private Handler addHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:

                    progressDialog.dismiss();
                    LogUtils.i("info", "保存地址信息返回的json数据为----->" + msg.obj);
                    ToastUtils.TextToast(AddressEditActivity.this, isEdit() ? "修改成功！" : "添加成功！", Toast.LENGTH_SHORT);

                    if (getIntent().hasExtra("CartConfirmActivity")) {
                        try {
                            // 解析成对象获得地址id
                            JSONObject jsonObject = new JSONObject((String) msg.obj);
                            JSONObject jsonObject1 = jsonObject.getJSONObject("adder");
                            String id = jsonObject1.getString("id");

                            LogUtils.i("info", "获得的地址id为----------------》" + id);
                            Intent cartConfirmIntent = new Intent(AddressEditActivity.this, CartConfirmActivity.class);
                            cartConfirmIntent.putExtra("id", id);
                            setResult(RESULT_OK, cartConfirmIntent);

                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Intent addressIntent = new Intent(AddressEditActivity.this, AddressActivity.class);
                        startActivity(addressIntent);
                        AppManager.getAppManager().finishActivity(AddressActivity.class);
                        finish();
                    }
                    break;

            }
        }
    };
    ;// 添加地址handler
    private Handler changeHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    String json = (String) msg.obj;
                    LogUtils.i("info", "获得的地址数据为----->" + json);
                    try {
                        JSONObject addressJsonObject = new JSONObject(json);
                        JSONObject addressDetailJsonobject = addressJsonObject.getJSONObject("adder");
                        et_address.setText(addressDetailJsonobject.getString("t"));
                        et_name.setText(addressDetailJsonobject.getString("name"));
                        et_mobile.setText(addressDetailJsonobject.getString("mobile"));
                        et_tel.setText(addressDetailJsonobject.getString("tel"));
                        et_address_detail.setText(addressDetailJsonobject.getString("address"));
                        progressDialog.dismiss();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };
    ;// 更改地址handler
    private ProgressDialog progressDialog; //正在加载进度条

    private Handler deleteHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case 1:
                    LogUtils.i("info", "删除地址获得数据为----->" + msg.obj);
                    ToastUtils.TextToast(AddressEditActivity.this, "删除成功！", Toast.LENGTH_SHORT);
                    Intent addressIntent = new Intent(AddressEditActivity.this, AddressActivity.class);
                    startActivity(addressIntent);
                    AppManager.getAppManager().finishActivity(AddressActivity.class);
                    finish();
                    break;
            }
        }
    };


    @Override
    public void initWidget() {
        setContentView(R.layout.activity_address_edit);
        initView();
        initJsonData();
        initDatas();
        mProvince.setViewAdapter(new ArrayWheelAdapter<String>(this, mProvinceDatas));
        // 添加change事件
        mProvince.addChangingListener(this);
        // 添加change事件
        mCity.addChangingListener(this);
        // 添加change事件
        mArea.addChangingListener(this);
        mProvince.setVisibleItems(5);
        mCity.setVisibleItems(5);
        mArea.setVisibleItems(5);
        updateCities();
        updateAreas();
        if (isEdit()) {
            progressDialog.show();
            id = getIntent().getStringExtra("id");
            selectAddress(id);
        }
    }

    /**
     * 实例化视图组件
     */
    private void initView() {
        right = (TextView) findViewById(R.id.right);
        left = (ImageView) findViewById(R.id.left);
        title = (TextView) findViewById(R.id.title);
        right.setOnClickListener(this);
        left.setOnClickListener(this);
        title.setText("编辑地址");
        right.setOnClickListener(this);
        right.setText("保存");
        right.setTextSize(15f);
        ll_province = (LinearLayout) findViewById(R.id.ll_province);
        mArea = (WheelView) findViewById(R.id.wv_area);
        mCity = (WheelView) findViewById(R.id.wv_city);
        mProvince = (WheelView) findViewById(R.id.wv_province);
        et_address = (EditText) findViewById(R.id.et_address);
        et_address.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_address_detail = (EditText) findViewById(R.id.et_address_detail);
        et_tel = (EditText) findViewById(R.id.et_tel);
        ll_province = (LinearLayout) findViewById(R.id.ll_province);
        ll_province.setVisibility(View.GONE);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在加载....");
        sp_default = (Spinner) findViewById(R.id.sp_default);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, strings);
        sp_default.setAdapter(adapter);
        sp_default.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (sp_default.getSelectedItem().toString().equals("是")) {
                    def = "2";
                } else {
                    def = "1";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_delete = (Button) findViewById(R.id.btn_delete);
        btn_delete.setOnClickListener(this);

    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
            case R.id.right:
                // 保存方法操作
                saveAddress(isEdit());
                break;

            case R.id.et_address:
                ll_province.setVisibility(View.VISIBLE);
                break;
            // 删除地址
            case R.id.btn_delete:
                deleteAddress(id);
                break;
        }
    }


    /**
     * 根据当前的市，更新区WheelView的信息
     */
    private void updateAreas() {
        int pCurrent = mCity.getCurrentItem();
        LogUtils.i("info", "当前的位置-----》" + pCurrent);
        mCurrentCityName = mCitisDatasMap.get(mCurrentProviceName)[pCurrent];
        String[] areas = mAreaDatasMap.get(mCurrentCityName);

        if (areas == null) {
            areas = new String[]{""};
        }
        mArea.setViewAdapter(new ArrayWheelAdapter<String>(this, areas));
        mArea.setCurrentItem(0);
        mCurrentAreaName = areas[0];
    }

    /**
     * 根据当前的省，更新市WheelView的信息
     */
    private void updateCities() {
        int pCurrent = mProvince.getCurrentItem();
        mCurrentProviceName = mProvinceDatas[pCurrent];
        String[] cities = mCitisDatasMap.get(mCurrentProviceName);
        if (cities == null) {
            cities = new String[]{""};
        }
        mCity.setViewAdapter(new ArrayWheelAdapter<String>(this, cities));
        mCity.setCurrentItem(0);
        updateAreas();
    }

    /**
     * 解析整个Json对象，完成后释放Json对象的内存
     */
    private void initDatas() {
        try {
            mProvinceDatas = new String[allJson.length()];
            for (int i = 0; i < allJson.length(); i++) {
                JSONObject jsonP = allJson.getJSONObject(i);// 每个省的json对象
                String province = jsonP.getString("state");// 省名字

                mProvinceDatas[i] = province;

                JSONArray jsonCs;
                try {
                    jsonCs = jsonP.getJSONArray("cities");
                } catch (Exception e1) {
                    continue;
                }
                String[] mCitiesDatas = new String[jsonCs.length()];
                for (int j = 0; j < jsonCs.length(); j++) {
                    JSONObject jsonCity = jsonCs.getJSONObject(j);
                    String city = jsonCity.getString("city");// 市名字
                    mCitiesDatas[j] = city;
                    String[] mAreasDatas;
                    JSONArray areas = jsonCity.getJSONArray("areas");

                    mAreasDatas = new String[areas.length()];
                    for (int g = 0; g < areas.length(); g++) {
                        mAreasDatas[g] = areas.get(g).toString();
                    }
                    mAreaDatasMap.put(city, mAreasDatas);
                }
                mCitisDatasMap.put(province, mCitiesDatas);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mJsonObj = null;
    }

    /**
     * 从assert文件夹中读取省市区的json文件，然后转化为json对象
     */
    private void initJsonData() {
        try {
            StringBuffer sb = new StringBuffer();
            final InputStream is = getAssets().open("city.json");
            InputStreamReader isr = new InputStreamReader(is, "gbk");
            int len = -1;
            char[] buf = new char[1024];
            while ((len = isr.read(buf)) != -1) {
                sb.append(new String(buf, 0, len));
            }
            is.close();
            allJson = new JSONArray(sb.toString());
            LogUtils.i("info", "获得的JSONArray为--->" + allJson.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * change事件的处理
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == mProvince) {

            updateCities();
            address = mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentAreaName;
            et_address.setText(address);
            LogUtils.i("info", "地址---->" + address);
        } else if (wheel == mCity) {
            updateAreas();
            address = mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentAreaName;
            et_address.setText(address);
            LogUtils.i("info", "地址---->" + address);
        } else if (wheel == mArea) {
            mCurrentAreaName = mAreaDatasMap.get(mCurrentCityName)[newValue];
            address = mCurrentProviceName + " " + mCurrentCityName + " " + mCurrentAreaName;
            et_address.setText(address);
            LogUtils.i("info", "地址---->" + address);
        }
    }

    /**
     * 判断是否是从编辑界面传送过来的数据
     *
     * @return 是否有id
     */
    private boolean isEdit() {
        return getIntent().hasExtra("id");
    }


    /**
     * 保存地址
     *
     * @param isEdit 是否为编辑状态
     */
    private void saveAddress(final boolean isEdit) {

        final JSONObject jsonObject = new JSONObject();
        progressDialog.show();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("default", def);
            jsonObject.put("name", et_name.getText().toString());
            jsonObject.put("mobile", et_mobile.getText().toString());
            jsonObject.put("province", mCurrentProviceName);
            jsonObject.put("area", mCurrentAreaName);
            jsonObject.put("act", isEdit ? "edit_orderadder" : "add_consignee");
            jsonObject.put("city", mCurrentCityName);
            jsonObject.put("t", address);
            jsonObject.put("address", et_address_detail.getText().toString());
            if (isEdit) {
                jsonObject.put("id", id);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LogUtils.i("info", "保存地址信息发送的json数据----------->" + jsonObject.toString());

        HttpUtils.httpPostResult(AddressEditActivity.this, jsonObject, addHandler, Config.URL_ADDRESS);

    }

    /**
     * 查询地址信息
     *
     * @param id
     */
    private void selectAddress(String id) {

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act", "select_consignee");
            jsonObject.put("id", id);
        jsonObject.put("token", Config.getAccountToken(this));
        jsonObject.put("sign", Config.getSign(this));
        } catch (JSONException e) {
        e.printStackTrace();
        }
        HttpUtils.httpPostResult(AddressEditActivity.this, jsonObject, changeHandler, Config.URL_ADDRESS);
        }


    private void deleteAddress(String id) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act", "delete_consignee");
            jsonObject.put("id", id);
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.httpPostResult(AddressEditActivity.this, jsonObject, deleteHandler, Config.URL_ADDRESS);
    }

    }




