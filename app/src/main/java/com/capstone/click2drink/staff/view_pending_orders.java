package com.capstone.click2drink.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.capstone.click2drink.My_Purchases_Adapter;
import com.capstone.click2drink.My_Purchases_Fetcher;
import com.capstone.click2drink.R;
import com.capstone.click2drink.menu_my_purchases;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class view_pending_orders extends AppCompatActivity {

    private static String View_Order_URL = "http://resources.click2drinkph.store/php/view_ordered_products.php";
    private RecyclerView view_products;
    private RecyclerView.LayoutManager layoutManager;
    private List<View_Pending_Fetcher>product_data;
    Adapter_View_Orders adapter_view_orders;
    private LottieAnimationView lottieAnimationView;
    private ImageView back;

    //
    SharedPreferences sharedPreferences;
    String o_number;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pending_orders);

        Intent intent = getIntent();
         sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
         o_number = intent.getStringExtra("order_number");
         lottieAnimationView = findViewById(R.id.lottie);
         back = findViewById(R.id.back);

         Loading(o_number);

         back.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 onBackPressed();
             }
         });
    }

    public void getOrderedProducts(String order_number){

        view_products = findViewById(R.id.view_products);
        layoutManager = new LinearLayoutManager(view_pending_orders.this,
                LinearLayoutManager.VERTICAL, false);
        view_products.setLayoutManager(layoutManager);
        product_data = new ArrayList<>();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "order_number";

                String[] data = new String[1];
                data[0] = order_number;

                PutData putData = new PutData(View_Order_URL,"POST",field,data);

                if(putData.startPut()){

                    if(putData.onComplete()){

                        String result = putData.getResult();

                        try {
                            //Getting Response form api (Base Url)
                            JSONArray array = new JSONArray(result);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);

                                String id = object.getString("order_id");
                                String product_id = object.getString("product_id");
                                String shop_id = object.getString("shop_id");
                                String product_name = object.getString("product_name");
                                String product_price = object.getString("product_price");
                                String image_url = object.getString("image_url");
                                String product_quantity = object.getString("product_quantity");
                                String order_number = object.getString("order_number");

                                View_Pending_Fetcher dataz = new View_Pending_Fetcher(id,product_id,shop_id,product_name,
                                        product_price,image_url,product_quantity,order_number);

                                product_data.add(dataz);


                            }

                        } catch (Exception e) {

                            //remove this in production
//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();


                        }
                        lottieAnimationView.setVisibility(View.GONE);
                        adapter_view_orders = new Adapter_View_Orders(view_pending_orders.this,product_data);
                        view_products.setAdapter(adapter_view_orders);


                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Internet Connectivity Error", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    public void Loading(String o_number) {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getOrderedProducts(o_number);
                lottieAnimationView.setVisibility(View.GONE);
            }
        }, 1000);
    }
}