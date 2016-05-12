package com.cbn.abcmall.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 订单切换Fragment适配器
 * Created by lost on 2015/12/17.
 */
public class OrderFragmentPagerAdapter extends FragmentPagerAdapter {



    public OrderFragmentPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return null;
    }

    @Override
    public int getCount() {
        return 0;
    }
}
