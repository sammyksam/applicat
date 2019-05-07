package com.example.applicat;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class Contacts extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        BottomNavigationView navigation =  findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.food:
                        String url = "https://www.webmd.com/diabetes/diabetic-food-list-best-worst-foods#1/";

                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                        break;
                    case R.id.message:
                        Intent b = new Intent(Contacts.this,message.class);
                        startActivity(b);
                        break;

                    case R.id.contacts:
//                        Intent c = new Intent(Contacts.this,Contacts.class);
//                        startActivity(c);
                        break;
                }
                return false;
            }
        });



    }
}
