package com.capstone.click2drink.staff;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.capstone.click2drink.R;

import java.util.List;

public class Adapter_Delivered_Orders extends RecyclerView.Adapter<Adapter_Delivered_Orders.MyViewHolder> {

    Context mContext;
    private List<Invoice_Log_Fetcher> invoice_data;
    private List<Address_Order_Fetcher> address_data;
    private List<Name_Fetcher> user_name_list;

    public Adapter_Delivered_Orders(Context mContext, List<Invoice_Log_Fetcher>
            invoice_data, List<Address_Order_Fetcher> address_data, List<Name_Fetcher> user_name_list ){

        this.mContext = mContext;
        this.invoice_data = invoice_data;
        this.address_data = address_data;
        this.user_name_list = user_name_list;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView fullname;
        private TextView combined_address;
        private TextView phone_number;
        private TextView order_number;
        private TextView total_price;
        private TextView total_gallons;
        private TextView ordered_at;
        private TextView view_details;


        public MyViewHolder (View view){
            super(view);

            fullname = view.findViewById(R.id.fullName);
            combined_address = view.findViewById(R.id.combined_address);
            phone_number = view.findViewById(R.id.phone_number);
            order_number = view.findViewById(R.id.order_number);
            total_price = view.findViewById(R.id.product_price);
            total_gallons = view.findViewById(R.id.order_quantity);
            ordered_at = view.findViewById(R.id.time_stamp);
            view_details = view.findViewById(R.id.view_details);


        }
    }


    @NonNull
    @Override
    public Adapter_Delivered_Orders.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.staff_delivered_orders_layout,parent,false);
        return new Adapter_Delivered_Orders.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull Adapter_Delivered_Orders.MyViewHolder holder, int position) {

        final Invoice_Log_Fetcher new_invoice_data = invoice_data.get(position);
        final Address_Order_Fetcher new_address_data = address_data.get(position);
        final Name_Fetcher new_user_name_list = user_name_list.get(position);

        holder.fullname.setText(new_user_name_list.getCuser_name());
        holder.combined_address.setText(new_address_data.getCombine_address());
        holder.phone_number.setText(new_address_data.getCuser_number());
        holder.order_number.setText(new_invoice_data.getOrder_number());
        holder.total_price.setText(new_invoice_data.getTotal_price()+".00");
        holder.total_gallons.setText(new_invoice_data.getTotal_gallons());
        holder.ordered_at.setText(new_invoice_data.getDate_ordered());

        holder.view_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x = new Intent(mContext,view_delivered_orders.class);
                String order_num = new_invoice_data.getOrder_number();
                x.putExtra("order_number",order_num);
                mContext.startActivity(x);
            }
        });

    }

    @Override
    public int getItemCount() {
        return invoice_data.size();
    }
}

