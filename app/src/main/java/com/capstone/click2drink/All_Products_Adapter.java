package com.capstone.click2drink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vishnusivadas.advanced_httpurlconnection.PutData;
import android.os.Handler;
import android.widget.Toast;

import java.util.List;
import java.util.Random;


public class All_Products_Adapter extends RecyclerView.Adapter<All_Products_Adapter.MyViewHolder> {

    private String ADDTOTRUCK_URL ="http://resources.click2drinkph.store/php/AddToTruck.php";
    SharedPreferences sharedPreferences;
    private Context mContext;
    private List<Product_Dispenser> product_dispensers;


    public All_Products_Adapter (Context context,List<Product_Dispenser> product_dispensers){
        this.mContext = context;
        this.product_dispensers = product_dispensers;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView image_url;
        private LinearLayout container;
        private TextView product_name;
        private TextView product_price;
        private RecyclerView all_products_recycler;
        TextView location;
        private Button add_order;
        private Button buy_now;
        private TextView display;


        public MyViewHolder (View view){
            super(view);

            container = view.findViewById(R.id.L_container);
            image_url = view.findViewById(R.id.pImage);
            product_name = view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            location = view.findViewById(R.id.location);
            all_products_recycler = view.findViewById(R.id.products_recycler);
            add_order = view.findViewById(R.id.add_order);
            buy_now = view.findViewById(R.id.buy_now);




        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.all_products_horizontal,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        sharedPreferences = mContext.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        String login_user_id = sharedPreferences.getString("login_user_id","");

        final Product_Dispenser product_dispenser = product_dispensers.get(position);
        Glide.with(mContext).load(product_dispenser.getImage_url()).into(holder.image_url);
        holder.product_name.setText(product_dispenser.getProduct_name());
        holder.product_price.setText(product_dispenser.getProduct_price());
        holder.location.setText(product_dispenser.getLocation());

        String temp_prod_id = product_dispenser.getProduct_id();
        String temp_prod_name = product_dispenser.getProduct_name();
        String temp_prod_price = product_dispenser.getProduct_price();
        String temp_prod_location = product_dispenser.getLocation();
        String temp_image_url = product_dispenser.getImage_url();
        String temp_shop_id = product_dispenser.getShop_id();

        // Generate Unique_Order Number
        final int min = 10;
        final int max = 90;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String order_number = login_user_id+random+temp_prod_id+temp_shop_id;

        holder.add_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Calling the Function
                ADD_ORDER(login_user_id,temp_prod_id,temp_prod_name,temp_prod_price,temp_prod_location,temp_image_url,temp_shop_id);

            }
        });
        holder.buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(mContext,buy_now.class);
                activity.putExtra("image_url",temp_image_url);
                activity.putExtra("product_name", temp_prod_name);
                activity.putExtra("location",temp_prod_location);
                activity.putExtra("product_price",temp_prod_price);
                activity.putExtra("shop_id",temp_shop_id);
                activity.putExtra("product_id",temp_prod_id);
                activity.putExtra("order_number",order_number);

                mContext.startActivity(activity);

            }
        });


    }


    @Override
    public int getItemCount() {
        return product_dispensers.size();
    }

    public void ADD_ORDER (String cuser_id,String product_id,String product_name,String product_price,String product_location,String image_url, String shop_id){

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[7];

                field[0] = "cuser_id";
                field[1] = "product_id";
                field[2] = "product_name";
                field[3] = "product_price";
                field[4] = "product_location";
                field[5] = "image_url";
                field[6] = "shop_id";

                String[] data = new String[7];
                data[0] = cuser_id;
                data[1] = product_id;
                data[2] = product_name;
                data[3] = product_price;
                data[4] = product_location;
                data[5] = image_url;
                data[6] = shop_id;


                PutData putData = new PutData(ADDTOTRUCK_URL,"POST",field,data);

                if(putData.startPut()){

                    if(putData.onComplete()){

//                        String result = putData.getResult();
                        Toast.makeText(mContext,"Added to truck",Toast.LENGTH_SHORT).show();

                        Handler handler1 = new Handler();
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                //truck item count
                                // kunin yung item tas ilalagay sa shared pref , para magamit ang refresh function
                                String x = sharedPreferences.getString("truck_item_count","0");
                                int old_count = Integer.parseInt(x);
                                old_count = old_count + 1;
                                sharedPreferences.edit().putString("truck_item_count",String.valueOf(old_count)).apply();

                            }
                        });
                    }

                }
                else{
                    Toast.makeText(mContext,"Error on Request",Toast.LENGTH_SHORT).show();
                }

            }
        });



    }
}
