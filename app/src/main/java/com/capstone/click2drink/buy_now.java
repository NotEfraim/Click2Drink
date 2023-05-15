package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class buy_now extends AppCompatActivity {

    //VIEW
    ImageView add;
    ImageView minus;
    TextView pPrice;
    TextView pQuantity;
    Button buy_again;

    //DATA
    String product_name;
    String product_price;
    String location;
    String image_url;
    String product_id;
    String shop_id;
    String order_number;
    int product_quantity = 1;
    int price_holder;
    SharedPreferences sharedPreferences;
    String DEFAULT_ADD, SP_ID;

    //CHECKOUT DIALOG
    Dialog checkout_dialog;
    Button Place_Order;
    LottieAnimationView place_order_progress;

    //DATE BETA
    String currentDateAndTime;

    //BACKEND
    private static String BUY_URL ="http://resources.click2drinkph.store/php/Buy_Now.php";
    private static String CREATE_INVOICE_URL = "http://resources.click2drinkph.store/php/create_invoice.php";

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_now);

        //SharedPref
        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        DEFAULT_ADD = sharedPreferences.getString("default_address","");
        SP_ID = sharedPreferences.getString("login_user_id","");

        //Current date used for time_stamp
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd | KK:mm:ss a", Locale.getDefault());
        currentDateAndTime = df.format(new Date());


        // VIEWS
        ImageView img = findViewById(R.id.image_url);
        TextView pName = findViewById(R.id.product_name);
        pPrice = findViewById(R.id.product_price);
        TextView loc = findViewById(R.id.location);
        add = findViewById(R.id.add_quantity);
        minus = findViewById(R.id.minus_quantity);
        ImageView back = findViewById(R.id.back);
        pQuantity = findViewById(R.id.product_quantity);
        buy_again = findViewById(R.id.Buy_Again);

        // BACK PRESSED
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //CHECKOUT DIALOG
        //Dialog
        checkout_dialog = new Dialog(buy_now.this);
        checkout_dialog.setContentView(R.layout.confirmation_layout);
        checkout_dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        Place_Order = checkout_dialog.findViewById(R.id.place_order);
        place_order_progress = checkout_dialog.findViewById(R.id.progress);

        // Getting Data from Intent
        Intent intent = getIntent();

        product_name = intent.getStringExtra("product_name");
        product_price = intent.getStringExtra("product_price");
        location = intent.getStringExtra("location");
        image_url = intent.getStringExtra("image_url");
        shop_id = intent.getStringExtra("shop_id");
        product_id = intent.getStringExtra("product_id");
        order_number = intent.getStringExtra("order_number");


        //LOAD THE CONTENT
        Glide.with(getApplicationContext()).load(image_url).into(img);
        pName.setText(product_name);
        pPrice.setText("₱"+product_price+".00");
        loc.setText(location);

        //Holder
        price_holder = Integer.parseInt(product_price);

        ADD();
        MINUS();
        Checkout();
        Place_Order();

    }

    public void Place_Order(){
        Place_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String[] field = new String[9];
                        field[0] = "cuser_id";
                        field[1] = "address_id";
                        field[2] = "product_id";
                        field[3] = "shop_id";
                        field[4] = "product_name";
                        field[5] = "product_price";
                        field[6] = "product_quantity";
                        field[7] = "image_url";
                        field[8] ="order_number";

                        String a = String.valueOf(price_holder);
                        String b = String.valueOf(product_quantity);

                        String[] data = new String[9];
                        data[0] = SP_ID;
                        data[1] = DEFAULT_ADD;
                        data[2] = product_id;
                        data[3] = shop_id;
                        data[4] = product_name;
                        data[5] = a;
                        data[6] = b;
                        data[7] = image_url;
                        data[8] = order_number;

                        PutData putData = new PutData(BUY_URL, "POST", field, data);

                        if(putData.startPut()){

                            if(putData.onComplete()){
                                CreateInvoice();
                                String result = putData.getResult();
                                Toast.makeText(getApplicationContext(),"Order Successfully Created", Toast.LENGTH_SHORT).show();
                                Intent x = new Intent(getApplicationContext(),Track_View.class);
                                startActivity(x);
                                finish();
                            }

                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Error on Internet Connection", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });
    }

    public void CreateInvoice(){

        String a = String.valueOf(price_holder);
        String b = String.valueOf(product_quantity);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[7];
                field[0] = "order_number";
                field[1] = "cuser_id";
                field[2] = "address_id";
                field[3] = "shop_id";
                field[4] = "total_price";
                field[5] = "total_gallons";
                field[6] = "date_ordered";

                String[] data = new String[7];
                data[0] = order_number;
                data[1] = SP_ID;
                data[2] = DEFAULT_ADD;
                data[3] = shop_id;
                data[4] = a;
                data[5] = b;
                data[6] = currentDateAndTime;

                PutData putData = new PutData(CREATE_INVOICE_URL,"POST",field,data);

                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
//                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();

                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Error on Internet Connection",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void ADD(){

        add.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if(product_quantity<20){
                    int price = Integer.parseInt(product_price);
                    price_holder = price_holder + price;
                    pPrice.setText("₱"+String.valueOf(price_holder)+".00");
                    product_quantity++;
                    pQuantity.setText(String.valueOf(product_quantity));
                }
                else{
                    Toast.makeText(getApplicationContext(),"You can order maximum of 20 Gallons at a time",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void MINUS(){
        minus.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                if(product_quantity>1){
                    int price = Integer.parseInt(product_price);
                    price_holder = price_holder - price;
                    pPrice.setText("₱"+String.valueOf(price_holder)+".00");
                    product_quantity--;
                    pQuantity.setText(String.valueOf(product_quantity));
                }
            }
        });
    }

    public void Checkout(){
        buy_again.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {


                if(DEFAULT_ADD.equals("")){
                    Toast.makeText(getApplicationContext(),"Set your default address first",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(buy_now.this,menu_address.class);
                    startActivity(intent);
                    finish();
                }
                else{

                    TextView c_subtotal = checkout_dialog.findViewById(R.id.subtotal);
                    TextView c_total = checkout_dialog.findViewById(R.id.total);
                    TextView c_gallons = checkout_dialog.findViewById(R.id.total_gallon);
                    TextView c_num = checkout_dialog.findViewById(R.id.user_phone_number);
                    TextView c_add = checkout_dialog.findViewById(R.id.user_default_address);

                    String get2 = sharedPreferences.getString("string_address","");
                    String get3 = sharedPreferences.getString("receipt_number","");

                    c_add.setText(get2);
                    c_num.setText(get3);
                    c_total.setText(String.valueOf(price_holder)+".00");
                    c_subtotal.setText(String.valueOf(price_holder)+".00");
                    c_gallons.setText(String.valueOf(product_quantity));

                    checkout_dialog.show();

                }


            }
        });
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
}