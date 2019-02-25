package com.emusafir.utility;

/**
 * Created by ravi on 12/21/2016.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;


public class IncomingSms extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    String senderNum = phoneNumber;
                    String message = currentMessage.getDisplayMessageBody();
                    Log.i("senderNum", senderNum);
                    try {
                        if (senderNum.contains("verification_code")) {
//                            OTP Sms = new OTP();
//                            message = message.replaceAll("\\D+", "");
//                            Sms.recivedSms(message);
                        }
                    } catch (Exception e) {
                    }

                }
            }

        } catch (Exception e) {

        }
    }

}
