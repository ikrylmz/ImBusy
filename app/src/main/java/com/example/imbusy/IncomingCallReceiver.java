package com.example.imbusy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telecom.TelecomManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.android.internal.telephony.ITelephony;
import java.lang.reflect.Method;

import static android.content.ContentValues.TAG;

public class IncomingCallReceiver extends BroadcastReceiver {

    //Receiver bildirim almıyor.OnReceive metoduna hiç girilmiyor. API 26 ve üstü için geçerli.

    //Not :  https://developer.android.com/guide/components/broadcast-exceptions
    @Override
    public void onReceive(Context context, Intent intent)
    {

        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        if (telephony.getCallState() != TelephonyManager.CALL_STATE_RINGING) {
            return;
        }

        if (!intent.hasExtra(TelephonyManager.EXTRA_INCOMING_NUMBER)) {
            Log.d(TAG, "Event had no incoming_number metadata. Lettin it keep ringing...");
            return;
        }

        // get incoming call number.
        String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
        Log.d(TAG, "Incoming number: " + number);

        if(number.equals(MainActivity.prevented_number))
        {
            breakCallNougatAndLower(context);
            new MessageSender();

        }

    }

    private void breakCallNougatAndLower(Context context) {
        Log.d(TAG, "Trying to break call for Nougat and lower with TelephonyManager.");
        TelephonyManager telephony = (TelephonyManager)
                context.getSystemService(Context.TELEPHONY_SERVICE);
        try {
            Class c = Class.forName(telephony.getClass().getName());
            Method m = c.getDeclaredMethod("getITelephony");
            m.setAccessible(true);
            ITelephony telephonyService = (ITelephony) m.invoke(telephony);
            telephonyService.endCall();
            Log.d(TAG, "Invoked 'endCall' on TelephonyService.");
        } catch (Exception e) {
            Log.e(TAG, "Could not end call. Check stdout for more info.");
            e.printStackTrace();
        }
    }

    private void breakCallPieAndHigher(Context context) {
        Log.d(TAG, "Trying to break call for Pie and higher with TelecomManager.");
        TelecomManager telecomManager = (TelecomManager)
                context.getSystemService(Context.TELECOM_SERVICE);
        Log.d(TAG, "GİRDİ");
        try {
            telecomManager.getClass().getMethod("endCall").invoke(telecomManager);
            Log.d(TAG, "Invoked 'endCall' on TelecomManager.");
        } catch (Exception e) {
            Log.e(TAG, "Could not end call. Check stdout for more info.");
            e.printStackTrace();
        }
    }
}
