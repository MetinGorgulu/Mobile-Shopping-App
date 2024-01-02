package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.buygo.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;


public class Register extends AppCompatActivity {


    EditText username,mail,password;
    private FirebaseAuth auth;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();

        firestore = FirebaseFirestore.getInstance();
        username= findViewById(R.id.register_username);
        mail= findViewById(R.id.register_email);
        password= findViewById(R.id.register_password);


    }


    public void register(View view){

        String name = username.getText().toString();
        String email = mail.getText().toString();
        String pass = password.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this,"Enter Name!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Enter E-Mail Address!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(pass)){
            Toast.makeText(this,"Enter Password!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.length()<6){
            Toast.makeText(this,"Password too short, enter minimum 6 characters!",Toast.LENGTH_SHORT).show();
            return;
        }
        auth.createUserWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful())
                                {
                                    Toast.makeText(Register.this , "Successfully Register",Toast.LENGTH_SHORT).show();

                                    final HashMap<String , Object > cartMap = new HashMap<>();
                                    cartMap.put("username", name);
                                    cartMap.put("userMail" , email);
                                    cartMap.put("userPassword" , pass);
                                    cartMap.put("role", "customer");
                                    firestore.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                                            .collection("UsersInformation").add(cartMap).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                                                @Override
                                                public void onComplete(@NonNull Task<DocumentReference> task) {
                                                    finish();

                                                }
                                            });
                                    startActivity(new Intent(Register.this, MainActivity.class));
                                }
                                else
                                {
                                    Toast.makeText(Register.this , "Registration Failed" + task.getException(),Toast.LENGTH_SHORT).show();
                                }
                            }
                        });


    }

    public void go_seller_register(View view){
        startActivity(new Intent(Register.this,Regiser_seller.class));
    }

    public void login(View view){
        onBackPressed();
    }
}