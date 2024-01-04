package com.example.buygo.adapters;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buygo.R;
import com.example.buygo.models.AddressModel;
import com.example.buygo.models.MyCartModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context context;
    private List<AddressModel> addressModelList;
    private SelectedAddress selectedAddress;

    private RadioButton selectedRadioButton;
    FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    FirebaseAuth auth = FirebaseAuth.getInstance();

    public AddressAdapter(Context context, List<AddressModel> addressModelList, SelectedAddress selectedAddress) {
        this.context = context;
        this.addressModelList = addressModelList;
        this.selectedAddress = selectedAddress;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.address_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.name.setText(addressModelList.get(position).getAddressName());
        holder.phone.setText(addressModelList.get(position).getUserNumber());
        holder.address.setText(addressModelList.get(position).getAddress());
        holder.delete.setOnClickListener(view -> {
            AddressModel selectedAddress = addressModelList.get(position);
            firestore.collection("CurrentUser")
                    .document(auth.getCurrentUser().getUid())
                    .collection("Address")
                    .get().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String selectedProductName = selectedAddress.getAddressName();
                                String productName = (String) document.getString("addressName");
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

        holder.radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (AddressModel address : addressModelList) {
                    address.setSelected(false);
                }
                addressModelList.get(position).setSelected(true);

                if (selectedRadioButton != null) {
                    selectedRadioButton.setChecked(false);
                }
                selectedRadioButton = (RadioButton) v;
                selectedRadioButton.setChecked(true);
                selectedAddress.setAddressName(addressModelList.get(position).getAddressName());
            }
        });
        if (position == 0) {
            holder.radioButton.performClick();
        }
    }

    public void refreshActivity() {
        if (context instanceof Activity) {
            ((Activity) context).recreate();
        }
    }
    private void deleteItem(String delete){
        firestore.collection("CurrentUser")
                .document(auth.getCurrentUser().getUid())
                .collection("Address")
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

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View radioButton;
        Button delete;
        TextView name, phone, address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.added_address_name);
            phone = itemView.findViewById(R.id.added_address_phone);
            address = itemView.findViewById(R.id.added_address_address);
            radioButton = itemView.findViewById(R.id.added_select_address);
            delete = itemView.findViewById(R.id.address_delete);
        }
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

    public interface SelectedAddress {
        void setAddressName(String name);
    }
}