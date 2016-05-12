package com.cbn.abcmall.activites;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.ProductCollectAdapter;
import com.cbn.abcmall.adapter.ShopCollectAdapter;
import com.cbn.abcmall.bean.GetCollectProduct;
import com.cbn.abcmall.bean.GetCollectProductDetail;
import com.cbn.abcmall.bean.GetCollectShop;
import com.cbn.abcmall.bean.GetCollectShopDetail;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * 收藏界面
 * Created by Administrator on 2015/10/14.
 */
public class CollectActivity extends BaseActivity {

    private ImageView left;
    private ImageView right;
    private TextView title;
    private Handler handler;
    private RecyclerView rv_collect;
    private ShopCollectAdapter shopCollectAdapter;
    private ProductCollectAdapter productCollectAdapter;
    private String type;
    private Handler cancelHandler;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_collect);
        initView();
        getData(type);
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String shop_json = (String) msg.obj;
                        GetCollectShop getCollectShop = (GetCollectShop) JsonUtils.jsonToBean(shop_json, GetCollectShop.class);
                        List<GetCollectShopDetail> lists = getCollectShop.getList();
                        LogUtils.i("info", "得到的收藏列表长度为------>" + lists.size());
                        shopCollectAdapter = new ShopCollectAdapter(CollectActivity.this, lists, type);
                        rv_collect.setAdapter(shopCollectAdapter);
                        break;

                    case 2:
                        String product_json = (String) msg.obj;
                        GetCollectProduct getCollectProduct = (GetCollectProduct) JsonUtils.jsonToBean(product_json, GetCollectProduct.class);
                        List<GetCollectProductDetail> productList = getCollectProduct.getList();
                        productCollectAdapter = new ProductCollectAdapter(CollectActivity.this, productList, type);
                        rv_collect.setAdapter(productCollectAdapter);
                        break;
                }
            }
        };


        cancelHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:

                        ToastUtils.TextToast(CollectActivity.this, "取消收藏成功！", Toast.LENGTH_SHORT);

                        break;
                }
            }
        };
    }

    /**
     * 初始化组件
     */
    private void initView() {
        type = getIntent().getStringExtra("type");
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        title = (TextView) findViewById(R.id.title);
        left.setOnClickListener(this);
        right.setVisibility(View.GONE);
        rv_collect = (RecyclerView) findViewById(R.id.rv_collect);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rv_collect.setLayoutManager(layoutManager);
        rv_collect.setHasFixedSize(true);
        switch (type) {
            case "product":
                title.setText("收藏商品");
                break;
            case "shop":
                title.setText("收藏店铺");
                break;
        }

    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.left:

                finish();

                break;
        }

    }


    private void getData(final String type) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("type", type);
            jsonObject.put("page", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = HttpUtils.JsonPost(CollectActivity.this, Config.URL_COLLECT_LIST, jsonObject);
                String json = JsonUtils.decodeUnicode(str);
                LogUtils.i("info", "得到的收藏信息为------->" + json);
                Message message = new Message();
                message.obj = json;
                switch (type) {
                    case "shop":
                        message.what = 1;
                        break;
                    case "product":
                        message.what = 2;
                        break;
                }
                handler.sendMessage(message);
            }
        }).start();
    }

}
