package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public class DetailedActivity extends AppCompatActivity {

    ImageView detailedImg;

    TextView detailedName,description,price,quantity;
    Button addToCart,favorite;
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
        detailedName = findViewById(R.id.detailed_name);
        description = findViewById(R.id.detailed_desc);
        price = findViewById(R.id.detailed_price);
        quantity = findViewById(R.id.quantity);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);
        favorite = findViewById(R.id.detailed_favourite);


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
            detailedName.setText(allProductsModel.getName());
            description.setText(allProductsModel.getDescription());
            price.setText(String.valueOf(allProductsModel.getPrice()));
            totalPrice = allProductsModel.getPrice() * totalQuantity;
        }
        addToCart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                addtoCart();


            }
        });
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                favorite();

            }
        });

    }
    String productKey ;

    private void favorite() {
        if(auth.getCurrentUser()!=null){
            CollectionReference allProductsCollection = firestore.collection("AllProducts");

            allProductsCollection.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String productsName = (String) document.getString("name");
                        String compareName = (String) detailedName.getText();

                        if (deney(productsName,compareName)) {

                            productKey = document.getId();
                            controlFavorite();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        }
        else {
            Toast.makeText(this, "Lütfen Önce Giriş Yapın.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

    }
    private void controlFavorite(){


        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Favorites")
                .get().addOnCompleteListener(task ->  {
                    if (task.isSuccessful()) {
                        boolean ada= false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String favoriteProductsKey = (String) document.getString("productKey");
                            String compareKey = productKey;

                            if (controlFavorite(favoriteProductsKey,compareKey)) {
                                ada=true;
                                Toast.makeText(this, "Ürün Zaten Favorilerde", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }
                        if (!ada){
                            addToFavorites();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private boolean controlFavorite(String a, String b){


        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }

    private boolean deney(String a, String b) {
        Log.d("DEBUG", "a: " + a);
        Log.d("DEBUG", "b: " + b);

        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }
    private void addToFavorites (){
        final HashMap<String , Object > cartMap = new HashMap<>();
        cartMap.put("productKey", productKey);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Favorites").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To Favorites", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
    }

    String ProductName;
    private void addtoCart(){
        if(auth.getCurrentUser()!=null){
            CollectionReference allProductsCollection = firestore.collection("AllProducts");

            allProductsCollection.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        String productsName = (String) document.getString("name");
                        String compareName = (String) detailedName.getText();

                        if (deney(productsName,compareName)) {

                            ProductName = document.getString("name");
                            controlCart();
                            break;
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            });

        }
        else {
            Toast.makeText(this, "Lütfen Önce Giriş Yapın.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), Login.class));
        }

    }

    private void controlCart(){


        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart")
                .get().addOnCompleteListener(task ->  {
                    if (task.isSuccessful()) {
                        boolean ada= false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String cartProductsName = (String) document.getString("productName");
                            String comparename = ProductName;

                            if (controlFavorite(cartProductsName,comparename)) {
                                ada=true;
                                Toast.makeText(this, "Ürün Zaten Sepete Eklendi", Toast.LENGTH_SHORT).show();
                                break;
                            }

                        }
                        if (!ada){
                            addCart();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void addCart() {

        final HashMap<String , Object > cartMap = new HashMap<>();
        cartMap.put("productName", detailedName.getText().toString());
        cartMap.put("productPrice" , price.getText().toString());
        cartMap.put("img_url" , allProductsModel.getImg_url());
        cartMap.put("totalQuantity", quantity.getText().toString());
        cartMap.put("totalPrice", totalPrice);
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(DetailedActivity.this, "Added To A Cart ", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });



    }
}