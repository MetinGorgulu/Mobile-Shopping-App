package com.example.buygo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.button.MaterialButton;

import java.util.Locale;

public class Settings extends AppCompatActivity {

    private void setlang(Activity activity, String localeCode){
        Locale locale = new Locale(localeCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration conf = resources.getConfiguration();
        conf.setLocale(locale);
        resources.updateConfiguration(conf,resources.getDisplayMetrics());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getApplicationContext();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        MaterialButton eng = (MaterialButton) findViewById(R.id.language_en);
        MaterialButton tr = (MaterialButton) findViewById(R.id.language_tr);

        eng.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setlang(Settings.this,"en");
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }

        });

        tr.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {
                setlang(Settings.this,"tr");
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                // myIntent.setAction(Intent.ACTION_SEND);
                myIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(myIntent);
            }

        });

    }


}