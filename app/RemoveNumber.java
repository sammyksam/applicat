package com.example.fbcon;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class RemoveNumber extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_number);
        listView = findViewById(R.id.viewEmergencyContact);
        preferences = getSharedPreferences("Emergency",MODE_PRIVATE);

    }

    ListView listView;
    String contacts[] = new String[5];
    String names[] = new String[5];
    String positions[] = new String[]{"Emergency1","Emergency2","Emergency3","Emergency4","Emergency5"};
    String positionsNames[] = new String[]{"EmergencyName1","EmergencyName2","EmergencyName3","EmergencyName4","EmergencyName5"};

    SharedPreferences preferences;
    public static int CONTACT_PICKER_RESULT = 100;
    static int  active = 0;

    @Override
    public void onResume(){
        super.onResume();
        contacts[0] = preferences.getString(positions[0],"Not Set: Tap to Add");
        contacts[1] = preferences.getString(positions[1],"Not Set: Tap to Add");
        contacts[2] = preferences.getString(positions[2],"Not Set: Tap to Add");
        contacts[3] = preferences.getString(positions[3],"Not Set: Tap to Add");
        contacts[4] = preferences.getString(positions[4],"Not Set: Tap to Add");

        names[0] = preferences.getString(positionsNames[0],"");
        names[1] = preferences.getString(positionsNames[1],"");
        names[2] = preferences.getString(positionsNames[2],"");
        names[3] = preferences.getString(positionsNames[3],"");
        names[4] = preferences.getString(positionsNames[4],"");

        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,R.layout.contact_strip,R.id.emergencyPhoneTextView,addArrays(names,contacts));

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                deleteNumber(position);
                active = position;

            }
        });
        listView.setSelection(3);

    }
    public  void deleteNumber(final int position){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.ic_launcher_foreground);
        builder.setMessage("Are you sure you want to remove this contact");
        builder.setTitle("Delete");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove(positions[active]);
                editor.remove(positionsNames[active]);
                editor.apply();
                finish();

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();


    }

    public String[] addArrays(String[] one,String[] two){
        String[] combine = new String[5];
        int n = 0;
        for(String name:one){
            combine[n] = name+" : "+two[n];
            n++;

        }
        return combine;

    }
}
