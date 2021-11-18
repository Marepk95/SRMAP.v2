package com.atosprojek.myotpauthentification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import javax.net.ssl.SSLEngineResult;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    public SBRListener SMSBroadcastReceiverListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction() == SmsRetriever.SMS_RETRIEVED_ACTION){

            Bundle extras = intent.getExtras();

            Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (smsRetrieverStatus.getStatusCode()){

                case CommonStatusCodes.SUCCESS:
                    Intent messageIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);
                    SMSBroadcastReceiverListener.onSuccess(messageIntent);
                    break;
                case CommonStatusCodes.TIMEOUT:
                    SMSBroadcastReceiverListener.onFailure();
                    break;

            }
        }
    }

    public interface SBRListener{

        void onSuccess(Intent intent);

        void onFailure();

    }

}
