package com.example.fbcon;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ViewNumber extends AppCompatActivity {
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACT = 100;
    ListView listView;
    String contacts[] = new String[5];
    String names[] = new String[5];
    String positions[] = new String[]{"Emergency1","Emergency2","Emergency3","Emergency4","Emergency5"};
    String positionsNames[] = new String[]{"EmergencyName1","EmergencyName2","EmergencyName3","EmergencyName4","EmergencyName5"};


    SharedPreferences preferences;
    public static int CONTACT_PICKER_RESULT = 100;
    static int  active = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_number);
        listView = findViewById(R.id.viewEmergencyContact);
        preferences = getSharedPreferences("Emergency",MODE_PRIVATE);


    }
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
                addNumber(position);
                active = position;

            }
        });
        listView.setSelection(3);

    }
    public  void addNumber(int position){

//        Intent intent = new Intent(this,AddNumber.class);
//        intent.putExtra("position",positions[position]);
//
//        startActivity(intent);
//
//        finish();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)!= PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_CONTACTS)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS},
                        MY_PERMISSIONS_REQUEST_READ_CONTACT);
            }
        }else{
           requestNumber();
        }




    }

    public void requestNumber(){
        Intent contactPickerIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);


        startActivityForResult(contactPickerIntent,CONTACT_PICKER_RESULT);

    }

    public void onRequestPermissionsResult(int requestCode,String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACT: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    requestNumber();

                } else {
                    Toast.makeText(getApplicationContext(),
                            "You Must allow the app to access Contacts.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == CONTACT_PICKER_RESULT){
            if(resultCode== Activity.RESULT_OK){

                Uri uri = data.getData();
                ContentResolver contentResolver = getContentResolver();
                Cursor contentCursor = contentResolver.query(uri, null, null,null, null);

                if(contentCursor.moveToFirst()){
                    String id = contentCursor.getString(contentCursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                    String hasPhone =
                            contentCursor.getString(contentCursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                    if (hasPhone.equalsIgnoreCase("1"))
                    {
                        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ id,null, null);
                        phones.moveToFirst();
                        String contactNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String contactName = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        Log.e("phoneNUmber", "The phone number is "+ contactNumber);
                        Log.e("phoneNumber", "The phone number is "+ contactName);
//                        contacts[active]=contactNumber;
//                        positionsNames[active]=contactName;
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(positionsNames[active],contactName);
                        editor.putString(positions[active],contactNumber);


                        editor.apply();




                    }
                }
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view_contact_menu,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.deleteContact){
            startActivity(new Intent(getApplicationContext(),RemoveNumber.class));
        }
        return super.onOptionsItemSelected(item);
    }


}
