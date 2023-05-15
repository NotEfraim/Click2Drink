package com.capstone.click2drink;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.android.material.navigation.NavigationView;
import com.onesignal.OneSignal;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;

public class MainActivity extends AppCompatActivity {
    ImageView bg;
    ImageView logo;
    TextView slogan;
    boolean maintenance;



    private static final String ONESIGNAL_APP_ID = "08fd962c-4285-45ef-ac2c-34a0aa4145e9";
    Animation anim;

    private static final int PAGE_NUMBER = 3;
    private ViewPager viewPager;
    private ScreenSlidePageAdapter pagerAdapter;
    SharedPreferences sharedPreferences;
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        // Enable verbose OneSignal logging to debug issues if needed.
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.initWithContext(this);
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        //

        bg = (ImageView) findViewById(R.id.splash_bg);
        logo = (ImageView) findViewById(R.id.logo);
        slogan = (TextView) findViewById(R.id.slogan);

        bg.animate().translationY(-3200).setDuration(1000).setStartDelay(3000);
        logo.animate().translationY(2000).setDuration(1000).setStartDelay(3000);
        slogan.animate().translationY(2000).setDuration(1000).setStartDelay(3000);

        viewPager = findViewById(R.id.pager);
        pagerAdapter = new ScreenSlidePageAdapter(getSupportFragmentManager());
        anim = AnimationUtils.loadAnimation(this,R.anim.in);

        //Checking if maintenance mode is on
        MaintenanceMode();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                //Para sa introductory
                sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
                boolean firstTime = sharedPreferences.getBoolean("firstTime",true);
                String alreadyLogout = sharedPreferences.getString("temp_email","");
                String staffLogout = sharedPreferences.getString("staff_email","");

                //Checking if maintenance mode is off
                if(!maintenance){

                    if(firstTime){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean("firstTime",false);
                        editor.commit();

                        //First Time User for intro
                        viewPager.setAnimation(anim);
                        viewPager.setAdapter(pagerAdapter);
                    }
                    // Redirection to other activity
                    else {

                        //if both user is already logout
                        if( (alreadyLogout == null || alreadyLogout.equals(""))
                        && (staffLogout == null || staffLogout.equals(""))){

                            Intent activity = new Intent(MainActivity.this,login.class);
                            startActivity(activity);
                            finish();
                        }

                        else if(!staffLogout.isEmpty()){
                                //if the staff user is already Login
                            Intent activity = new Intent(MainActivity.this,
                                    com.capstone.click2drink.staff.staff_main_ui.class);
                            startActivity(activity);

                        }
                        else{
                            // If the customer user is already Login
                            Intent activity = new Intent(MainActivity.this,MainUi.class);
                            startActivity(activity);
                            finish();

                        }

                    }

                } // end of if

                else{
                    // If maintenance is on
                    Intent intent = new Intent(getApplicationContext(),maintenance_mode.class);
                    startActivity(intent);
                    finish();
                }



            }
        },4300); // This delay aims to load all the content and url to validate




    }

    static class ScreenSlidePageAdapter extends FragmentStatePagerAdapter{

        public ScreenSlidePageAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            switch (position){

                case 0:
                    fragment1 tab1 = new fragment1();
                    return tab1;

                case 1:
                    fragment2 tab2 = new fragment2();
                    return tab2;

                case 2:
                    fragment3 tab3 = new fragment3();
                    return tab3;
            }

            return null;
        }

        @Override
        public int getCount() {
            return PAGE_NUMBER;
        }
    }

    public void MaintenanceMode(){

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String url = "http://resources.click2drinkph.store/php/maintenance_mode.php";
                        FetchData fetchData = new FetchData(url);


                        if(fetchData.startFetch()){

                            if(fetchData.onComplete()){

                                String result = fetchData.getResult();

                                if (result.equals("on")){

                                  maintenance = true;

                                }
                                else{
                                    maintenance = false;
                                }
                            }

                        }

                    }
                });
            }


}