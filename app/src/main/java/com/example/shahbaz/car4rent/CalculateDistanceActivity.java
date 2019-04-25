package com.example.shahbaz.car4rent;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;




public class CalculateDistanceActivity extends AppCompatActivity {
    EditText toLocation,fromLocation;
    int PLACE_PICKER_REQUEST_TO =1;
    int PLACE_PICKER_REQUEST_FROM =2;
    String tocurrentlat,tocurrentlong,fromcurrentlat,fromcurrentlong;
    String latitudefrom,longitudefrom;
    String latitudeto,longitudeto;
    String locTo,locFrom;
    String addOfTo;
    String addOfFrom;
    View v;
    Button Ok;
    float distance;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String DISTANCE  = "distanceKey";
    public static final String LOCATION_To = "location_to";
    public static final String LOCATION_From = "location_from";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate_distance);

        toLocation=(EditText) findViewById(R.id.Etto);
        fromLocation=(EditText) findViewById(R.id.Etfrom);
        Ok=(Button)findViewById(R.id.buttonok);
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);


        fromLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                goforaddressFrom();




            }
        });
        toLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                goforaddressTo();






            }
        });
        Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

String toLocationstr,fromLocationstr;
toLocationstr=toLocation.getText().toString();
fromLocationstr=fromLocation.getText().toString();

                if (TextUtils.isEmpty(toLocationstr)) {
                    Toast.makeText(getApplicationContext(), "Select Location...", Toast.LENGTH_LONG).show();


                } else if (TextUtils.isEmpty(fromLocationstr)) {
                    Toast.makeText(getApplicationContext(), "Select Location...", Toast.LENGTH_LONG).show();


                } else {
                    intentOk();

                }

            }
        });

    }

    private void goforaddressFrom() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = intentBuilder.build(CalculateDistanceActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST_FROM);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }
    private void goforaddressTo() {
        PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        try {
            Intent intent = intentBuilder.build(CalculateDistanceActivity.this);
            startActivityForResult(intent, PLACE_PICKER_REQUEST_TO);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    protected void onActivityResult(int requestcode,int resultcode,Intent data){

        // From
        if(requestcode== PLACE_PICKER_REQUEST_FROM){
            if(resultcode==RESULT_OK){
                Place place = PlacePicker.getPlace(this,data);
                addOfFrom = String.format("%s",place.getAddress());
                Double latitude= place.getLatLng().latitude;
                Double longitude= place.getLatLng().longitude;
                fromcurrentlat =Double.toString(latitude);
                fromcurrentlong = Double.toString(longitude);
                fromLocation.setText(addOfFrom);
                latitudefrom = fromcurrentlat;
                longitudefrom = fromcurrentlong;


            }
        }

        //To
        if(requestcode== PLACE_PICKER_REQUEST_TO){
            if(resultcode==RESULT_OK){
                Place place = PlacePicker.getPlace(this,data);
                addOfTo = String.format("%s",place.getAddress());
                Double latitude= place.getLatLng().latitude;
                Double longitude= place.getLatLng().longitude;
                tocurrentlat = Double.toString(latitude);
                tocurrentlong = Double.toString(longitude);
                toLocation.setText(addOfTo);
                latitudeto = tocurrentlat;
                longitudeto = tocurrentlong;
            }
        }






    }
    public void intentOk ()
    {
//

        Location locationA = new Location("point A");
        locationA.setLatitude(Float.parseFloat(latitudeto));
        locationA.setLongitude(Float.parseFloat(longitudeto));
        Location locationB = new Location("point B");
        locationB.setLatitude(Float.parseFloat(latitudefrom));
        locationB.setLongitude(Float.parseFloat(longitudefrom));

        distance = locationA.distanceTo(locationB) /1000;
        locTo = toLocation.getText().toString();
        locFrom = fromLocation.getText().toString();


        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putFloat(DISTANCE, (float) distance);
        editor.putString(LOCATION_To,(String)locTo);
        editor.putString(LOCATION_From,(String)locFrom);
        editor.commit();
        Intent intent=new Intent(CalculateDistanceActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();


//                   Intent intent =  new Intent(CalculateDistanceActivity .this, com.example.shahbaz.car4rent.HomePageActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putFloat("CALCULATED_DISTANCE",distance);//float
//
//                   bundle.putString("LOC_TO",locTo);
//
//                   bundle.putString("LOC_FROM",locFrom);
//                  intent.putExtras(bundle);
//                  startActivity(intent);

    }


    @Override
    public void onBackPressed() {

        Intent intent=new Intent(CalculateDistanceActivity.this, HomePageActivity.class);
        startActivity(intent);
        finish();


        super.onBackPressed();
    }
}
