package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.buygo.R;
import com.example.buygo.models.AllProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthMultiFactorException;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;

    TextView name,description,price,quantity;
    Button addToCart,buyNow;
    ImageView addItems,removeItems;

    //All Products
    AllProductsModel allProductsModel = null;
    FirebaseAuth auth;
    private FirebaseFirestore firestore;

    int totalQuantity=1;
    int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        final Object obj = getIntent().getSerializableExtra("detailed");

        if(obj instanceof AllProductsModel){
            allProductsModel = (AllProductsModel) obj;
         }
        addToCart = findViewById(R.id.add_to_cart);
        detailedImg = findViewById(R.id.detailed_image);
        name = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        quantity = findViewById(R.id.quantity);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);


        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((totalQuantity<10)){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                    if(allProductsModel != null){
                        totalPrice = allProductsModel.getPrice() * totalQuantity;
                    }
                }
                else
                    Toast.makeText(DetailedActivity.this,R.string.max_quantity_10,Toast.LENGTH_SHORT).show();


            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((totalQuantity>1)){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }

            }
        });
        // All Products
        if(allProductsModel != null){
            Glide.with(getApplicationContext()).load(allProductsModel.getImg_url()).into(detailedImg);
            name.setText(allProductsModel.getName());
            description.setText(allProductsModel.getDescription());
            price.setText(String.valueOf(allProductsModel.getPrice()));
            totalPrice = allProductsModel.getPrice() * totalQuantity;
        }
        addToCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                addtoCart();
            }
                                      }
        );

    }

    private void addtoCart() {

        String saveCurrentTime,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd, MM ,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String , Object > cartMap = new HashMap<>();
        cartMap.put("productName", name.getText().toString());
        cartMap.put("productPrice" , price.getText().toString());
        cartMap.put("currentTime" , saveCurrentTime);
        cartMap.put("currentDate" , saveCurrentDate);
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);
        firestore.collection("AddToCart").document(auth.getCurrentUser().getUid())
                .collection("User").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart ", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });

    }
}