package com.example.buygo.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.buygo.R;
import com.example.buygo.activities.Admin;
import com.example.buygo.activities.DetailedActivity;
import com.example.buygo.activities.Home;
import com.example.buygo.activities.MainActivity;
import com.example.buygo.activities.admin_product_detail;
import com.example.buygo.models.AllProductsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class AllProductsAdapter extends RecyclerView.Adapter<AllProductsAdapter.ViewHolder> {

    FirebaseFirestore db;
    FirebaseAuth auth;
    private Context context;
    private List<AllProductsModel> allProductsModelList;

    public AllProductsAdapter(Context context, List<AllProductsModel> allProductsModelList) {
        this.context = context;
        this.allProductsModelList = allProductsModelList;
    }

    @NonNull
    @Override
    public AllProductsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.all_items, parent,  false));
    }

    @Override
    public void onBindViewHolder(@NonNull AllProductsAdapter.ViewHolder holder, int position) {

        db= FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        Glide.with(context).load(allProductsModelList.get(position).getImg_url()).into(holder.imageView);
        holder.name.setText(allProductsModelList.get(position).getName());
        holder.price.setText(String.valueOf(allProductsModelList.get(position).getPrice()));

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                if(auth.getCurrentUser()!=null){

                    adminControl(position);

                }
                else {
                    noAdmin(position);
                }

            }
        }
        );

    }
    private boolean isLoopExecuted = false;
    private void adminControl(int position){


        if(!isLoopExecuted) {
            db.collection("CurrentUser")
                    .document(auth.getCurrentUser().getUid())
                    .collection("UsersInformation").get().addOnCompleteListener(task ->  {
                        if (task.isSuccessful()&& !isLoopExecuted) {
                            for (QueryDocumentSnapshot document : task.getResult()) {


                                String userRole = (String) document.getString("role");

                                Boolean isaAdmin = compare(userRole);
                                if(isaAdmin){


                                            Intent intent = new Intent(context, admin_product_detail.class);
                                            intent.putExtra("detailed",allProductsModelList.get(position));
                                            context.startActivity(intent);

                                    break;

                                }
                                else {
                                    noAdmin(position);
                                    break;
                                }
                            }
                            isLoopExecuted = true;
                        }
                        isLoopExecuted = false;
                    });

        }



    }
    private boolean compare(String userRole){

        if(userRole.equals("admin")){
            return true;
        }
        else {
            return false;
        }



    }
    private void noAdmin(int position){


                Intent intent = new Intent(context, DetailedActivity.class);
                intent.putExtra("detailed",allProductsModelList.get(position));
                context.startActivity(intent);

    }

    @Override
    public int getItemCount() {
        return allProductsModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView name, price;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_img);
            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
        }
    }
}
