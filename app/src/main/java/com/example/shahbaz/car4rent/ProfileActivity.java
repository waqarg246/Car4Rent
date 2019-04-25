package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormatSymbols;

public class ProfileActivity extends AppCompatActivity {

    ImageButton pback;
TextView uname,ucontact,uemail,ucity;

    DatabaseReference userRef;
    String id;
    private FirebaseAuth firebaseAuth;
    FirebaseUser user;
    String currentuser;
    String strname,strcontact,stremail,strcity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        pback=(ImageButton)findViewById(R.id.profileback);
        uname=(TextView)findViewById(R.id.textViewusername);
        ucontact=(TextView)findViewById(R.id.textViewphone);
        uemail= (TextView)findViewById(R.id.textViewemail);
        ucity=(TextView)findViewById(R.id.textViewcity);
        firebaseAuth = FirebaseAuth.getInstance();
        userRef= FirebaseDatabase.getInstance().getReference("UsersInfo");

        if (firebaseAuth.getCurrentUser() != null) {
            currentuser  = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }


        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                finish();
            }

        });

        userRef.child(currentuser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

               strname=dataSnapshot.child("name").getValue(String.class);
               stremail=dataSnapshot.child("email").getValue(String.class);
               strcontact=dataSnapshot.child("phone").getValue(String.class);
               strcity=dataSnapshot.child("city").getValue(String.class);


                uname.setText(strname);
                ucity.setText(strcity);
                ucontact.setText(strcontact);
                uemail.setText(stremail);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
        finish();
        super.onBackPressed();
    }
}
