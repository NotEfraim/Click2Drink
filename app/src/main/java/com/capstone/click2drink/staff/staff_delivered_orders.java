package com.capstone.click2drink.staff;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.capstone.click2drink.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class staff_delivered_orders extends AppCompatActivity {

    //Recycler
    private static String INVOICE_LOG_URL ="http://resources.click2drinkph.store/php/view_invoice_log.php";
    public static final String ADDRESS_URL = "http://resources.click2drinkph.store/php/getDefaultAddress.php";
    private static String GET_NAME_URL = "http://resources.click2drinkph.store/php/get_UserName_by_Id.php";

    private List<Invoice_Log_Fetcher> Invoice_data_list;
    private List<Address_Order_Fetcher> address_data_list;
    private List<Name_Fetcher> user_name_list;

    RecyclerView delivered_orders;
    Address_Order_Fetcher address_order_fetcher;
    Invoice_Log_Fetcher invoice_order_fetcher;
    Name_Fetcher name_fetcher;
    Adapter_Delivered_Orders adapter_delivered_orders;
    RecyclerView.LayoutManager layoutManager;
    LottieAnimationView lottieAnimationView;
    TextView empty_alert;
    ImageView back;

    SharedPreferences sharedPreferences;
    String shop_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_delivered_orders);

        sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        shop_id = sharedPreferences.getString("staff_shop_id","");
        lottieAnimationView = findViewById(R.id.lottie);
        empty_alert = findViewById(R.id.empty_alert);
        back = findViewById(R.id.back);

        delivered_orders = findViewById(R.id.delivered_orders);
        layoutManager = new LinearLayoutManager(staff_delivered_orders.this,
                LinearLayoutManager.VERTICAL, false);
        delivered_orders.setLayoutManager(layoutManager);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Loading();

    }

    public void getInvoice(){
        Invoice_data_list = new ArrayList<>();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] ="shop_id";

                String[] data = new String[1];
                data[0] = shop_id;

                PutData putData = new PutData(INVOICE_LOG_URL, "POST", field, data);

                if(putData.startPut()){

                    if(putData.onComplete()){
                        String result = putData.getResult();

                        try {

                            JSONArray array = new JSONArray(result);
                            for(int i = 0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);

                                String id = object.getString("id");
                                String order_number = object.getString("order_number");
                                String cuser_id = object.getString("cuser_id");
                                String address_id = object.getString("address_id");
                                String total_price = object.getString("total_price");
                                String total_gallons = object.getString("total_gallons");
                                String received_date = object.getString("received_date");

                                String DEFAULT_ADDRESS_ID = object.getString("address_id");


                                invoice_order_fetcher = new Invoice_Log_Fetcher(order_number,total_price,
                                        total_gallons,received_date, address_id);

                                Invoice_data_list.add(invoice_order_fetcher);

                                //calling function
                                        getOrderName(cuser_id);
                                        getOrderAddress(DEFAULT_ADDRESS_ID);



                            }

                        }
                        catch (Exception e){
                                lottieAnimationView.setVisibility(View.GONE);
                                empty_alert.setVisibility(View.VISIBLE);
//                           Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Internet Connection Error", Toast.LENGTH_SHORT).show();
                }



            }
        });
    }
    public void getOrderAddress(String address_id) {

        address_data_list = new ArrayList<>();


        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "address_id";

                String[] data = new String[1];
                data[0] = address_id;

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

                                address_order_fetcher = new Address_Order_Fetcher(address_id,cuser_number,address_title,combined_address_txt);
                                address_data_list.add(address_order_fetcher);

                            }

                        } catch (Exception e) {
//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }

                        lottieAnimationView.setVisibility(View.GONE);
                        adapter_delivered_orders = new Adapter_Delivered_Orders(staff_delivered_orders.this,
                                Invoice_data_list,address_data_list,user_name_list);
                        delivered_orders.setAdapter(adapter_delivered_orders);

                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Unstable internet Connection",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void getOrderName(String cuser_id){

        user_name_list = new ArrayList<>();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "cuser_id";

                String[] data = new String[1];
                data[0] = cuser_id;

                PutData putData = new PutData(GET_NAME_URL,"POST",field,data);

                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();

                        try {
                            JSONArray array = new JSONArray(result);

                            for(int i = 0; i < array.length(); i++){
                                JSONObject object = array.getJSONObject(i);
                                String cuser_name = object.getString("cuser_name");

                                name_fetcher = new Name_Fetcher(cuser_name);
                                user_name_list.add(name_fetcher);
                            }
                        }
                        catch (Exception e){

//                            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                }

            }
        });
    }

    public void Loading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getInvoice();
            }
        },1000);

    }

}