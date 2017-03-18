package com.vineture.movhaul;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

/**
 * Created by SQINDIA on 10/24/2016.
 */
public class ReceiveSmsBroadcastReceiver extends BroadcastReceiver {

    // Get the object of SmsManager
    final SmsManager sms = SmsManager.getDefault();
    LoginOtpActivity getSms = new LoginOtpActivity();
    private static SmsListener mListener;

    public void onReceive(Context context, Intent intent) {


        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        Object[] pdus = (Object[]) bundle.get("pdus");

        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();

           // Log.e("tag","mmm"+messageBody);

            //Pass on the text to our listener.
         /*   if (messageBody!=null){
                mListener.messageReceived(messageBody);
            }*/


           /* LoginOtpActivity inst = LoginOtpActivity.instance();
            inst.updateList(messageBody);*/



            try
            {
                LoginOtpActivity Sms = new LoginOtpActivity();
                Sms.recivedSms(messageBody);
            }
            catch(Exception e){

            }

        }


    }
    public static void bindListener(SmsListener  listener) {
        mListener = listener;
    }
}


