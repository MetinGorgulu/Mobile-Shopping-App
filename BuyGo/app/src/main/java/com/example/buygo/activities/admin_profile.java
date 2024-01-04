package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.buygo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class admin_profile extends AppCompatActivity {

    TextView userName, userMail;
    FirebaseFirestore firestore;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        userName = findViewById(R.id.admin_username);
        userMail = findViewById(R.id.admin_mail);

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


        BottomNavigationView bottomNavigationView = findViewById(R.id.admin_profile_navigation);
        bottomNavigationView.setSelectedItemId(R.id.admin_profile);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.admin_home:
                    startActivity(new Intent(getApplicationContext(), Admin.class));
                    finish();
                    return true;
                case R.id.admin_profile:
                    return true;
            }


            return false;
        });
    }

    public void admin_go_settings(View view){
        startActivity(new Intent(admin_profile.this,Settings.class));
    }

    public void admin_go_orders(View view){
        startActivity(new Intent(admin_profile.this,admin_orders.class));
    }
    public void admin_log_out(View view){
        FirebaseAuth.getInstance().signOut();
        finishAffinity();
        Intent intent = new Intent(admin_profile.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}