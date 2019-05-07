package com.example.fbcon;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class EditEmergencyMessage extends AppCompatActivity {
    SharedPreferences preferences;
    TextInputLayout emergencyText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_emergency_message);
        preferences = getSharedPreferences("Emergency",MODE_PRIVATE);
        emergencyText = findViewById(R.id.emergency_text);

        emergencyText .getEditText().setText(preferences.getString("EmergencyText","Emergency"));
        emergencyText.setCounterMaxLength(120);
        emergencyText.setCounterEnabled(true);


    }

    public void save(View view){
        SharedPreferences.Editor  editor = preferences.edit();
        if(emergencyText.getEditText().toString().length()>10) {
            editor.putString("EmergencyText", emergencyText.getEditText().getText().toString());
            editor.commit();
            Toast.makeText(this,"Saved",Toast.LENGTH_LONG);
            finish();
        }else {
            emergencyText.setError("Length must be greater than ten");
        }

    }
}
