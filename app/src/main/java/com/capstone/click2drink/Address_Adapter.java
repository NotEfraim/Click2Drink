package com.capstone.click2drink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Address_Adapter extends RecyclerView.Adapter<Address_Adapter.MyViewHolder> {

    private Context mContext;
    private List<Address_Fetcher> address_fetchers;
    SharedPreferences sharedPreferences;

    public Address_Adapter(Context context, List<Address_Fetcher> address_fetchers){

        this.mContext = context;
        this.address_fetchers = address_fetchers;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView address_title;
        private TextView cuser_phone_number;
        private TextView cuser_address;
        private TextView cuser_province;
        private TextView identifier;
        private CardView container;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            address_title = itemView.findViewById(R.id.address_title);
            cuser_phone_number = itemView.findViewById(R.id.cuser_phone_number);
            cuser_address = itemView.findViewById(R.id.cuser_address);
            cuser_province = itemView.findViewById(R.id.cuser_province);
            container = itemView.findViewById(R.id.address_container);
            identifier = itemView.findViewById(R.id.default_identifier);

        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.recycler_address_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Address_Adapter.MyViewHolder holder, int position) {
        final Address_Fetcher address_fetcher = address_fetchers.get(position);
        holder.address_title.setText(address_fetcher.getCuser_address_title());
        holder.cuser_phone_number.setText(address_fetcher.getCuser_number());

        String combined_address = address_fetcher.getCuser_house_number()
                +" "+address_fetcher.getCuser_brgy()+" "+address_fetcher.getCuser_municipality();
        String combined_zip_prov = address_fetcher.getCuser_province()+" "+address_fetcher.getCuser_zipcode();
        holder.cuser_address.setText(combined_address);
        holder.cuser_province.setText(combined_zip_prov);

        //

        sharedPreferences = mContext.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        String Default_address = sharedPreferences.getString("default_address","");
        String Context_id = address_fetcher.getAddress_id();

        if(Default_address.equals(Context_id)){
            holder.identifier.setVisibility(View.VISIBLE);
        }

        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent crud = new Intent(mContext,Update_address.class);
                crud.putExtra("address_title",address_fetcher.getCuser_address_title());
                crud.putExtra("phone_number",address_fetcher.getCuser_number());
                crud.putExtra("house_number",address_fetcher.getCuser_house_number());
                crud.putExtra("brgy",address_fetcher.getCuser_brgy());
                crud.putExtra("municipality",address_fetcher.getCuser_municipality());
                crud.putExtra("province",address_fetcher.getCuser_province());
                crud.putExtra("zipcode",address_fetcher.getCuser_zipcode());
                crud.putExtra("address_id",address_fetcher.getAddress_id());

                mContext.startActivity(crud);
            }
        });

    }

    @Override
    public int getItemCount() {
        return address_fetchers.size();
    }


}
