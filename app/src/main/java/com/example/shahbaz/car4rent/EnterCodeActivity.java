package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class EnterCodeActivity extends AppCompatActivity {
    Button verify;
    EditText code;

    String phoneNumber;
    ProgressBar progressBar;
    private  String mVerificationId;
    private FirebaseAuth mAuth;
    String usercode;
    String name,city,phone;


   DatabaseReference userRef;
   FirebaseUser currentFirebaseUser;
   String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_code);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        verify=(Button)findViewById(R.id.buttonverify);
        code = (EditText) findViewById(R.id.editTextcode);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        userRef=FirebaseDatabase.getInstance().getReference("UsersInfo");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;

        phoneNumber = getIntent().getStringExtra("number");
        name=getIntent().getStringExtra("name");
         city=getIntent().getStringExtra("city");

        mAuth=FirebaseAuth.getInstance();

        sendVerificationCode(phoneNumber);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                usercode= code.getText().toString();
                validatecode();

                verifyVerificationCode(usercode);

            }
        });
    }

    private void validatecode() {


        if (usercode.length()<6 || usercode.length()>6)
        {

            code.setError("Enter valid code");
            code.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(usercode)) {
            code.setError("Enter valid code");
            code.requestFocus();
            return;

        }
    }
    private void sendVerificationCode(String phoneNumber) {
        progressBar.setVisibility(View.VISIBLE);
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                EnterCodeActivity.this,
                mCallbacks);
    }
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String usercode = phoneAuthCredential.getSmsCode();


            if (usercode != null) {
                code.setText(usercode);

                verifyVerificationCode(usercode);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(getApplicationContext(), "Code Sent", Toast.LENGTH_LONG).show();
            mVerificationId = s;

        }
    };
    private void verifyVerificationCode(String usercode) {


        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, usercode);

        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(EnterCodeActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Please wait", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(), EnterEmailActivity.class);
                            intent.putExtra("name",name);
                            intent.putExtra("city",city);
                            intent.putExtra("phone",phoneNumber);
                            startActivity(intent);
                            finish();

                        }
                        else {

                            Toast.makeText(getApplicationContext(), "Error occured ! We will fix it Soon !", Toast.LENGTH_LONG).show();

                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {

        Toast.makeText(getApplicationContext(), "Please Finish Registration", Toast.LENGTH_LONG).show();

    }

}

