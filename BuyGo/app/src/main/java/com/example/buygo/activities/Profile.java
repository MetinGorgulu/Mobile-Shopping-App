package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.buygo.R;
import com.example.buygo.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Profile extends AppCompatActivity {

    TextView userName, userMail;
    FirebaseFirestore firestore;
    FirebaseAuth auth;
    Button logOut;

    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.profile_username);
        userMail = findViewById(R.id.profile_usermail);
        logOut = findViewById(R.id.log_out);

        firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("UsersInformation")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        for(QueryDocumentSnapshot document : task.getResult()) {
                            Object userEmail = document.get("userMail");
                            Object name = document.get("username");
                            userMail.setText(String.valueOf(userEmail));
                            userName.setText(String.valueOf(name));
                        }
                    }
                });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                finishAffinity();

            }
        });





        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setSelectedItemId(R.id.menu_user);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.menu_home:
                    startActivity(new Intent(getApplicationContext(), Home.class));
                    finish();
                    return true;
                case R.id.menu_chart:
                    startActivity(new Intent(getApplicationContext(),CartActivity.class));
                    finish();
                    return true;
                case R.id.menu_user:
                    return true;
            }


            return false;
        });

    }

    public void go_settings(View view){
        startActivity(new Intent(Profile.this,Settings.class));
    }

    public void go_orders(View view){
        startActivity(new Intent(Profile.this, Orders.class));

    }

    public void go_favorites(View view){
        startActivity(new Intent(Profile.this,Favorites.class));

    }


}