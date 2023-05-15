package com.capstone.click2drink;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.capstone.click2drink.utility.NetwokChangeListener;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainUi extends AppCompatActivity {

    //Shared Preferences
    SharedPreferences sharedPreferences;
    private String DEFAULT_ADD, MUNICIPALITY;

    //Java from package utility
    NetwokChangeListener netwokChangeListener = new NetwokChangeListener();
    //Base Url from Hosting
    public static final String PARTNERS_URL = "https://resources.click2drinkph.store/php/getPartners.php";
    public static final String PROMOS_URL = "https://resources.click2drinkph.store/php/getPromoDeals.php";
    public static final String ALL_PRODUCTS_URL = "http://resources.click2drinkph.store/php/viewAllProducts.php";
    public static final String COUNTER_URL = "http://resources.click2drinkph.store/php/item_counter.php";
    public static final String ADDRESS_URL = "http://resources.click2drinkph.store/php/getDefaultAddress.php";

    //For Partners
    private List<Partners> partners;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView holder;
    private RecyclerView.LayoutManager manager;
    private LottieAnimationView partner_progress;

    //Firebase



    // For Promos
    List<SlideModel> slideModels = new ArrayList<>();
    ArrayList<String> intent_url = new ArrayList<String>();
    ArrayList<String> intent_description = new ArrayList<String>();
    ArrayList<String> intent_promo_id = new ArrayList<String>();

    private List<PromosFetcher> promosFetcherList;
    ImageSlider imageSlider;
    private LottieAnimationView promo_progress;
    String promo_id, description;

    // Category Fragment
    Button dispenser , square, bottled;

    // Drawer Navigation
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    LottieAnimationView logout_progress;
    TextView user_email,user_name;
    String header_email;
    View headerView;

    // To Truck
    ImageView truck;
    CardView item_layout;
    TextView item_count;
    String item_counter;

    // For all products
    RecyclerView all_products;
    private List<Product_Dispenser> product_dispensers;
    private RecyclerView.Adapter pAdapter;
    private RecyclerView.LayoutManager pmanager;

    //Scheduled order
    Button set_schedule;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //in animation
        overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_fade_out);
        setContentView(R.layout.activity_main_ui);

        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        DEFAULT_ADD = sharedPreferences.getString("default_address","");
        //Scheduled Order
        set_schedule = findViewById(R.id.set_schedule);
        set_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Coming Soon....",Toast.LENGTH_SHORT).show();
            }
        });

        //To Truck
        truck = findViewById(R.id.to_truck);
        item_layout = findViewById(R.id.item_layout);
        item_count = findViewById(R.id.item_count);


        //Drawer Navigation
        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar_main);

        //Layout
        logout_progress = findViewById(R.id.logout_progress);

        // Getting email and setting it to header of navigationView/Drawer
        header_email = sharedPreferences.getString("temp_email","");
        headerView = navigationView.getHeaderView(0);
        user_email = headerView.findViewById(R.id.user_email);
        user_name = headerView.findViewById(R.id.user_name);
        user_email.setText(header_email);

        //Process
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id=item.getItemId();
                switch (id)
                {
                    case R.id.profile:
                        Profile();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.address:
                        Address();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.purchases:
                        My_Purchases();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.scheduled_order:
                        Toast.makeText(getApplicationContext(),"Coming Soon....",Toast.LENGTH_SHORT).show();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.logout:
                        logout_progress.setVisibility(View.VISIBLE);
                        FirebaseAuth.getInstance().signOut();
                        Logout();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.track_order:
                        TrackOrder();
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;

                }

                return true;
        }
        });


        //load to main UI
                getName();
                Categories();
                getPartners();
                getPromoDeals();
                getMunicipality();
                TruckNotification();
                Truck();
                setItem_count();
                getAllProductsWithMunicipality();

    }

    // FUNCTIONS   --------------------------------------------  //

    public void getMunicipality(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "address_id";

                String[] data = new String[1];
                data[0] = DEFAULT_ADD;

                PutData putData = new PutData(ADDRESS_URL,"POST",field,data);

                if(putData.startPut()){

                    if(putData.onComplete()) {
                        String result = putData.getResult();

                        try {

                            //Getting Response form api (Base Url)
                            JSONArray array = new JSONArray(result);


                                JSONObject object = array.getJSONObject(0);
                                String cuser_municipality = object.getString("cuser_municipality");
                                sharedPreferences.edit().putString("municipality", cuser_municipality).apply();


                        } catch (Exception e) {
//                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage()+"Municipality", Toast.LENGTH_LONG).show();

                        }


                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Unstable internet Connection",Toast.LENGTH_SHORT).show();
                }



            }
        });
    }

    public void getName(){

        String PROFILE_URL = "http://resources.click2drinkph.store/php/Profile.php";

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "cuser_email";

                String[] data = new String[1];
                data[0] = header_email;

                PutData putData = new PutData(PROFILE_URL,"POST",field,data);

                if(putData.startPut()){

                    if(putData.onComplete()){

                        String result = putData.getResult();

                        try {

                            //Getting Response form api (Base Url)
                            JSONArray array = new JSONArray(result);


                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                String cuser_id = object.getString("cuser_id");
                                String cuser_name = object.getString("cuser_name");
                                user_name.setText(cuser_name);
                                //Inserting current cuser_id in shared_pref
                                sharedPreferences.edit().putString("login_user_id",cuser_id).apply();
                                user_name.setVisibility(View.VISIBLE);

                            }

                        } catch (Exception e) {

                            //remove this in production
//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }


                    }

                }
                else{
                    Toast.makeText(getApplicationContext(),"Internet Connectivity Error", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void TruckNotification(){

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

        String temp_count = sharedPreferences.getString("truck_item_count","");
        assert temp_count != null;
        if(temp_count.equals("0") || temp_count.equals("")){
            item_layout.setVisibility(View.GONE);
        }
        else{
            item_count.setText(temp_count);
            item_layout.setVisibility(View.VISIBLE);
        }

    }

    public void My_Purchases(){
        Intent intent = new Intent(getApplicationContext(),menu_my_purchases.class);
        startActivity(intent);
    }

    public void Profile(){
        Intent intent = new Intent(getApplicationContext(),menu_profile.class);
        startActivity(intent);

    }

    public void Address(){
        Intent intent = new Intent(getApplicationContext(),menu_address.class);
        startActivity(intent);
    }

    public void Logout(){

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logout_progress.setVisibility(View.GONE);
                //clearing past data
                sharedPreferences.edit().remove("temp_email").apply();
                sharedPreferences.edit().remove("temp_number").apply();
                sharedPreferences.edit().remove("temp_fname").apply();
                sharedPreferences.edit().remove("receipt_number").apply();
                sharedPreferences.edit().remove("string_address").apply();
                sharedPreferences.edit().remove("default_address").apply();
                sharedPreferences.edit().remove("truck_item_count").apply();
                sharedPreferences.edit().remove("municipality").apply();
                Intent intent = new Intent(getApplicationContext(),login.class);
                startActivity(intent);
                finish();
                //
            }
        },2000);

    }

    public void Truck(){
        truck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainUi.this,Truck.class);
                startActivity(intent);
            }
        });
    }

    public void TrackOrder(){
        Intent a = new Intent(MainUi.this,Track_View.class);
        startActivity(a);

    }

    public void Categories(){
        dispenser = findViewById(R.id.dispenser_button);
        square = findViewById(R.id.square_button);
        bottled = findViewById(R.id.bottled_button);

        dispenser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent activity = new Intent(MainUi.this,Main_Category.class);
                activity.putExtra("tab_selected","0");
                startActivity(activity);
            }
        });

        square.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(getApplicationContext(),Main_Category.class);
               intent.putExtra("tab_selected","1");
               startActivity(intent);
            }
        });

        bottled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Main_Category.class);
                intent.putExtra("tab_selected","2");
                startActivity(intent);
            }
        });

    }

    // On back Press
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            finish();

        }
    }

    //new function para sa response ng API
    public void getPartners() {

        //Partners with Recycler View
        holder = findViewById(R.id.recycler);
        manager = new LinearLayoutManager(MainUi.this, LinearLayoutManager.HORIZONTAL, false);
        holder.setLayoutManager(manager);
        partners = new ArrayList<>();
        partner_progress = findViewById(R.id.partners_progress);
        partner_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PARTNERS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        try {


                            //Getting Response form api (Base Url)
                            JSONArray array = new JSONArray(response);


                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);

                                String shop_id = object.getString("shop_id");
                                String image_url = object.getString("image_url");
                                String fb_page = object.getString("shop_fb_page");
                                String brgy = object.getString("shop_brgy");
                                String municipality = object.getString("shop_municipality");
                                String contact_number = object.getString("shop_contact_number");
                                String status = object.getString("shop_status");

                                Partners data = new Partners(shop_id,fb_page,image_url,brgy,municipality,contact_number,status);
                                partners.add(data);

                            }
                        } catch (Exception e) {
                            partner_progress.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }

                        mAdapter = new RecyclerAdapter(MainUi.this, partners);
                        holder.setAdapter(mAdapter);
                        partner_progress.setVisibility(View.GONE);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                partner_progress.setVisibility(View.GONE);

