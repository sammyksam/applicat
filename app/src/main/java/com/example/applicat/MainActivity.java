package com.example.applicat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicat.keydown.EmergencyButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
//    public static class BottomNavigationViewHelper {
//
//
//}

    GraphView graphView;
    EditText yValue;
    Button submit;
    FirebaseDatabase database;
    DatabaseReference reference;
    DatabaseReference upRef;
    String id;
    FirebaseAuth mAuth;
    SimpleDateFormat sdf = new SimpleDateFormat("hh");
    TextView name;
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;

    LineGraphSeries series;
    EmergencyButton emergencyButton;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        emergencyButton = new EmergencyButton();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Toast.makeText(this, "settings was selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.profile:
                startActivity(new Intent(this, Profile.class));
                return true;
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        yValue = findViewById(R.id.yValue);
        submit = findViewById(R.id.submit);
        graphView = findViewById(R.id.graphView);
        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() != null) {
            id = firebaseAuth.getCurrentUser().getUid();
        }
        series = new LineGraphSeries();
        graphView.addSeries(series);
        database = FirebaseDatabase.getInstance();
        String ref = database.getReference().child("chartTable").child(id).push().getKey();
        reference = database.getReference().child("chartTable").child(id).child(ref);
        upRef = database.getReference().child("chartTable").child(id);


                setListeners();

        graphView.getGridLabelRenderer().setNumHorizontalLabels(15);
        graphView.getGridLabelRenderer().setVerticalAxisTitle("glucose level");
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("time");


        graphView.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {

                if (isValueX) {
                    return sdf.format(new Date((long) value));
                } else {
                    return super.formatLabel(value, isValueX);
                }

            }
        });
    }


    private void setListeners() {
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                long x = new Date().getTime();
                int y = Integer.parseInt(yValue.getText().toString().trim());

                PointValue pointValue = new PointValue(x, y);
                reference.setValue(pointValue);
                Toast.makeText(MainActivity.this, "submitted successful", Toast.LENGTH_LONG).show();

                yValue.getText().clear();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser mUser = firebaseAuth.getCurrentUser();
        if(mUser != null){
        upRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                DataPoint[] dp = new DataPoint[(int) dataSnapshot.getChildrenCount()];
                int index = 0;

                for (DataSnapshot myDataSnapshot : dataSnapshot.getChildren()) {

                    PointValue pointValue = myDataSnapshot.getValue(PointValue.class);

//                    if (pointValue != null) {
                    dp[index] = new DataPoint(pointValue.getxValue(), pointValue.getyValue());
                    index++;
//                    }

                }

                series.resetData(dp);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        BottomNavigationView navigation = findViewById(R.id.navigation);

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
                        Intent b = new Intent(MainActivity.this, message.class);
                        startActivity(b);
                        break;

                    case R.id.contacts:
                        Intent c = new Intent(MainActivity.this, EmergencyTestActivity.class);
                        startActivity(c);
                        break;
                }
                return false;
            }
        });
    } else {
            Intent signIntent = new Intent(MainActivity.this, Login.class);
            startActivity(signIntent);
            finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseUser mUser = firebaseAuth.getCurrentUser();
        if(mUser == null){
            Intent signIntent = new Intent(MainActivity.this, Login.class);
            startActivity(signIntent);
            finish();

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        return emergencyButton.sense(this,keyCode,event);

    }
}

