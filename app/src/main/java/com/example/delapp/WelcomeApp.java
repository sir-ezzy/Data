package com.example.delapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class WelcomeApp extends AppCompatActivity {

    private TextView textView;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_app);

        textView = findViewById(R.id.W_text);
        animation = AnimationUtils.loadAnimation(this,R.anim.fadein);
        textView.setAnimation(animation);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeApp.this,Register.class);
                startActivity(intent);
                finish();
            }
        },8000);
    }
}