//                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(MainUi.this).add(stringRequest);
    }


    // New function para sa promo deals
    public void getPromoDeals() {

        //Slider (List to String)
        imageSlider = findViewById(R.id.image_slider);
        promosFetcherList = new ArrayList<>();
        promo_progress = findViewById(R.id.promo_progress);
        promo_progress.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, PROMOS_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {


                            //Getting Response form api (Base Url)
                            JSONArray array = new JSONArray(response);


                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);

                                String image_url = object.getString("image_url");
                                promo_id = object.getString("shop_id");
                                description = object.getString("description");

                                PromosFetcher data = new PromosFetcher(image_url,promo_id);
                                promosFetcherList.add(data);

                                //E parse natin yung List Array para malagay sa slider
                                String url = data.getImage().toString();
                                // ilagay ulit natin yung url sa array list para sa intent mamaya
                                intent_url.add(url);
                                intent_promo_id.add(promo_id);
                                intent_description.add(description);

                                slideModels.add((new SlideModel(url, ScaleTypes.CENTER_INSIDE)));
//

                            }


                        } catch (Exception e) {
                            partner_progress.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                        }

                        //E on na natin Slider
                        imageSlider.setImageList(slideModels);
                        promo_progress.setVisibility(View.GONE);
                        imageSlider.setItemClickListener(new ItemClickListener() {
                            @Override
                            public void onItemSelected(int i) {

                            Intent activity = new Intent(MainUi.this,view_promo.class);
                            activity.putExtra("image_url",intent_url.get(i));
                            activity.putExtra("promo_id",intent_promo_id.get(i));
                            activity.putExtra("description",intent_description.get(i));

                            startActivity(activity);


                            }
                        });

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                partner_progress.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_SHORT).show();


            }
        });

        Volley.newRequestQueue(MainUi.this).add(stringRequest);

    }

    //ALL PRODUCTS
    public void getAllProductsWithMunicipality() {
        MUNICIPALITY = sharedPreferences.getString("municipality","");

        //Partners with Recycler View
        all_products = findViewById(R.id.products_recycler);
        pmanager = new GridLayoutManager(this, 2);
        all_products.setLayoutManager(pmanager);
        product_dispensers = new ArrayList<>();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "municipality";

                String[] data = new String[1];
                data[0] = MUNICIPALITY;

                PutData putData = new PutData(ALL_PRODUCTS_URL, "POST", field, data);
                if (putData.startPut()) {
                    if (putData.onComplete()) {
                        String result = putData.getResult();

                        try {

                            //Getting Response form api (Base Url)
                            JSONArray array = new JSONArray(result);
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject object = array.getJSONObject(i);
                                String shop_id = object.getString("shop_id");
                                String product_id = object.getString("prod_id");
                                String image_url = object.getString("image_url");
                                String product_name = object.getString("prod_name");
                                String product_price = object.getString("prod_price");
                                String location = object.getString("location");

                                Product_Dispenser datax = new Product_Dispenser(product_id, product_name, product_price, image_url, location, shop_id);
                                product_dispensers.add(datax);


                            }
                        } catch (Exception e) {
                            partner_progress.setVisibility(View.GONE);
//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                            getAllProductsRandom();
                        }

                        pAdapter = new All_Products_Adapter(MainUi.this, product_dispensers);
                        all_products.setAdapter(pAdapter);
                        partner_progress.setVisibility(View.GONE);

                    }


                }

            }
        });
    }

    public void getAllProductsRandom(){

            //Partners with Recycler View
            all_products = findViewById(R.id.products_recycler);
            pmanager = new GridLayoutManager(this, 2);
            all_products.setLayoutManager(pmanager);
            product_dispensers = new ArrayList<>();

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                   FetchData fetchData = new FetchData(ALL_PRODUCTS_URL);
                    if (fetchData.startFetch()) {
                        if (fetchData.onComplete()) {
                            String result = fetchData.getResult();

                            try {

                                //Getting Response form api (Base Url)
                                JSONArray array = new JSONArray(result);

                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);
                                    String shop_id = object.getString("shop_id");
                                    String product_id = object.getString("prod_id");
                                    String image_url = object.getString("image_url");
                                    String product_name = object.getString("prod_name");
                                    String product_price = object.getString("prod_price");
                                    String location = object.getString("location");

                                    Product_Dispenser datax = new Product_Dispenser(product_id, product_name, product_price, image_url, location, shop_id);
                                    product_dispensers.add(datax);


                                }
                            } catch (Exception e) {
                                partner_progress.setVisibility(View.GONE);
//                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            }

                            pAdapter = new All_Products_Adapter(MainUi.this, product_dispensers);
                            all_products.setAdapter(pAdapter);
                            partner_progress.setVisibility(View.GONE);

                        }


                    }

                }
            });

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

    //Internet Checker on Start and onStop

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netwokChangeListener, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(netwokChangeListener);
        super.onStop();

    }
}