package com.example.applicat;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.applicat.adapters.DoctorRecyclerAdapter;
import com.example.applicat.models.Doctor;
import com.example.applicat.models.Patient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.os.Build.VERSION_CODES.P;

public class Profile extends AppCompatActivity {
    Toolbar toolbar;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase database;
    DatabaseReference reference;
    EditText miaka, makao, hali;
    ImageView img;
    TextView name;
    RadioGroup radioSexGroup;
    RadioButton radioSexButton;
    Button submit, select_doctor, change;
    String user_id;
    DoctorRecyclerAdapter doctorRecyclerAdapter;
    List<Doctor> doc_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

       miaka = findViewById(R.id.age);
        makao = findViewById(R.id.residence);
        hali = findViewById(R.id.type);
        submit = findViewById(R.id.submit);
        name = findViewById(R.id.txt_name);
        change=  findViewById(R.id.button);
        select_doctor = findViewById(R.id.select_doctor);
        radioSexGroup = findViewById(R.id.rdbtn);
        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference();
        doc_list = new ArrayList<>();
        doctorRecyclerAdapter = new DoctorRecyclerAdapter(doc_list, getApplicationContext(),user_id);

        reference.child("patients").child(user_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    //for(DataSnapshot data : dataSnapshot.getChildren()){
                    String names = dataSnapshot.child("name").getValue().toString();
                    name.setText(names);
                //}
            }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        int selectedId = radioSexGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioSexButton = findViewById(selectedId);

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_doctor.setEnabled(true);
                change.setEnabled(false);
                finish();
                Intent intent = new Intent(Profile.this, Profile.class);
                startActivity(intent);
            }
        });
            reference.child("Chosen").child(user_id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        final String docid = dataSnapshot.child("id").getValue().toString();
                        select_doctor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                select_doctor.setEnabled(false);

                                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                                View view = getLayoutInflater().inflate(R.layout.select, null);
                                RecyclerView docView = view.findViewById(R.id.doc_view);

                                docView.setLayoutManager(new LinearLayoutManager(Profile.this));
                                docView.setHasFixedSize(true);
                                docView.setAdapter(doctorRecyclerAdapter);

                                reference.child("doctors").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                                Doctor doctor = data.getValue(Doctor.class);
                                                String a = doctor.getId();
                                                if(!docid.equals(a)) {
                                                    doc_list.add(doctor);
                                                    doctorRecyclerAdapter.notifyDataSetChanged();
                                                }
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();

                                change.setEnabled(true);

                            }

                        });
                    } else {
                        select_doctor.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                select_doctor.setEnabled(false);

                                AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
                                View view = getLayoutInflater().inflate(R.layout.select, null);
                                RecyclerView docView = view.findViewById(R.id.doc_view);

                                docView.setLayoutManager(new LinearLayoutManager(Profile.this));
                                docView.setHasFixedSize(true);
                                docView.setAdapter(doctorRecyclerAdapter);

                                reference.child("doctors").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){
                                            for(DataSnapshot data : dataSnapshot.getChildren()){
                                                Doctor doctor = data.getValue(Doctor.class);
                                                String a = doctor.getId();
                                                    doc_list.add(doctor);
                                                    doctorRecyclerAdapter.notifyDataSetChanged();
                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                builder.setView(view);
                                AlertDialog dialog = builder.create();
                                dialog.show();

                                change.setEnabled(true);

                            }

                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pProfile();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.profile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                firebaseAuth.signOut();
                finish();
                startActivity(new Intent(this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);


        }
    }

    public void pProfile() {
        String age = miaka.getText().toString().trim();
        String type = hali.getText().toString().trim();
        String residence = makao.getText().toString().trim();
        String gender = radioSexButton.getText().toString().trim();

        if (!TextUtils.isEmpty(residence) && !TextUtils.isEmpty(age) && !TextUtils.isEmpty(type) && !TextUtils.isEmpty(gender)) {

            Map profileMap = new HashMap();
            profileMap.put("age", age);
            profileMap.put("type", type);
            profileMap.put("residence", residence);
            profileMap.put("gender", gender);

            reference.child("patients").child(user_id).updateChildren(profileMap, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    Toast.makeText(Profile.this, "Profile changed successfully", Toast.LENGTH_LONG).show();
                }
            });


        } else {
            Toast.makeText(this, "it is important you enter your personal details", Toast.LENGTH_SHORT).show();
        }



    }

}
