package com.cbn.abcmall.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * SharePreference工具类
 * Created by Administrator on 2015/9/10.
 */
public class SharePreferenceUtil {

    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String name;


    public SharePreferenceUtil(Context context, String name) {
        this.context = context;
        this.name = name;
        sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
    }

    /**
     * 保存数据到SharePreference
     * @param key
     * @param value
     */
    public void saveStr( String key, String value) {
        sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    /**
     * 从SharePreference获取数据
     * @param key
     * @return
     */
    public String getStr(String key) {
        sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        String str = sharedPreferences.getString(key, "");
        if (str == "") {
            return null;
        }
        return str;
    }

    /**
     * 获取所有数据
     * @param name
     * @return
     */
    public Map<String, ?> getAll(String name) {
        sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);

        return  sharedPreferences.getAll();
    }


    public boolean hasStr(String key) {
        sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        return sharedPreferences.contains(key);
    }

    /**
     * 清空SharePreference数据
     */
    public void clearStr() {
        sharedPreferences = context.getSharedPreferences(name, context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.clear();
    }
}
