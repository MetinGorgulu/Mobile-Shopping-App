package com.example.buygo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class Register extends AppCompatActivity {


    EditText username,mail,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        username= findViewById(R.id.register_username);
        mail= findViewById(R.id.register_email);
        password= findViewById(R.id.register_password);


    }


    public void register(View view){

        String name = username.getText().toString();
        String email = mail.getText().toString();
        String pass = password.getText().toString();

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
        Toast.makeText(this,"Register Successful",Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Register.this,Login.class));

    }

    public void go_hh(View view){
        startActivity(new Intent(Register.this,Home.class));
    }

    public void go_seller_register(View view){
        startActivity(new Intent(Register.this,Regiser_seller.class));
    }

    public void login(View view){
        onBackPressed();
    }
}