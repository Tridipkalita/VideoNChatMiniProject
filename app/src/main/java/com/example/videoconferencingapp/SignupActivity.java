package com.example.videoconferencingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;

    EditText emailBox,passwordBox,nameBox;
    Button loginBtn , signupBtn;

    FirebaseFirestore database;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dialog = new ProgressDialog(this);
        dialog.setMessage("Please Wait");

        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        emailBox = findViewById(R.id.emailbox);
        nameBox = findViewById(R.id.namebox);
        passwordBox = findViewById(R.id.passwordbox);

        loginBtn = findViewById(R.id.loginbtn);
        signupBtn = findViewById(R.id.createbtn);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email, pass ,name;
                 email = emailBox.getText().toString();
                 pass = passwordBox.getText().toString();
                 name = nameBox.getText().toString();

                 if((email.isEmpty()) && (pass.isEmpty()) && (name.isEmpty())){
                     Toast.makeText(SignupActivity.this,"Credentials Invalid",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     dialog.show();
                     // Add document data with auto-generated id.
                     Map<String, Object> data = new HashMap<>();
                     data.put("name", name);
                     data.put("Email", email);

                     auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                         @Override
                         public void onComplete(@NonNull Task<AuthResult> task) {
                             dialog.dismiss();
                             if (task.isSuccessful()) {
                                 database.collection("Users")
                                         .document().set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                                     @Override
                                     public void onSuccess(Void unused) {
                                         Toast.makeText(SignupActivity.this, "Account is created", Toast.LENGTH_SHORT).show();
                                         startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                     }
                                 });

                             } else {
                                 Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                             }
                         }
                     });
                 }
            }
        });
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this , LoginActivity.class));
            }
        });

    }
}