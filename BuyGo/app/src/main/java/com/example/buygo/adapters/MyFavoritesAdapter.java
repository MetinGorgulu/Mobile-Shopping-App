package com.example.buygo.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buygo.R;
import com.example.buygo.activities.Favorites;
import com.example.buygo.models.MyFavoritesModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;


public class MyFavoritesAdapter extends RecyclerView.Adapter<MyFavoritesAdapter.ViewHolder> {


    FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    private MyFavoritesModel selectedFavorite;

    public MyFavoritesModel getSelectedFavorite() {
        return selectedFavorite;
    }


    private Context context;
    private  List<MyFavoritesModel>list;

    public MyFavoritesAdapter(Context context, List<MyFavoritesModel> favoritesModelList) {
        this.context = context;
        this.list = favoritesModelList;

    }

    public void setSelectedFavorite(MyFavoritesModel selectedFavorite) {
        this.selectedFavorite = selectedFavorite;
    }
    @NonNull
    @Override
    public MyFavoritesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.favorites_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyFavoritesAdapter.ViewHolder holder, int position) {

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.newImg);
        holder.newName.setText(list.get(position).getName());
        holder.newPrice.setText(String.valueOf(list.get(position).getPrice()));
        holder.btnFavorite.setOnClickListener(view -> {
            MyFavoritesModel selectedFavorite = list.get(position);
            firestore.collection("CurrentUser")
                    .document(auth.getCurrentUser().getUid())
                    .collection("Favorites")
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String selectedFavoriteid = selectedFavorite.getId();
                                String productkey = (String) document.getString("productKey");
                                if(deney (selectedFavoriteid, productkey )){

                                    deleteFavorite(document.getId());
                                    refreshActivity();

                                }


                            }
                        } else {
                           // Toast.makeText(getApplicationContext(), "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

        });

    }

    public void refreshActivity() {
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
    }
    private void deleteFavorite(String delete){
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("Favorites")
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
        ImageView newImg;
        TextView newName, newPrice;
        Button btnFavorite;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newImg= itemView.findViewById(R.id.favorites_img);
            newName = itemView.findViewById(R.id.favorites_name);
            newPrice = itemView.findViewById(R.id.favorites_price);
            btnFavorite = itemView.findViewById(R.id.favorites_delete);
        }
    }
}
