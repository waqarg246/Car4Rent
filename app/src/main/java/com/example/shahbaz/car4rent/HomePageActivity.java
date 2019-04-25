package com.example.shahbaz.car4rent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import  android.R;

public class HomePageActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout ndrawerlayout;
    private ActionBarDrawerToggle ntoggle;
    NavigationView navigationView;
    String dataBaseCarId;
    DatabaseReference ref;
    String username, userphone, carname;
    float distance_float;
    ImageView carimage;
    TextView tdistance, tvprice;
    TextView note_TextView;
    Button Booknow;
    SharedPreferences permissionStatus;
    private boolean sentToSettings = false;
    private static final int SMS_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private FirebaseAuth firebaseAuth;
    String currentuser, distanceToMatch;

    private static final String ownerMobileNumer = "03055965126";  //U can place own mobile number for testing
    public String sendBookingDetails;
    public String userDetails;
    public String rideDetails;//use String builder or concatination to collect user daata
    DatabaseReference carRef;
    TextView bookingMenu;
    String imageurl;
    LinearLayout carMenu;
    String Pricestr;
    float Price;
    String distanceText;
    float fare;
    String uName, uPhone;

    String distance_to, distance_from;
    Float calculated_distance_float;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String DISTANCE = "distanceKey";
    public static final String PRICE_PER_KM = "price_key";
    public static final String LOCATION_To = "location_to";
    public static final String LOCATION_From = "location_from";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);


        getSupportActionBar().setTitle("Book Your Ride");

        if (!isConnected(HomePageActivity.this)) {
            buildDialog(HomePageActivity.this).show();
        } else {
            setContentView(R.layout.activity_home_page);

            Animation animation;
            animation = AnimationUtils.loadAnimation(getApplicationContext(),
                    R.anim.fade_in);

            setValuesInTextViews();


            ref = FirebaseDatabase.getInstance().getReference(" UsersInfo");

            ndrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
            ntoggle = new ActionBarDrawerToggle(this, ndrawerlayout, R.string.open, R.string.close);
            navigationView = (NavigationView) findViewById(R.id.nav);
            ndrawerlayout.addDrawerListener(ntoggle);
            ntoggle.syncState();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Booknow = (Button) findViewById(R.id.buttongo);
            carRef = FirebaseDatabase.getInstance().getReference("Cars Data");
            navigationView.setNavigationItemSelectedListener(this);

            bookingMenu = (TextView) findViewById(R.id.textViewbookride);
            bookingMenu.setPaintFlags(bookingMenu.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            carimage = (ImageView) findViewById(R.id.imageArea);
            tdistance = (TextView) findViewById(R.id.textvdistance);
            permissionStatus = getSharedPreferences("permissionStatus", MODE_PRIVATE);
            firebaseAuth = FirebaseAuth.getInstance();
            tvprice = (TextView) findViewById(R.id.textViewprice);
            distanceText = tdistance.getText().toString();

            note_TextView=(TextView)findViewById(R.id.tv_note);
            note_TextView.startAnimation(animation);


            carMenu = (LinearLayout) findViewById(R.id.bookingmenu);  /*car menu which includes car image and button
                                                                   which are invisible if distance is zero*/

            if (firebaseAuth.getCurrentUser() != null) {
                currentuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            } else if (distance_float > 0) {
                carimage.setEnabled(true);
            }


            dataBaseCarId = getIntent().getStringExtra("Id");
            if (dataBaseCarId != null) {
                carRef.child(dataBaseCarId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        imageurl = dataSnapshot.child("carPicurl").getValue(String.class);
                        Pricestr = dataSnapshot.child("pricPerKM").getValue(String.class);
                        carname = dataSnapshot.child("carName").getValue(String.class);
                        Price = Float.parseFloat(Pricestr);
                        Picasso.get().load(imageurl).into(carimage);


                        if (Price == 0.00) {
                            Toast.makeText(HomePageActivity.this, "Try Again", Toast.LENGTH_SHORT).show();
                        } else if (distanceText.matches(" ")) {
                            Toast.makeText(HomePageActivity.this, "Please Select Your Destination", Toast.LENGTH_SHORT).show();


                        } else {


                            fare = Price * calculated_distance_float;
                            tvprice.setText("Fare : " + fare + "  RS");
                            tdistance.setText("DISTANCE: " + calculated_distance_float + " Kms");
                        }

                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
            carimage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(distance_float>0){
                        startActivity(new Intent(getApplicationContext(), CarsListActivity.class));
                        finish();

                    }
                    else{

                        Toast.makeText(HomePageActivity.this, "Please Select Your Destination First", Toast.LENGTH_SHORT).show();

                    }
                                   }
            });

            bookingMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), CalculateDistanceActivity.class);
                    startActivity(intent);


                }
            });

            Booknow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    if (carimage.getDrawable() == null) {
                        Toast.makeText(HomePageActivity.this, "Select your Car..", Toast.LENGTH_SHORT).show();
                    } else if (tvprice.getText().toString().matches(" ")) {
                        Toast.makeText(HomePageActivity.this, "Try Again", Toast.LENGTH_SHORT).show();

                    } else if (tdistance.getText().toString().matches("  ")) {
                        Toast.makeText(HomePageActivity.this, "Select Distance", Toast.LENGTH_SHORT).show();
                    } else {


                        DatabaseReference firebaseDatabase = FirebaseDatabase.getInstance().getReference("UsersInfo");
                        firebaseDatabase.child(currentuser).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                userphone = dataSnapshot.child("phone").getValue(String.class);
                                username = dataSnapshot.child("name").getValue(String.class);
                                uName = String.valueOf(username);
                                uPhone = String.valueOf(userphone);

                                // ",Car Name :-" + carname
                                userDetails = "\nName:- " + uName;
                                rideDetails = "\nDestination:- " + distance_to + ",Total Fare=" + fare;
                                sendBookingDetails = "Booking Details:-" + userDetails + " " + rideDetails;
                                tdistance.setText("Distance : ");
                                tvprice.setText("Fare : ");
                                carimage.setImageResource(R.drawable.buttonstyle);
                                if (!uName.matches("") && !uPhone.matches("")) {
                                    SendMessage(ownerMobileNumer, sendBookingDetails);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Incomplete info", Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }


                }
            });
            if (ActivityCompat.checkSelfPermission(HomePageActivity.this, android.Manifest.permission.SEND_SMS)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(HomePageActivity.this, android.Manifest.permission.SEND_SMS)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
                    builder.setTitle("Need SMS Permission");
                    builder.setMessage("This app needs SMS permission to send Messages.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            ActivityCompat.requestPermissions(HomePageActivity.this,
                                    new String[]{android.Manifest.permission.SEND_SMS}, SMS_PERMISSION_CONSTANT);
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    builder.show();
                } else if (permissionStatus.getBoolean(android.Manifest.permission.SEND_SMS, false)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(HomePageActivity.this);
                    builder.setTitle("Need SMS Permission");
                    builder.setMessage("This app needs SMS permission to send Messages.");
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            sentToSettings = true;
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                            Toast.makeText(getBaseContext(),
                                    "Go to Permissions to Allow SMS permissions to this application.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                }
            }


        }
    }


    private void setValuesInTextViews() {
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);

        if (sharedpreferences.contains(DISTANCE) && !DISTANCE.matches("")) {
            calculated_distance_float = sharedpreferences.getFloat(DISTANCE, 0);
            distance_to = sharedpreferences.getString(LOCATION_To, null);
            distance_from = sharedpreferences.getString(LOCATION_From, null);

            distance_float=calculated_distance_float;
            //check whether distance is zero or not;
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (ntoggle.onOptionsItemSelected(item)) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.dprofile) {

            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            startActivity(intent);
            finish();
        }


        if (id == R.id.dhome) {
            Intent intent = new Intent(getApplicationContext(), HomePageActivity.class);
            startActivity(intent);
            finish();
        }


        if (id == R.id.dcontactus) {

            Intent intent = new Intent(getApplicationContext(), ContactusActivity.class);
            startActivity(intent);
            finish();

        }


        if (id == R.id.dlogout) {

            FirebaseAuth.getInstance().signOut();

            Toast.makeText(HomePageActivity.this, "Bye!", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(HomePageActivity.this, SignInActivity.class);

            startActivity(i);
            finish();
        }
        return false;
    }

    public void SendMessage(String strMobileNo, String strMessage) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(strMobileNo, null, strMessage, null, null);
            Toast.makeText(getApplicationContext(), "Your Message Sent",
                    Toast.LENGTH_LONG).show();
        } catch (Exception ex)

        {
            Toast.makeText(getApplicationContext(), ex.getMessage().toString(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if ((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting()))
                return true;
            else
                return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or Wifi to access this. Press Ok to Exit");

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                finish();
            }
        });

        return builder;
    }

    @Override
    public void onBackPressed() {


        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Exit");
        builder.setMessage("Do you want to Exit? ");
        builder.setPositiveButton("Exit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                sharedpreferences.edit().clear().commit();
                finish();

            //    HomePageActivity.super.onBackPressed();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.show();

    }
}
