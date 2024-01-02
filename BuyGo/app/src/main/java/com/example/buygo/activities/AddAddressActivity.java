package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buygo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class AddAddressActivity extends AppCompatActivity {


    EditText name,address,city,postalCode,phoneNumber;
    Button addAddressbtn;

    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();


        name=findViewById(R.id.add_address_name);
        address=findViewById(R.id.add_address_lane);
        city=findViewById(R.id.add_address_city);
        postalCode=findViewById(R.id.add_address_postal);
        phoneNumber=findViewById(R.id.add_address_phone);
        addAddressbtn = findViewById(R.id.add_add_address_button);

        addAddressbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userAddressName = name.getText().toString();
                String userCity = city.getText().toString();
                String userAddress = address.getText().toString();
                String userCode = postalCode.getText().toString();
                String userNumber = phoneNumber.getText().toString();

                if(!userAddressName.isEmpty() && !userCity.isEmpty() && !userAddress.isEmpty() && !userCode.isEmpty() && !userNumber.isEmpty() ){
                    controlAddressName(userAddressName);


                }else {
                    Toast.makeText(AddAddressActivity.this, "Kindly Fill All Field", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
    private void controlAddressName(String addressName){


        CollectionReference allProductsCollection = firestore.collection("CurrentUser");

        allProductsCollection.document(auth.getCurrentUser().getUid())
                .collection("Address").get()
                .addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                boolean control= false;
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String dbName = (String) document.getString("addressName");
                    String compareName = (String) addressName;

                    if (deney(dbName,compareName)) {

                        Log.d("DEBUG", dbName + compareName);
                        Toast.makeText(this, "Zaten Address Adı İle Address Ekli", Toast.LENGTH_SHORT).show();
                        control = true;
                        break;
                    }

                }
                if(!control){
                    writeToDb ();
                }
            } else {
                Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

            }
        });




    }
    private void writeToDb (){

        String userAddressName = name.getText().toString();
        String userCity = city.getText().toString();
        String userAddress = address.getText().toString();
        String userCode = postalCode.getText().toString();
        String userNumber = phoneNumber.getText().toString();

        final HashMap<String , Object > cartMap = new HashMap<>();
        cartMap.put("addressName", userAddressName);
        cartMap.put("cityName" , userCity);
        cartMap.put("address" , userAddress);
        cartMap.put("postalCode" , userCode);
        cartMap.put("userNumber", userNumber);
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(AddAddressActivity.this, "Address Added", Toast.LENGTH_SHORT).show();
                        }
                        startActivity(new Intent(AddAddressActivity.this, AddressActivity.class));

                    }
                });
    }
    private boolean deney(String a,String b){

        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }

}