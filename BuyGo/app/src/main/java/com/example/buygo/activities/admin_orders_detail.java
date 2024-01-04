package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.buygo.R;
import com.example.buygo.adapters.OrdersDetailAdapter;
import com.example.buygo.models.OrdersDetailModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class admin_orders_detail extends AppCompatActivity {

    private String orderId,userId;
    long genelToplam = 0 ;
    TextView overAllAmount;
    RecyclerView recyclerView;
    List<OrdersDetailModel> ordersDetailModelList;
    OrdersDetailAdapter ordersDetailAdapter;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_detail);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Object obj = getIntent().getSerializableExtra("detailId");
        orderId = (String)obj;
        Object obj2 = getIntent().getSerializableExtra("userId");
        userId = (String)obj2;


        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        overAllAmount = findViewById(R.id.orders_detail_total_price);

        recyclerView = findViewById(R.id.orders_detail_rec);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));

        ordersDetailModelList = new ArrayList<>();

        ordersDetailAdapter = new OrdersDetailAdapter(this,ordersDetailModelList);

        recyclerView.setAdapter(ordersDetailAdapter);
        firestore.collection("CurrentUser").document(userId)
                .collection("MyOrders").document(orderId).collection("Carts")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        if(task.isSuccessful()){

                            for(QueryDocumentSnapshot document : task.getResult()){

                                String productName = document.getString("productName");
                                String totalQuantity = document.getString("totalQuantity");
                                String img_url = document.getString("img_url");
                                String productPrice = document.getString("productPrice");
                                int totalPrice = document.getDouble("totalPrice").intValue();
                                genelToplam += document.getDouble("totalPrice").intValue();

                                OrdersDetailModel myOrdersDetailModel =new OrdersDetailModel(productName,totalQuantity,img_url,productPrice,totalPrice);



                                //OrdersDetailModel myOrdersDetailModel;
                                //myOrdersDetailModel = document.toObject(OrdersDetailModel.class);
                                ordersDetailModelList.add(myOrdersDetailModel);
                                ordersDetailAdapter.notifyDataSetChanged();
                                overAllAmount.setText(String.valueOf(genelToplam) );


                            }
                        }

                    }
                });


    }

}