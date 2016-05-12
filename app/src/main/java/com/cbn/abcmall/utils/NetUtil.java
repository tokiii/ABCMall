package com.cbn.abcmall.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Proxy;
import android.widget.Toast;

import com.cbn.abcmall.activites.LoginActivity;

import java.net.InetSocketAddress;
import java.util.Locale;
/**
 * 网络工具类
 * @author Administrator
 *
 */
public class NetUtil {
	public static final int NETWORN_NONE = 0;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;
/**
 * 获取网络状态
 * @return
 */
	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		// Wifi
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORN_WIFI;
		}
		// 2G、3G
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORN_MOBILE;
		}
		return NETWORN_NONE;
	}

	/**
	 * 网络是否可用
	 * @return
	 */
	public static boolean isNetworkAvaiable(Context context) {
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager == null) {
			return false;
		} else {
			NetworkInfo activeNet = connectivityManager.getActiveNetworkInfo();
			if (activeNet == null) {
				return false;
			}
			return activeNet.isConnected();
		}
	}
	/**
	 * 网络是否连接
	 * @return
	 */
	public static boolean isNetConnected(Context context,int type) {
		ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = mgr.getNetworkInfo(type);
		if (info != null) {
			return info.isConnected();
		}
		return false;
	}
	/**
	 * 获取apn java代理
	 * @return
	 */
	public static java.net.Proxy getApnJavaProxy(Context context) {
		String apnName = getApnName(context);
		if (apnName != null && apnName.length() > 0) {
			apnName = apnName.toLowerCase(Locale.CHINA);
			if (apnName.contains("uninet") || apnName.contains("3gnet") || apnName.contains("cmnet")
					|| apnName.contains("ctnet")) {
				return null;
			}
			String host = "";
			int port;
			if (apnName.contains("cmwap") || apnName.contains("3gwap") || apnName.contains("uniwap")) {
				host = "10.0.0.172";
				port = 80;
			} else if (apnName.contains("ctwap")) {
				host = "10.0.0.200";
				port = 80;
			} else {
				host = Proxy.getDefaultHost();
				port = Proxy.getDefaultPort();
			}
			if (host != null) {
				java.net.Proxy p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(host, port));
				return p;
			}

		}
		return null;
	}

	/**
	 * 获取apn名字
	 * @return
	 */
	private static String getApnName(Context context) {
		ConnectivityManager mgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo wifiConn = mgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		if (wifiConn.isConnected()) { // 如果有wifi连接，不需要代理
			return null;
		}
		NetworkInfo net = mgr.getActiveNetworkInfo();
		if (net != null) {
			return net.getExtraInfo();
		}
		return null;
	}

	/**
	 * 加测是否登录过期
	 * @param json 访问的网络请求数据
	 * @param context
	 */
	private void checkTokenIsLate(String json, Context context) {
		if (json.contains("登录失效")) {
			ToastUtils.TextToast(context, "登录失效！请重新登录！", Toast.LENGTH_SHORT);
			Intent loginIntent = new Intent(context, LoginActivity.class);
			context.startActivity(loginIntent);
			((Activity) context).finish();
		}
	}

}
