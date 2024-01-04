package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.buygo.R;
import com.example.buygo.adapters.MyCartAdapter;
import com.example.buygo.models.MyCartModel;
import com.example.buygo.models.MyFavoritesModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    TextView total;
    EditText cardNumber, month, year, ccv;
    long totalAmount;
    Button paymentbtn;
    String addressName;
    String getusername ;
    long geneltoplam=0;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Object getAddressName = getIntent().getSerializableExtra("addressName");

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        getTotalAmount();
        totalAmount=geneltoplam;
        addressName = (String) getAddressName;
        total = findViewById(R.id.payment_total_price);

        paymentbtn = findViewById(R.id.payment_payment_button);
        cardNumber = findViewById(R.id.card_number);
        month = findViewById(R.id.card_month);
        year = findViewById(R.id.card_year);
        ccv = findViewById(R.id.card_ccv);


        paymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String cardNumbers = cardNumber.getText().toString();
                String cardMonth = month.getText().toString();
                String cardYear = year.getText().toString();
                String cardCcv = ccv.getText().toString();


                if (!cardNumbers.isEmpty() && !cardMonth.isEmpty() && !cardYear.isEmpty() && !cardCcv.isEmpty()) {
                    controlCardInformations(cardNumbers, cardMonth, cardYear, cardCcv);


                } else {
                    Toast.makeText(PaymentActivity.this, "Kindly Fill All Field", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }
    private void getTotalAmount(){
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("AddToCart").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                             geneltoplam+= document.getLong("totalPrice");
                            total.setText(String.valueOf(geneltoplam) + " $");
                        }
                    }
                });

    }

    private void controlCardInformations(String cardNum, String cardMon, String cardY, String cardC) {
        CollectionReference cardInformationsCollection = firestore.collection("CardInformations");

        cardInformationsCollection.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        boolean control = false;
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String cardNumber = (String) document.getString("cardNumber");
                            String cardMonth = (String) document.getString("cardMonth");
                            String cardYear = (String) document.getString("cardYear");
                            String cardCcv = (String) document.getString("cardCcv");
                            String compareCardNumber = (String) cardNum;
                            String compareCardMonth = (String) cardMon;
                            String compareCardYear = (String) cardY;
                            String compareCardCcv = (String) cardC;

                            if (deney(cardNumber, compareCardNumber) && deney(cardMonth, compareCardMonth) && deney(cardYear, compareCardYear) && deney(cardCcv, compareCardCcv)) {
                                control = true;
                                break;
                            }

                        }
                        if (control) {
                            getUserName();

                        } else {
                            Toast.makeText(this, "İşlem Başarısız", Toast.LENGTH_SHORT).show();
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
    private void completeOrder(String username){

        String saveCurrentTime,saveCurrentDate;
        Calendar calForDate = Calendar.getInstance();


        SimpleDateFormat currentDate = new SimpleDateFormat("dd.MM.yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());
        final HashMap<String , Object > cartMap = new HashMap<>();
        cartMap.put("Date", saveCurrentDate);
        cartMap.put("Time", saveCurrentTime);
        cartMap.put("Total_Price", total.getText());
        cartMap.put("UserName",username);
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrders").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            if (documentReference != null) {
                                String addedDocumentId = documentReference.getId();
                                AddCartsOrder(addedDocumentId);
                                //finish();
                            }
                        }


                    }
                });



    }
    private void deleteCart() {
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("AddToCart")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Koleksiyon içindeki tüm belgeleri al
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Her belgeyi sil
                                firestore.collection("CurrentUser")
                                        .document(auth.getCurrentUser().getUid())
                                        .collection("AddToCart")
                                        .document(document.getId())
                                        .delete()
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
        goHome();
    }
    private void goHome(){
        Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent( this, Home.class);
        startActivity(intent);
    }
    private boolean isLoopExecuted = false;
    private void AddCartsOrder(String id){


        final MyCartModel[] mycartmodel = new MyCartModel[1];
        MyCartAdapter cartAdapter;
        mycartmodel[0] = new MyCartModel();
        if(!isLoopExecuted)
        {
            firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                    .collection("AddToCart")
                    .get().addOnCompleteListener (new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()&& !isLoopExecuted) {
                                List<DocumentSnapshot> documents = task.getResult().getDocuments();

                                // Sadece bir kez döngü yap
                                for (DocumentSnapshot document : documents) {
                                    MyCartModel pmycartmodel = document.toObject(MyCartModel.class);

                                    mycartmodel[0] = pmycartmodel;
                                    WriteCartsPayment(id, mycartmodel[0]);
                                    //finish();
                                }

                                isLoopExecuted = true;
                            } else {
                                Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
        }

    }
    private void WriteCartsPayment(String id,MyCartModel carts){
        final HashMap<String , Object > cartMap = new HashMap<>();
        cartMap.put("img_url" , carts.getImg_url());
        cartMap.put("productName", carts.getProductName());
        cartMap.put("productPrice", carts.getProductPrice());
        cartMap.put("totalQuantity", carts.getTotalQuantity());
        cartMap.put("totalPrice", carts.getTotalPrice());
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("MyOrders")
                .document(id)
                .collection("Carts")
                .add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            DocumentReference documentReference = task.getResult();
                            if (documentReference != null) {
                                String addedDocumentId = documentReference.getId();
                                AddCartsOrder(addedDocumentId);
                               // finish();
                            }
                        }


                    }
                });
        deleteCart();
    }

    public void getUserName(){

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("UsersInformation").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            getusername = (String)document.getString("username");
                            completeOrder(getusername);
                        }
                    }
                });


    }
    public void yazdir(String a){
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }
}