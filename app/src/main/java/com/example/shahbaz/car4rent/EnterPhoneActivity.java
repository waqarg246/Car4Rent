package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EnterPhoneActivity extends AppCompatActivity {
    Button next;
    EditText phonenumber;
    String Phone;
    FirebaseUser loginuser;
    String uName,uPhone,uCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_phone);
        next = (Button) findViewById(R.id.buttonnext);
        phonenumber = (EditText) findViewById(R.id.editTextPhone);
        loginuser = FirebaseAuth.getInstance().getCurrentUser();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        uName=getIntent().getStringExtra("Name");
        uPhone=getIntent().getStringExtra("Phone");
        uCity=getIntent().getStringExtra("City");

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validation();



            }
        });
    }
    private void validation() {

        Phone = phonenumber.getText().toString().trim();

        if (Phone.isEmpty()) {
            phonenumber.setError("ENTER YOUR PHONE NUMBER");
            phonenumber.requestFocus();
            return;
        }


        if (phonenumber.length() < 13 || phonenumber.length() > 13) {
            phonenumber.setError("ENTER YOUR PHONE NUMBER IN PROPER FORMAT (+92)");
            phonenumber.requestFocus();
            return;
        } else {
            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
            String userphone = phonenumber.getText().toString();
            Intent i = new Intent(EnterPhoneActivity.this,EnterCodeActivity.class);
            i.putExtra("name",uName);
            i.putExtra("city",uCity);
            i.putExtra("number", userphone);
            startActivity(i);
            finish();
        }


    }
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if (loginuser != null)
//        {
//            Intent i =new Intent( EnterPhoneActivity.this,HomePageActivity.class );
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(i);
//
//        }
//
//    }


    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
        finish();

        super.onBackPressed();
    }
}

