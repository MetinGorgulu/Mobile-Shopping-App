package com.example.buygo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class DetailedActivity extends AppCompatActivity {


    TextView quantity;
    ImageView addItems,removeItems;

    int totalQuantity=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        quantity = findViewById(R.id.quantity);
        addItems = findViewById(R.id.add_item);
        removeItems = findViewById(R.id.remove_item);


        addItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((totalQuantity<10)){
                    totalQuantity++;
                    quantity.setText(String.valueOf(totalQuantity));
                }
                else
                    Toast.makeText(DetailedActivity.this,R.string.max_quantity_10,Toast.LENGTH_SHORT).show();


            }
        });

        removeItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((totalQuantity>1)){
                    totalQuantity--;
                    quantity.setText(String.valueOf(totalQuantity));
                }

            }
        });

    }
}