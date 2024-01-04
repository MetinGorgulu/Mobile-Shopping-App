package com.example.buygo.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buygo.R;
import com.example.buygo.activities.OrdersDetail;
import com.example.buygo.activities.admin_orders_detail;
import com.example.buygo.models.AdminOrdersModel;
import com.example.buygo.models.MyCartModel;
import com.example.buygo.models.OrdersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class AdminOrdersAdapter extends RecyclerView.Adapter<AdminOrdersAdapter.ViewHolder>{

    Context context;
    List<MyCartModel> myCartModelsList;
    List<AdminOrdersModel> list;
    int totalAmount =0;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth= FirebaseAuth.getInstance();

    public AdminOrdersAdapter(Context context, List<AdminOrdersModel> list ) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public AdminOrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AdminOrdersAdapter.ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_orders_rec, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrdersAdapter.ViewHolder holder, int position) {

        holder.date.setText(list.get(position).getDate());
        holder.time.setText(list.get(position).getTime());
        holder.totalPrice.setText(list.get(position).getTotal_Price());
        holder.userName.setText(list.get(position).getUserName());
        holder.table.setOnClickListener(view -> {
            firestore.collection("CurrentUser")
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                getOrder(position ,document.getId());
                            }
                        } else {
                            // Toast.makeText(context, "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });



    }
    private void getOrder(int position ,String Id){
        AdminOrdersModel selectedOrder = list.get(position);
        firestore.collection("CurrentUser")
                .document(Id)
                .collection("MyOrders")
                .get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String selectedDate = selectedOrder.getDate();
                            String compareDate = (String) document.getString("Date");
                            String selectedTime = selectedOrder.getTime();
                            String compareTime = (String) document.getString("Time");
                            String selectedMail = selectedOrder.getUserName();
                            String compareMail = (String) document.getString("UserName");
                            if (deney(selectedDate, compareDate) && deney(selectedTime, compareTime)&& deney(selectedMail,compareMail)) {
                                String id = document.getId();
                                Intent intent = new Intent(context, admin_orders_detail.class);
                                intent.putExtra("userId" ,Id);
                                intent.putExtra("detailId", id);
                                context.startActivity(intent);
                            }
                        }
                    } else {
                        // Toast.makeText(context, "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date, time, totalPrice, userName;
        LinearLayout table;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.admin_orders_date);
            time = itemView.findViewById(R.id.admin_orders_time);
            totalPrice = itemView.findViewById(R.id.admin_orders_totalprice);
            userName = itemView.findViewById(R.id.admin_orders_username);
            table = itemView.findViewById(R.id.admin_orders_table);
        }
    }
    public void yazdir(String a){
        Toast.makeText(context, a, Toast.LENGTH_SHORT).show();
    }
    private boolean deney(String a, String b) {

        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }



}
