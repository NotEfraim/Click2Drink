package com.capstone.click2drink;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.common.util.ArrayUtils;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;

public class Truck extends AppCompatActivity {
    RecyclerView truckItem;
    private List<Truck_Class> truck_classes;
    private Truck_Adapter pAdapter;
    private RecyclerView.LayoutManager pManager;

    private String TRUCK_URL = "http://resources.click2drinkph.store/php/ViewTrucks.php";
    private String PLACE_ORDER_URL = "http://resources.click2drinkph.store/php/truck_checkout.php";
    private String CREATE_INVOICE_URL = "http://resources.click2drinkph.store/php/create_invoice.php";
    private String UPDATE_ADDRESS_URL = "http://resources.click2drinkph.store/php/update_address_id.php";
    private String VIEW_TRUCK_BY_ORDER_NUMBER = "http://resources.click2drinkph.store/php/VtruckByOrderNumber.php";
    LottieAnimationView place_order_progress;
    SharedPreferences sharedPreferences;
    private String SP_ID,DEFAULT_ADD;
    ImageView back;
    CardView checkout_container;
    TextView total_gallons;
    TextView subtotal;
    TextView empty_alert,header;
    TextView total;
    Button Place_Order;
    Button checkout;

    // Creating Order Number
    private String order_number;

    // For unique price and quantity of shop products invoice
    int shop_product_price;
    int shop_product_quantity;

    // Dialog
    Dialog checkout_dialog;

    // For shop_id's
    private String[] shopId_Holder;
    private String getS, putS;
    private String[] order_number_holder;
    int execution_count = 1, index_caller = 0;
    ArrayList<String> new_shop_id_Holder;

