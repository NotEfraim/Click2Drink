package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Track_Order extends AppCompatActivity {

    public static final String ADDRESS_URL = "http://resources.click2drinkph.store/php/getDefaultAddress.php";
    public static final String RECEIVED_URL = "http://resources.click2drinkph.store/php/order_received.php";
    private TextView order_number;
    private TextView total_price, total_gallons;
    private String order_number_txt;
    private CardView order_process, delivery, order_received;
    private FrameLayout a,b,c,d;
    private String delivery_status;
    private ImageView back;
    private SharedPreferences sharedPreferences;
    private String SP_ID, SP_EMAIL, ADDRESS_ID;
    private String order_quantity, order_price;
    private String order_AddressID;
    private Button received_bttn;

    //

    TextView address_title_v, phone_number, combined_address;

    // RECYCLER
    private List<Process_Order_Fetcher> process_order_fetcher;
    Process_Order_Adapter process_order_adapter;
    RecyclerView order_summary;
    RecyclerView.LayoutManager layoutManager;

    //DATE BETA
    String currentDateAndTime;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track__order);

        sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        SP_ID = sharedPreferences.getString("login_user_id","");
        SP_EMAIL = sharedPreferences.getString("temp_email","");
        ADDRESS_ID = sharedPreferences.getString("default_address","");


        Intent intent = getIntent();
        delivery_status = intent.getStringExtra("order_status");
        order_number_txt = intent.getStringExtra("order_number");
        order_price = intent.getStringExtra("order_price");
        order_quantity = intent.getStringExtra("order_quantity");

        order_number = findViewById(R.id.order_number);
        order_number.setText(order_number_txt);
        order_process = findViewById(R.id.order_process);
        delivery = findViewById(R.id.delivery);
        order_received = findViewById(R.id.order_received);
        total_price = findViewById(R.id.total_price);
        total_gallons = findViewById(R.id.total_gallons);
        back = findViewById(R.id.back);
        address_title_v = findViewById(R.id.fullName);
        phone_number = findViewById(R.id.phone_number);
        combined_address = findViewById(R.id.combined_address);
        received_bttn = findViewById(R.id.button_received);

        total_price.setText(order_price+".00");
        total_gallons.setText(order_quantity);

        //Current date used for time_stamp
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd | KK:mm:ss a", Locale.getDefault());
        currentDateAndTime = df.format(new Date());

        a = findViewById(R.id.vertical_1);
        b = findViewById(R.id.vertical_2);
        c = findViewById(R.id.vertical_3);
        d = findViewById(R.id.vertical_2_1);

        if(delivery_status.equals("Processing")){
            order_received.setVisibility(View.GONE);
            delivery.setVisibility(View.GONE);
            c.setVisibility(View.GONE);
        }
        else if (delivery_status.equals("Out for Delivery")){
            order_received.setVisibility(View.GONE);
            b.setVisibility(View.GONE);
        }
        else{
            received_bttn.setVisibility(View.VISIBLE);
        }

        OnbackPressed();
        getOrders();


        received_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Received_Orders();
            }
        });

    }

    public void OnbackPressed(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void getOrders(){

        // -----------------

        order_summary = findViewById(R.id.order_summary);
        layoutManager = new LinearLayoutManager(Track_Order.this,
                LinearLayoutManager.VERTICAL, false);
        order_summary.setLayoutManager(layoutManager);
        process_order_fetcher = new ArrayList<>();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[2];
                field[0] = "cuser_id";
                field[1] = "order_number";

                String[] data = new String[2];
                data[0] = SP_ID;
                data[1] = order_number_txt;

                PutData putData = new PutData("http://resources.click2drinkph.store/php/view_user_order.php","POST",field,data);

                    if(putData.startPut()){

                        if(putData.onComplete()){

                            String result = putData.getResult();

                            try {

                                //Getting Response form api (Base Url)
                                JSONArray array = new JSONArray(result);

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);

                                    String order_id = object.getString("order_id");
                                    String product_id = object.getString("product_id");
                                    String shop_id = object.getString("shop_id");
                                    String product_name = object.getString("product_name");
                                    String product_price = object.getString("product_price");
                                    order_AddressID = object.getString("address_id");
                                    String image_url = object.getString("image_url");
                                    String product_quantity = object.getString("product_quantity");

                                    Process_Order_Fetcher dataz = new Process_Order_Fetcher(order_id,product_id,shop_id,product_name,product_price,image_url,product_quantity);
                                    process_order_fetcher.add(dataz);


                                }

                            } catch (Exception e) {

                                //remove this in production
//                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            }

                            process_order_adapter = new Process_Order_Adapter(Track_Order.this,process_order_fetcher);
                            order_summary.setAdapter(process_order_adapter);
                            getOrderAddress();


                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Internet Connectivity Error", Toast.LENGTH_SHORT).show();
                    }



            }
        });

    }

    public void getOrderAddress() {


        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                    String[] field = new String[1];
                    field[0] = "address_id";

                    String[] data = new String[1];
                    data[0] = order_AddressID;

                    PutData putData = new PutData(ADDRESS_URL,"POST",field,data);

                    if(putData.startPut()){

                        if(putData.onComplete()) {
                            String result = putData.getResult();

                            try {

                                //Getting Response form api (Base Url)
                                JSONArray array = new JSONArray(result);


                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);

                                    String address_id = object.getString("address_id");
                                    String address_title = object.getString("address_title");
                                    String cuser_number = object.getString("cuser_number");
                                    String cuser_house_number = object.getString("cuser_house_number");
                                    String cuser_brgy = object.getString("cuser_brgy");
                                    String cuser_municipality = object.getString("cuser_municipality");
                                    String cuser_province = object.getString("cuser_province");
                                    String cuser_zipcode = object.getString("zipcode");

                                    String combined_address_txt = cuser_house_number+" "+cuser_brgy+" "
                                            +cuser_municipality+" "+ cuser_province+" "+cuser_zipcode;


                                    address_title_v.setText(address_title);
                                    phone_number.setText(cuser_number);
                                    combined_address.setText(combined_address_txt);




                                }

                            } catch (Exception e) {
//                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            }



                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Unstable internet Connection",Toast.LENGTH_SHORT).show();
                    }



            }
        });
    }

    public void Received_Orders(){

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String [2];
                field[0] = "order_number";
                field[1] = "current_date";

                String[] data = new String[2];
                data[0] = order_number_txt;
                data[1] = currentDateAndTime;

                PutData putData = new PutData(RECEIVED_URL, "POST", field, data);

                if(putData.startPut()){

                    if(putData.onComplete()){
                        Intent a = new Intent(Track_Order.this,Track_View.class);
                        startActivity(a);
                        finish();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Error on Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}