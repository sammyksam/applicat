package com.example.applicat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity implements View.OnClickListener {

    EditText fName, PhoneNo, Email, Password;
    Button submit;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        Email = findViewById(R.id.email);
        fName = findViewById(R.id.name);
        PhoneNo = findViewById(R.id.phone);
        Password = findViewById(R.id.password);
        submit = findViewById(R.id.submit);


        mAuth = FirebaseAuth.getInstance();


        submit.setOnClickListener(this);


    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mAuth.getCurrentUser() != null) ;
    }

    private void registerPatient() {

        final String name = fName.getText().toString().trim();
        final String phone = PhoneNo.getText().toString().trim();
        final String email = Email.getText().toString().trim();
        final String password = Password.getText().toString().trim();

        if (name.isEmpty()) {
            fName.setError("require");
            fName.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            Email.setError("require");
            Email.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            Password.setError("require");
            Password.requestFocus();
            return;
        }
        if (phone.length() != 10) {
            PhoneNo.setError("enter a valid phone number");
            PhoneNo.requestFocus();
        }



        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            String id = mAuth.getCurrentUser().getUid();

                            User user = new User(name, phone, email,id);
                            FirebaseDatabase.getInstance().getReference("patients")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        finish();
                                        Toast.makeText(Register.this, "registration successful", Toast.LENGTH_LONG).show();
                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        startActivity(intent);

                                    } else {
                                        Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }

                                }
                            });


                        } else {
                            Toast.makeText(Register.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case  R.id.submit:
                registerPatient();
//                Intent intent = new Intent(Register.this, Login.class);
//                startActivity(intent);
                break;


        }
    }
}
