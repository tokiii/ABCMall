package com.cbn.abcmall.activites;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.AddCartPost;
import com.cbn.abcmall.bean.GetProduct;
import com.cbn.abcmall.bean.Product;
import com.cbn.abcmall.bean.ProductProperty;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.utils.BitMapCache;
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.ToastUtils;
import com.cbn.abcmall.views.FlowLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商品属性选择界面
 * Created by Administrator on 2015/9/15.
 */
public class PropertyActivity extends BaseActivity implements View.OnClickListener {

    private FlowLayout fl_color; //颜色
    private FlowLayout fl_size; //尺码
    private List<String> colors;
    private List<String> sizes;
    private Map<String, String> types;
    private String image_url; //展示的图片属性
    private String product_id; //产品id
    private String product_property_id; //产品属性具体型号id
    private String price;
    private List<ProductProperty> productProperties;
    private List<CheckBox> colorLists;
    private List<CheckBox> sizeLists;
    private String color = "";
    private String size = "";
    private Map<String, String> stringmapMap;
    private String getJson; //从上一个界面传入的JSON数据
    private String stock;
    private TextView tv_color;
    private TextView tv_size;
    private TextView tv_stock;
    private TextView tv_price;
    private TextView tv_add;
    private TextView tv_reduce;
    private TextView tv_num;
    private Button btn_confirm;
    private NetworkImageView niv_image;
    private ImageLoader imageLoader;
    private RequestQueue requestQueue;
    private JSONObject jsonObject;
    private Handler handler;
    private ImageView tv_close;// 关闭界面
    private boolean hasProductType = true;// 判断产品是否有属性值

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_property);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);//透明状态栏

        initView();
        showAndSelect();
        LogUtils.i("info", "获得的image的URL地址为-------->" + image_url);
        niv_image.setImageUrl(image_url, imageLoader);
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);

                switch (msg.what) {
                    case 1:
                        Toast toast = Toast.makeText(getApplicationContext(), "加入购物清单成功", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.BOTTOM, 0, 50);
                        toast.show();
                        break;

                    case 10:
                        Config.Login(PropertyActivity.this);
                        addToCart(product_id, product_property_id);
                        break;
                }
            }
        };

    }

    /**
     * 初始化组件和数据
     */
    private void initView() {
        types = new HashMap<>();
        colors = new ArrayList<>();
        sizes = new ArrayList<>();
        stringmapMap = new HashMap<>();
        colorLists = new ArrayList<>();
        sizeLists = new ArrayList<>();
        productProperties = new ArrayList<>();
        fl_size = (FlowLayout) findViewById(R.id.fl_size);
        fl_color = (FlowLayout) findViewById(R.id.fl_color);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        tv_color = (TextView) findViewById(R.id.tv_color);
        tv_stock = (TextView) findViewById(R.id.tv_stock);
        tv_size = (TextView) findViewById(R.id.tv_size);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_reduce = (TextView) findViewById(R.id.tv_reduce);
        tv_price = (TextView) findViewById(R.id.tv_price);
        tv_add.setOnClickListener(this);
        tv_reduce.setOnClickListener(this);
        tv_close = (ImageView) findViewById(R.id.tv_close);
        tv_close.setOnClickListener(this);
        niv_image = (NetworkImageView) findViewById(R.id.niv_product);
        requestQueue = MyApplication.getInstance().getRequestQueue();
        imageLoader = new ImageLoader(requestQueue, new BitMapCache());
        btn_confirm.setOnClickListener(this);
        getJson = getIntent().getStringExtra("sendJson");
        LogUtils.i("info", "属性数据为----->" + getJson);
        loadData(getJson);
    }


    private void showAndSelect() {
        String size_colors = "";
        for (int i = 0; i < sizes.size(); i++) {
            for (int j = 0; j < productProperties.size(); j++) {
                String str = productProperties.get(j).getSetmeal();
                String[] split = str.split(",");
                if (str.contains("," + sizes.get(i)) && !size_colors.contains(split[0])) {
                    size_colors = split[0] + size_colors;
                    LogUtils.i("info", "得到的颜色数组为--------->" + size_colors);
                }
            }
            types.put(sizes.get(i), size_colors);
            size_colors = "";
        }

        // 布置颜色布局数据
        for (int i = 0; i < colors.size(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(colors.get(i));
            checkBox.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            checkBox.setTextColor(Color.BLACK);
            checkBox.setTextSize(20);
            checkBox.setBackgroundResource(R.drawable.single_select_chk_bg);
            fl_color.addView(checkBox);
            checkBox.setPadding(10, 0, 10, 0);
            colorLists.add(checkBox);
        }

        // 布置尺码数据
        for (int i = 0; i < sizes.size(); i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(sizes.get(i));
            checkBox.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            checkBox.setTextColor(Color.BLACK);
            checkBox.setTextSize(20);
            checkBox.setBackgroundResource(R.drawable.single_select_chk_bg);
            fl_size.addView(checkBox);
            sizeLists.add(checkBox);
            checkBox.setPadding(10, 0, 10, 0);
        }

        // 对颜色进行循环判断是否选中状态
        for (int i = 0; i < colorLists.size(); i++) {
            final CheckBox checkBox = colorLists.get(i);
            checkBox.setOnClickListener(new View.OnClickListener() {
                

                @Override
                public void onClick(View v) {
                    tv_num.setText("0");

                    // 如果颜色选中，则改变颜色的字体颜色并存储该值
                    if (checkBox.isChecked()) {
                        colorLists.remove(checkBox);
                        color = checkBox.getText().toString();
                        LogUtils.i("info", "得到的颜色值为----->" + color);
                        tv_color.setText("颜色为：" + color);
                        checkBox.setTextColor(Color.WHITE);
                        // 对其他checkbox进行循环，变为unChecked状态
                        for (int i = 0; i < colorLists.size(); i++) {
                            colorLists.get(i).setChecked(false);
                            colorLists.get(i).setTextColor(Color.BLACK);// 设置其他颜色为黑色
                        }
                        colorLists.add(checkBox);
                        //对尺码进行筛选
                        for (int i = 0; i < sizeLists.size(); i++) {
                            CheckBox checkbox = sizeLists.get(i);
                            String s = checkbox.getText().toString();
                            if (checkbox.isChecked()) {
                                checkbox.setTextColor(Color.WHITE);
                                size = checkbox.getText().toString();
                                tv_size.setText("尺码为：" + size);
                            } else {
                                size = "";
                                tv_size.setText("请选择尺码！");
                                checkbox.setTextColor(Color.BLACK);
                                clearPriceAndStock();
                            }

                            LogUtils.i("info", "获得的商品的颜色和尺寸为-----》" + color + size);

                            getProductInfo(color, size); //都选择之后获得商品信息

                            // 判断是否有库存
                            if (types.get(s).contains(color) && color != "") {
                                checkbox.setTextColor(Color.BLACK);
                                checkbox.setClickable(true);
                            } else {
                                checkbox.setTextColor(Color.GRAY);
                                checkbox.setChecked(false);
                                checkbox.setClickable(false);
                                size = "";
                            }
                        }
                        for (int i = 0; i < sizeLists.size(); i++) {
                            if (!sizeLists.get(i).isChecked()) {
                                sizeLists.get(i).setChecked(false);
                                sizeLists.get(i).setClickable(true);
                                sizeLists.get(i).setTextColor(Color.BLACK);
                            }else {
                                tv_size.setText("尺码为：" + sizeLists.get(i).getText());
                                sizeLists.get(i).setTextColor(Color.WHITE);// 如果选中则显示字体为白色
                            }
                        }
                    } else {
                        tv_color.setText("请选择颜色！");
                        checkBox.setTextColor(Color.BLACK);// 显示字体为黑色
                        clearPriceAndStock();
                        for (int i = 0; i < sizeLists.size(); i++) {
                            if (!sizeLists.get(i).isChecked()) {
                                sizeLists.get(i).setChecked(false);
                                sizeLists.get(i).setClickable(true);
                                sizeLists.get(i).setTextColor(Color.BLACK);
                            }else {
                                tv_size.setText("尺码为：" + sizeLists.get(i).getText());
                                sizeLists.get(i).setTextColor(Color.WHITE);// 如果选中则显示字体为白色
                            }
                        }
                        color = "";
                    }
                }
            });
        }
        /**
         * 对尺码进行循环遍历
         */
        for (int i = 0; i < sizeLists.size(); i++) {
            final CheckBox checkbox = sizeLists.get(i);
            checkbox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tv_num.setText("0");

                    // 如果尺码是被选中状态
                    if (checkbox.isChecked()) {
                        size = checkbox.getText().toString();
                        LogUtils.i("inf          o", "获得的商品的颜色和尺寸为-----》" + color + size);
                                getProductInfo(color, size);
                                tv_size.setText("尺码为：" + size);
                                checkbox.setTextColor(Color.WHITE);// 选中则设置为白色
                                for (int i = 0; i < colorLists.size(); i++) {
                                    CheckBox checkBox = colorLists.get(i);
                                    String c = checkBox.getText().toString();

                                    // 如果有库存则可点击否则不可点击
                                    if (types.get(size).contains(c)) {
                                        checkBox.setClickable(true);
                                        checkBox.setTextColor(Color.BLACK);
                                    } else {
                                        checkBox.setClickable(false);
                                        checkBox.setTextColor(Color.GRAY);
                                    }
                                }
                                sizeLists.remove(checkbox);
                                for (int i = 0; i < sizeLists.size(); i++) {
                                    sizeLists.get(i).setChecked(false);
                                    sizeLists.get(i).setTextColor(Color.BLACK);
                                }
                                sizeLists.add(checkbox);

                                for (int i = 0; i < colorLists.size(); i++) {
                                    if (colorLists.get(i).isChecked()) {
                                        color = colorLists.get(i).getText().toString();
                                        tv_color.setText("颜色为:" + color);
                                        colorLists.get(i).setTextColor(Color.WHITE);
                                    } else {
                                        colorLists.get(i).setTextColor(Color.BLACK);
                                        colorLists.get(i).setClickable(true);
                                        tv_color.setText("请选择颜色！");
                                    }
                                }
                            } else {
                                size = "";
                                tv_size.setText("请选择尺码！");
                                clearPriceAndStock();
                                checkbox.setTextColor(Color.BLACK);
                                for (int i = 0; i < colorLists.size(); i++) {
                                    if (colorLists.get(i).isChecked()) {
                                        color = colorLists.get(i).getText().toString();
                                        tv_color.setText("颜色为:" + color);
                                        colorLists.get(i).setTextColor(Color.WHITE);
                                    } else {
                                        colorLists.get(i).setTextColor(Color.BLACK);
                                        colorLists.get(i).setClickable(true);
                                        tv_color.setText("请选择颜色！");
                                    }
                        }
                    }
                }
            });
        }
    }

    /**
     * 给数组添加数据
     *
     * @param getJson
     */
    private void loadData(String getJson) {

        if (getJson == null) {

            ToastUtils.TextToast(this, "请稍等", Toast.LENGTH_SHORT);
            return;
        }
        GetProduct getProduct = (GetProduct) JsonUtils.jsonToBean(getJson, GetProduct.class);
        Product product = getProduct.getProduct();
        productProperties = product.getProperty();
        ProductProperty productProperty = null;
        String color; //局部颜色变量
        String size; //局部尺寸变量
        String[] strings;
        product_id = product.getId(); //获得pid
        image_url = product.getPic(); //获得网络图片的地址
        if (productProperties != null) {
            hasProductType = true;
            for (int i = 0; i < productProperties.size(); i++) {
                productProperty = productProperties.get(i);
                strings = productProperty.getSetmeal().split(","); //分割尺码和颜色


                LogUtils.i("info", "得到的属性值为------》" + strings);

                color = strings[0];
                size = strings[1];
                // 添加颜色到数组
                if (!colors.contains(color)) {
                    colors.add(color);
                }
                // 添加尺码到数组
                if (!sizes.contains(size)) {
                    sizes.add(size);
                }
                stringmapMap.put(productProperty.getSetmeal(), productProperty.getId()); //把颜色和尺码放到map集合中
            }
        } else  {
            hasProductType = false;
        }

    }


    /**
     * 根据颜色和尺码获取商品的基本信息
     *
     * @param color
     * @param size
     */
    private void getProductInfo(String color, String size) {
        product_property_id = stringmapMap.get(color + "," + size);
        ProductProperty property;
        for (int i = 0; i < productProperties.size(); i++) {
            property = productProperties.get(i);
            if (product_property_id == property.getId()) {
                price = property.getPrice();
                stock = property.getStock();
                tv_price.setText("¥" + price);
                tv_stock.setText("库存为：" + stock);
                tv_price.setTextColor(Color.RED);
                tv_stock.setTextColor(Color.BLACK);
                LogUtils.i("info", "获得的商品信息为------->" + price + stock);
            }
        }
    }

    @Override
    public void initWidget() {

    }

    @Override
    public void widgetClick(View v) {

    }

    @Override
    public void onClick(View v) {
        String num = tv_num.getText().toString();
        int i = Integer.valueOf(num);
        switch (v.getId()) {
            case R.id.btn_confirm:
                if (color == "" || size == "") {
                    Toast toast = Toast.makeText(PropertyActivity.this, "请选择产品属性", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 50);
                    toast.show();
                    return;
                }else if (Integer.valueOf(tv_num.getText().toString()) < 1) {
                    Toast toast = Toast.makeText(PropertyActivity.this, "商品数量不能小于1", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 50);
                    toast.show();
                    return;
                }
                addToCart(product_id, product_property_id);
                tv_num.setText("0");
                break;
            case R.id.tv_add:
                tv_num.setText(String.valueOf(i + 1));
                break;
            case R.id.tv_reduce:
                if (i > 1) {
                    tv_num.setText(String.valueOf(i - 1));
                } else {
                    Toast toast = Toast.makeText(PropertyActivity.this, "商品数量不能小于1", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 50);
                    toast.show();
                }
                break;

            case R.id.tv_close:
                finish();
                break;
        }
    }

    /**
     * 清空价格和库存
     */
    private void clearPriceAndStock() {
        tv_price.setText("");
        tv_stock.setText("");
    }

    /**
     * 把选择的商品添加到购物车
     */
    private void addToCart(String product_id, String product_property_id) {
        SharePreferenceUtil sharePreference = new SharePreferenceUtil(PropertyActivity.this, Config.ACCOUNT);
        String token = sharePreference.getStr(Config.ACCOUNT_TOKEN);
        AddCartPost addCartPost = new AddCartPost();
        addCartPost.setNum(tv_num.getText().toString());
        addCartPost.setPid(product_id);
        addCartPost.setSid(product_property_id);
        addCartPost.setAdd_cart("true");
        addCartPost.setToken(token);
        addCartPost.setSign(EncryptUtils.getMD5(token + Config.APP_KEY));
        String json = JsonUtils.objectToJson(addCartPost);

        LogUtils.i("info", "加入购物车发送的JSON请求-------->" + json);
       jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.httpPostResult(PropertyActivity.this, jsonObject, handler, Config.URL_ADD_TO_CART);

    }



    @Override
    public void finish() {
        super.finish();
        this.overridePendingTransition(R.anim.push_bottom_out,0);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
