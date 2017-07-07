package com.training.app.presenter;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import com.training.app.contract.LifeCycleContract;

public class SmsVerificationReceiver extends BroadcastReceiver {

    private LifeCycleContract.View view;
    private static final String SMS_TAG = "Sms Tag";
    public static final String SMS_RECEIVED_INTENT = "android.provider.Telephony.SMS_RECEIVED";

    public SmsVerificationReceiver(LifeCycleContract.View view) {
        this.view = view;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                for (int i = 0; i < pdusObj.length; i++) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String message = sms.getDisplayMessageBody();
                    view.setText(message);
                }
            }
        } catch (Exception e) {
            Log.e(SMS_TAG, e.getMessage(), e);
        }
    }
}
