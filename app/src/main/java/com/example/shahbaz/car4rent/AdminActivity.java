package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.IOException;

public class AdminActivity extends AppCompatActivity {
    EditText carName,carModel,pricePerKm;
    String Carname,Carmodel,Priceperkm;
    Button oK;
    ImageView logout;
    ImageView carImage;
    String carImageurl;
    private static final int CHOOSE_IMAGE=101;
    int carId=0;
    DatabaseReference carRef;
    Uri uriSignImage;
    String signImageDownloadUri;
    AddCarData addCarData;
    TextView show;
String Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        carName=(EditText)findViewById(R.id.editTextcarname);
        carModel=(EditText)findViewById(R.id.editTextcarmodel);
        pricePerKm=(EditText)findViewById(R.id.editTextprice);
        oK=(Button)findViewById(R.id.buttonok);
        carImage=(ImageView)findViewById(R.id.imageViewcar);
        logout=(ImageView) findViewById(R.id.buttonlogout);
        show=(TextView) findViewById(R.id.show);
        addCarData=new AddCarData();
        carRef=FirebaseDatabase.getInstance().getReference("Cars Data");

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(AdminActivity.this,"Successfull",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                finish();

            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                        Toast.makeText(AdminActivity.this, "Loading Please Wait... ", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), AdminCarListActivity.class));
                        finish();




            }
        });
        oK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                checkId();






            }
        });
carImage.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {



        showImageChooser();
    }
});


    }
    public void checkId(){
        carRef.orderByKey().limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for(DataSnapshot data: dataSnapshot.getChildren()) {
                        String key = data.getKey();
                        carId = Integer.parseInt(key);
                        carId= carId+ 1;

                    }
                }
                else {
                    carId=1;
                }
                Id=String.valueOf(carId);

                validateEditTexts();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {


            }
        });
    }

    public void validateEditTexts(){
        Carname=carName.getText().toString();
        Carmodel=carModel.getText().toString();
        Priceperkm=pricePerKm.getText().toString();

        if(TextUtils.isEmpty(Carname)){
            carName.setError("Enter Car Name");
            carName.requestFocus();
        }

        else if(TextUtils.isEmpty(Carmodel)){
           carModel.setError("Enter Car Model");
            carModel.requestFocus();
        }
        else if(TextUtils.isEmpty(Priceperkm)){
            pricePerKm.setError("Enter Price/Km");
           pricePerKm.requestFocus();
        }

        else if (carImage.getDrawable() ==null) {
            Toast.makeText(AdminActivity.this,"Select Image",Toast.LENGTH_SHORT).show();
        }

        else
        {

   uploadDataToDataBase(Id,Carname,Carmodel,Priceperkm,signImageDownloadUri);
            Toast.makeText(AdminActivity.this,"Uploading Wait..",Toast.LENGTH_SHORT).show();
        }


    }

    private void uploadDataToDataBase(String Carid,String Carname,String Carmodel,String PricePerKM, String CarPicurl) {
//        id++;
        addCarData=new AddCarData(Carid,Carname,Carmodel,PricePerKM,CarPicurl);
        carRef.child(Id).setValue(addCarData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(AdminActivity.this,"Successful",Toast.LENGTH_SHORT).show();
                carName.getText().clear();
                carModel.getText().clear();
                pricePerKm.getText().clear();
                carImage.setImageResource(R.drawable.bggradient);

            }
    });

    }

    private void showImageChooser(){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(intent.createChooser(intent,"Choose Sign"),CHOOSE_IMAGE);
    }

    private void uploadImageToFirebaseStorage(){
        StorageReference signRef= FirebaseStorage.getInstance().getReference("Signs/"+System.currentTimeMillis()+".jpg");
        if(uriSignImage != null){
            signRef.putFile(uriSignImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    signImageDownloadUri=taskSnapshot.getDownloadUrl().toString();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(AdminActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode== CHOOSE_IMAGE && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            uriSignImage=data.getData();
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),uriSignImage);
               carImage.setImageBitmap(bitmap);
                uploadImageToFirebaseStorage();

            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }

        }
    }

    @Override
    public void onBackPressed() {

        startActivity(new Intent(getApplicationContext(), SignInActivity.class));
        finish();

        super.onBackPressed();
    }
}
