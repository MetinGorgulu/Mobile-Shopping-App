package com.example.buygo.activities;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Instrumentation;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.example.buygo.R;
import com.example.buygo.adapters.MyCartAdapter;
import com.example.buygo.models.AllProductsModel;
import com.example.buygo.models.MyCartModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

public class admin_product_update extends AppCompatActivity {


    Uri imageUri;
    ImageView img1;
    EditText name,price, descrip;
    AllProductsModel allProductsModel = null;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    FirebaseStorage storage;
    StorageReference storageReference;
    String storageName;
    boolean imgUploaded;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_product_update);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        imgUploaded = false;

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference= storage.getReference();

        final Object obj = getIntent().getSerializableExtra("detailed");

        if(obj instanceof AllProductsModel){
            allProductsModel = (AllProductsModel) obj;

        }

        img1 = findViewById(R.id.update_image);
        descrip = findViewById(R.id.admin_product_descrip_update);
        name = findViewById(R.id.update_name);
        price = findViewById(R.id.update_price);

        String gettingPrice = String.valueOf(allProductsModel.getPrice());
        name.setText(allProductsModel.getName());
        price.setText(gettingPrice);
        descrip.setText(allProductsModel.getDescription());
        Glide.with(this)
                .load(allProductsModel.getImg_url())
                .into(img1);
        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
                imgUploaded = true;
            }
        });

    }

    private void choosePicture() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1 && resultCode == RESULT_OK && data!=null && data.getData()!=null){
            imageUri = data.getData();
            img1.setImageURI(imageUri);
            uploadPicture();
        }
    }

    private void uploadPicture() {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final String randomKey = UUID.randomUUID().toString();
        StorageReference photoRef = storageReference.child("images"+ randomKey);
        storageName = "images"+randomKey;
        Log.d("TAG",storageName);
        photoRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>(){
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                        pd.dismiss();;
                        Snackbar.make(findViewById(android.R.id.content),"Image Uploaded." , Snackbar.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "Failed To Upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercent = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Percentage: "+ (int) progressPercent + "%");
                    }
                });



    }

    public void send_update_database(View view){
        firestore.collection("AllProducts")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String productName = allProductsModel.getName();
                                String compareProductName = document.getString("name");
                                deleteFromAllCarts(productName);
                                if(deney(productName,compareProductName)){
                                    updateThis(document.getId());
                                    if(imgUploaded){
                                        updateUrl(document.getId());
                                    }
                                }

                            }

                        }
                    }
                });


        Intent intent = new Intent(admin_product_update.this, Admin.class);
        intent.putExtra("sayi", allProductsModel);
        startActivity(intent);

    }



    private void updateThis(String id){
        DocumentReference documentRef = firestore.collection("AllProducts").document(id);
        String nameUpdate = name.getText().toString();
        String convertPrice = price.getText().toString();
        String descripUpdate = descrip.getText().toString();

        long priceUpdate = Long.parseLong(convertPrice);
        Map<String, Object> updates = new HashMap<>();
        updates.put("name", nameUpdate);
        updates.put("price", priceUpdate);
        updates.put("description", descripUpdate);

        documentRef.update(updates)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }
    private void updateUrl(String id){

        FirebaseStorage storage1 = FirebaseStorage.getInstance();
        StorageReference storageRef2 = storage1.getReference();

        StorageReference dosyaRef = storageRef2.child(storageName);

        dosyaRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            UpdateimageUrl(imageUrl, id);

        }).addOnFailureListener(exception -> {

        });
    }
    private void UpdateimageUrl(String imageUrl,String id){

        final HashMap<String , Object > cartMap = new HashMap<>();

        cartMap.put("img_url", imageUrl);


        firestore.collection("AllProducts")
                .document(id)
                .update(cartMap)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });
    }


    private void deleteFromAllCarts(String productName){


        final MyCartModel[] mycartmodel = new MyCartModel[1];
        MyCartAdapter cartAdapter;
        mycartmodel[0] = new MyCartModel();


        CollectionReference productsRef = firestore.collection("CurrentUser");
        productsRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {


                QuerySnapshot querySnapshot = task.getResult();
                Log.d("DEBUG", "FirebaseCurrentUser");



                // Belge listesini döngü ile gezip "AddToCart" koleksiyonlarına eriş
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String usersId = document.getId();

                    CollectionReference addToCartRef = productsRef.document(usersId)
                            .collection("AddToCart");

                    addToCartRef.get().addOnCompleteListener(cartTask -> {

                        if (cartTask.isSuccessful()) {

                            QuerySnapshot cartQuerySnapshot = cartTask.getResult();

                            for (QueryDocumentSnapshot cartDocument : cartQuerySnapshot) {

                                String cartItemName = (String) cartDocument.get("productName");
                                if(deney(cartItemName,productName)){
                                    deleteThisCartItem(cartDocument.getId(), usersId);
                                }
                            }
                        } else {
                            // "AddToCart" koleksiyonunu alma hatası
                        }
                    });
                }
            } else {
                // "Products" koleksiyonunu alma hatası
            }
        });

    }
    private void deleteThisCartItem(String delete,String id) {
        firestore.collection("CurrentUser")
                .document(id)
                .collection("AddToCart")
                .document(delete)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });


    }
    private boolean deney(String a, String b) {
        Log.d("DEBUG", "a: " + a);
        Log.d("DEBUG", "b: " + b);

        if (a.equals(b)) {
            Log.d("DEBUG", "girdim");
            return true;
        } else {
            return false;
        }
    }
}