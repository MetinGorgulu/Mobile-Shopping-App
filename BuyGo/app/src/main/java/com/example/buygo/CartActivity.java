package com.example.buygo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.View;
import android.view.WindowManager;

import com.example.buygo.databinding.ActivityCartBinding;
import com.example.buygo.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CartActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


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
                     // misafir ise alttaki satır çalışacak
                    if(true){
                        startActivity(new Intent(getApplicationContext(),Login.class));
                    }
                    // kullanıcı ise
                   // startActivity(new Intent(getApplicationContext(),Profile.class));
                    finish();
                    return true;
            }


            return false;
        });

    }

    public void buy_now(View view){
        startActivity(new Intent(CartActivity.this,AddressActivity.class));
    }


}
