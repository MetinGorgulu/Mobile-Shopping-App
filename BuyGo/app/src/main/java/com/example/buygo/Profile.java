package com.example.buygo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Profile extends AppCompatActivity {



    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

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

    public void exit_app(View view){
//firebase bağlantı kes
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