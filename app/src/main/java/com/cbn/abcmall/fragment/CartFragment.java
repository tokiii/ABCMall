package com.cbn.abcmall.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.CartConfirmActivity;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.activites.LoginActivity;
import com.cbn.abcmall.activites.MainActivity;
import com.cbn.abcmall.activites.ProductDetailActivity;
import com.cbn.abcmall.adapter.MyExpandListViewAdapter;
import com.cbn.abcmall.bean.CartGood;
import com.cbn.abcmall.bean.CartShop;
import com.cbn.abcmall.bean.GetCart;
import com.cbn.abcmall.bean.GetCartPost;
import com.cbn.abcmall.bean.Store;
import com.cbn.abcmall.bean.Store_Order;
import com.cbn.abcmall.utils.DebugUtil;
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车界面展示
 * Created by Administrator on 2015/9/10.
 */
public class CartFragment extends Fragment implements MyExpandListViewAdapter.isDelete {
    private String token;
    private String sign;
    private Handler handler;
    private JSONObject jsonObject;
    private String json;
    private LinearLayout ll_hasdata;
    private RelativeLayout ll_nodata;
    private Button btn_go;
    private Button btn_submit; //结算按钮
    private ExpandableListView expandableListView;
    private TextView tv_price;
    private List<CartShop> groupList = new ArrayList<CartShop>();
    private MyExpandListViewAdapter adapter;
    private List<Object> gridList = new ArrayList<Object>();
    private CheckBox cb_all;
    private double price = 0;
    private double totalPrice = 0;
    private ArrayList<String> ids;
    private List<CartGood> cartGoods;
    private ProgressDialog progressDialog;
    private double totalPriceIncludeExpress = 0;
    public String pid = null;


