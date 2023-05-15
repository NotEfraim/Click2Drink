package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Main_Category extends AppCompatActivity implements TabLayout.OnTabSelectedListener {

    ViewPager viewpager;
    TabLayout tabLayout;
    private ImageView truck;
    private CardView item_layout;
    private TextView item_count;
    public static final String COUNTER_URL = "http://resources.click2drinkph.store/php/item_counter.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__category);

        // To Truck
        truck = findViewById(R.id.to_truck);
        item_layout = findViewById(R.id.item_layout);
        item_count = findViewById(R.id.item_count);

        // TAB SELECTED
        Intent intent = getIntent();
        String manual_tab = intent.getStringExtra("tab_selected");

        // Fragments
        tabLayout = findViewById(R.id.tabLayout);
        viewpager = findViewById(R.id.pager);

        //tabLayout Initialization

        tabLayout.addTab(tabLayout.newTab().setText("Dispenser Gallon"));
        tabLayout.addTab(tabLayout.newTab().setText("Square Gallon"));
        tabLayout.addTab(tabLayout.newTab().setText("Bottled Water"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final CategoryAdapter adapter = new CategoryAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewpager.setAdapter(adapter);

        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);

        //
        ImageView back = findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // to view what is clicked
        // Kung ano pinindot sa main
        if(!manual_tab.equals("")){
            int index = Integer.parseInt(manual_tab);
            viewpager.setCurrentItem(index);
        }
        //

        // Calling the functions
        TruckNotification();
        Truck();
        setItem_count();
    }


    // ON tab attributes generated for fragments

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        viewpager.setCurrentItem(tab.getPosition());
//        String x = String.valueOf(tab.getPosition());
//        Toast.makeText(getApplicationContext(),x,Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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