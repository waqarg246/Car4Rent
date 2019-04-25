package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {
    Button finish;
    EditText Name,City;
    String username,usercity,userphone;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        finish=(Button)findViewById(R.id.buttonfinish);
        Name=(EditText)findViewById(R.id.editTextname);

        City=(EditText)findViewById(R.id.editTextcity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                username=Name.getText().toString();
                usercity=City.getText().toString();

                uploadvalidation();






            }
        });
    }
    public  void uploadvalidation() {



        if (TextUtils.isEmpty(username)) {
            Name.setError("ENTER YOUR NAME");
            Name.requestFocus();
            return;



        }


        if (TextUtils.isEmpty(usercity)) {
            City.setError("ENTER YOUR CITY");
            City.requestFocus();
            return;


        }

        else {

            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
            Intent i1 = new Intent(RegistrationActivity.this,EnterPhoneActivity.class);
            i1.putExtra("Name",username);
            i1.putExtra("City",usercity);
            i1.putExtra("Phone",userphone);
            startActivity(i1);
            finish();


            }
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(),SignInActivity.class));
        finish();
        super.onBackPressed();
    }
}
