package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class menu_my_purchases extends AppCompatActivity {

    // RECYCLER
    private List<My_Purchases_Fetcher> my_purchases_fetchers;
    My_Purchases_Adapter my_purchases_adapter;
    RecyclerView received_orders;
    RecyclerView.LayoutManager layoutManager;
    SharedPreferences sharedPreferences;

    String SP_ID, SP_EMAIL, order_AddressID;
    TextView empty_alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my__purchases);

        sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        SP_ID = sharedPreferences.getString("login_user_id","");
        SP_EMAIL = sharedPreferences.getString("temp_email","");
        ImageView back = findViewById(R.id.back);
        empty_alert = findViewById(R.id.empty_alert);

        getOrders();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_my_purchases.this,MainUi.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void getOrders(){

        // -----------------

        received_orders = findViewById(R.id.received_orders);
        layoutManager = new LinearLayoutManager(menu_my_purchases.this,
                LinearLayoutManager.VERTICAL, false);
        received_orders.setLayoutManager(layoutManager);
        my_purchases_fetchers = new ArrayList<>();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "cuser_id";

                String[] data = new String[1];
                data[0] = SP_ID;

                PutData putData = new PutData("http://resources.click2drinkph.store/php/get_user_my_purchases.php","POST",field,data);

                if(putData.startPut()){

                    if(putData.onComplete()){

                        String result = putData.getResult();

                        try {

                            //Getting Response form api (Base Url)
                            JSONArray array = new JSONArray(result);

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);

                                String id = object.getString("id");
                                String product_id = object.getString("product_id");
                                String shop_id = object.getString("shop_id");
                                String product_name = object.getString("product_name");
                                String product_price = object.getString("product_price");
                                order_AddressID = object.getString("address_id");
                                String image_url = object.getString("image_url");
                                String product_quantity = object.getString("product_quantity");
                                String order_number = object.getString("order_number");
                                String received_on = object.getString("received_on");

                                My_Purchases_Fetcher dataz = new My_Purchases_Fetcher(id,product_id,shop_id,product_name,
                                        product_price,image_url,product_quantity,order_number, received_on);
                                my_purchases_fetchers.add(dataz);


                            }

                        } catch (Exception e) {

                            //remove this in production
//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            empty_alert.setVisibility(View.VISIBLE);

                        }

                        my_purchases_adapter = new My_Purchases_Adapter(menu_my_purchases.this,my_purchases_fetchers);
                        received_orders.setAdapter(my_purchases_adapter);



                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Internet Connectivity Error", Toast.LENGTH_SHORT).show();
                }



            }
        });

    }
}