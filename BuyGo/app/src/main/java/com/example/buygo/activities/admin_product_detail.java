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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.buygo.R;
import com.example.buygo.adapters.MyCartAdapter;
import com.example.buygo.models.AllProductsModel;
import com.example.buygo.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;
import java.util.Map;


public class admin_product_detail extends AppCompatActivity {


    ImageView detailedImg;

    TextView detailedName,description,price;
    Button updateButton,deleteButton;

    //All Products
    AllProductsModel allProductsModel = null;
    FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        final Object obj = getIntent().getSerializableExtra("detailed");

        if(obj instanceof AllProductsModel){
            allProductsModel = (AllProductsModel) obj;
        }
        updateButton = findViewById(R.id.admin_detail_update);
        detailedImg = findViewById(R.id.admin_detail_image);
        detailedName = findViewById(R.id.admin_detail_name);
        description = findViewById(R.id.admin_product_descrip);
        price = findViewById(R.id.admin_detail_price);
        deleteButton = findViewById(R.id.admin_detail_delete);

        if(allProductsModel != null){
            Glide.with(getApplicationContext()).load(allProductsModel.getImg_url()).into(detailedImg);
            detailedName.setText(allProductsModel.getName());
            description.setText(allProductsModel.getDescription());
            price.setText(String.valueOf(allProductsModel.getPrice()));
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                delete_item();

                startActivity(new Intent(admin_product_detail.this,MainActivity.class));
            }

        });


    }

    public void go_update(View view){
        Context context = this;
        Intent intent = new Intent(context, admin_product_update.class);
        intent.putExtra("detailed",allProductsModel);
        context.startActivity(intent);
    }

    public void delete_item(){



        String productName = allProductsModel.getName();

        firestore.collection("AllProducts")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            String compareName = (String) document.getString("name");
                            if(deney (productName, compareName )){
                                Log.d("DEBUG", "olumlu");

                                deleteFromAllCarts(productName);
                                String key = document.getId();
                                deleteFromAllFavorites(key);
                                deleteProduct(document.getId());

                            }


                        }
                    } else {
                        // Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });




    }

    private void deleteProduct(String delete){
        firestore.collection("AllProducts")
                .document(delete)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

    }
    private boolean deney(String a, String b) {
        Log.d("DEBUG", "a: " + a);
        Log.d("DEBUG", "b: " + b);

        if (a.equals(b)) {
            Log.d("DEBUG", "girdim");
            return true;
        } else {
            return false;
        }
    }
    private void deleteFromAllFavorites(String key){

        Log.d("DEBUG", "deleteFromAllCarts");

        CollectionReference productsRef = firestore.collection("CurrentUser");
        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Belge listesini al

                QuerySnapshot querySnapshot = task.getResult();
                Log.d("DEBUG", "FirebaseCurrentUser");



                // Belge listesini döngü ile gezip "AddToCart" koleksiyonlarına eriş
                for (QueryDocumentSnapshot document : querySnapshot) {
                    // Her belgenin ID'sini al
                    String usersId = document.getId();
                    Log.d("DEBUG", "User Id:" +usersId);

                    // "AddToCart" koleksiyonunu temsil eden referans
                    CollectionReference addToCartRef = productsRef.document(usersId)
                            .collection("Favorites");

                    // "AddToCart" koleksiyonundaki bütün belgeleri al
                    addToCartRef.get().addOnCompleteListener(cartTask -> {

                        if (cartTask.isSuccessful()) {
                            // "AddToCart" koleksiyonundaki belge listesini al
                            QuerySnapshot cartQuerySnapshot = cartTask.getResult();

                            // Belge listesini döngü ile gezip işlemleri yapabilirsiniz
                            for (QueryDocumentSnapshot cartDocument : cartQuerySnapshot) {
                                // İlgili belge üzerinde işlemleri gerçekleştirin
                                Log.d("DEBUG", "Cart Id:" +cartDocument.getId());

                                String cartItemName = (String) cartDocument.get("productKey");
                                Log.d("Tag","" + cartItemName);
                                if(deney(cartItemName,key)){
                                    deleteThisFavoriItem(cartDocument.getId(), usersId);
                                }
                            }
                        } else {
                            // "AddToCart" koleksiyonunu alma hatası
                        }
                    });
                }
            } else {
                // "Products" koleksiyonunu alma hatası
            }
        });
    }
    private void deleteFromAllCarts(String productName){


        final MyCartModel[] mycartmodel = new MyCartModel[1];
        MyCartAdapter cartAdapter;
        mycartmodel[0] = new MyCartModel();

        Log.d("DEBUG", "deleteFromAllCarts");

            CollectionReference productsRef = firestore.collection("CurrentUser");
            productsRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Belge listesini al

                    QuerySnapshot querySnapshot = task.getResult();
                    Log.d("DEBUG", "FirebaseCurrentUser");



                    // Belge listesini döngü ile gezip "AddToCart" koleksiyonlarına eriş
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        // Her belgenin ID'sini al
                        String usersId = document.getId();
                        Log.d("DEBUG", "User Id:" +usersId);

                        // "AddToCart" koleksiyonunu temsil eden referans
                        CollectionReference addToCartRef = productsRef.document(usersId)
                                .collection("AddToCart");

                        // "AddToCart" koleksiyonundaki bütün belgeleri al
                        addToCartRef.get().addOnCompleteListener(cartTask -> {

                            if (cartTask.isSuccessful()) {
                                // "AddToCart" koleksiyonundaki belge listesini al
                                QuerySnapshot cartQuerySnapshot = cartTask.getResult();

                                // Belge listesini döngü ile gezip işlemleri yapabilirsiniz
                                for (QueryDocumentSnapshot cartDocument : cartQuerySnapshot) {
                                    // İlgili belge üzerinde işlemleri gerçekleştirin
                                    Log.d("DEBUG", "Cart Id:" +cartDocument.getId());

                                    String cartItemName = (String) cartDocument.get("productName");
                                    Log.d("Tag","ananai sikim"+ cartItemName);
                                    if(deney(cartItemName,productName)){
                                        deleteThisCartItem(cartDocument.getId(), usersId);
                                    }
                                }
                            } else {
                                // "AddToCart" koleksiyonunu alma hatası
                            }
                        });
                    }
                } else {
                    // "Products" koleksiyonunu alma hatası
                }
            });






        }




        private void deleteThisFavoriItem(String delete,String id){
            firestore.collection("CurrentUser")
                    .document(id)
                    .collection("Favorites")
                    .document(delete)
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


        }
    private void deleteThisCartItem(String delete,String id) {
        firestore.collection("CurrentUser")
                .document(id)
                .collection("AddToCart")
                .document(delete)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }




}