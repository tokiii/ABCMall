package com.cbn.abcmall.activites;

import android.content.ContentResolver;
import android.content.ContentValues;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cbn.abcmall.R;
import com.cbn.abcmall.bean.RefundPost;
import com.cbn.abcmall.utils.BitmapUtil;
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
import java.util.Date;

/**
 * 退货界面
 * Created by Administrator on 2015/9/29.
 */
public class RefundActivity extends BaseActivity {

    private ImageView left;
    private ImageView right;
    private TextView title;
    private Button btn_upload_image;
    private EditText et_reason;
    private TextView tv_refund_type; // 退款类型
    private TextView tv_refund_amount; //退款金额
    private final int SELECT_PICTURE = 102;
    private final int SELECT_CAMERA = 101;
    private Bitmap bitmap;
    private ImageView iv_evidence;
    private Uri imageUri;
    private Button btn_confirm;
    private String order_id;
    private String id;
    private String price;
    private String reason;
    private String pic = "";
    private String status;
    private String goods_status;
    private String seller_id;
    private JSONObject jsonObject;
    private Handler handler;
    private SelectPicPopupWindow picPopupWindow;

    private View.OnClickListener itemOnclick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            picPopupWindow.dismiss();
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
    public void initWidget() {
        setContentView(R.layout.activity_refund);
        initView();

        Intent intent = getIntent();


        order_id = intent.getStringExtra("orderId");
        goods_status = intent.getStringExtra("goodStatus");
        status = intent.getStringExtra("status");
        id = intent.getStringExtra("id");
        price = intent.getStringExtra("price");
        if (intent.hasExtra("changeRefund")) {
            reason = intent.getStringExtra("reason");
            et_reason.setText(reason);
        }
        seller_id = intent.getStringExtra("sellerId");
        LogUtils.i("info", "get the sellerId-->" + seller_id);

        if (status.equals("1")) {
            tv_refund_type.setText("仅退款");
        } else {
            tv_refund_type.setText("退款退货");
        }
        tv_refund_amount.setText(price);


        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String json = (String) msg.obj;
                switch (msg.what) {
                    case 1:
                        if (!getIntent().hasExtra("changeRefund")) {
                            try {
                                JSONObject jsonObject = new JSONObject(json);
                                if (jsonObject.getInt("statu") == 1) {
                                    ToastUtils.TextToast(RefundActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT);
//                                    Intent waitDeliverIntent = new Intent(RefundActivity.this, AllOrdersActivity.class);
//                                    waitDeliverIntent.putExtra("status", 4);
//                                    startActivity(waitDeliverIntent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            ToastUtils.TextToast(RefundActivity.this, "退货修改成功!", Toast.LENGTH_SHORT);
                            finish();
                        }
                        break;
                }

            }
        };
    }

    private void initView() {
        left = (ImageView) findViewById(R.id.left);
        left.setOnClickListener(this);
        right = (ImageView) findViewById(R.id.right);
        title = (TextView) findViewById(R.id.title);
        btn_upload_image = (Button) findViewById(R.id.btn_upload_image);
        btn_upload_image.setOnClickListener(this);
        iv_evidence = (ImageView) findViewById(R.id.iv_evidence);
        right.setVisibility(View.GONE);
        title.setText("申请退款");
       picPopupWindow = new SelectPicPopupWindow(this, new View.OnClickListener() {
           @Override
           public void onClick(View v) {

           }
       });
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm");
        String fileName = dateFormat.format(new Date());
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, fileName);
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        et_reason = (EditText) findViewById(R.id.et_reason);
        tv_refund_amount = (TextView) findViewById(R.id.tv_refund_amount);
        tv_refund_type = (TextView) findViewById(R.id.tv_refund_type);

    }

    @Override
    public void widgetClick(View v) {

        switch (v.getId()) {
            case R.id.left:
                finish();
                break;

            case R.id.btn_upload_image:
                picPopupWindow = new SelectPicPopupWindow(this, itemOnclick);
                picPopupWindow.showAtLocation(RefundActivity.this.findViewById(R.id.rl_refund), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
                break;

            case R.id.btn_confirm:
                if (TextUtils.isEmpty(et_reason.getText())) {
                    ToastUtils.TextToast(this, "请填写退款原因！", Toast.LENGTH_SHORT);
                    return;
                } else {
                    reason = et_reason.getText().toString();
                }
                refund();
                break;
        }

    }

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
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                iv_evidence.setImageBitmap(bitmap);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);//参数100表示不压缩

                byte[] bytes = bos.toByteArray();
                pic = Base64.encodeToString(bytes, Base64.NO_WRAP);
            }
        }

    }


    /**
     * 退款操作
     */
    private void refund() {
        RefundPost refundPost = new RefundPost();
        refundPost.setAct("apply");
        refundPost.setGoods_status(goods_status);
        refundPost.setId(id);
        refundPost.setOrder_id(order_id);
        refundPost.setSeller_id(seller_id);
        refundPost.setPic(pic);
        refundPost.setStatus(status);
        refundPost.setReason(reason);
        refundPost.setPrice(price);
        refundPost.setToken(Config.getAccountToken(this));
        refundPost.setSign(Config.getSign(this));

        String json = JsonUtils.objectToJson(refundPost);
        LogUtils.i("info", "发送的JSON数据-------->" + json);
        try {
            jsonObject = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        HttpUtils.httpPostResult(RefundActivity.this, jsonObject, handler, Config.URL_REFUND_ORDER);
    }



}
