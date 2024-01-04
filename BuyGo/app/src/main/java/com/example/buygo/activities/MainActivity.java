package com.example.buygo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.buygo.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    FirebaseFirestore firestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        auth= FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        if(auth.getCurrentUser()!=null){

            adminControl();

        }
        else {
            noAdmin();
        }




    }
    private void adminControl(){


        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("UsersInformation").get().addOnCompleteListener(task ->  {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {


                            String userRole = (String) document.getString("role");

                            Boolean isaAdmin = compare(userRole);
                           if(isaAdmin){


                               Intent intent = new Intent(MainActivity.this,Admin.class);
                               startActivity(intent);
                               finish();


                               break;

                           }
                           else {
                               noAdmin();

                           }
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
    private boolean compare(String userRole){

        if(userRole.equals("admin")){
            return true;
        }
        else {
            return false;
        }



    }
    private void noAdmin(){


        Intent intent = new Intent(MainActivity.this,Home.class);
        startActivity(intent);
        finish();
    }

}