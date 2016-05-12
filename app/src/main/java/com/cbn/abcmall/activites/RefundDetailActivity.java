package com.cbn.abcmall.activites;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.adapter.RefundDetailAdapter;
import com.cbn.abcmall.bean.GetRefundInfo;
import com.cbn.abcmall.bean.GetRefundTalk;
import com.cbn.abcmall.bean.Getfastmail;
import com.cbn.abcmall.utils.BitmapUtil;
import com.cbn.abcmall.utils.DebugUtil;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.LogUtils;
import com.cbn.abcmall.utils.ToastUtils;
import com.cbn.abcmall.views.SelectPicPopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 退款详情页
 * Created by Administrator on 2015/10/13.
 */
public class RefundDetailActivity extends BaseActivity {
    private ImageView left;
    private ImageView right;
    private TextView title;
    private String token;
    private String sign;
    private Handler getCommonHandler;
    private String orderId;// 订单编号
    private String refundId;// 退款编号
    private String sellerId;// 卖家id
    private RefundDetailAdapter adapter;
    private ListView lv_talk;
    private TextView tv_status;
    private List<String> logistcs;// 保存的物流信息
    private String status;// 传过来的状态
    private RelativeLayout rl_refund_agree;// 同意退款
    private LinearLayout ll_refund_close;// 退款关闭
    private LinearLayout ll_refunding;// 退款中
    private RelativeLayout rl_refund_change_btn;// 修改以及取消退款按钮
    private RelativeLayout rl_talk;// 发表留言上传凭证按钮
    private EditText et_message;// 留言内容
    private EditText et_number;// 快递单号
    private Button btn_commit;// 发表留言
    private Button btn_upload;// 上传凭证
    private Button btn_change;// 修改申请
    private Button btn_cancel; // 取消退款
    private Button btn_change_number;// 修改快递单号
    private Spinner sp_logistic;// 物流
    private String logistic_name;// 获得的物流名称
    private LinearLayout ll_wait_ll_wait_confirm;// 等待卖家确认
    private String id;
    private String reason;
    private String price;
    private TextView tv_name;// 联系人名字
    private TextView tv_phone;// 联系人电话
    private TextView tv_address;// 退货地址
    @Override
    public void initWidget() {
        setContentView(R.layout.activity_refunddetail);
        initView();
    }

    private void initView() {
        left = (ImageView) findViewById(R.id.left);
        right = (ImageView) findViewById(R.id.right);
        title = (TextView) findViewById(R.id.title);
        tv_status = (TextView) findViewById(R.id.tv_status);
        rl_refund_agree = (RelativeLayout) findViewById(R.id.rl_refund_agree);
        ll_refund_close = (LinearLayout) findViewById(R.id.ll_refund_close);
        ll_refunding = (LinearLayout) findViewById(R.id.ll_refunding);
        rl_refund_change_btn = (RelativeLayout) findViewById(R.id.rl_change_refund_btn);
        rl_talk = (RelativeLayout) findViewById(R.id.rl_talk);
        et_message = (EditText) findViewById(R.id.et_message);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        et_message = (EditText) findViewById(R.id.et_message);
        et_number = (EditText) findViewById(R.id.et_number);
        btn_change = (Button) findViewById(R.id.btn_change);
        sp_logistic = (Spinner) findViewById(R.id.sp_logistic);
        btn_cancel = (Button) findViewById(R.id.btn_cancel);
        btn_upload = (Button) findViewById(R.id.btn_upload);
        ll_wait_ll_wait_confirm = (LinearLayout) findViewById(R.id.ll_wait_confirm);
        iv_evidence = (ImageView) findViewById(R.id.iv_evidence);
        btn_change_number = (Button) findViewById(R.id.btn_change_number);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        tv_address = (TextView) findViewById(R.id.tv_address);
        btn_change_number.setOnClickListener(this);
        btn_change.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        btn_commit.setOnClickListener(this);
        btn_upload.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        left.setOnClickListener(this);
        right.setVisibility(View.GONE);
        title.setText("退款详情");
        sign = Config.getSign(this);
        token = Config.getAccountToken(this);
        orderId = getIntent().getStringExtra("orderId");
        refundId = getIntent().getStringExtra("refundId");
        LogUtils.i("info", "退款ID-------》" + orderId);
        getCommonList(refundId);
        lv_talk = (ListView) findViewById(R.id.lv_talk);
        switch (getIntent().getStringExtra("status")) {
            case "0":
                tv_status.setText("退货关闭");
                break;
            case "1":
                tv_status.setText("审核中");
                ll_refunding.setVisibility(View.VISIBLE);
                rl_refund_change_btn.setVisibility(View.VISIBLE);
                rl_talk.setVisibility(View.VISIBLE);
                break;
            case "2":
                tv_status.setText("审核成功，卖家同意退货");
                rl_refund_agree.setVisibility(View.VISIBLE);
                rl_refund_change_btn.setVisibility(View.VISIBLE);
                ll_wait_ll_wait_confirm.setVisibility(View.VISIBLE);
                break;
            case "3":
                tv_status.setText("买家发货");
                rl_refund_change_btn.setVisibility(View.VISIBLE);
                rl_talk.setVisibility(View.VISIBLE);
                ll_wait_ll_wait_confirm.setVisibility(View.VISIBLE);
                break;
            case "4":
                tv_status.setText("拒绝退货");
                rl_refund_change_btn.setVisibility(View.VISIBLE);
                break;
            case "5":
                tv_status.setText("退货成功");
                break;
        }
    }

