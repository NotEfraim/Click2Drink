package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class menu_address extends AppCompatActivity {

    public static final String ADDRESS_URL = "http://resources.click2drinkph.store/php/Address.php";
    private Address_Adapter mAdapter;
    private RecyclerView.LayoutManager manager;
    private List<Address_Fetcher> address_fetchers;
    RecyclerView recycler_address;

    // HTTP RESULT
    String address_title;
    String cuser_number;
    String cuser_house_number;
    String cuser_brgy;
    String cuser_municipality;
    String cuser_province;
    String cuser_zipcode;
    String address_id;

    //Shared Pref
    SharedPreferences sharedPreferences;
    private String cuser_email_txt;
    //
    LottieAnimationView address_progress;
    ImageView back;
    Button add_address;
    boolean Restarted = false;
    TextView empty_alert;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_address);

        sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        cuser_email_txt = sharedPreferences.getString("temp_email", "");
        recycler_address = findViewById(R.id.recycler_address);
        manager = new LinearLayoutManager(menu_address.this, LinearLayoutManager.VERTICAL, false);
        recycler_address.setLayoutManager(manager);
        address_fetchers = new ArrayList<>();
        //
        address_progress = findViewById(R.id.address_progress);
        add_address = findViewById(R.id.add_address);
        back = findViewById(R.id.back);
        empty_alert = findViewById(R.id.empty_alert);


        BackButton();
        AddAddress();
        getUserAddress();

    }

    @Override
    protected void onRestart() {
        finish();
        Intent intent = new Intent(menu_address.this,menu_address.class);
        startActivity(intent);
        super.onRestart();
    }

    public void getUserAddress() {

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                address_progress.setVisibility(View.VISIBLE);


                if(cuser_email_txt!= null && !cuser_email_txt.isEmpty()){

                    String[] field = new String[1];
                    field[0] = "cuser_email";

                    String[] data = new String[1];
                    data[0] = cuser_email_txt;

                    PutData putData = new PutData(ADDRESS_URL,"POST",field,data);

                    if(putData.startPut()){

                        if(putData.onComplete()) {
                            String result = putData.getResult();

                            try {

                                //Getting Response form api (Base Url)
                                JSONArray array = new JSONArray(result);


                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);

                                    address_id = object.getString("address_id");
                                    address_title = object.getString("address_title");
                                    cuser_number = object.getString("cuser_number");
                                    cuser_house_number = object.getString("cuser_house_number");
                                    cuser_brgy = object.getString("cuser_brgy");
                                    cuser_municipality = object.getString("cuser_municipality");
                                    cuser_province = object.getString("cuser_province");
                                    cuser_zipcode = object.getString("zipcode");

                                    Address_Fetcher addressFetcher = new Address_Fetcher(address_id,cuser_number,address_title,
                                            cuser_house_number,cuser_brgy,cuser_municipality,cuser_province,cuser_zipcode);

                                    address_fetchers.add(addressFetcher);

                                }

                            } catch (Exception e) {
                                    empty_alert.setVisibility(View.VISIBLE);
//                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            }

                            mAdapter = new Address_Adapter(menu_address.this,address_fetchers);
                            recycler_address.setAdapter(mAdapter);


                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Unstable internet Connection",Toast.LENGTH_SHORT).show();
                    }

                }

               address_progress.setVisibility(View.GONE);

            }
        });
    }

    public void BackButton(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_address.this,MainUi.class);
                startActivity(intent);
                finish();
            }
        });
    }

    public void AddAddress(){
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(menu_address.this,Add_Address.class);
                startActivity(intent);
            }
        });
    }
}