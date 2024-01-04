package com.example.buygo.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.buygo.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import javax.annotation.Nullable;

public class addmin_add_item extends AppCompatActivity {


    Uri imageUri;
    ImageView img_add;
    EditText name,price, descrip;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth auth;
    FirebaseFirestore firestore;
    String imageUrl;
    String storageName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addmin_add_item);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        storage = FirebaseStorage.getInstance();
        storageReference= storage.getReference();


        img_add = findViewById(R.id.add_image);
        name = findViewById(R.id.add_name);
        descrip = findViewById(R.id.admin_add_item_descrip);
        price = findViewById(R.id.add_price);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choosePicture();
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
            img_add.setImageURI(imageUri);
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

    public void addInformations(){
        Map<String, Object> cartMap = new HashMap<>();
        cartMap.put("price" ,  Long.parseLong(price.getText().toString()));
        cartMap.put("description", descrip.getText().toString());
        cartMap.put("name", name.getText().toString());
        // Belgeyi koleksiyona ekle
        firestore.collection("AllProducts").add(cartMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        // Belge ekleme başarılı olduğunda yapılacak işlemler
                        Log.d("Firestore", "Belge eklendi. Belge ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Belge ekleme başarısız olduğunda yapılacak işlemler
                        Log.w("Firestore", "Belge eklenirken hata oluştu", e);
                    }
                });
        firestore.collection("AllProducts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Belge listesini al

                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String productName = name.getText().toString();
                    String compareName = document.getString("name");

                    if(deney(productName,compareName)){
                        addIdToItem(document.getId());
                        get_url(document.getId());
                        break;
                    }

                    }
                }

        });

    }
public boolean same= true;
    public void add_item(View view){




        firestore.collection("AllProducts").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Belge listesini al

                QuerySnapshot querySnapshot = task.getResult();
                for (QueryDocumentSnapshot document : querySnapshot) {
                    String productName = name.getText().toString();
                    String compareName = document.getString("name");

                    if(deney(productName,compareName)){
                        same =false;
                        break;

                    }else{

                    }

                }
                if(same){
                    addInformations();

                }else {
                    Toast.makeText(this, "Bu Ad İle Zaten Ürün Var", Toast.LENGTH_SHORT).show();


                }
            }
        });


        startActivity(new Intent(addmin_add_item.this, addmin_add_item.class));


    }

    private void addIdToItem(String id){
        final HashMap<String , Object > cartMap = new HashMap<>();

        cartMap.put("id", id);


        firestore.collection("AllProducts")
                .document(id)
                .update(cartMap)
                .addOnSuccessListener(aVoid -> {
                })
                .addOnFailureListener(e -> {
                });

    }
    private void get_url(String id){

        FirebaseStorage storage1 = FirebaseStorage.getInstance();
        StorageReference storageRef2 = storage1.getReference();


        StorageReference dosyaRef = storageRef2.child(storageName);


        dosyaRef.getDownloadUrl().addOnSuccessListener(uri -> {
            String imageUrl = uri.toString();
            addImageUrl(imageUrl, id);

        }).addOnFailureListener(exception -> {

                });
    }
    private void addImageUrl(String imageUrl,String id){

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
    private boolean deney(String a, String b) {

        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(addmin_add_item.this, Admin.class));
    }
    public void yazdir(String a){
        Toast.makeText(this, a, Toast.LENGTH_SHORT).show();
    }
}