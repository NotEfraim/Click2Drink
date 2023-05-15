package com.capstone.click2drink.staff;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.ParcelUuid;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.capstone.click2drink.R;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class staff_main_ui extends AppCompatActivity {
    private static String getShop_url = "http://resources.click2drinkph.store/php/staff_getShop.php";
    // Main View
    private ImageView logout;
    private TextView logout_text;
    private SharedPreferences sharedPreferences;
    LottieAnimationView progress_bar;
    ConstraintLayout pending_order;
    ConstraintLayout delivered_orders;
    ConstraintLayout account;
    ConstraintLayout scheduled_order;

    //Data
    String shop_id;
    String staff_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.in, R.anim.fade_out);
        setContentView(R.layout.activity_staff_main_ui);
        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        staff_email = sharedPreferences.getString("staff_email", "");

        //VIEWS
        logout = findViewById(R.id.logout);
        logout_text = findViewById(R.id.logout_text);
        progress_bar = findViewById(R.id.logout_progress);
        pending_order = findViewById(R.id.pending_orders);
        delivered_orders = findViewById(R.id.delivered_orders);
        account = findViewById(R.id.account);
        scheduled_order = findViewById(R.id.scheduled_order);


        //Functions
        getShopid();
        LogOUt();
        ActivityIntent();


    }
    public void ActivityIntent(){
        pending_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getApplicationContext(),staff_pending_orders.class);
                a.putExtra("shop_id",shop_id);
                startActivity(a);
            }
        });

        delivered_orders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getApplicationContext(),staff_delivered_orders.class);
                startActivity(a);
            }
        });

        account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent a = new Intent(getApplicationContext(),staff_profile.class);
                startActivity(a);
            }
        });

        scheduled_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Comming Soon...", Toast.LENGTH_SHORT).show();
            }
        });


    }
    public void LogOUt(){
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress_bar.setVisibility(View.VISIBLE);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        sharedPreferences.edit().remove("staff_email").apply();
                        Intent intent = new Intent(staff_main_ui.this,com.capstone.click2drink.login.class);
                        progress_bar.setVisibility(View.GONE);
                        startActivity(intent);
                        finish();
                    }
                },2000);


            }
        });

    }
    public void getShopid(){
        // Shop id ng staff user para malaman kung anong shop siya
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] ="staff_email";

                String[] data = new String[1];
                data[0] = staff_email;

                PutData putData = new PutData(getShop_url, "POST", field, data);

                if(putData.startPut()){

                    if(putData.onComplete()){
                        String result = putData.getResult();

                        try {

                            JSONArray array = new JSONArray(result);

                            for( int i=0; i<array.length(); i++){

                                JSONObject object = array.getJSONObject(i);
                                shop_id = object.getString("shop_id");
                            }

                        }
                        catch (Exception e){
//                            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }

                        sharedPreferences.edit().putString("staff_shop_id", shop_id).apply();
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Problem on Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

}