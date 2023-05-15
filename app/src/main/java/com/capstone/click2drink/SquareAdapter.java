package com.capstone.click2drink;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

// HINDI NAGAMIT KASE SAME LANG SILA NG ADAPTER NG DISPENSER

public class SquareAdapter extends RecyclerView.Adapter<SquareAdapter.MyViewHolder> {

    private Context mContext;
    private List <Product_Dispenser> squareGallon;

    public SquareAdapter(Context context, List <Product_Dispenser> squareGallon){

        this.mContext = context;
        this.squareGallon = squareGallon;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image_url;
        private TextView product_name;
        private TextView product_price;
        private TextView location;

        public MyViewHolder(@NonNull View view) {
            super(view);
            image_url = view.findViewById(R.id.product_image);
            product_name = view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            location = view.findViewById(R.id.location);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.categories_layout,parent,false);
        return new MyViewHolder(view);
    }

    // Dito lahat ng operations
    @Override
    public void onBindViewHolder(@NonNull SquareAdapter.MyViewHolder holder, int position) {

        final Product_Dispenser product_getter = squareGallon.get(position);
        holder.product_name.setText(product_getter.getProduct_name());
        holder.product_price.setText(product_getter.getProduct_price());
        Glide.with(mContext).load(product_getter.getImage_url()).into(holder.image_url);
        holder.location.setText(product_getter.getLocation());

    }

    @Override
    public int getItemCount() {
        return squareGallon.size();
    }

}
