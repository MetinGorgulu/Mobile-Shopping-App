package com.example.buygo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.example.buygo.databinding.ActivityHomeBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Home extends AppCompatActivity {


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

       // bottomNavigationView.getMenu().getItem(R.id.menu_chart).setChecked(false);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

     //   bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){

                case R.id.menu_home:
                    return true;
                case R.id.menu_chart:
                    startActivity(new Intent(getApplicationContext(),CartActivity.class));
                    finish();
                    return true;
                case R.id.menu_user:
                     // misafir ise alttaki satır çalışacak
                    if(true){
                        startActivity(new Intent(getApplicationContext(),Login.class));
                    }
                    // kullanıcı ise alttaki satır çalışacak
                    // startActivity(new Intent(getApplicationContext(),Profile.class));
                    finish();
                    return true;
            }


            return false;
        });




    }




}
