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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity {

    TextView total;
    EditText cardNumber, month, year, ccv;
    long totalAmount;
    Button paymentbtn;
    String addressName;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Object obj = getIntent().getSerializableExtra("item");
        Object getAddressName = getIntent().getSerializableExtra("addressName");

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        addressName = (String) getAddressName;
        totalAmount = (long) obj;
        total = findViewById(R.id.payment_total_price);
        total.setText(String.valueOf(totalAmount) + " $");
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
                            Toast.makeText(this, "İşlem Başarılı", Toast.LENGTH_SHORT).show();
                            completeOrder();
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
    private void completeOrder(){

        String saveCurrentTime,saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd, MM ,yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        saveCurrentTime = currentTime.format(calForDate.getTime());

        final HashMap<String , Object > cartMap = new HashMap<>();
        cartMap.put("Date", saveCurrentDate);
        cartMap.put("Time", saveCurrentTime);
        cartMap.put("Total_Price" , total.getText());
        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("MyOrders").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        Toast.makeText(PaymentActivity.this, "Sipariş Tamamlandı ", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                });
    }
}