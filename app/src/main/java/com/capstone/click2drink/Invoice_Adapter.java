package com.capstone.click2drink;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Invoice_Adapter extends RecyclerView.Adapter<Invoice_Adapter.MyViewHolder> {

    private Context mContext;
    private List<Invoice_Fetcher> invoice_fetchers;

    public Invoice_Adapter (Context context, List<Invoice_Fetcher> invoice_fetchers){
        this.mContext = context;
        this.invoice_fetchers = invoice_fetchers;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView order_number;
        private TextView price;
        private TextView quantity;
        private TextView order_status;
        private TextView date_ordered;
        private ImageView order_status_logo;
        private CardView invoice_container;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            order_number = itemView.findViewById(R.id.order_number);
            price = itemView.findViewById(R.id.product_price);
            quantity = itemView.findViewById(R.id.order_quantity);
            order_status = itemView.findViewById(R.id.order_status);
            date_ordered = itemView.findViewById(R.id.time_stamp);
            order_status_logo = itemView.findViewById(R.id.status_logo);
            invoice_container = itemView.findViewById(R.id.invoice_container);
        }

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.invoice_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Invoice_Fetcher invoice_fetcher = invoice_fetchers.get(position);
        holder.order_number.setText(invoice_fetcher.getOrder_number());
        holder.price.setText(invoice_fetcher.getPrice());
        holder.quantity.setText(invoice_fetcher.getQuantity());
        holder.order_status.setText(invoice_fetcher.getOrder_status());
        holder.date_ordered.setText(invoice_fetcher.getDate_ordered());

        String order_number_txt = invoice_fetcher.getOrder_number();
        String order_status_txt = invoice_fetcher.getOrder_status();
        String order_price_txt = invoice_fetcher.getPrice();
        String order_quantity_txt = invoice_fetcher.getQuantity();

        if(order_status_txt.equals("Out for Delivery")){
            holder.order_status_logo.setImageResource(R.drawable.ic_circle_blue);
        }
        else if (order_status_txt.equals("Delivered")){
            holder.order_status_logo.setImageResource(R.drawable.ic_circle_red);
        }

        holder.invoice_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent a = new Intent(mContext,Track_Order.class);
                a.putExtra("order_number", order_number_txt);
                a.putExtra("order_status",order_status_txt);
                a.putExtra("order_price",order_price_txt);
                a.putExtra("order_quantity", order_quantity_txt);
                mContext.startActivity(a);

            }
        });

    }

    @Override
    public int getItemCount() {
        return invoice_fetchers.size();
    }
}
