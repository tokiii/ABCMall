package com.cbn.abcmall.utils;

import android.content.Context;

/**
 * PX DP转换工具
 * Created by lost on 15-12-29.
 */
public class DensityUtil {


    /**
     * 根据手机的分辨率 从dp转化成px
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从px转化成 dp
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
