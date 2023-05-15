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

public class BottleAdapter extends RecyclerView.Adapter<BottleAdapter.MyViewHolder> {

    private Context mContext;
    private List <Product_Dispenser> bottleWater;

    public BottleAdapter(Context context, List<Product_Dispenser> bottleWater ){
        this.mContext = context;
        this.bottleWater = bottleWater;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image_url;
        private TextView product_name;
        private TextView product_price;
        private TextView error;
        private TextView location;

        public MyViewHolder(@NonNull View view) {
            super(view);

            image_url = view.findViewById(R.id.product_image);
            product_name = view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            error = view.findViewById(R.id.error);
            location = view.findViewById(R.id.location);
        }
    }

    @NonNull
    @Override
    public BottleAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.categories_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BottleAdapter.MyViewHolder holder, int position) {

        final Product_Dispenser product_getter = bottleWater.get(position);

            holder.error.setVisibility(View.GONE);
            holder.product_name.setText(product_getter.getProduct_name());
            holder.product_price.setText(product_getter.getProduct_price());
            Glide.with(mContext).load(product_getter.getImage_url()).into(holder.image_url);
            holder.location.setText(product_getter.getLocation());

    }

    @Override
    public int getItemCount() {
        return bottleWater.size();
    }


}
