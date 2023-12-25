package com.example.buygo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class Regiser_seller extends AppCompatActivity {

    EditText sel_name,sel_mail,sel_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser_seller);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        sel_name= findViewById(R.id.seller_name);
        sel_mail= findViewById(R.id.seller_email);
        sel_pass= findViewById(R.id.seller_password);

    }

    public void seller_register(View view){

        String name = sel_name.getText().toString();
        String email = sel_mail.getText().toString();
        String pass = sel_pass.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Enter Name!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter E-Mail Address!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Enter Password!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.length()<6){
            Toast.makeText(this,"Password too short, enter minimum 6 characters!",Toast.LENGTH_SHORT).show();
            return;
        }

        //   startActivity(new Intent(Register.this,MainActivity.class));

    }


    public void login(View view){
        Intent myIntent = new Intent(Regiser_seller.super.getApplicationContext(), Login.class);
        startActivity(myIntent);
    }

}