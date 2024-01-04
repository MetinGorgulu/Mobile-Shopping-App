package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.buygo.R;
import com.example.buygo.adapters.AddressAdapter;
import com.example.buygo.models.AddressModel;
import com.example.buygo.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AddressActivity extends AppCompatActivity implements AddressAdapter.SelectedAddress {

    private Button addAddress;
    private RecyclerView recyclerView;

    private List<AddressModel> addressModelList;
    private AddressAdapter addressAdapter;
    private Button paymentButton;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;
    private String mAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        recyclerView = findViewById(R.id.address_rec);
        paymentButton = findViewById(R.id.payment_button);
        addAddress = findViewById(R.id.add_address_button);
        Object obj = getIntent().getSerializableExtra("item");

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        addressModelList = new ArrayList<>();
        addressAdapter = new AddressAdapter(this, addressModelList, this);
        recyclerView.setAdapter(addressAdapter);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Address")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AddressModel addressModel = document.toObject(AddressModel.class);
                                addressModelList.add(addressModel);
                                addressAdapter.notifyDataSetChanged();

                            }
                        }
                    }
                });

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (addressModelList.isEmpty()){
                    Toast.makeText(AddressActivity.this, "Kayıtlı Address Yok", Toast.LENGTH_SHORT).show();
                }
                else {
                    long amount = (long) obj;
                    Intent intent = new Intent(AddressActivity.this, PaymentActivity.class);
                    intent.putExtra("totalAmount", amount);
                    intent.putExtra("addressName" ,mAddress);
                    startActivity(intent);
                }




            }
        });

        addAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddressActivity.this, AddAddressActivity.class));
            }
        });

    }
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }
    @Override
    public void setAddressName(String addressName) {
        mAddress = addressName;
    }
}