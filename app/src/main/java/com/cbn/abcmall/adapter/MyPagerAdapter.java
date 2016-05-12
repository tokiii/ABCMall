package com.cbn.abcmall.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 自定义PagerAdapter
 * Created by Administrator on 2015/9/10.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragments;
    private Context context;

    public MyPagerAdapter(Context context, List<Fragment> fragments, FragmentManager fragmentManager) {
        super(fragmentManager);
        this.context = context;
        this.fragments = fragments;
    }

    public MyPagerAdapter(FragmentManager fm) {
        super(fm);
    }


    @Override
    public Fragment getItem(int i) {
        return fragments.get(i);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

}
