package com.example.shahbaz.car4rent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AdminCarsAdapter extends RecyclerView.Adapter<AdminCarsAdapter.AdminViewHolder> {
    Context context;
    ArrayList<AddCarData>addCarData;
    DatabaseReference carsDelRef;
    String carId;
    FirebaseAuth mAuth;



    public AdminCarsAdapter(Context c , ArrayList<AddCarData>CarData)
    {
        context = c;
        addCarData=CarData;
    }

    @NonNull
    @Override
    public AdminViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        carsDelRef=FirebaseDatabase.getInstance().getReference(" Cars Data");
        mAuth=FirebaseAuth.getInstance();


        final View view=LayoutInflater.from(context).inflate(R.layout.admincarscv,viewGroup,false);


        return new AdminCarsAdapter.AdminViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminViewHolder adminViewHolder, final int i) {

        adminViewHolder.Carname.setText("Car Name : "+addCarData.get(i).getCarName());
        adminViewHolder.Carmodel.setText(" Model # : "+addCarData.get(i).getCarModel());
        adminViewHolder.Price.setText(" Price Per KM : Rs "+(addCarData).get(i).getPricPerKM());
        Picasso.get().load(addCarData.get(i).getCarPicurl()).into(adminViewHolder.CarPic);



        adminViewHolder.Del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carId=addCarData.get(i).getCarId();
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Are you sure you want to Delete?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (mAuth.getCurrentUser() == null ) {

                                    carsDelRef.child(carId).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot){
                                            carsDelRef.child(carId).setValue(null);


                                                }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
//                                    carsDelRef.child(carId).setValue(null);
//                                    Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();




            }
        });

    }

    @Override
    public int getItemCount() {
        return addCarData.size();
    }

    public class AdminViewHolder extends RecyclerView.ViewHolder{


        TextView Carname,Carmodel,Price;
        ImageView CarPic;
        Button Del;

        public AdminViewHolder(@NonNull View itemView) {
            super(itemView);
            Carname=(TextView)itemView.findViewById(R.id.carname);
            Carmodel=(TextView)itemView.findViewById(R.id.carmodel);
            Price=(TextView)itemView.findViewById(R.id.rate);
            CarPic=(ImageView)itemView.findViewById(R.id.Carimage);
            Del=(Button) itemView.findViewById(R.id.imageDel);

        }
    }



}
