package com.capstone.click2drink;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class Truck_Adapter extends RecyclerView.Adapter<Truck_Adapter.MyViewHolder> {

    private Context mContext;
    private List<Truck_Class> truck_classes;
    private String DELETE_URL = "http://resources.click2drinkph.store/php/Delete_Truck_Item.php";
    private String UPDATE_URL = "http://resources.click2drinkph.store/php/order_price_quantity.php";
    private String UPDATE_ORDER_NUMBER = "http://resources.click2drinkph.store/php/update_order_number.php";
    private String truck_id;
    private int[] quantity_holder ;
    private int[] price_holder;
    private int[] truck_id_holder;
    private int total_gallons;
    private int subtotal;
    private int j;
    String shop_id;
    SharedPreferences sharedPreferences;

    public Truck_Adapter(Context context, List<Truck_Class> truck_classes){

        this.mContext = context;
        this.truck_classes = truck_classes;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView product_name;
        private TextView product_price;
        private TextView product_location;
        private TextView product_quantity;
        private ImageView product_image;
        private ImageView truck_del;
        private ImageView minus,add;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

           sharedPreferences = mContext.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);

           product_name = itemView.findViewById(R.id.product_name);
           product_price = itemView.findViewById(R.id.product_price);
           product_location = itemView.findViewById(R.id.location);
           product_image = itemView.findViewById(R.id.product_image);
           product_quantity = itemView.findViewById(R.id.product_quantity);
           truck_del = itemView.findViewById(R.id.truck_del);
           minus = itemView.findViewById(R.id.minus_quantity);
           add = itemView.findViewById(R.id.add_quantity);

        }
    }

    @NonNull
    @Override
    public Truck_Adapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.truck_item_layout,parent,false);
        return new Truck_Adapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Truck_Adapter.MyViewHolder holder, int position) {
        final Truck_Class truck_class = truck_classes.get(position);
        holder.product_name.setText(truck_class.getTruckProduct_name());
        holder.product_price.setText(truck_class.getTruckProduct_price());
        holder.product_location.setText(truck_class.getTruckProduct_location());
        Glide.with(mContext).load(truck_class.getTruckImage_url()).into(holder.product_image);


        truck_id = truck_class.getTruck_id();

        //Quantity
        int l = truck_classes.size();
        quantity_holder = new int[l];
        j = l;
        total_gallons = l;

        //giving the array value of 1
        for (int i = 0; i < l; i++ ){
            quantity_holder[i] = 1 ;

        }
        //Price
        price_holder = new int[l];

        //Initialize truck_array
        truck_id_holder = new int[l];

        //Inputting Value of Price in Array
        for(int x = 0; x<l; x++){
            //The variable x representing the position of the in the list
            Truck_Class all_price = truck_classes.get(x);
            String temp_price = all_price.getTruckProduct_price();
            price_holder[x] = Integer.parseInt(temp_price);
        }

        //inputting truck id in array
        for(int x= 0; x<l;x++){
            //The variable x representing the position of the in the list
            Truck_Class all_id = truck_classes.get(x);
            String temp_id = all_id.getTruck_id();
            truck_id_holder[x] = Integer.parseInt(temp_id);
        }


        //DELETE
        holder.truck_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteTruck();
                toShared();

            }
        });

        //Para kung di na trigger yung button mag re-reset siya sa default
        QuantityAndPriceListener(position);

        //Update agad sa startup
        for(int x = 0; x < l; x++){
            Truck_Class shop_idx = truck_classes.get(x);
            String f = shop_idx.getShop_id();
            String s = String.valueOf(truck_id_holder[x]);
            UpdateOrderNumber(s,f);
        }


        //QUANTITY
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shop_id = truck_class.getShop_id();

                int y = quantity_holder[position];

                if(y == 0){
                    quantity_holder[position] = 1;
                }
                    quantity_holder[position] = quantity_holder[position] + 1;
                    int x = quantity_holder[position];
                    holder.product_quantity.setText(String.valueOf(x));
                    total_gallons = total_gallons + 1 ;

                    // Adding Price by itself
                    String str_price = truck_class.getTruckProduct_price();
                    int temp_price = Integer.parseInt(str_price);
                    subtotal = price_holder[position];
                    // validation
                    if(subtotal == 0){
                        subtotal = temp_price;
                    }
                    subtotal = subtotal + temp_price;
                    // Re-input the value of array in the latest one
                    price_holder[position] = subtotal;
                    toShared();

                    ////UPDATING THE VALUE TO DATABASE EVERY CLICK
                    QuantityAndPriceListener(position);


            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                shop_id = truck_class.getShop_id();


                if( quantity_holder[position] > 1) {
                    quantity_holder[position] = quantity_holder[position] - 1;
                    int x = quantity_holder[position];
                    holder.product_quantity.setText(String.valueOf(x));
                    total_gallons = total_gallons - 1;

                    // Subtracting price by itself
                    // Adding Price by itself
                    String str_price = truck_class.getTruckProduct_price();
                    int temp_price = Integer.parseInt(str_price);
                    subtotal = price_holder[position];
                    subtotal = subtotal - temp_price;
                    // Re-input the value of array in the latest one
                    price_holder[position] = subtotal;
                    toShared();

                    //UPDATING THE VALUE TO DATABASE EVERY CLICK
                    QuantityAndPriceListener(position);
                }

            }
        });
        //Shared pref value
        toShared();


    }

    public void toShared(){
        //For Quantity
        int sum = 0;
        for(int i = 0 ; i < j; i ++){
             sum = sum + quantity_holder[i];
        }
        //For Gallons // Calculating Prices
        int price_sum = 0;
        for(int i = 0 ; i < j; i ++){
            price_sum = price_sum + price_holder[i];
        }

        sharedPreferences.edit().putString("total_gallons",String.valueOf(sum)).apply();
        sharedPreferences.edit().putString("subtotal",String.valueOf(price_sum)).apply();
    }

    public void QuantityAndPriceListener(int position){

        //UPDATING THE VALUE TO DATABASE EVERY CLICK
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[3];
                field[0] = "order_quantity";
                field[1] = "order_price";
                field[2] = "truck_id";

                String a = String.valueOf(quantity_holder[position]);
                String b = String.valueOf(price_holder[position]);
                String c = String.valueOf(truck_id_holder[position]);

                String[] data = new String[3];
                data[0] = a;
                data[1] = b;
                data[2] = c;

                PutData putData = new PutData(UPDATE_URL,"POST",field,data);
                if (putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
//                        Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();


                    }
                }
                else{
                    Toast.makeText(mContext,"Error on internet Connection",Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void DeleteTruck(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "truck_id";

                String[] data = new String[1];
                data[0] = truck_id;

                PutData putData = new PutData(DELETE_URL, "POST", field, data);

                if (putData.startPut()) {

                    if (putData.onComplete()) {
//                        String result = putData.getResult();
//
//                        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, Truck.class);
                        //Truck item count
                        Handler handler1 = new Handler();
                        handler1.post(new Runnable() {
                            @Override
                            public void run() {
                                String x_count = sharedPreferences.getString("truck_item_count","");
                                assert x_count != null;
                                if(!x_count.equals("0")) {
                                    int old_count = Integer.parseInt(x_count);
                                    if (old_count >= 1) {
                                        old_count--;
                                    }
                                    sharedPreferences.edit().putString("truck_item_count", String.valueOf(old_count)).apply();

                                    mContext.startActivity(intent);
                                    ((Activity)mContext).finish();

                                }

                            }
                        });

                    }

                } else {
                    Toast.makeText(mContext, "Error on Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void UpdateOrderNumber(String truck_id, String shop_id){

        String keyName = "orderNumOf"+shop_id;
        String getS = sharedPreferences.getString(keyName,"");

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[2];
                field[0] = "order_number";
                field[1] = "truck_id";

                String[] data = new String[2];
                data[0] = getS;
                data[1] = truck_id;

                PutData putData = new PutData(UPDATE_ORDER_NUMBER, "POST", field, data);

                        if(putData.startPut()){
                        if(putData.onComplete()){

                         }

                }
                else{
                    Toast.makeText(mContext,"Error on Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return truck_classes.size();
    }
}
