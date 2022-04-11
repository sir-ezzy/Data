package com.example.delapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginSp extends AppCompatActivity {

    private EditText email;
    private EditText password;
    private  TextView alredlog;
    private TextView forgottenPass;
    private Button login;
    private ProgressBar  progressBar;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore  firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sp);

        email = findViewById(R.id.Lmail);
        password = findViewById(R.id.Lpass);
        alredlog = findViewById(R.id.Al_Log);
        forgottenPass = findViewById(R.id.Al_forPs);
        login = findViewById(R.id.Log);
        progressBar = findViewById(R.id.proBar);

        firebaseAuth = FirebaseAuth.getInstance();

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

                firebaseAuth.signInWithEmailAndPassword(Lmail,Lpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginSp.this,"user logged in ", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginSp.this,Main_Dashboard.class));
                        }else {
                            Toast.makeText(LoginSp.this, task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(view.GONE);
                        }

                    }
                });
            }
        });

        alredlog.setOnClickListener(view -> {
            Intent intent = new Intent(LoginSp.this, Register.class);
            LoginSp.this.startActivity(intent);
            finish();
        });

        forgottenPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final EditText resetmail = new EditText(view.getContext());
                final AlertDialog.Builder passwordreset = new AlertDialog.Builder(view.getContext());
                passwordreset.setTitle("Reset Your Password");
                passwordreset.setMessage("Please Enter Email");
                passwordreset.setView(resetmail);

                passwordreset.setPositiveButton("Send Link", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String rmail = resetmail.getText().toString();
                        firebaseAuth.sendPasswordResetEmail(rmail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(LoginSp.this, "A Link Will Be Sent To You Shortly",Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginSp.this,"An Error Just Occurred \n"+ e.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
                passwordreset.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // close dialog
                    }
                });
                passwordreset.create().show();
            }
        });
    }
}