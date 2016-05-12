package com.cbn.abcmall.utils;

import android.app.ProgressDialog;

/**
 * 进度条工具类
 * Created by Administrator on 2015/9/28.
 */
public class ProgressDialogUtil {

    public static void createDialog(ProgressDialog progressDialog, String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }


    public static void dismissDialog(ProgressDialog progressDialog) {
        progressDialog.dismiss();
    }

}
