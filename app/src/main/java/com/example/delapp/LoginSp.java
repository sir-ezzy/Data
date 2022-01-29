package com.example.delapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginSp extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private  TextView alredlog;
    private Button login;
    private ProgressBar  progressBar;
    private FirebaseAuth lAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sp);

        email = findViewById(R.id.Lmail);
        password = findViewById(R.id.Lpass);
        alredlog = findViewById(R.id.Al_Log);
        login = findViewById(R.id.Log);
        progressBar = findViewById(R.id.proBar);

        lAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Lmail = email.getText().toString().trim();
                String Lpassword = password.getText().toString().trim();
                if(TextUtils.isEmpty(Lmail)){
                    email.setError("field is empty");
                    return;
                }
                if(TextUtils.isEmpty(Lpassword)){
                    password.setError("password is required");
                    return;
                }
                if(password.length()<6){
                    password.setError("password is not Strong");
                    return;
                }
                progressBar.setVisibility(view.VISIBLE);

                lAuth.signInWithEmailAndPassword(Lmail,Lpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginSp.this,"user logged in ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginSp.this,MainActivity.class));
                        }else {
                            Toast.makeText(LoginSp.this,"Error"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(view.GONE);
                        }

                    }
                });
            }
        });

        alredlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginSp.this , Register.class);
                startActivity(intent);
                finish();
            }
        });
    }
}