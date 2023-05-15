package com.capstone.click2drink;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {

    private Context mContext;
    private List<Partners> partners;


    public RecyclerAdapter (Context context,List<Partners> partners){
        this.mContext = context;
        this.partners = partners;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image_url;
        private LinearLayout container;


        public MyViewHolder (View view){
            super(view);

           container = view.findViewById(R.id.L_container);
           image_url = view.findViewById(R.id.pImage);


        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.partners_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Partners partner = partners.get(position);
        Glide.with(mContext).load(partner.getImage()).into(holder.image_url);

        if(partner.getStatus().equals("inactive")){
            holder.container.setVisibility(View.GONE);
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,view_partner.class);
                intent.putExtra("shop_id", partner.getShop_id());
                intent.putExtra("image_url", partner.getImage());
                intent.putExtra("address", partner.getAddress());
                intent.putExtra("contact_number", partner.getContact_number());
                intent.putExtra("fb_page", partner.getFbPage());
                intent.putExtra("status",partner.getStatus());
                mContext.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return partners.size();
    }
}
