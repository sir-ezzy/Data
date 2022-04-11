package com.example.delapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private TextView wlcome;
    private TextView Pname;
    private TextView Pfone;
    private TextView Pemail, promail;
    FirebaseAuth firebaseAuth;
    FirebaseFirestore firestore;
    private Button log, picreset;
    private String userID;
    private ImageView imageprofile;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        log = findViewById(R.id.Lobtn);
        wlcome = findViewById(R.id.welcome);
        Pname  = findViewById(R.id.profile_name);
        Pfone = findViewById(R.id.profil_phone);
        Pemail = findViewById(R.id.profile_email);
        promail = findViewById(R.id.pro_mail);
        imageprofile = findViewById(R.id.profile_pic);
        picreset = findViewById(R.id.reset_pic);

        firebaseAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("user/"+ firebaseAuth.getCurrentUser().getUid()+ "/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(imageprofile);
            }
        });
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
        picreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photogallery = new Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(photogallery,1000);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1000){
            if(resultCode== Activity.RESULT_OK){
                Uri imageUri = data.getData();
//                imageprofile.setImageURI(imageUri);
                uploadImageToFirebase(imageUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        final StorageReference fileRef = storageReference.child("user/"+firebaseAuth.getCurrentUser().getUid()+"/profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(imageprofile);
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(MainActivity.this,"Error just Occurred",Toast.LENGTH_SHORT).show();

            }
        });
    }
}