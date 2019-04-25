package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class ContactusActivity extends AppCompatActivity {
    ImageButton pback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contactus);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        pback=(ImageButton)findViewById(R.id.profileback);


        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                finish();
            }

        });

    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(ContactusActivity.this,HomePageActivity.class);
        startActivity(i);
        finish();
    }
}
