package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import static java.lang.Thread.sleep;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        getSupportActionBar().hide();

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {

                try {
                    sleep(3000);
                    Intent i=new Intent(SplashScreen.this,SignInActivity.class);
                    startActivity(i);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        });
        thread.start();

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}

