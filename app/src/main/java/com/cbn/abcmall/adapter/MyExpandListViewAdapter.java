package com.cbn.abcmall.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.cbn.abcmall.R;
import com.cbn.abcmall.activites.Config;
import com.cbn.abcmall.bean.CartGood;
import com.cbn.abcmall.bean.CartShop;
import com.cbn.abcmall.bean.DeleteCartPost;
import com.cbn.abcmall.common.MyApplication;
import com.cbn.abcmall.fragment.CartFragment;
import com.cbn.abcmall.utils.BitMapCache;
import com.cbn.abcmall.utils.EncryptUtils;
import com.cbn.abcmall.utils.HttpUtils;
import com.cbn.abcmall.utils.JsonUtils;
import com.cbn.abcmall.utils.SharePreferenceUtil;
import com.cbn.abcmall.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MyExpandListViewAdapter extends BaseExpandableListAdapter {
    private Context ctx;
    private List<CartShop> groupList;
    private isDelete isDelete;
    private static ImageLoader imageLoader;
    private JSONObject jsonObject;
    private List<CartGood> list;
    private CartFragment cartFragment;

    public MyExpandListViewAdapter(Context ctx, List<CartShop> groupList, CartFragment cartFragment) {
        this.ctx = ctx;
        this.groupList = groupList;
        this.cartFragment = cartFragment;
        imageLoader = new ImageLoader(MyApplication.getInstance().getRequestQueue(), new BitMapCache());
    }

    public void setIsDelete(isDelete isDelete) {
        this.isDelete = isDelete;
    }

    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groupList.get(groupPosition).getShopList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groupList.get(groupPosition).getShopList().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final CartShop group = (CartShop) getGroup(groupPosition);

        imageLoader = new ImageLoader(MyApplication.getInstance().getRequestQueue(), new BitMapCache());
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.cart_group_item, null);
        }
        TextView groupText = (TextView) convertView.findViewById(R.id.groupText);
        CheckBox groupCheckBox = (CheckBox) convertView.findViewById(R.id.groupCheckBox);
        CheckBox visibleCheckBox = (CheckBox) convertView.findViewById(R.id.cb_edit);
        if (group.getShopList().get(0).isVisible()) {
            visibleCheckBox.setChecked(true);
            visibleCheckBox.setText("完成");
        } else {
            visibleCheckBox.setChecked(false);
            visibleCheckBox.setText("编辑");
        }
        visibleCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((CheckBox) v).isChecked()) {
                    ((CheckBox) v).setText("完成");
                    isDelete.isDeleteVisible(groupList, groupPosition, true);
                } else {
                    ((CheckBox) v).setText("编辑");
                    isDelete.isDeleteVisible(groupList, groupPosition, false);
                }
            }
        });

        groupText.setText(group.getName());
        groupCheckBox.setChecked(group.isChecked());
        groupCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                group.changeChecked();
                notifyDataSetChanged();
                cartFragment.reFlashGridView();
            }
        });
        convertView.setClickable(true);
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final CartGood city = (CartGood) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = LayoutInflater.from(ctx).inflate(R.layout.cart_child_item, null);
        }
        TextView childText = (TextView) convertView.findViewById(R.id.childText);
        CheckBox childCheckBox = (CheckBox) convertView.findViewById(R.id.childCheckBox);
        NetworkImageView niv_item = (NetworkImageView) convertView.findViewById(R.id.niv_item);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_count);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
        TextView tv_color = (TextView) convertView.findViewById(R.id.tv_color);
        tv_color.setText(city.getColor());
        tv_price.setText("¥" + city.getPrice());
        tv_count.setText("X" + city.getCount());
        niv_item.setImageUrl(city.getImage(), imageLoader);
        childCheckBox.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CartGood city = groupList.get(groupPosition).getShopList().get(childPosition);
                city.changeChecked();
                notifyDataSetChanged();
                cartFragment.reFlashGridView();
            }
        });
        final Button deleteButton = (Button) convertView.findViewById(R.id.btn_delete);
        deleteButton.setVisibility(city.isVisible() ? View.VISIBLE : View.GONE);
        deleteButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                list = groupList.get(groupPosition).getShopList();
                DeleteCartPost deleteCartPost = new DeleteCartPost();
                deleteCartPost.setAct("delete");
                String token = new SharePreferenceUtil(ctx, Config.ACCOUNT).getStr(Config.ACCOUNT_TOKEN);
                deleteCartPost.setToken(token);
                deleteCartPost.setSign(EncryptUtils.getMD5(token + Config.APP_KEY));
                deleteCartPost.setCid(city.getId());

                String json = JsonUtils.objectToJson(deleteCartPost);
                jsonObject = null;
                try {
                    jsonObject = new JSONObject(json);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final ProgressDialog progressDialog = new ProgressDialog(ctx);
                progressDialog.show();
                final Handler handler = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        switch (msg.what) {
                            case 1:
                                ToastUtils.TextToast(ctx, "删除成功！", Toast.LENGTH_SHORT);
                                progressDialog.dismiss();
                                break;
                            case 10:

                                break;
                        }
                    }
                };
                HttpUtils.httpPostResult(ctx, jsonObject, handler, Config.URL_DELETE_CART);

                isDelete.deleted(groupList, childPosition, groupPosition);

                list.remove(list.get(childPosition));
                if (list.size() == 0) {
                    groupList.remove(groupPosition);
                }
                notifyDataSetChanged();
                cartFragment.reFlashGridView();
            }
        });
        childText.setText(city.getName());
        childCheckBox.setChecked(city.isChecked());
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface isDelete {
        void isDeleteVisible(List<CartShop> groupList, int groupPosition, boolean isVisible);

        void deleted(List<CartShop> groupList, int childPosition, int groupPosition);
    }

}
