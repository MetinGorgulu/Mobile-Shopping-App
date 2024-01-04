package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buygo.R;
import com.example.buygo.adapters.MyCartAdapter;
import com.example.buygo.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.checkerframework.checker.i18n.qual.LocalizableKeyBottom;

import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    long genelToplam ;
    Button payment;
    TextView overAllAmount;
    RecyclerView recyclerView;
    List<MyCartModel> cartModelList;
    MyCartAdapter cartAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        overAllAmount = findViewById(R.id.cart_total_price);
        recyclerView = findViewById(R.id.my_cart_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        cartModelList = new ArrayList<>();
        cartAdapter = new MyCartAdapter(this,cartModelList);
        recyclerView.setAdapter(cartAdapter);
        payment = findViewById(R.id.cart_buy_button);


        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){


                                MyCartModel myCartModel = document.toObject(MyCartModel.class);
                                cartModelList.add(myCartModel);
                                cartAdapter.notifyDataSetChanged();

                                Object totalPriceObj = document.get("totalPrice");
                                genelToplam += (long) totalPriceObj;

                                overAllAmount.setText(String.valueOf(genelToplam));
                            }
                        }
                    }
                });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView3);
        bottomNavigationView.setSelectedItemId(R.id.menu_chart);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.menu_home:
                    startActivity(new Intent(getApplicationContext(),Home.class));
                    finish();
                    return true;
                case R.id.menu_chart:
                    return true;
                case R.id.menu_user:
                    startActivity(new Intent(getApplicationContext(),Profile.class));
                    finish();
                    return true;
            }


            return false;
        });
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            if(genelToplam == 0){
                Toast.makeText(CartActivity.this, "Sepetiniz Boş ", Toast.LENGTH_SHORT).show();

            }
            else {
                Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                startActivity(intent);
            }

            }
        });


    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);

    }


}