    //DATE BETA
    String currentDateAndTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_fade_out);
        setContentView(R.layout.activity_truck);
        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        SP_ID = sharedPreferences.getString("login_user_id","");
        DEFAULT_ADD = sharedPreferences.getString("default_address","");
        String item_count = sharedPreferences.getString("truck_item_count","");
        total_gallons = findViewById(R.id.total_gallon);
        subtotal = findViewById(R.id.subtotal);
        checkout = findViewById(R.id.checkout);
        header = findViewById(R.id.textView19);
        empty_alert = findViewById(R.id.empty_alert);
        checkout_container = findViewById(R.id.checkout_container);

        total = findViewById(R.id.total);
        back = findViewById(R.id.back);

        //Current date used for time_stamp
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd | KK:mm:ss a", Locale.getDefault());
        currentDateAndTime = df.format(new Date());

        getShopID();
        onBackpress();
        RefreshView();
        checkOut();


        // Error Catchers
        assert item_count != null;
        if(item_count.equals("0") || item_count.equals("")){
            checkout_container.setVisibility(View.GONE);
            header.setVisibility(View.GONE);
            empty_alert.setVisibility(View.VISIBLE);
        }
        else{
            header.setVisibility(View.VISIBLE);
            empty_alert.setVisibility(View.GONE);
        }
        //


        //Dialog
        checkout_dialog = new Dialog(Truck.this);
        checkout_dialog.setContentView(R.layout.confirmation_layout);
        checkout_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Place_Order = checkout_dialog.findViewById(R.id.place_order);
        place_order_progress = checkout_dialog.findViewById(R.id.progress);


        Place_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                place_order_progress.setVisibility(View.VISIBLE);
                removeDuplicateArrayValue();
                placeOrder();

                // removing shared pref order_number
                execution_count = new_shop_id_Holder.size();
                index_caller = 0;

                while(index_caller < execution_count) {
                    String keyName = "orderNumOf"+new_shop_id_Holder.get(index_caller);
                    sharedPreferences.edit().remove(keyName).apply();
                    index_caller ++;
                }


            }
        });
    }

    public void CreateInvoice(String keyName, String shop_id){

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    String get = String.valueOf(shop_product_price);
                    String get1 = String.valueOf(shop_product_quantity);

                    String[] field = new String[7];
                    field[0] = "order_number";
                    field[1] = "cuser_id";
                    field[2] = "address_id";
                    field[3] = "shop_id";
                    field[4] = "total_price";
                    field[5] = "total_gallons";
                    field[6] = "date_ordered";

                    String[] data = new String[7];
                    data[0] = keyName;
                    data[1] = SP_ID;
                    data[2] = DEFAULT_ADD;
                    data[3] = shop_id;
                    data[4] = get;
                    data[5] = get1;
                    data[6] = currentDateAndTime;

                    PutData putData = new PutData(CREATE_INVOICE_URL,"POST",field,data);

                    if(putData.startPut()){
                        if(putData.onComplete()){
//                            String result = putData.getResult();
//                        Toast.makeText(getApplicationContext(),currentDateAndTime,Toast.LENGTH_SHORT).show();
                            sharedPreferences.edit().putString("truck_item_count","0").apply();
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Error on Internet Connection",Toast.LENGTH_SHORT).show();
                    }


                }
            });

    }

    public void placeOrder(){

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                // e ple-place na natin lahat ng nasa truck
                String[] field = new String[1];
                field[0] = "cuser_id";

                String[] data = new String[1];
                data[0] = SP_ID;

                PutData putData = new PutData(PLACE_ORDER_URL,"POST",field,data);

                if(putData.startPut()){
                    if(putData.onComplete()){
                        checkout_dialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Order is Successfully Created",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Truck.this,Track_View.class);
                        place_order_progress.setVisibility(View.GONE);
                        startActivity(intent);
                        finish();


                    }
                }
                else{
                    Toast.makeText(getApplicationContext(),"Error on Internet Connection",Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void refresh(int milliseconds){
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                RefreshView();
            }
        };
        handler.postDelayed(runnable,milliseconds);
    }
    @SuppressLint("SetTextI18n")
    public void RefreshView(){

        String get = sharedPreferences.getString("total_gallons","");
        String get1 = sharedPreferences.getString("subtotal","");

        subtotal.setText(get1+".00");
        total.setText(get1+".00");
        total_gallons.setText(get);
        refresh(300);
    }

    public void checkOut(){
        checkout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if(DEFAULT_ADD.equals("")){
                    Toast.makeText(getApplicationContext(),"Set your default address first",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Truck.this,menu_address.class);
                    startActivity(intent);
                    finish();
                }
                else{

                    TextView c_subtotal = checkout_dialog.findViewById(R.id.subtotal);
                    TextView c_total = checkout_dialog.findViewById(R.id.total);
                    TextView c_gallons = checkout_dialog.findViewById(R.id.total_gallon);
                    TextView c_num = checkout_dialog.findViewById(R.id.user_phone_number);
                    TextView c_add = checkout_dialog.findViewById(R.id.user_default_address);

                    String get = sharedPreferences.getString("total_gallons","");
                    String get1 = sharedPreferences.getString("subtotal","");
                    String get2 = sharedPreferences.getString("string_address","");
                    String get3 = sharedPreferences.getString("receipt_number","");

                    c_add.setText(get2);
                    c_num.setText(get3);
                    c_subtotal.setText(get1+".00");
                    c_total.setText(get1+".00");
                    c_gallons.setText(get);

                    checkout_dialog.show();

                }

            }
        });
    }

    public void GetProductPriceAndQuantity(String order_number){

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                // View truck by order number ginawa ko to para kunin mga product quantity at price ng individual
                //shop products

                String[] field = new String[2];
                field[0] = "cuser_id";
                field[1] = "order_number";

                String[] data = new String[2];
                data[0] = SP_ID;
                data[1] = order_number;

                PutData putData = new PutData(VIEW_TRUCK_BY_ORDER_NUMBER, "POST", field, data);

                if(putData.startPut()){

                    if(putData.onComplete()){
                        String result = putData.getResult();

                        //Setting size of array
                        int[] Pvalue = new int[result.length()];
                        int[] Qvalue = new int[result.length()];


                        try {

                            //Getting Response form api (Base Url)
                            JSONArray array = new JSONArray(result);


                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                String product_price = object.getString("order_price");
                                String product_quantity = object.getString("order_quantity");

                                Pvalue[i] = Integer.parseInt(product_price);
                                Qvalue[i] = Integer.parseInt(product_quantity);

                            }

                        } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                        }
                                shop_product_price = 0;
                                shop_product_quantity = 0;

                            for(int j = 0; j < 3; j++){
                                shop_product_price = shop_product_price + Pvalue[j];
                                shop_product_quantity = shop_product_quantity + Qvalue[j];
                            }

                    } //IF

                }
                else{
                    Toast.makeText(getApplicationContext(),"Error on Internet Connection", Toast.LENGTH_SHORT).show();
                }




            }
        });

    }

    public void getShopID(){

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "cuser_id";

                String[] data = new String[1];
                data[0] = SP_ID;


                    PutData putData = new PutData(TRUCK_URL,"POST",field,data);
                    if(putData.startPut()){

                        if(putData.onComplete()){
                            String result = putData.getResult();

                            try {

                                //Getting Response form api (Base Url)
                                JSONArray array = new JSONArray(result);

                                shopId_Holder = new String[array.length()];
                                order_number_holder = new String[array.length()];
                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    String shop_id = object.getString("shop_id");
                                    shopId_Holder[i] = shop_id;
                                    order_number_holder[i] = "0";

                                }

                                ViewTruck();

                            } catch (Exception e) {

                                //remove this in production
//                                 Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            }

                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Error on Internet Connection",Toast.LENGTH_SHORT).show();
                    }






            }
        });
    }

    public void removeDuplicateArrayValue(){
        // Ginamit mo to Efraim para tanggalin yung duplicate sa shop_id holder
        // na gagamitin mo mamaya para sa execution count
        // ilalagay mo lang to after ng truckview para di maka apekto sa bilang ng item sa truck

//        Toast.makeText(getApplicationContext(),String.valueOf(numbers),Toast.LENGTH_SHORT).show();
        List<String> numbers = Arrays.asList(shopId_Holder);
        Set<String> hashSet = new LinkedHashSet(numbers);
        new_shop_id_Holder = new ArrayList(hashSet);

//        Toast.makeText(getApplicationContext(),String.valueOf(new_shop_id_Holder),Toast.LENGTH_SHORT).show();

        for(int i = 0; i<new_shop_id_Holder.size(); i++){
            String keyName = "orderNumOf"+new_shop_id_Holder.get(i);
            getS = sharedPreferences.getString(keyName,"");

            assert getS != null;
            if(!getS.equals("")){
                execution_count ++;
            }

        }


//        Toast.makeText(getApplicationContext(),String.valueOf(new_shop_id_Holder.size()),Toast.LENGTH_SHORT).show();
        execution_count = new_shop_id_Holder.size();
        // kailangan to para gumawa ng invoice sa different shop id

        while(index_caller < execution_count) {
            String keyName = "orderNumOf"+new_shop_id_Holder.get(index_caller);
            getS = sharedPreferences.getString(keyName,"");
            String shop_id = new_shop_id_Holder.get(index_caller);
            GetProductPriceAndQuantity(getS);
            CreateInvoice(getS, shop_id);
            index_caller ++;
            }

        }

    public void ViewTruck(){
        truckItem = findViewById(R.id.truck_recycler);
        pManager = new LinearLayoutManager(Truck.this,LinearLayoutManager.VERTICAL,false);
        truckItem.setLayoutManager(pManager);
        truck_classes = new ArrayList<>();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "cuser_id";

                String[] data = new String[1];
                data[0] = SP_ID;



                if(!SP_ID.equals("")){

                    PutData putData = new PutData(TRUCK_URL,"POST",field,data);
                    if(putData.startPut()){

                        if(putData.onComplete()){
                            String result = putData.getResult();

                            try {

                                //Getting Response form api (Base Url)
                                JSONArray array = new JSONArray(result);

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);

                                    String truck_id = object.getString("truck_id");
                                    String product_id = object.getString("product_id");
                                    String shop_id = object.getString("shop_id");
                                    String product_name = object.getString("product_name");
                                    String product_price = object.getString("product_price");
                                    String product_location = object.getString("product_location");
                                    String image_url = object.getString("image_url");

                                    //Kinuha muna natin yung shop_id sa taas yung getshop_id at nilagay sa isang array
                                    //tapos kinompare siya sa if else
                                    // pag true gagawa tayo ng keyname na unique gamit yung shopID sa dulo
                                    // if yung keyname ay may laman na yun ang magiging orderNumber uyng data galing sa shared pref
                                    // if hindi at blank ang laman mag gegenerate siya ng bago , ilalagay ang keyname sa sharedpref
                                    // pati ang value
                                    if(shopId_Holder[i].equals(shop_id)){

                                        String keyName = "orderNumOf"+shop_id;
                                        getS = sharedPreferences.getString(keyName,"");

                                        assert getS != null;
                                        if(getS.equals("")){

                                            // Generate Unique_Order Number
                                            final int min = 100;
                                            final int max = 999;
                                            final int random = new Random().nextInt((max - min) + 1) + min;
                                            String generate = SP_ID+random+shop_id+product_id;

                                            sharedPreferences.edit().putString(keyName,generate).apply();
                                            order_number = generate;
                                        }
                                        else{
                                            order_number = getS;
                                        }

                                    }

                                    Truck_Class truck_data = new Truck_Class(truck_id,product_id,product_name,product_price,
                                            product_location,image_url,order_number,shop_id);
                                    truck_classes.add(truck_data);
                                }

                            } catch (Exception e) {
                                total_gallons.setVisibility(View.GONE);
                                subtotal.setVisibility(View.GONE);
                                total.setVisibility(View.GONE);
                                //remove this in production
//                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            }

                            pAdapter = new Truck_Adapter(Truck.this,truck_classes);
                            truckItem.setAdapter(pAdapter);
                            UpdateAddressID();

                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Error on Internet Connection",Toast.LENGTH_SHORT).show();
                    }


                }
                else{
                    Toast.makeText(getApplicationContext(),"Login ID not found please try again",
                            Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    public void UpdateAddressID(){

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[2];
                field[0] = "address_id";
                field[1] = "cuser_id";

                String[] data = new String[2];
                data[0] = DEFAULT_ADD;
                data[1] = SP_ID;

                PutData putData = new PutData(UPDATE_ADDRESS_URL,"POST", field, data);
                if(putData.startPut()){

                    if(putData.onComplete()){
                        String result = putData.getResult();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Error on Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void onBackpress(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }



}