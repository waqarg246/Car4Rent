package com.example.shahbaz.car4rent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CarsListAdapter extends RecyclerView.Adapter<CarsListAdapter.CarsListViewHolder> {
    Context context;
    ArrayList<AddCarData>addCarData;
    DatabaseReference carsIdRef;
    String dataBaseId;
String carId;

    public CarsListAdapter(Context c , ArrayList<AddCarData>CarData)
    {
        context = c;
        addCarData=CarData;
    }


    @NonNull
    @Override
    public CarsListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        carsIdRef=FirebaseDatabase.getInstance().getReference(" Cars Data");


             final View view=LayoutInflater.from(context).inflate(R.layout.carscv,viewGroup,false);


        return new CarsListViewHolder(view);


    }


    @Override
    public void onBindViewHolder(@NonNull CarsListViewHolder carsListViewHolder, final int i) {


        carsListViewHolder.Carname.setText("Car Name : "+addCarData.get(i).getCarName());
        carsListViewHolder.Carmodel.setText(" Model # : "+addCarData.get(i).getCarModel());
        carsListViewHolder.Price.setText(" Price Per KM : Rs "+(addCarData).get(i).getPricPerKM());
        Picasso.get().load(addCarData.get(i).getCarPicurl()).into(carsListViewHolder.CarPic);


        carsListViewHolder.book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                carId=addCarData.get(i).getCarId();

                Intent intent = new Intent(context, HomePageActivity.class);

                intent.putExtra("Id",carId);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity)context).finish();


            }
        });

    }

    @Override
    public int getItemCount() {
        return addCarData.size();
    }

    class CarsListViewHolder extends RecyclerView.ViewHolder {

        TextView Carname,Carmodel,Price;
        ImageView CarPic;
        Button book;

        public CarsListViewHolder(@NonNull View itemView) {
            super(itemView);
            Carname=(TextView)itemView.findViewById(R.id.carname);
            Carmodel=(TextView)itemView.findViewById(R.id.carmodel);
            Price=(TextView)itemView.findViewById(R.id.rate);
            CarPic=(ImageView)itemView.findViewById(R.id.Carimage);
            book=(Button)itemView.findViewById(R.id.btnBook);


        }
    }
}
