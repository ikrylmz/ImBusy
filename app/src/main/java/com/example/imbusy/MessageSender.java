package com.example.imbusy;

import android.telephony.SmsManager;

import static com.example.imbusy.MainActivity.prevented_number;
import static com.example.imbusy.MainActivity.text;

public class MessageSender {

    public  MessageSender()
    {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(prevented_number, null, text, null, null);
    }

}
