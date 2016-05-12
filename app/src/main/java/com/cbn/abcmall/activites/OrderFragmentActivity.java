package com.cbn.abcmall.activites;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.MyPagerAdapter;
import com.cbn.abcmall.fragment.OrderFragment;
import com.cbn.abcmall.views.MyViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单Fragment切换宿主Activity
 * Created by lost on 2015/12/14.
 */
public class OrderFragmentActivity extends BaseActivity {

    private MyViewPagerIndicator pagerIndicator;
    private ViewPager myViewPager;
    private MyPagerAdapter adapter;
    private List<Fragment> lists;
    private final String[] CONTENT_TITLE = new String[]{"全部", "待付款", "待发货", "待收货", "待评价"};
    private TextView title;
    private ImageView left;
    private List<String> datas;


    private int status;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_orderfragment);
        pagerIndicator = (MyViewPagerIndicator) findViewById(R.id.pagerIndicator);
        myViewPager = (ViewPager) findViewById(R.id.myViewpager);
        datas = new ArrayList<>();
        lists = new ArrayList<>();
        for (String k : CONTENT_TITLE) {
            datas.add(k);
        }

        pagerIndicator.setTabItemTitles(datas);
        title = (TextView) findViewById(R.id.title);
        title.setText("我的订单");
        left = (ImageView) findViewById(R.id.left);
        left.setOnClickListener(this);

        for (int i = 0; i < 5; i++) {
            OrderFragment fragment = new OrderFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("status", i);
            fragment.setArguments(bundle);
            lists.add(fragment);

        }

        adapter = new MyPagerAdapter(getSupportFragmentManager()) {

            @Override
            public int getCount() {
                return lists.size();
            }

            @Override
            public Fragment getItem(int i) {
                return lists.get(i);
            }
        };

        myViewPager.setAdapter(adapter);
        myViewPager.setOffscreenPageLimit(4);
        pagerIndicator.setViewPager(myViewPager, 0);

        if (getIntent().hasExtra("status")) {
            status = getIntent().getIntExtra("status", 0);
            pagerIndicator.setViewPager(myViewPager, status);
        }

    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.left:

                if (getIntent().hasExtra("payCancel")) {
                    Intent cartFragmentIntent = new Intent(OrderFragmentActivity.this, MainActivity.class);
                    // 如果从取消支付进来的，则返回到购物车界面
                    cartFragmentIntent.putExtra("payCancel", "payCancel");
                    startActivity(cartFragmentIntent);
                    finish();
                }else {
                    finish();
                }

                break;
        }

    }


}
