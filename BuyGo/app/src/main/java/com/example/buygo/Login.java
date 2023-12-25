package com.example.buygo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        username= findViewById(R.id.login_username);
        password= findViewById(R.id.login_password);

    }

    public void login(View view){

        String name = username.getText().toString();
        String pass = password.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Enter Name!",Toast.LENGTH_SHORT).show();
            return;
        }


        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Enter Password!",Toast.LENGTH_SHORT).show();
            return;
        }


        //   startActivity(new Intent(Register.this,MainActivity.class));

    }

    //register e gitmeli
    public void register(View view){
        startActivity(new Intent(Login.this,Register.class));
    }

}