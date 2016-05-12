package com.cbn.abcmall.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.cbn.abcmall.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @description ui公共(spinner)
 * @date 2015年3月24日 下午5:15:45
 */
public class UICommon {

	/**
	 * 获取重试对话框
	 * 
	 * @param context
	 *            上下文
	 * @param confirmListener
	 *            确认事件
	 * @param cancelListener
	 *            取消事件
	 * @return 对话框对象
	 */
	public static AlertDialog getRetryDialog(Context context,
			DialogInterface.OnClickListener confirmListener,
			DialogInterface.OnClickListener cancelListener) {
		return getRetryDialog(context, confirmListener, cancelListener, false);
	}

	/**
	 * 获取重试对话框
	 * 
	 * @param context
	 *            上下文
	 * @param confirmListener
	 *            确认事件
	 * @param cancelListener
	 *            取消事件
	 * @param isCancel
	 *            是否可以取消对话框
	 * @return 对话框对象
	 */
	public static AlertDialog getRetryDialog(Context context,
			DialogInterface.OnClickListener confirmListener,
			DialogInterface.OnClickListener cancelListener, boolean isCancel) {
		return new AlertDialog.Builder(context)
				.setCancelable(isCancel)
				.setMessage(
						"错误")
				.setTitle(
						"重试")
				.setPositiveButton(
						"确认",
						confirmListener)
				.setNegativeButton(
						"取消",
						cancelListener).create();
	}

	public static void spinnerBindData(Context context, Spinner spinner,
			int arrId, int defaultId) {
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				context, arrId, R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(R.layout.simple_spinner_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(defaultId, true);
	}

	/**
	 * Spinner 绑定数据-数组+选中索引
	 *
	 * @param spinner
	 *            Spinner控件
	 * @param arrs
	 *            数组
	 * @param defaultId
	 *            Spinner设置默认值，string-array的序号
	 */
	public static void spinnerBindData(Context context, Spinner spinner,
			String[] arrs, int defaultId) {
		if (arrs != null) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					R.layout.simple_spinner_item, arrs);
			adapter.setDropDownViewResource(R.layout.simple_spinner_item);
			spinner.setAdapter(adapter);
			spinner.setSelection(defaultId, true);
		}
	}

	/**
	 * Spinner 绑定数据-数组资源id+默认值
	 * 
	 * @param
	 *
	 * @param spinner
	 *            Spinner控件
	 * @param arrId
	 *            string-array的Id
	 * @param defaultValue
	 *            Spinner设置默认值，默认值内容
	 */
	public static int spinnerBindDataAndReturnPosition(Context context,
			Spinner spinner, int arrId, String defaultValue) {
		int position = -1;
		String[] strs = context.getResources().getStringArray(arrId);
		if (!TextUtils.isEmpty(defaultValue)) {
			for (int i = 0, len = strs.length; i < len; i++) {
				if (defaultValue.equals(strs[i])) {
					position = i;
					break;
				}
			}
		}
		if (position != -1) {
			spinnerBindData(context, spinner, strs, position);
		} else {
			spinnerBindData(context, spinner, strs, 0);
		}

		return position;
	}

	/**
	 * 获取Spinner的text对应的value
	 * 
	 * @param context
	 *            上下文
	 * @param spinner
	 *            Spinner控件
	 * @param arrId
	 *            value string-array的Id
	 * @param position
	 *            Spinner控件选中项目位置
	 * @return 返回选中的spinner
	 */
	public static String getSpinnerValue(Context context, Spinner spinner,
			int arrId, int position) {
		String[] values = context.getResources().getStringArray(arrId);
		String result = "";
		List<String> lst = new ArrayList<String>();
		if (values != null && values.length > 0) {
			for (String s : values) {
				lst.add(s);
			}
			result = lst.get(position);
		}
		return result;
	}

	/**
	 * 根据Array的Value获取Text;
	 * 
	 * @param context
	 *            上下文
	 *            string-array的Id
	 * @param
	 *
	 * @return
	 */
	public static String getArrayText(Context context, int arrTextId,
			int arrValueId, String value) {
		String[] values = context.getResources().getStringArray(arrValueId);
		String[] texts = context.getResources().getStringArray(arrTextId);
		String result = "";
		int position = 0;
		for (int i = 0; i < values.length; i++) {
			if (values[i].equals(value)) {
				position = i;
			}
		}

		List<String> lst = new ArrayList<String>();
		if (texts != null && texts.length > 0) {
			for (String text : texts) {
				lst.add(text);
			}
			result = lst.get(position);
		}
		return result;
	}
}
