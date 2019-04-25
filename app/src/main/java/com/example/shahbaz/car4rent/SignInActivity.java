package com.example.shahbaz.car4rent;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInActivity extends AppCompatActivity implements AdminDialogue.DialogueListener {
    EditText userEmail, userPassword;
    Button Signin;
    Animation smalltobig;
    private FirebaseAuth firebaseAuth;
TextView adminlogin;
LinearLayout main;
TextView forgotPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);


        if(!isConnected(SignInActivity.this)) buildDialog(SignInActivity.this).show();
        else {


        }

        smalltobig=AnimationUtils.loadAnimation(this,R.anim.smalltobig);
        TextView clickhere = (TextView) findViewById(R.id.textView1);
        userEmail = (EditText) findViewById(R.id.editTextuseremail);
        userPassword = (EditText) findViewById(R.id.editTextpassword);
        Signin = (Button) findViewById(R.id.buttonlogin);
        firebaseAuth = FirebaseAuth.getInstance();
        main=(LinearLayout)findViewById(R.id.mainLayout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        main.startAnimation(smalltobig);
        adminlogin=(TextView)findViewById(R.id.textViewadmin);
        forgotPass=(TextView)findViewById(R.id.textViewforGot);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), ForgotPasswordActivity.class));
                finish();
            }
        });




        if (firebaseAuth.getCurrentUser() != null) {
            //close this activity

            //opening profile activity
            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
            finish();
        }
        adminlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                openDialogue();
            }
        });

        clickhere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(SignInActivity.this, RegistrationActivity.class);

                startActivity(i);
                finish();
            }
        });

        Signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               registerUser();

            }
        });
    }


    private void registerUser() {


        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();

        if (TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            userEmail.setError("ENTER VALID EMAIL ");
            userEmail.requestFocus();
            return;
        }

        else if (TextUtils.isEmpty(password) || password.length() < 3) {
            userPassword.setError("ENTER VALID PASSWORD");
            userPassword.requestFocus();
            return;
        }
        else {Toast.makeText(getApplicationContext(), "Verifying...", Toast.LENGTH_SHORT).show();

            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            //if the task is successfull
                            if (task.isSuccessful()) {
                                //start the profile activity
                                Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();

                                startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                                finish();
                            } else {
                                Toast.makeText(getApplicationContext(), "Invalid Email and Password..", Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    public void openDialogue(){

AdminDialogue adminDialogue=new AdminDialogue();
adminDialogue.show(getSupportFragmentManager(),"adminDialogue");
    }

    @Override
    public void applyTexts(String username, String Password) {
        String checkusername,checkpassword;
        checkusername=username;
        checkpassword=Password;
        if (checkusername.equals("admin") && checkpassword.equals("56789")){

            Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_LONG).show();
            Intent i = new Intent(SignInActivity.this, AdminActivity.class);

            startActivity(i);
            finish();

        }
        else{
            Toast.makeText(getApplicationContext(), "Sorry!There is Some Error", Toast.LENGTH_LONG).show();

        }

    }




    public boolean isConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netinfo = cm.getActiveNetworkInfo();

        if (netinfo != null && netinfo.isConnectedOrConnecting()) {
            android.net.NetworkInfo wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            android.net.NetworkInfo mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

            if((mobile != null && mobile.isConnectedOrConnecting()) || (wifi != null && wifi.isConnectedOrConnecting())) return true;
            else
                return false;
        } else
            return false;
    }

    public AlertDialog.Builder buildDialog(Context c) {

        AlertDialog.Builder builder = new AlertDialog.Builder(c);
        builder.setTitle("No Internet Connection");
        builder.setMessage("You need to have Mobile Data or wifi to access this. Press Ok to Exit");

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
        finish();
        super.onBackPressed();
    }
}
