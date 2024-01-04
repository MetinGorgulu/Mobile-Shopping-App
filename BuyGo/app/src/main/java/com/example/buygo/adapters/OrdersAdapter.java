package com.example.buygo.adapters;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buygo.R;
import com.example.buygo.activities.AddAddressActivity;
import com.example.buygo.activities.Home;
import com.example.buygo.activities.OrdersDetail;
import com.example.buygo.activities.Profile;
import com.example.buygo.activities.admin_orders;
import com.example.buygo.activities.admin_orders_detail;
import com.example.buygo.models.MyCartModel;
import com.example.buygo.models.OrdersModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firestore.v1.StructuredQuery;

import java.io.Serializable;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    Context context;
    List<MyCartModel> myCartModelsList;
    List<OrdersModel> list;
    int totalAmount =0;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth= FirebaseAuth.getInstance();


    public OrdersAdapter(Context context, List<OrdersModel> list ) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public OrdersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersAdapter.ViewHolder holder, int position) {

        holder.date.setText(list.get(position).getDate());
        holder.time.setText(list.get(position).getTime());
        holder.totalPrice.setText(list.get(position).getTotal_Price());
        holder.table.setOnClickListener(view -> {
            OrdersModel selectedOrder = list.get(position);
            firestore.collection("CurrentUser")
                    .document(auth.getCurrentUser().getUid())
                    .collection("MyOrders")
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String selectedDate = selectedOrder.getDate();
                                String compareDate = (String) document.getString("Date");
                                String selectedTime = selectedOrder.getTime();
                                String compareTime = (String) document.getString("Time");

                                if (deney(selectedDate, compareDate) && deney(selectedTime, compareTime)) {
                                    String id = document.getId();
                                    Intent intent = new Intent(context, OrdersDetail.class);
                                    intent.putExtra("detailId", id);
                                    context.startActivity(intent);
                                }
                            }
                        } else {
                            // Toast.makeText(context, "Belge okuma hatası: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView  date, time, totalPrice;
        LinearLayout table;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.orders_current_date);
            time = itemView.findViewById(R.id.orders_current_time);
            totalPrice = itemView.findViewById(R.id.orders_total_price);
            table = itemView.findViewById(R.id.orders_table);
        }
    }
    private boolean deney(String a, String b) {

        if (a.equals(b)) {
            return true;
        } else {
            return false;
        }
    }
}
