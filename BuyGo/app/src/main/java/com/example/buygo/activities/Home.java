package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.buygo.R;
import com.example.buygo.adapters.AllProductsAdapter;
import com.example.buygo.models.AllProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class Home extends AppCompatActivity {

    RecyclerView allProductRecyclerview;

    //All Products
    AllProductsAdapter allProductsAdapter;
    List<AllProductsModel> allProductsModelList;
    Toolbar toolbar;
    FirebaseFirestore db;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        allProductRecyclerview = findViewById(R.id.products_rec);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // bottomNavigationView.getMenu().getItem(R.id.menu_chart).setChecked(false);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        //   bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.menu_home:
                    return true;
                case R.id.menu_chart:
                    if(auth.getCurrentUser()!=null){
                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        finish();

                    }
                    else {
                        Toast.makeText(this, "Lütfen Önce Giriş Yapın.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));

                    }
                    break;

                case R.id.menu_user:
                    if(auth.getCurrentUser()!=null){
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                    break;
            }


            return false;
        });


        // Products
        allProductRecyclerview.setLayoutManager(new GridLayoutManager(this,2));
        allProductsModelList = new ArrayList<>();
        allProductsAdapter = new AllProductsAdapter(this, allProductsModelList);
        allProductRecyclerview.setAdapter(allProductsAdapter);

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AllProductsModel allProductsModel = document.toObject(AllProductsModel.class);
                                allProductsModelList.add(allProductsModel);
                                allProductsAdapter.notifyDataSetChanged();

                            }

                        } else {
                            Toast.makeText(Home.this, " " + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    @Override
    public void onBackPressed() {
        finishAffinity();


    }
}