    @Override
    public void widgetClick(View v) {
        switch (v.getId()) {
            case R.id.left:
                finish();
                break;

            // 提交评论
            case R.id.btn_commit:
                commitTalk(refundId);

                et_message.clearFocus();
                et_message.setText("");
                break;

            // 修改申请
            case R.id.btn_change:
                Intent refundIntent = new Intent(RefundDetailActivity.this, RefundActivity.class);
                refundIntent.putExtra("orderId", orderId);
                refundIntent.putExtra("goodStatus", "1");
                refundIntent.putExtra("status", "2");
                refundIntent.putExtra("id", id);
                refundIntent.putExtra("price", price);
                refundIntent.putExtra("changeRefund", "changeRefund");
                refundIntent.putExtra("reason", reason);
                refundIntent.putExtra("sellerId", sellerId);
                startActivity(refundIntent);

                break;

            // 取消退款
            case R.id.btn_cancel:
                new AlertDialog.Builder(this)
                        .setTitle("取消订单")
                        .setMessage("是否取消订单")
                        .setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialoginterface, int i) {
                                        cancelRefund(refundId);
                                        getCommonList(refundId);
                                    }
                                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
                break;

            // 上传凭证
            case R.id.btn_upload:
                uploadImg();
                break;

            // 修改快递单号
            case R.id.btn_change_number:
                commitExpress(refundId);
                break;
        }

    }


    /**
     * 获取订单评论列表
     *
     * @param refundId
     */
    private void getCommonList(String refundId) {
        getCommonHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        String json = (String) msg.obj;
                        DebugUtil.put(json, "info");// 保存log信息到文本文档
                        GetRefundInfo getRefundInfo = (GetRefundInfo) JsonUtils.jsonToBean(json, GetRefundInfo.class);
                        id = getRefundInfo.getRefund_info().getId();
                        price = getRefundInfo.getRefund_info().getPrice();
                        reason = getRefundInfo.getRefund_info().getReason();
                        sellerId = getRefundInfo.getRefund_info().getSeller_id();
                        List<Getfastmail> getfastmails = getRefundInfo.getFastmail();
                        logistcs = new ArrayList<>();
                        if (getfastmails != null) {
                            for (int i = 0; i < getfastmails.size(); i++) {
                                logistcs.add(getfastmails.get(i).getCompany());
                            }

                            tv_address.setText("地址：" + getRefundInfo.getRefund_info().getReturn_address());// 返款地址
                            tv_name.setText("姓名：" + getRefundInfo.getRefund_info().getUser());// 返款联系人
                            tv_phone.setText("电话：" + getRefundInfo.getRefund_info().getReturn_mobile());// 联系电话

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(RefundDetailActivity.this, android.R.layout.simple_spinner_item, logistcs);
                            sp_logistic.setAdapter(arrayAdapter);
                            sp_logistic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                    logistic_name = sp_logistic.getSelectedItem().toString();
                                    LogUtils.i("info", "获得的物流名称----->" + logistic_name);
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {

                                }
                            });
                        }
                        List<GetRefundTalk> list = getRefundInfo.getTalk();
                        adapter = new RefundDetailAdapter(RefundDetailActivity.this, list);
                        lv_talk.setAdapter(adapter);
                        break;
                }
            }
        };

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", token);
            jsonObject.put("sign", sign);
            jsonObject.put("refund_id", refundId);
            jsonObject.put("act", "getrefund");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String str = HttpUtils.JsonPost(RefundDetailActivity.this, Config.URL_REFUND_ORDER, jsonObject);
                String json = JsonUtils.decodeUnicode(str);
                Message message = new Message();
                message.what = 1;
                message.obj = json;
                LogUtils.i("info", "获得的退款详情页----->" + json);
                getCommonHandler.sendMessage(message);
            }
        }).start();

    }

    Handler cancelHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    LogUtils.i("info", "退款申请返回的数据为------>" + msg.obj);
                    String json = (String) msg.obj;
                    if (json.contains("由于退单状态不对,关闭退单失败")){
                        ToastUtils.TextToast(RefundDetailActivity.this, "此状态不可取消申请！", Toast.LENGTH_SHORT);
                        return;
                    }

                    Intent allRefundOrderIntent = new Intent(RefundDetailActivity.this, AllOrdersActivity.class);
                    setResult(RESULT_OK, allRefundOrderIntent);
                    finish();
                    break;
            }
        }
    };

    /**
     * 取消退款申请
     *
     * @param refundId
     */
    private void cancelRefund(String refundId) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("refund_id", refundId);
            jsonObject.put("act", "close");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.httpPostResult(RefundDetailActivity.this, jsonObject, cancelHandler, Config.URL_REFUND_ORDER);
    }

    Handler commitHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    ToastUtils.TextToast(RefundDetailActivity.this, "提交留言成功！", Toast.LENGTH_SHORT);
                    getCommonList(refundId);
                    break;
            }
        }
    };

    /**
     * 提交评论
     *
     * @param refundId
     */
    private void commitTalk(final String refundId) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("msg", et_message.getText().toString());
            jsonObject.put("pic", pic);
            jsonObject.put("act", "review");
            jsonObject.put("refund_id", refundId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LogUtils.i("info", "提交评论的json ----->" + jsonObject.toString());
        HttpUtils.httpPostResult(RefundDetailActivity.this, jsonObject, commitHandler, Config.URL_REFUND_ORDER);

    }

    /**
     * 上传凭证
     */
    private void uploadImg() {
        selectPicPopupWindow = new SelectPicPopupWindow(RefundDetailActivity.this, itemOnclick);
        selectPicPopupWindow.showAtLocation(RefundDetailActivity.this.findViewById(R.id.ll_refund_detail), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    private SelectPicPopupWindow selectPicPopupWindow;
    private final int SELECT_PICTURE = 102;
    private final int SELECT_CAMERA = 101;
    private String pic = "";
    private Uri imageUri;
    private Bitmap bitmap;
    private ImageView iv_evidence;
    private View.OnClickListener itemOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            selectPicPopupWindow.dismiss();
            switch (v.getId()) {
                case R.id.btn_take_photo:
                    Intent photoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                    String fileName = dateFormat.format(new Date());
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.TITLE, fileName);
                    imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    photoIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                    startActivityForResult(photoIntent, SELECT_CAMERA);
                    break;
                case R.id.btn_select_photo:
                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
                    galleryIntent.setType("image/*");
                    startActivityForResult(Intent.createChooser(galleryIntent, "选择图片"), SELECT_PICTURE);
                    break;

            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            bitmap = BitmapFactory.decodeStream(null);
            Uri uri = null;
            if (data != null && data.getData() != null) {
                uri = data.getData();
            } else {
                uri = imageUri;
            }
// 一些机型无法从getData中获取uri，则需手动指定拍照后存储照片的Uri
            LogUtils.i("info", "得到的返回数据为----->" + uri);
            ContentResolver cr = this.getContentResolver();
            if (bitmap != null) {
                bitmap.recycle();
            } else {
                try {
                    bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                    bitmap = BitmapUtil.comp(bitmap);
                    iv_evidence.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩

                byte[] bytes = bos.toByteArray();
                pic = Base64.encodeToString(bytes, Base64.NO_WRAP);
            }
        }
    }

    Handler commitExpressHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {

                case 1:
                    LogUtils.i("info", "修改快递单号的界面------------>" + msg.obj);
                    ToastUtils.TextToast(RefundDetailActivity.this, "成功发送快递单号！", Toast.LENGTH_SHORT);
                    break;

                case 10:
                    Config.Login(RefundDetailActivity.this);
                    commitExpress(refundId);
                    break;
            }
        }
    };

    /**
     * 提交快递单号
     *
     * @param refundId
     */
    private void commitExpress(String refundId) {

        if (TextUtils.isEmpty(et_number.getText())) {
            ToastUtils.TextToast(this, "请填写快递单号！", Toast.LENGTH_SHORT);
            return;
        }

        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("act", "send_express");
            jsonObject.put("token", Config.getAccountToken(this));
            jsonObject.put("sign", Config.getSign(this));
            jsonObject.put("refund_id", refundId);
            jsonObject.put("logistics_name", logistic_name);
            jsonObject.put("invoice_no", et_number.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HttpUtils.httpPostResult(RefundDetailActivity.this, jsonObject, commitExpressHandler, Config.URL_REFUND_ORDER);
    }

}
