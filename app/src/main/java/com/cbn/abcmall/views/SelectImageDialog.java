package com.cbn.abcmall.views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.cbn.abcmall.R;

/**
 * Created by Administrator on 2015/9/30.
 */
public class SelectImageDialog extends Dialog implements View.OnClickListener{

    private Context context;
    private MyDialogClickListener listener;
    private Button btn_select_photo;
    private Button btn_select_gallery;

    @Override
    public void onClick(View v) {
        listener.Onclick(v);
    }


    public interface MyDialogClickListener{ void Onclick(View view);}

    public SelectImageDialog(Context context) {
        super(context);
        this.context = context;
    }

    public SelectImageDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.context = context;
    }

    protected SelectImageDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public SelectImageDialog(Context context, MyDialogClickListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_select_photo);

        btn_select_gallery = (Button) findViewById(R.id.btn_select_gallery);
        btn_select_photo = (Button) findViewById(R.id.btn_select_photo);
        btn_select_photo.setOnClickListener(this);
        btn_select_gallery.setOnClickListener(this);

    }
}
