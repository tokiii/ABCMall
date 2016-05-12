package com.cbn.abcmall.broadcasts;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;

import com.cbn.abcmall.utils.StringUtil;

/**
 * 接收短信监听器
 * Created by lost on 16-1-7.
 */
public class MsgReceiver extends BroadcastReceiver {

    private static MessageListener messageListener;
    public static final String SMS_RECEIVED_ACTION = "android.provider.Telephony.SMS_RECEIVED";

    public MsgReceiver() {
        super();
    }


    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent.getAction().equals(SMS_RECEIVED_ACTION)) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");

            for(Object object:objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])object);
                String message = smsMessage.getDisplayMessageBody();
                messageListener.onReceive(StringUtil.getNumberFromString(message));
                abortBroadcast();
            }
        }

        abortBroadcast();

    }

    public interface MessageListener {
        public void onReceive(String message);
    }


    /**
     * 设置短信监听器
     * @param messageListener
     */
    public void setOnReceivedMessageListener(MessageListener messageListener) {
        this.messageListener = messageListener;
    }

}
