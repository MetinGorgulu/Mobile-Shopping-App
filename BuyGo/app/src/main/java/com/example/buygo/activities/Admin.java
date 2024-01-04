package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Admin extends AppCompatActivity {
    RecyclerView allProductRecyclerview;

    //All Products
    AllProductsAdapter allProductsAdapter;
    List<AllProductsModel> allProductsModelList;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_navigation);
        bottomNavigationView.setSelectedItemId(R.id.admin_home);

        Object obj = getIntent().getSerializableExtra("sayi");
        if(obj instanceof AllProductsModel){
            refreshActivity();
            startActivity(new Intent(Admin.this,MainActivity.class));
        }


        auth = FirebaseAuth.getInstance();
        allProductRecyclerview = findViewById(R.id.admin_products);
        db = FirebaseFirestore.getInstance();


        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.admin_home:
                    return true;

                case R.id.admin_profile:
                    if(auth.getCurrentUser()!=null){
                        startActivity(new Intent(getApplicationContext(), admin_profile.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                    break;
            }


            return false;
        });


        // admin ürünleri gelsin
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
                            Toast.makeText(Admin.this, " " + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }


    public void refreshActivity() {
        Object context = this;
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
    }

    public void go_add_item(View view){
        startActivity(new Intent(Admin.this,addmin_add_item.class));
    }
    @Override
    public void onBackPressed() {
        finishAffinity();


    }
}