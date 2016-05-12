package com.cbn.abcmall.views;

/**
 * 对外的ViewPager的回调接口
 * Created by lost on 2015/12/18.
 */
public interface PageChangeListener {

    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);

    public void onPageSelected(int position);

    public void onPageScrollStateChanged(int state);

}
