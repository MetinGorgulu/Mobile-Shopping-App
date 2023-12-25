package com.example.buygo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

public class AddAddressActivity extends AppCompatActivity {


    EditText name,address,city,postalCode,phoneNumber;
    Button addAddressbtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name=findViewById(R.id.add_address_name);
        address=findViewById(R.id.add_address_lane);
        city=findViewById(R.id.add_address_city);
        postalCode=findViewById(R.id.add_address_postal);
        phoneNumber=findViewById(R.id.add_address_phone);



    }
}