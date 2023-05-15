package com.capstone.click2drink;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Random;

// ITO LAHAT GINAMIT PARA SA 3 NA TABS KASE SAME LANG

public class DispenserAdapter extends RecyclerView.Adapter<DispenserAdapter.MyViewHolder> {

    private static String ADDTOTRUCK_URL ="http://resources.click2drinkph.store/php/AddToTruck.php";
    private Context mContext;
    private List<Product_Dispenser> dispensers;


    public DispenserAdapter(Context context, List<Product_Dispenser> dispensers){

        this.mContext = context;
        this.dispensers = dispensers;

    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        private ImageView image_url;
        private TextView product_name;
        private TextView product_price;
        private TextView location;
        private Button buy_now;
        private Button add_to_truck;


        public MyViewHolder(View view) {
            super(view);

            image_url = view.findViewById(R.id.product_image);
            product_name = view.findViewById(R.id.product_name);
            product_price = view.findViewById(R.id.product_price);
            location = view.findViewById(R.id.location);
            buy_now = view.findViewById(R.id.buy_now);
            add_to_truck = view.findViewById(R.id.add_to_truck);



        }
    }



    @NotNull
    @Override
    public MyViewHolder onCreateViewHolder(@NotNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.categories_layout,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Product_Dispenser product_dispenser = dispensers.get(position);
        holder.product_name.setText(product_dispenser.getProduct_name());
        holder.product_price.setText(product_dispenser.getProduct_price());
        Glide.with(mContext).load(product_dispenser.getImage_url()).into(holder.image_url);
        holder.location.setText(product_dispenser.getLocation());

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        String login_user_id = sharedPreferences.getString("login_user_id","");


        String iUrl = product_dispenser.getImage_url();
        String pPrice = product_dispenser.getProduct_price();
        String pname = product_dispenser.getProduct_name();
        String loc = product_dispenser.getLocation();
        String sId = product_dispenser.getShop_id();
        String pId = product_dispenser.getProduct_id();

        // Generate Unique_Order Number
        final int min = 10;
        final int max = 90;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String order_number = login_user_id+random+pId+sId;

        holder.buy_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext,buy_now.class);

                intent.putExtra("image_url", iUrl);
                intent.putExtra("product_name",pname);
                intent.putExtra("product_price",pPrice);
                intent.putExtra("location", loc);
                intent.putExtra("shop_id", sId);
                intent.putExtra("product_id",pId);
                intent.putExtra("order_number", order_number);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);

            }
        });

        holder.add_to_truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ADD_ORDER(login_user_id,pId,pname,pPrice,loc,iUrl,sId);

            }
        });

    }

    @Override
    public int getItemCount() {
        return dispensers.size();
    }

    public void ADD_ORDER (String cuser_id,String product_id,String product_name,String product_price,String product_location,String image_url, String shop_id){

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("SharedPref",Context.MODE_PRIVATE);

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

                        String result = putData.getResult();
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
