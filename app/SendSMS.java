package com.example.fbcon;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.widget.Toast;

public class SendSMS extends AppCompatActivity {
    String message;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS =0 ;
    SharedPreferences preferences;
    String[] contacts = new String[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        preferences = getSharedPreferences("Emergency",MODE_PRIVATE);

       populate();
    }

    public void populate(){
        if(preferences.contains("Emergency1")||preferences.contains("Emergency2")||preferences.contains("Emergency3")||preferences.contains("Emergency4")||preferences.contains("Emergency5")) {

            contacts[0] = preferences.getString("Emergency1", null);
            contacts[1] = preferences.getString("Emergency2", null);
            contacts[2] = preferences.getString("Emergency3", null);
            contacts[3] = preferences.getString("Emergency4", null);
            contacts[4] = preferences.getString("Emergency5", null);
            message = preferences.getString("EmergencyText","Emergency");

            for (String contact:contacts){
                if(contact!=null){
                    sendSMSMessage(contact);
                }
            }
        }else {
            startActivity(new Intent(this,ViewNumber.class));
        }
    }

        protected void sendSMSMessage(String phoneNo) {


            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.SEND_SMS)) {
                } else {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.SEND_SMS},
                            MY_PERMISSIONS_REQUEST_SEND_SMS);
                }
            }else{
                sendSMS(phoneNo);
            }
        }

        @Override
        public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
            switch (requestCode) {
                case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                        populate();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }

        }

        private void sendSMS(String phoneNo){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNo, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS sent.",
                    Toast.LENGTH_LONG).show();

        }

    }

