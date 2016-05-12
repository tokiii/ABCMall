package com.cbn.abcmall.activites;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;

import com.cbn.abcmall.R;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.views.FlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 测试Activity
 * Created by Administrator on 2015/9/11.
 */
public class TestActivity extends Activity {

    private FlowLayout fl_color; //颜色
    private FlowLayout fl_size; //尺码
    private List<String> colors;
    private List<String> sizes;
    private Map<String, String> types;
    /**
     * checkbox专用
     */
    private List<CheckBox> colorLists;
    private List<CheckBox> sizeLists;

    private String id;
    private String color = "";
    private String size = "";
    private Map<String, String> stringmapMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        types = new HashMap<>();
        colors = new ArrayList<>();
        sizes = new ArrayList<>();
        stringmapMap = new HashMap<>();
        colorLists = new ArrayList<>();
        sizeLists = new ArrayList<>();
        fl_size = (FlowLayout) findViewById(R.id.fl_size);
        fl_color = (FlowLayout) findViewById(R.id.fl_color);
        stringmapMap.put("颜色+尺码", "ID");
        types.put("尺码1", "颜色1颜色2颜色3颜色4");
        types.put("尺码3", "颜色2颜色4颜色5颜色6");
        types.put("尺码2", "颜色3");
        types.put("尺码0", "颜色2");
        for (int i = 0; i < 6; i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("颜色" + i);
            checkBox.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            checkBox.setTextColor(Color.BLACK);
            checkBox.setTextSize(30);
            colors.add("颜色" + i);
            checkBox.setBackgroundResource(R.drawable.single_select_chk_bg);
            fl_color.addView(checkBox);
            colorLists.add(checkBox);
        }
        for (int i = 0; i < 4; i++) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText("尺码" + i);
            checkBox.setButtonDrawable(new ColorDrawable(Color.TRANSPARENT));
            checkBox.setTextColor(Color.BLACK);
            checkBox.setTextSize(30);
            checkBox.setBackgroundResource(R.drawable.single_select_chk_bg);
            fl_size.addView(checkBox);
            sizes.add("尺码" + i);
            sizeLists.add(checkBox);
        }
        for (int i = 0; i < colorLists.size(); i++) {
            final CheckBox checkBox = colorLists.get(i);
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {
                        colorLists.remove(checkBox);
                        color = checkBox.getText().toString();
                        LogUtils.i("info", "得到的颜色值为----->" + color);

                        // 对其他checkbox进行循环，变为unChecked状态
                        for (int i = 0; i < colorLists.size(); i++) {
                            colorLists.get(i).setChecked(false);
                        }
                        colorLists.add(checkBox);
                        //对尺码进行筛选
                        for (int i = 0; i < sizeLists.size(); i++) {
                            CheckBox checkbox = sizeLists.get(i);
                            String s = checkbox.getText().toString();
                            if (checkbox.isChecked())
                                size = checkbox.getText().toString();
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
                    } else {
                        for (int i = 0; i < sizeLists.size(); i++) {
                            if (!sizeLists.get(i).isChecked()) {
                                sizeLists.get(i).setChecked(false);
                                sizeLists.get(i).setClickable(true);
                                sizeLists.get(i).setTextColor(Color.BLACK);
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
                   if (checkbox.isChecked()) {
                       size = checkbox.getText().toString();
                       for (int i = 0; i < colorLists.size(); i++) {
                           CheckBox checkBox = colorLists.get(i);
                           String c = checkBox.getText().toString();
                           if (types.get(size).contains(c)) {
                               checkBox.setTextColor(Color.BLACK);
                               checkBox.setClickable(true);
                           } else {
                               checkBox.setTextColor(Color.GRAY);
                               checkBox.setClickable(false);
                           }
                       }
                       sizeLists.remove(checkbox);
                       for (int i = 0; i < sizeLists.size(); i++) {
                           sizeLists.get(i).setChecked(false);
                       }
                       sizeLists.add(checkbox);
                   } else {
                       for (int i = 0; i < colorLists.size(); i++) {
                           if (colorLists.get(i).isChecked()) {
                               color = colorLists.get(i).getText().toString();
                           }else {
                               colorLists.get(i).setTextColor(Color.BLACK);
                               colorLists.get(i).setClickable(true);
                           }
                       }
                   }
               }
           });
       }
    }

}

