package com.example.fbcon;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class EmergencyTestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_test);


    }
    public void addNumber(View view){
        startActivity(new Intent(this, ViewNumber.class));
    }
    public void editMessage(View view){
        startActivity(new Intent(this,EditEmergencyMessage.class));
    }
}
