package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminCarListActivity extends AppCompatActivity {
    DatabaseReference carsRef;
    RecyclerView recyclerView;
    ArrayList<AddCarData> list;
    AdminCarsAdapter adapter;
    ImageButton adminback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_car_list);
        recyclerView = (RecyclerView) findViewById(R.id.admincarsrv);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        carsRef= FirebaseDatabase.getInstance().getReference().child("Cars Data");
        adminback=(ImageButton)findViewById(R.id.adminlistrback);

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        carsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<AddCarData>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    AddCarData p = dataSnapshot1.getValue(AddCarData.class);
                    list.add(p);
                }
                adapter = new AdminCarsAdapter(AdminCarListActivity.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(AdminCarListActivity.this, "Something is wrong...", Toast.LENGTH_SHORT).show();
            }
        });


        adminback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(getApplicationContext(),AdminActivity.class));
                finish();
            }
        });


    }


}
