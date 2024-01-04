package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.buygo.R;
import com.example.buygo.adapters.AllProductsAdapter;
import com.example.buygo.models.AllProductsModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Home extends AppCompatActivity {

    RecyclerView allProductRecyclerview;

    //All Products
    AllProductsAdapter allProductsAdapter;
    List<AllProductsModel> allProductsModelList;
    FirebaseFirestore db;
    FirebaseAuth auth;
    String id="My_Channel";
    boolean control=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("notifi","nottt", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }



        createNotificationChannel();

        allProductRecyclerview = findViewById(R.id.products_rec);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        if(auth.getCurrentUser()!=null &&!control){
            control =true;
            controlFavorite();
        }


        // bottomNavigationView.getMenu().getItem(R.id.menu_chart).setChecked(false);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);

        //   bottomNavigationView.setSelectedItemId(R.id.menu_home);
        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {

                case R.id.menu_home:

                    return true;
                case R.id.menu_chart:
                    if(auth.getCurrentUser()!=null){

                        startActivity(new Intent(getApplicationContext(), CartActivity.class));
                        finish();

                    }
                    else {
                        Toast.makeText(this, "Lütfen Önce Giriş Yapın.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), Login.class));

                    }
                    break;

                case R.id.menu_user:
                    if(auth.getCurrentUser()!=null){
                        startActivity(new Intent(getApplicationContext(), Profile.class));
                        finish();
                    }
                    else {
                        startActivity(new Intent(getApplicationContext(), Login.class));
                    }
                    break;
            }


            return false;
        });


        // Products
        allProductRecyclerview.setLayoutManager(new GridLayoutManager(this,2));
        allProductsModelList = new ArrayList<>();
        allProductsAdapter = new AllProductsAdapter(this, allProductsModelList);
        allProductRecyclerview.setAdapter(allProductsAdapter);

        db.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                AllProductsModel allProductsModel = document.toObject(AllProductsModel.class);
                                allProductsModelList.add(allProductsModel);
                                allProductsAdapter.notifyDataSetChanged();

                            }

                        } else {
                            Toast.makeText(Home.this, " " + task.getException(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });


    }

    public void controlFavorite(){

        db.collection("CurrentUser").document(auth.getCurrentUser().getUid())
                .collection("Favorites")
                .get().addOnCompleteListener(task ->  {
                    if (task.isSuccessful()) {

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String favoriteProductsKey = (String) document.getString("productKey");
                            String favoriteProductsPrice;
                            String sayi = document.getString("productPrice");

                            int number =Integer.parseInt(sayi);


                                compare(favoriteProductsKey, number, document.getId());



                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });


    }
    public void compare(String Allproductkey,int favoriproductPrice,String favoriProductKey ){

        db.collection("AllProducts").document(Allproductkey)
                .get().addOnCompleteListener(task ->  {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document= task.getResult();
                            //String allProductsKey = (String) document.getString(document.getId());
                            String allProductsPrice;

                            Long number =  document.getLong("price");
                            allProductsPrice  =String.valueOf(number);
                        Toast.makeText(this, Allproductkey, Toast.LENGTH_SHORT).show();

                            if(compareDown(number , favoriproductPrice  )){
                                updateFavoritePrice(favoriProductKey, allProductsPrice);
                                bildirim(1);
                            }
                            else if(compareUp(number,favoriproductPrice)){
                                updateFavoritePrice(favoriProductKey,allProductsPrice);
                                bildirim(2);
                            }




                    } else {
                        Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                    }
                });
    }
    public void updateFavoritePrice(String favorikey, String newPrice) {
        if (newPrice != null && !newPrice.isEmpty()) {
            try {
                // Güncellenecek veriyi bir Map nesnesi içinde tanımlayın
                Map<String, Object> updates = new HashMap<>();
                updates.put("productPrice", newPrice);

                // Belirtilen favorikey'e sahip belirli bir Favorites belgesini güncelle
                db.collection("CurrentUser")
                        .document(auth.getCurrentUser().getUid())
                        .collection("Favorites")
                        .document(favorikey)
                        .update(updates)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                // Güncelleme başarılı olduğunda yapılacak işlemler
                                Log.d("TAG", "Veri güncelleme başarılı!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Güncelleme başarısız olduğunda yapılacak işlemler
                                Log.e("TAG", "Veri güncelleme hatası: " + favorikey);
                                Log.e("TAG", "Veri güncelleme hatası: " + e.getMessage(), e);
                            }
                        });
            } catch (NumberFormatException e) {
                // newPrice uygun bir sayıya dönüştürülemezse burada işlem yapabilirsiniz
                Log.e("TAG", "Fiyatı güncelleme hatası: " + e.getMessage());
            }
        } else {
            // newPrice null veya boşsa burada işlem yapabilirsiniz
            Log.e("TAG", "Yeni fiyat boş veya null.");
        }
    }
    public boolean compareDown(Long a,int b){

        Log.d("TAG", toString().valueOf(a) );
        Log.d("TAG", toString().valueOf(b));
        if(a>= b){

            return false;
        }
        else {
            return true;
        }

    }
    public boolean compareUp(Long a,int b){
        Log.d("TAG", toString().valueOf(a) );
        Log.d("TAG", toString().valueOf(b));
        if(b< a){
            return true;
        }
        else{
            return false;
        }
    }





    public void bildirim(int x){
        NotificationCompat.Builder builder= new NotificationCompat.Builder(Home.this,id);
        builder.setContentTitle("Price Alert");
        if(x==1){
            builder.setContentText("Your Favorite product price is Down");
        }
        else if(x==2){
            builder.setContentText("Your Favorite product price is UP");
        }

        builder.setSmallIcon(R.drawable.dollar);
        builder.setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager!=null)
            notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "asdas";
            String description = "getString(sss)";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
    @Override
    public void onBackPressed() {
        finishAffinity();


    }
    public void yazdir(String a){
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }
}