    private ImageView left;// 返回键
    private TextView title;// 标题栏

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_cart, container, false);
        setUpViews(v);

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        progressDialog.dismiss();
                        json = (String) msg.obj;
                        if (json != null) {
                            groupList.clear();
                            totalPrice = 0;
                            getIntentData(false, json);
                            LogUtils.i("info", "获取到的totalPrice=====" + totalPrice);
                            init();
                        } else {
                            Intent loginIntent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(loginIntent);
                        }
                        break;
                }
            }


        };

        if (groupList.size() == 0) {
            goToEmpty();
        }

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    /**
     * 当fragment可见时再请求网络数据
     *
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

        LogUtils.i("info", "购物车界面是否可见----->" + isVisibleToUser);

        if (getUserVisibleHint()) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载....");
            progressDialog.show();
            token = new SharePreferenceUtil(getActivity(), Config.ACCOUNT).getStr(Config.ACCOUNT_TOKEN);
            sign = EncryptUtils.getMD5(token + Config.APP_KEY);
            getCartList(token, sign);
        }


    }

    @Override
    public void onResume() {
        super.onResume();
        if (pid!=null) {
            left.setVisibility(View.VISIBLE);
        }else {
            left.setVisibility(View.GONE);
        }
//        refresh();
    }

    /**
     * 获得购物车数据
     *
     * @param token
     * @param sign
     */
    private void getCartList(String token, String sign) {
        GetCartPost getCartPost = new GetCartPost();
        getCartPost.setSign(sign);
        getCartPost.setToken(token);
        String json = JsonUtils.objectToJson(getCartPost);
        jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.httpPostResult(getActivity(), jsonObject, handler, Config.URL_GET_CART);
    }

    private void setUpViews(View v) {
        expandableListView = (ExpandableListView) v.findViewById(R.id.expandableListView);
        cb_all = (CheckBox) v.findViewById(R.id.cb_all);
        tv_price = (TextView) v.findViewById(R.id.tv_price);
        ll_hasdata = (LinearLayout) v.findViewById(R.id.ll_hasdata);
        ll_nodata = (RelativeLayout) v.findViewById(R.id.ll_nodata);
        btn_go = (Button) v.findViewById(R.id.btn_go);
        btn_submit = (Button) v.findViewById(R.id.btn_commit);
        ids = new ArrayList<>();
        cartGoods = new ArrayList<>();
        title = (TextView) v.findViewById(R.id.title);
        title.setText("购物车");
        left = (ImageView) v.findViewById(R.id.left);
    }

    private void init() {
        adapter = new MyExpandListViewAdapter(getActivity(), groupList, CartFragment.this);
        adapter.setIsDelete(this);
        expandableListView.setAdapter(adapter);
        for (int i = 0; i < groupList.size(); i++) {
            expandableListView.expandGroup(i);
        }
        reFlashGridView();
        cb_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cb_all.isChecked()) {
                    ids.clear();
                    cartGoods.clear();
                    totalPrice = 0;
                    checkAll(true, groupList);
                    adapter.notifyDataSetChanged();
                } else {
                    ids.clear();
                    cartGoods.clear();
                    totalPrice = 0;
                    checkAll(false, groupList);
                    adapter.notifyDataSetChanged();

                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ids.size() != 0) {

                    Intent intent = new Intent(getActivity(), CartConfirmActivity.class);
                    intent.putExtra("goods", (Serializable) cartGoods);
                    intent.putExtra("totalPrice", String.valueOf(price == 0 ? totalPrice : price));
                    startActivity(intent);

                } else {
                    ToastUtils.TextToast(getActivity(), "请先选择商品", Toast.LENGTH_SHORT);
                }
            }
        });


        // 点击返回到商品详情界面
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBackToProductDetail();
            }
        });

    }

    public void reFlashGridView() {
        gridList.clear();
        ids.clear();
        cartGoods.clear();
        price = 0;
        for (CartShop group : groupList) {
            if (group.isChecked()) {
                gridList.add(group);
            } else {
                for (CartGood good : group.getShopList()) {
                    if (good.isChecked()) {
                        ids.add(good.getId());
                        cartGoods.add(good);
                        price = Double.valueOf(good.getPrice()) * Double.valueOf(good.getCount()) + price;
                    }
                }
            }
        }
        for (int i = 0; i < gridList.size(); i++) {
            Object o = gridList.get(i);
            if (o instanceof CartShop) {
                for (int j = 0; j < ((CartShop) o).getShopList().size(); j++) {
                    cartGoods.add(((CartShop) o).getShopList().get(j));
                    ids.add(((CartShop) o).getShopList().get(j).getId());
                    price = Double.valueOf(((CartShop) o).getShopList().get(j).getPrice()) * Double.valueOf(((CartShop) o).getShopList().get(j).getCount()) + price;
                }
            } else if (o instanceof CartGood) {

            }
        }
        tv_price.setText("¥" + price + "");

        LogUtils.i("info", "比较结果" + "price===" + price + "totalprice==" + totalPrice);
        if (totalPrice != 0 && price == totalPrice) {
            cb_all.setChecked(true);
        } else {
            cb_all.setChecked(false);
        }
        if (ids.size() != 0) {
            btn_submit.setText("结算(" + ids.size() + ")");
        } else {
            btn_submit.setText("结算");
        }
    }

    /**
     * 获取并解析数据
     *
     * @param flag 是否是全选check状态
     * @param json 获取的json数据
     */
    private void getIntentData(boolean flag, String json) {
        LogUtils.i("info", "得到的购物车数据——---------》" + json);
        DebugUtil.put(json, "购物车");
        GetCart getCart = (GetCart) JsonUtils.jsonToBean(json, GetCart.class);
        LogUtils.i("info", "得到的购物车的数据大小为------->" + getCart.getData().getCartlist().getCart());
        if (getCart.getData().getCartlist().getCart() == null) {
            goToEmpty();
            return;
        } else {
            ll_hasdata.setVisibility(View.VISIBLE);
            ll_nodata.setVisibility(View.GONE);
        }
        List<Store> carts = getCart.getData().getCartlist().getCart();
        Store store;
        Store_Order store_order;
        for (int i = 0; i < carts.size(); i++) {
            store = carts.get(i);
            String comany = store.getCompany();
            CartShop group = new CartShop();
            group.setName(comany);
            group.setSumprice(carts.get(i).getSumprice() + "");
            group.setChecked(flag);
            List<CartGood> shopList = new ArrayList<>();
            for (int j = 0; j < store.getProlist().size(); j++) {
                CartGood cartGood = new CartGood();
                cartGood.setChecked(flag);
                store_order = store.getProlist().get(j);
                cartGood.setName(store_order.getPname());
                cartGood.setImage(store_order.getPic());
                cartGood.setId(store_order.getId());
                cartGood.setCount(store_order.getQuantity());
                cartGood.setPrice(store_order.getPrice());
                cartGood.setCompany(store.getCompany());
                cartGood.setExpress(store.getExpress());
                cartGood.setSellerid(store.getSeller_id());
                cartGood.setColor(store_order.getSetmealname());
                totalPrice = Double.valueOf(store_order.getPrice()) * Double.valueOf(store_order.getQuantity()) + totalPrice;
                totalPriceIncludeExpress = Double.valueOf(store_order.getPrice()) * Double.valueOf(store_order.getQuantity()) + totalPriceIncludeExpress;
                shopList.add(cartGood);
                cartGood.addObserver(group);
                group.addObserver(cartGood);

            }
            group.setShopList(shopList);
            groupList.add(group);
        }

        LogUtils.i("info", "totalPrice============>" + totalPrice);

    }


    /**
     * 全选
     *
     * @param flag
     * @param groupList
     */
    private void checkAll(boolean flag, List<CartShop> groupList) {
        for (int i = 0; i < groupList.size(); i++) {
            CartShop cartShop = groupList.get(i);
            cartShop.setChecked(flag);

            for (int j = 0; j < cartShop.getShopList().size(); j++) {
                CartGood cartGood = cartShop.getShopList().get(j);
                cartGood.setChecked(flag);
                totalPrice = Double.valueOf(cartGood.getPrice()) * Double.valueOf(cartGood.getCount()) + totalPrice;
                if (flag) {
                    ids.add(cartGood.getId());
                    cartGoods.add(cartGood);
                }
            }
        }
        if (flag) {
            tv_price.setText("¥" + totalPrice + "");
            btn_submit.setText("结算(" + ids.size() + ")");
        } else {
            tv_price.setText("");
            btn_submit.setText("结算");
        }
    }

    @Override
    public void isDeleteVisible(List<CartShop> groupList, int groupPosition, boolean isVisible) {
        List<CartGood> shopList = groupList.get(groupPosition).getShopList();
        if (shopList.size() != 0) {
            for (int i = 0; i < shopList.size(); i++) {
                shopList.get(i).setIsVisible(isVisible);
            }
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 删除
     *
     * @param groupList
     */
    @Override
    public void deleted(List<CartShop> groupList, int childPosition, int groupPosition) {

        for (int i = 0; i < groupList.size(); i++) {
            totalPrice = totalPrice - Double.valueOf(groupList.get(groupPosition).getShopList().get(childPosition).getPrice()) *
                    Double.valueOf(groupList.get(groupPosition).getShopList().get(childPosition).getCount());
            reFlashGridView();
            adapter.notifyDataSetChanged();

            if (totalPrice == 0) { //如果总价为0时，证明购物车内无商品
                goToEmpty();
            } else {
                ll_nodata.setVisibility(View.GONE);
                ll_hasdata.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 购物车为空时进行的操作
     */
    private void goToEmpty() {
        ll_hasdata.setVisibility(View.GONE);
        ll_nodata.setVisibility(View.VISIBLE);
        btn_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(getActivity(), MainActivity.class);
                homeIntent.putExtra("page", 0);
                startActivity(homeIntent);
            }
        });
    }




    private void refresh() {
        LogUtils.i("info", "是否传入进了数据---------" + getArguments().isEmpty());
        if (!getArguments().isEmpty()) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载....");
            progressDialog.show();
            token = new SharePreferenceUtil(getActivity(), Config.ACCOUNT).getStr(Config.ACCOUNT_TOKEN);
            sign = EncryptUtils.getMD5(token + Config.APP_KEY);
            getCartList(token, sign);
        }
    }


    /**
     * 返回商品详情页
     */
    public void goBackToProductDetail() {
        Intent intent1 = new Intent((Activity)getActivity(), ProductDetailActivity.class);
        intent1.putExtra("pid", pid);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent1);
    }

}
