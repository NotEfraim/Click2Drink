package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class specific_prod_viewer extends AppCompatActivity {

    private RecyclerView holder;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager manager;
    private List<Product_Dispenser> product_dispensers;
    private LottieAnimationView progress;
    private TextView error;

    private ImageView truck;
    private CardView item_layout;
    private TextView item_count;
    public static final String COUNTER_URL = "http://resources.click2drinkph.store/php/item_counter.php";

    LottieAnimationView x_progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_prod_viewer);

        String url = "http://resources.click2drinkph.store/php/specific_prod_view.php";

        Intent intent = getIntent();
        String shop_id = intent.getStringExtra("shop_id");

        holder = findViewById(R.id.view_products);
        manager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        holder.setLayoutManager(manager);
        product_dispensers = new ArrayList<>();

        error = findViewById(R.id.empty_alert);


        // To Truck
        truck = findViewById(R.id.to_truck);
        item_layout = findViewById(R.id.item_layout);
        item_count = findViewById(R.id.item_count);

        //Progress
        x_progress = findViewById(R.id.x_progress);


        // Calling the functions
        TruckNotification();
        Truck();
        setItem_count();

        //BACK
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "shop_id";

                String[] data = new String[1];
                data[0] = shop_id;

                PutData putData = new PutData(url, "POST", field, data);

                if(putData.startPut()){

                    if(putData.onComplete()){

                        String result = putData.getResult();

                        try {

                            JSONArray array = new JSONArray(result);

                            for(int i = 0 ; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);

                                String shop_id = object.getString("shop_id");
                                String product_id = object.getString("prod_id");
                                String image_url = object.getString("image_url");
                                String product_name = object.getString("prod_name");
                                String product_price = object.getString("prod_price");
                                String location = object.getString("location");
                                Product_Dispenser dataX = new Product_Dispenser(product_id,product_name,product_price,image_url,location,shop_id);

                                product_dispensers.add(dataX);
                            }

                        }
                        catch (Exception e){
                             error.setVisibility(View.VISIBLE);
                            x_progress.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                        mAdapter = new DispenserAdapter(getApplicationContext(), product_dispensers);
                        holder.setAdapter(mAdapter);

                        Handler x = new Handler();
                        x.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                x_progress.setVisibility(View.GONE);
                            }
                        },1000);


                    }

                }
                else{

                    Toast.makeText(getApplicationContext(),"Error on Operation", Toast.LENGTH_SHORT).show();
                }

            }
        },1000);


    }

    //    // Truck utilities
    public void TruckNotification(){
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref", Context.MODE_PRIVATE);
        String login_user_id = sharedPreferences.getString("login_user_id","");

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "cuser_id";

                String[] data = new String[1];
                data[0] = login_user_id;

                PutData putData = new PutData(COUNTER_URL,"POST",field,data);

                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();

                        if(!result.trim().equals("0")){
                            sharedPreferences.edit().putString("truck_item_count",result).apply();
                            setItem_count();
                        }
                        else {
                            item_layout.setVisibility(View.GONE);
                        }



                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Truck API Error:\n Order is now on Truck",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void setItem_count(){

        refresh(500);
        SharedPreferences sharedPreferences = getSharedPreferences("SharedPref",Context.MODE_PRIVATE);
        String temp_count = sharedPreferences.getString("truck_item_count","0");
        assert temp_count != null;
        if(temp_count.equals("0") || temp_count.equals("")){
            item_layout.setVisibility(View.GONE);
        }
        else{
            item_count.setText(temp_count);
            item_layout.setVisibility(View.VISIBLE);
        }

    }
    //REFRESH
    public void refresh(int milliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                setItem_count();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }

    public void Truck(){
        truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent x  = new Intent(getApplicationContext(),Truck.class);
                startActivity(x);
            }
        });
    }


}