package com.example.gappa;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.gappa.Models.Users;
import com.example.gappa.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding binding1;
    private FirebaseAuth auth;
    FirebaseDatabase firebaseDatabase;  // he used database instead of firebasedatabase
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding1 = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding1.getRoot());

        getSupportActionBar().hide();       // this is for hiding the toolbar

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        // this will show the progress
        progressDialog = new ProgressDialog(SignUpActivity.this);
        progressDialog.setTitle("Creating Account...");
        progressDialog.setMessage("We are creating your account");

        binding1.signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding1.signUpEmail.getText().toString(),binding1.signUpPassword.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();   //we have to dismiss that progressdialog other wise it will not stop
                                if(task.isSuccessful())
                                {
                                    Users user = new Users(binding1.signUpUsername.getText().toString(),binding1.signUpEmail.getText().toString(),binding1.signUpPassword.getText().toString());
                                    String id = task.getResult().getUser().getUid();
                                    firebaseDatabase.getReference().child("Users").child(id).setValue(user);
                                    Toast.makeText(SignUpActivity.this, "User Created Successfuly !!!", Toast.LENGTH_SHORT).show();
                                    Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                                    startActivity(i);
                                    SignUpActivity.this.finish();
                                }
                                else
                                {
                                    Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding1.signUpAlreadyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(intent1);
                SignUpActivity.this.finish();
            }
        });


    }

}