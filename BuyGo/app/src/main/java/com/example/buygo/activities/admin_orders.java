package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.buygo.R;
import com.example.buygo.adapters.AdminOrdersAdapter;
import com.example.buygo.adapters.OrdersAdapter;
import com.example.buygo.models.AdminOrdersModel;
import com.example.buygo.models.OrdersModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class admin_orders extends AppCompatActivity {


    RecyclerView OrdersRecyclerview;
    AdminOrdersAdapter adminordersAdapter;
    List<AdminOrdersModel> adminordersModelList;
    FirebaseFirestore firestore ;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_orders);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        OrdersRecyclerview = findViewById(R.id.admin_orders_recycler);
        firestore = FirebaseFirestore.getInstance();
        auth= FirebaseAuth.getInstance();

        OrdersRecyclerview.setLayoutManager(new GridLayoutManager(this,1));
        adminordersModelList = new ArrayList<>();
        adminordersAdapter = new AdminOrdersAdapter(this, adminordersModelList);
        OrdersRecyclerview.setAdapter(adminordersAdapter);
        getAllUsers();


    }

    public void getAllUsers(){

        firestore.collection("CurrentUser")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {

                            controlMyOrders(document.getId());

                        }
                    } else {
                        // Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void controlMyOrders(String id){

        firestore.collection("CurrentUser").document(id).collection("MyOrders").get().addOnCompleteListener(task ->{
            if(task.isSuccessful()){
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot != null && !querySnapshot.isEmpty()) {
                    getAllOrders(id);
                }
            }
        });


    }
    public void getAllOrders(String id){

        firestore.collection("CurrentUser").document(id)
                .collection("MyOrders")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                AdminOrdersModel adminordersModel = document.toObject(AdminOrdersModel.class);
                                adminordersModelList.add(adminordersModel);
                                adminordersAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });

    }
    public void yazdir(String a){
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }


}