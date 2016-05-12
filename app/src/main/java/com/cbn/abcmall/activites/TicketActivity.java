package com.cbn.abcmall.activites;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.MyPagerAdapter;
import com.cbn.abcmall.fragment.TicketMineFragment;
import com.cbn.abcmall.fragment.TicketOutFragment;
import com.cbn.abcmall.fragment.TicketUsedFragment;
import com.cbn.abcmall.views.TicketIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * 代金券界面
 * Created by lost on 2016/5/8.
 */
public class TicketActivity extends BaseActivity {


    private MyPagerAdapter adapter;
    private List<Fragment> lists;
    private ViewPager myViewPager;
    private TicketIndicator indicator;

    private final String[] CONTENT_TITLE = new String[]{"我的礼券", "过期礼券", "使用记录"};
    private List<String> datas;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_ticket);

        myViewPager = (ViewPager) findViewById(R.id.viewPager);
        indicator = (TicketIndicator) findViewById(R.id.pagerIndicator);
        datas = new ArrayList<>();
        lists = new ArrayList<>();
        for (String k : CONTENT_TITLE) {
            datas.add(k);
        }
        TicketMineFragment ticketMineFragment = new TicketMineFragment();
        TicketOutFragment ticketOutFragment = new TicketOutFragment();
        TicketUsedFragment ticketUsedFragment = new TicketUsedFragment();

        lists.add(ticketMineFragment);
        lists.add(ticketOutFragment);
        lists.add(ticketUsedFragment);

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

        indicator.setViewPager(myViewPager,0);


    }

    @Override
    public void widgetClick(View v) {

    }
}
