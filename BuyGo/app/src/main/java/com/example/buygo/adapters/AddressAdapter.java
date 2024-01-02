package com.example.buygo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.buygo.R;
import com.example.buygo.models.AddressModel;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context context;
    private List<AddressModel> addressModelList;
    private SelectedAddress selectedAddress;

    private RadioButton selectedRadioButton;

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

    @Override
    public int getItemCount() {
        return addressModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public View radioButton;
        TextView name, phone, address;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.added_address_name);
            phone = itemView.findViewById(R.id.added_address_phone);
            address = itemView.findViewById(R.id.added_address_address);
            radioButton = itemView.findViewById(R.id.added_select_address);
        }
    }

    public interface SelectedAddress {
        void setAddressName(String name);
    }
}