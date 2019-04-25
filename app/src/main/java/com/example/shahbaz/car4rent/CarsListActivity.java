package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CarsListActivity extends AppCompatActivity {
    DatabaseReference carsRef;
    RecyclerView recyclerView;
    ArrayList<AddCarData> list;
    CarsListAdapter adapter;
    CardView cardView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_list);
        getSupportActionBar().setTitle("Select your Car");
        recyclerView = (RecyclerView) findViewById(R.id.carsrv);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));
        carsRef= FirebaseDatabase.getInstance().getReference().child("Cars Data");

        carsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list = new ArrayList<AddCarData>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
                {
                    AddCarData p = dataSnapshot1.getValue(AddCarData.class);
                    list.add(p);
                }
                adapter = new CarsListAdapter(CarsListActivity.this,list);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(CarsListActivity.this, "Something is wrong...", Toast.LENGTH_SHORT).show();
            }
        });




    }

    @Override
    public void onBackPressed() {

        Intent intent=new Intent(CarsListActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
