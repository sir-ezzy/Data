package com.example.delapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class MainActivity extends AppCompatActivity {

    private TextView wlcome;
    private TextView Pname;
    private TextView Pfone;
    private TextView Pemail;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    private Button log;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = findViewById(R.id.Lobtn);
        wlcome = findViewById(R.id.welcome);
        Pname  = findViewById(R.id.profile_name);
        Pfone = findViewById(R.id.profil_phone);
        Pemail = findViewById(R.id.profile_email);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        userID = firebaseAuth.getCurrentUser().getUid();

        DocumentReference documentReference = firestore.collection("user").document(userID);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException error) {
                Pname.setText(documentSnapshot.getString("FullName"));
                Pfone.setText(documentSnapshot.getString("Phone"));
                Pemail.setText(documentSnapshot.getString("Email"));
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(MainActivity.this,LoginSp.class));
                finish();
            }
        });

    }

}