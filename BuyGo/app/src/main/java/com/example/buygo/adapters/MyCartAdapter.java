package com.example.buygo.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buygo.R;
import com.example.buygo.models.MyCartModel;
import com.example.buygo.models.MyFavoritesModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.ViewHolder> {

    Context context;
    List<MyCartModel> list;
    int totalAmount =0;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();


    public MyCartAdapter(Context context, List<MyCartModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCartAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.image);
        holder.price.setText(list.get(position).getProductPrice() + "$");
        holder.name.setText(list.get(position).getProductName());
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice()));
        holder.totalQuantity.setText(list.get(position).getTotalQuantity());
        holder.delete_button.setOnClickListener(view -> {
            MyCartModel selectedProduct = list.get(position);
            firestore.collection("CurrentUser")
                    .document(auth.getCurrentUser().getUid())
                    .collection("AddToCart")
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String selectedProductName = selectedProduct.getProductName();
                                String productName = (String) document.getString("productName");
                                if(deney (selectedProductName, productName)){

                                    deleteItem(document.getId());
                                    refreshActivity();

                                }


                            }
                        } else {
                            // Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        });

        totalAmount = totalAmount + list.get(position).getTotalPrice();
        Intent intent = new Intent("MyTotalAmount");
        intent.putExtra("totalAmount",totalAmount);


    }
    public void refreshActivity() {
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
    }
    private void deleteItem(String delete){
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
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
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, price, totalQuantity, totalPrice;
        ImageView image;
        TextView delete_button;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.cart_name);
            price = itemView.findViewById(R.id.cart_price);
            image = itemView.findViewById(R.id.cart_img);
            totalQuantity = itemView.findViewById(R.id.cart_quantity);
            totalPrice = itemView.findViewById(R.id.cart_total_price);
            delete_button = itemView.findViewById(R.id.cart_delete);
        }
    }
    private void deleteFavorite(String delete){
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
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
}
