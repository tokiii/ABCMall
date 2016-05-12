package com.cbn.abcmall.activites;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cbn.abcmall.R;

/**
 * 添加地址
 * Created by Administrator on 2015/10/9.
 */
public class AddressAddActivity extends BaseActivity {

    private ImageView left;
    private ImageView right;
    private TextView title;

    @Override
    public void initWidget() {
        setContentView(R.layout.activity_address_add);
        initView();

    }

    private void initView() {
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        title = (TextView) findViewById(R.id.title);
        title.setText("添加地址");
        left.setOnClickListener(this);
        right.setVisibility(View.GONE);
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
