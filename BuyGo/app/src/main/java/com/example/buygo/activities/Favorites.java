package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.buygo.R;
import com.example.buygo.adapters.AllProductsAdapter;
import com.example.buygo.adapters.MyFavoritesAdapter;
import com.example.buygo.models.AllProductsModel;
import com.example.buygo.models.MyFavoritesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Favorites extends AppCompatActivity {


    RecyclerView favoritesRecyclerview;

    Button buttonSelectFavorite;

    //All Products
    MyFavoritesAdapter favoritesAdapter;
    List<MyFavoritesModel> favoritesModelList;
    FirebaseFirestore firestore ;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        favoritesRecyclerview = findViewById(R.id.favorites_cart_rec);
        firestore = FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();

        favoritesRecyclerview.setLayoutManager(new GridLayoutManager(this,2));
        favoritesModelList = new ArrayList<>();
        favoritesAdapter = new MyFavoritesAdapter(this, favoritesModelList);
        favoritesRecyclerview.setAdapter(favoritesAdapter);




        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Favorites")
                .get().addOnCompleteListener(task ->  {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String favoriteProductsKey = (String) document.getString("productKey");

                            getir(favoriteProductsKey);

                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
    private void getir(String favoriteProductKey){

        CollectionReference favoritesCollection = firestore.collection("AllProducts");

        favoritesCollection.get().addOnCompleteListener(task -> {
            String productsKey;
            String favoriteProductsKey = (String) favoriteProductKey;
            if (task.isSuccessful()) {

                for (QueryDocumentSnapshot document : task.getResult()) {
                    productsKey = (String) document.getId();

                    if (deney(productsKey,favoriteProductsKey)) {


                        MyFavoritesModel myFavoritesModel = document.toObject(MyFavoritesModel.class);

                        favoritesModelList.add(myFavoritesModel);

                        favoritesAdapter.notifyDataSetChanged();
                        break;
                    }

                }
            } else {
                Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }
    private boolean deney(String a, String b) {


        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }


}