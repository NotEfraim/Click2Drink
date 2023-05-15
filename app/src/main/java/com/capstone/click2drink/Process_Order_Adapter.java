package com.capstone.click2drink;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;

import java.util.List;


public class Process_Order_Adapter extends RecyclerView.Adapter<Process_Order_Adapter.MyViewHolder> {

    SharedPreferences sharedPreferences;
    private Context mContext;
    private List<Process_Order_Fetcher> process_order_fetchers;


    public Process_Order_Adapter(Context context, List<Process_Order_Fetcher> process_order_fetchers){
        this.mContext = context;
        this.process_order_fetchers = process_order_fetchers;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView product_image;
        private TextView product_name;
        private TextView product_price;
        private TextView product_quantity;


        public MyViewHolder (View view){
            super(view);

            product_image = view.findViewById(R.id.product_image);
            product_name = view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            product_quantity = view.findViewById(R.id.product_quantity);


        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.order_process_layout,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        sharedPreferences = mContext.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        String login_user_id = sharedPreferences.getString("login_user_id","");

        final Process_Order_Fetcher process_order_fetcher = process_order_fetchers.get(position);
        Glide.with(mContext).load(process_order_fetcher.get_product_Image_url()).into(holder.product_image);
        holder.product_name.setText(process_order_fetcher.get_Product_name());
        holder.product_price.setText(process_order_fetcher.get_Product_price());
        holder.product_quantity.setText(process_order_fetcher.get_Product_quantity());


    }


    @Override
    public int getItemCount() {
        return process_order_fetchers.size();
    }

}
