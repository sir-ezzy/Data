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

public class Register extends AppCompatActivity {

    private EditText fullname;
    private EditText gmail;
    private EditText password;
    private EditText fone;
    private Button reglog;
    private TextView registered;
    private FirebaseAuth regAuth;
    private ProgressBar probar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        fullname = findViewById(R.id.name);
        fone = findViewById(R.id.phone);
        gmail = findViewById(R.id.mail);
        password = findViewById(R.id.pass);
        reglog = findViewById(R.id.regis);
        registered = findViewById(R.id.Al_reg);
        probar = findViewById(R.id.pros);

        regAuth = FirebaseAuth.getInstance();

        if(regAuth.getCurrentUser()!= null){
            startActivity(new Intent(Register.this,MainActivity.class));
            finish();
        }

        reglog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = fullname.getText().toString().trim();
                String mphone = fone.getText().toString().trim();
                String mail = gmail.getText().toString().trim();
                String pass = password.getText().toString().trim();
;
                if(TextUtils.isEmpty(name)){
                    fullname.setError("Field is Empty");
                    return;
                }
                if(TextUtils.isEmpty(mphone)){
                    fone.setError("field is empty");
                    return;
                }
                if(TextUtils.isEmpty(mail)){
                    gmail.setError("gmail is Required");
                    return;
                }
                if(password.length()<6|| TextUtils.isEmpty(pass)){
                    password.setError("password is not Strong");
                    return;
                }
                probar.setVisibility(view.VISIBLE);

                regAuth.createUserWithEmailAndPassword(mail,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(Register.this,"Account Created Successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));

                        }else {
                            Toast.makeText(Register.this,"Error!"+ task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                            probar.setVisibility(view.GONE);
                        }
                    }
                });

            }
        });

        registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginSp.class);
                startActivity(intent);
            }
        });
    }
}