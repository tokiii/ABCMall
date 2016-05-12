package com.cbn.abcmall.activites;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.MyPagerAdapter;
import com.cbn.abcmall.fragment.CartFragment;
import com.cbn.abcmall.fragment.HomeFragment;
import com.cbn.abcmall.fragment.MineFragment;
import com.cbn.abcmall.utils.AppManager;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.views.MyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment宿主Activity，包括首页、购物车、个人中心等界面展示
 * Created by Administrator on 2015/9/10.
 */
public class MainActivity extends BaseActivity implements View.OnClickListener {
    private MyViewPager myViewPager;
    private MyPagerAdapter myPagerAdapter;
    private List<Fragment> fragments;
    private HomeFragment homeFragment;
    private CartFragment cartFragment;
    private MineFragment mineFragment;
    private LinearLayout ll_cart;
    private LinearLayout ll_home;
    private LinearLayout ll_mine;
    private String pid;
    private ImageView iv_home, iv_cart, iv_mine;
    private int index = 0;

    @Override
    public void initWidget() {
        initView();

        LogUtils.i("info", "从取消订单回来进购物车------>" + getIntent().hasExtra("payCancel"));
        changeFromIntent(getIntent());

    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.ll_home:
                myViewPager.setCurrentItem(0, false);
                index = 0;
                iv_mine.setImageResource(R.mipmap.mine);
                iv_cart.setImageResource(R.mipmap.cart);
                iv_home.setImageResource(R.mipmap.home_h);
                break;
            case R.id.ll_cart:
                myViewPager.setCurrentItem(1, false);
                iv_home.setImageResource(R.mipmap.home);
                iv_mine.setImageResource(R.mipmap.mine);
                iv_cart.setImageResource(R.mipmap.cart_h);
                index = 1;
                break;
            case R.id.ll_mine:
                iv_home.setImageResource(R.mipmap.home);
                iv_mine.setImageResource(R.mipmap.mine_h);
                iv_cart.setImageResource(R.mipmap.cart);
                myViewPager.setCurrentItem(2, false);
                index = 2;
                break;
        }
    }

    /**
     * 初始化界面
     */
    private void initView() {
        setContentView(R.layout.activity_main);
        ll_cart = (LinearLayout) findViewById(R.id.ll_cart);
        ll_home = (LinearLayout) findViewById(R.id.ll_home);
        ll_mine = (LinearLayout) findViewById(R.id.ll_mine);
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_cart = (ImageView) findViewById(R.id.iv_cart);
        iv_mine = (ImageView) findViewById(R.id.iv_mine);
        ll_cart.setOnClickListener(this);
        ll_mine.setOnClickListener(this);
        ll_home.setOnClickListener(this);
        fragments = new ArrayList<>();
        myViewPager = (MyViewPager) findViewById(R.id.myViewpager);
        cartFragment = new CartFragment();
        mineFragment = new MineFragment();
        homeFragment = new HomeFragment();
        fragments.add(homeFragment);
        fragments.add(cartFragment);
        fragments.add(mineFragment);
        myPagerAdapter = new MyPagerAdapter(this, fragments, getSupportFragmentManager());
        myViewPager.setAdapter(myPagerAdapter);


        myViewPager.setOffscreenPageLimit(3);// 设置缓存fragment页数

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (index) {
                case 0:
                    ExitDialog(MainActivity.this).show();
                    break;
                case 1:
                    if (getIntent().hasExtra("fromProduct")) {
                        finish();
                        break;
                    }
                    myViewPager.setCurrentItem(0, false);
                    index = 0;
                    iv_mine.setImageResource(R.mipmap.mine);
                    iv_cart.setImageResource(R.mipmap.cart);
                    iv_home.setImageResource(R.mipmap.home_h);
                    break;
                case 2:
                    myViewPager.setCurrentItem(0, false);
                    index = 0;
                    iv_mine.setImageResource(R.mipmap.mine);
                    iv_cart.setImageResource(R.mipmap.cart);
                    iv_home.setImageResource(R.mipmap.home_h);
                    break;
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        switch (index) {
            case 0:
                finish();
                break;
            case 2:
                myViewPager.setCurrentItem(0, false);
                index = 0;
                iv_mine.setImageResource(R.mipmap.mine);
                iv_cart.setImageResource(R.mipmap.cart);
                iv_home.setImageResource(R.mipmap.home_h);
                break;
            case 3:
                myViewPager.setCurrentItem(0, false);
                index = 0;
                iv_mine.setImageResource(R.mipmap.mine);
                iv_cart.setImageResource(R.mipmap.cart);
                iv_home.setImageResource(R.mipmap.home_h);
                break;
        }
    }

    /**
     * 提示退出系统
     *
     * @param context
     * @return
     */
    private Dialog ExitDialog(final Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("系统信息");
        builder.setMessage("确定要退出程序吗?");
        builder.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        AppManager.getAppManager().AppExit(context);
                    }
                });
        builder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });
        return builder.create();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        changeFromIntent(intent);
    }


    private void changeFromIntent(final Intent intent) {

        if (intent.hasExtra("pid")) {
            pid = intent.getStringExtra("pid");
        }


        // 如果是从支付成功界面或者是从产品详情界面传过来则显示购物车界面
        if (intent.hasExtra("paySuccess") || intent.hasExtra("fromProduct")) {
            LogUtils.i("info", "从商品详情页跳转过来。");
            myViewPager.setCurrentItem(1);
            myPagerAdapter.notifyDataSetChanged();
            iv_home.setImageResource(R.mipmap.home);
            iv_mine.setImageResource(R.mipmap.mine);
            index = 1;
            iv_cart.setImageResource(R.mipmap.cart_h);
            cartFragment.pid = pid; //向fragment里面传值
            // 显示返回键并且返回到商品详情页
      /*      left.setVisibility(View.VISIBLE);
            left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    LogUtils.i("info", "5555555555555555555555555555555555555");
//                    AppManager.getAppManager().findActivity(ProductDetailActivity.class);
               Intent intent1 = new Intent(MainActivity.this, ProductDetailActivity.class);
                    intent1.putExtra("pid", pid);
                    startActivity(intent1);

                }
            });*/

        } else if (intent.hasExtra("page")) {

            if (intent.getIntExtra("page", 0) == 0) {
                myViewPager.setCurrentItem(0);
                index = 0;
                myViewPager.setCurrentItem(0, false);
                index = 0;
                iv_mine.setImageResource(R.mipmap.mine);
                iv_cart.setImageResource(R.mipmap.cart);
                iv_home.setImageResource(R.mipmap.home_h);
            } else if (intent.getIntExtra("page", 2) == 2) {
                iv_home.setImageResource(R.mipmap.home);
                iv_mine.setImageResource(R.mipmap.mine_h);
                iv_cart.setImageResource(R.mipmap.cart);
                myViewPager.setCurrentItem(2, false);
                index = 2;
            }

        }else if (intent.hasExtra("payCancel")) {
//            myViewPager.setCurrentItem(1);
//            iv_home.setImageResource(R.mipmap.home);
//            iv_mine.setImageResource(R.mipmap.mine);
//            index = 1;
//            iv_cart.setImageResource(R.mipmap.cart_h);
//            rl_head.setVisibility(View.VISIBLE);
//            title.setText("购物车");
//
//            AppManager.getAppManager().finishOthersActivity(MainActivity.class);
        }

    }



}
