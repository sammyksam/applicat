package com.example.fbcon.keydown;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;

import com.example.fbcon.SendSMS;


public class EmergencyButton  {
    public boolean sense(Context context,int keyCode,KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN || keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            if (event.getRepeatCount() > 50) {
                Log.e("100", Integer.toString(event.getRepeatCount()));
                if (event.getRepeatCount() > 100) {
                    Log.e("2000", Integer.toString(event.getRepeatCount()));
                    context.startActivity(new Intent(context, SendSMS.class));
                    return false;
                }


            }
            return true;


        }
        return true;
    }
}
