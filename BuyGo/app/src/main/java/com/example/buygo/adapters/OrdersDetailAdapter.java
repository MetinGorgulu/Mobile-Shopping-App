package com.example.buygo.adapters;

import android.content.Context;
import android.content.Intent;
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
import com.example.buygo.models.MyCartModel;
import com.example.buygo.models.OrdersDetailModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class OrdersDetailAdapter extends RecyclerView.Adapter<OrdersDetailAdapter.ViewHolder>{

    Context context;
    List<OrdersDetailModel> list;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public OrdersDetailAdapter(Context context, List<OrdersDetailModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrdersDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //yazdir();
        return new OrdersDetailAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_detail_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersDetailAdapter.ViewHolder holder, int position) {
        //yazdir();

        Glide.with(context).load(list.get(position).getImg_url()).into(holder.image);
        holder.name.setText(list.get(position).getProductName());
        holder.totalPrice.setText(String.valueOf(list.get(position).getTotalPrice()));
        holder.totalQuantity.setText(list.get(position).getTotalQuantity());



    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, totalQuantity, totalPrice;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.orders_detail_name);
            image = itemView.findViewById(R.id.orders_detail_img);
            totalQuantity = itemView.findViewById(R.id.orders_detail_quantity);
            totalPrice = itemView.findViewById(R.id.orders_detail_price);
        }
    }

}
