package com.emusafir.utility;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.regex.Pattern;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Created by MMD06 on 11/20/2017.
 */

public class MySMSBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str = "";
        Pattern p = Pattern.compile("(|^)\\d{4}");
        try {
            if (bundle != null) {
                // Get the SMS message

                Object[] pdus = (Object[]) bundle.get("pdus");
                smsm = new SmsMessage[pdus.length];

                for (int i = 0; i < smsm.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();

                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();

                    Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    if (senderNum.contains("SUNSIN")) {

//                        smsm[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
//                    if (smsm != null) {
//                        sms_str += smsm[i].getMessageBody().toString();
//                        String Sender = smsm[i].getOriginatingAddress();
//                        //Check here sender is yours
//                        Matcher m = p.matcher(sms_str);
//                        if(m.find()) {
                        Intent smsIntent = new Intent("otp");
                        message = message.replaceAll("\\D+", "");
                        smsIntent.putExtra("message", message);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(smsIntent);
//                        }
                    }
                }
            }

        } catch (Exception e) {
            Log.e("SmsReceiver", "Exception smsReceiver" + e);

        }
    }
}