package com.capstone.click2drink.staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.capstone.click2drink.R;

import java.util.List;

public class Adapter_View_Orders extends RecyclerView.Adapter<Adapter_View_Orders.MyViewHolder> {

    private Context mContext;
    private List<View_Pending_Fetcher> order_data;

    public Adapter_View_Orders(Context context, List<View_Pending_Fetcher> order_data){

        this.mContext = context;
        this.order_data = order_data;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView product_image;
        private TextView product_name;
        private TextView product_price;
        private TextView product_quantity;
        private TextView order_number;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            product_image = itemView.findViewById(R.id.product_image);
            product_name = itemView.findViewById(R.id.product_name);
            product_price = itemView.findViewById(R.id.product_price);
            product_quantity = itemView.findViewById(R.id.product_quantity);
            order_number = itemView.findViewById(R.id.order_number);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.view_pending_orderes_layout,parent,false);
        return new Adapter_View_Orders.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final View_Pending_Fetcher new_order_data = order_data.get(position);

        Glide.with(mContext).load(new_order_data.get_product_Image_url()).into(holder.product_image);
        holder.product_name.setText(new_order_data.get_Product_name());
        holder.product_price.setText(new_order_data.get_Product_price());
        holder.product_quantity.setText(new_order_data.get_Product_quantity());
        holder.order_number.setText(new_order_data.getOrder_number());


    }

    @Override
    public int getItemCount() {
        return order_data.size();
    }



}
