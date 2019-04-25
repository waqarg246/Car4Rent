package com.example.shahbaz.car4rent;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EnterEmailActivity extends AppCompatActivity {
    EditText useremail,userpassword;
    Button next;
    private FirebaseAuth firebaseAuth;
    String email,password;
    UsersInfo uinfo;
    DatabaseReference userRef;
FirebaseUser currentFirebaseUser;
String uName,uCity,uPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        setContentView(R.layout.activity_enter_email);
        useremail=(EditText)findViewById(R.id.editTextemail);
        userpassword=(EditText)findViewById(R.id.editTextpassword);
        next=(Button)findViewById(R.id.buttonnext);
        firebaseAuth = FirebaseAuth.getInstance();
        userRef=FirebaseDatabase.getInstance().getReference("UsersInfo");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser() ;
        uName=getIntent().getStringExtra("name");
        uCity=getIntent().getStringExtra("city");
        uPhone=getIntent().getStringExtra("phone");



next.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {

        registerUser();
        registerUser();Toast.makeText(getApplicationContext(), "Please Wait, ", Toast.LENGTH_LONG).show();


    }
});

    }

    private void registerUser(){


         email = useremail.getText().toString().trim();
        password  = userpassword.getText().toString().trim();

        if(TextUtils.isEmpty(email) || !Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            useremail.setError("ENTER VALID EMAIL ");
            useremail.requestFocus();
            return;
        }

      else  if(TextUtils.isEmpty(password) || password.length()!=6)
        {
            userpassword.setError("ENTER VALID PASSWORD (upto 6 Characters");
            userpassword.requestFocus();
            return;
        }




        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            finish();
                            startActivity(new Intent(getApplicationContext(), HomePageActivity.class));
                          String userID= FirebaseAuth.getInstance().getCurrentUser().getUid();
                         uinfo = new UsersInfo(userID,uName,uCity,uPhone,email,password);
                         userRef.child(userID).setValue(uinfo);
                         Toast.makeText(getApplicationContext(), "User Added Successfully", Toast.LENGTH_LONG).show();
                         }
                        else
                            {
                                Toast.makeText(getApplicationContext(), "Registration Error ", Toast.LENGTH_LONG).show();

                        }

                    }
                });

    }
    @Override
    public void onBackPressed() {

        Toast.makeText(getApplicationContext(), "Please Finish Registration", Toast.LENGTH_LONG).show();
    }

}
