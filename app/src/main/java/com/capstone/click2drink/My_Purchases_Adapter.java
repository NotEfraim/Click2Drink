package com.capstone.click2drink;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class My_Purchases_Adapter extends RecyclerView.Adapter<My_Purchases_Adapter.MyViewHolder> {
    SharedPreferences sharedPreferences;
    private Context mContext;
    private List<My_Purchases_Fetcher> my_purchases_fetchers;

    public My_Purchases_Adapter(Context context, List<My_Purchases_Fetcher> my_purchases_fetchers) {
        this.mContext = context;
        this.my_purchases_fetchers = my_purchases_fetchers;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView product_name;
        private TextView product_price;
        private TextView product_quantity;
        private TextView order_number;
        private TextView received_on;


        public MyViewHolder(View view) {
            super(view);

            product_image = view.findViewById(R.id.product_image);
            product_name = view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            product_quantity = view.findViewById(R.id.product_quantity);
            order_number = view.findViewById(R.id.order_number);
            received_on = view.findViewById(R.id.ordered_at);



        }

    }

    @NonNull
    @Override
    public My_Purchases_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.my_purchases_layout,parent,false);
        return new My_Purchases_Adapter.MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        sharedPreferences = mContext.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        String login_user_id = sharedPreferences.getString("login_user_id","");

        final My_Purchases_Fetcher my_purchases_fetcher = my_purchases_fetchers.get(position);
        Glide.with(mContext).load(my_purchases_fetcher.get_product_Image_url()).into(holder.product_image);
        holder.product_name.setText(my_purchases_fetcher.get_Product_name());
        holder.product_price.setText(my_purchases_fetcher.get_Product_price());
        holder.product_quantity.setText(my_purchases_fetcher.get_Product_quantity());
        holder.order_number.setText(my_purchases_fetcher.getOrder_number());
        holder.received_on.setText(my_purchases_fetcher.getReceived_on());


    }

    @Override
    public int getItemCount() {
        return my_purchases_fetchers.size();
    }

}
