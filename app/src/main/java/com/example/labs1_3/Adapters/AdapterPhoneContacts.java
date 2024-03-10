package com.example.labs1_3.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labs1_3.Models.PhoneContact;
import com.example.labs1_3.R;

import java.util.ArrayList;

public class AdapterPhoneContacts extends RecyclerView.Adapter<AdapterPhoneContacts.ViewHolder> {
    Context context;
    ArrayList<PhoneContact> phoneContacts;

    public AdapterPhoneContacts(Context context, ArrayList<PhoneContact> phoneContacts) {
        this.context = context;
        this.phoneContacts = phoneContacts;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_phone_contact, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PhoneContact phoneContact = phoneContacts.get(position);

        holder.textContactName.setText(phoneContact.getName());
        holder.textContactNumber.setText(phoneContact.getNumber());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneContact.getNumber()));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return phoneContacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView textContactName;
        TextView textContactNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textContactName = itemView.findViewById(R.id.textContactName);
            textContactNumber = itemView.findViewById(R.id.textContactNumber);

        }

    }

}
