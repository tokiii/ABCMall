package com.cbn.abcmall.activites;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.MyPagerAdapter;
import com.cbn.abcmall.fragment.CouponAddFragment;
import com.cbn.abcmall.fragment.CouponMineFragment;
import com.cbn.abcmall.fragment.CouponUseHistoryFragment;
import com.cbn.abcmall.views.CouponIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠券界面
 * Created by lost on 2016/5/6.
 */
public class CouponActivity extends BaseActivity {

    private CouponIndicator indicator;
    private ViewPager myViewPager;
    private MyPagerAdapter adapter;
    private List<Fragment> lists;
    private ImageView iv_left;
    private TextView tv_title;
    private final String[] CONTENT_TITLE = new String[]{"我的优惠券", "添加优惠券", "使用记录"};
    private List<String> datas;


    @Override
    public void initWidget() {
        setContentView(R.layout.activity_coupon);
        tv_title = (TextView) findViewById(R.id.title);
        tv_title.setText("我的优惠券");
        iv_left = (ImageView) findViewById(R.id.left);
        iv_left.setOnClickListener(this);
        myViewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (CouponIndicator) findViewById(R.id.pagerIndicator);
        datas = new ArrayList<>();
        lists = new ArrayList<>();
        for (String k : CONTENT_TITLE) {
            datas.add(k);
        }
        CouponMineFragment couponMineFragment = new CouponMineFragment();
        CouponAddFragment couponAddFragment = new CouponAddFragment();
        CouponUseHistoryFragment couponUseHistoryFragment = new CouponUseHistoryFragment();

        lists.add(couponMineFragment);
        lists.add(couponAddFragment);
        lists.add(couponUseHistoryFragment);

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

        indicator.setViewPager(myViewPager, 0);

    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.left:
                finish();
                break;
        }

    }
}
