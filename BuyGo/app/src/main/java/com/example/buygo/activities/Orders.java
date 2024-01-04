package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.example.buygo.R;
import com.example.buygo.adapters.MyFavoritesAdapter;
import com.example.buygo.adapters.OrdersAdapter;
import com.example.buygo.models.AddressModel;
import com.example.buygo.models.MyFavoritesModel;
import com.example.buygo.models.OrdersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class Orders extends AppCompatActivity {

    RecyclerView OrdersRecyclerview;
    OrdersAdapter ordersAdapter;
    List<OrdersModel> ordersModelList;
    FirebaseFirestore firestore ;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        OrdersRecyclerview = findViewById(R.id.orders_rec);
        firestore = FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();

        OrdersRecyclerview.setLayoutManager(new GridLayoutManager(this,1));
        ordersModelList = new ArrayList<>();
        ordersAdapter = new OrdersAdapter(this, ordersModelList);
        OrdersRecyclerview.setAdapter(ordersAdapter);


        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrders")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                OrdersModel ordersModel = document.toObject(OrdersModel.class);
                                ordersModelList.add(ordersModel);
                                ordersAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });


    }
}