package com.example.shahbaz.car4rent;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class AdminDialogue extends AppCompatDialogFragment {
    EditText username;
    EditText userpassword;
    private DialogueListener listener;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();




        View view = inflater.inflate(R.layout.admindialogue, null);
        builder.setView(view).setTitle("Login as Admin").setNegativeButton("Canel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        }).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
String name=username.getText().toString();
String password=userpassword.getText().toString();
listener.applyTexts(name,password);


            }
        });

        username=(EditText)view.findViewById(R.id.editTextusername);
        username.setCursorVisible(true);
        userpassword=(EditText)view.findViewById(R.id.editTextpassword);
        userpassword.setCursorVisible(true);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener=(DialogueListener)context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    public interface DialogueListener{
        void applyTexts(String username,String Password);


    }

}
