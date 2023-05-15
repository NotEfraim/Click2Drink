package com.capstone.click2drink;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.VideoView;
import com.capstone.click2drink.utility.NetwokChangeListener;
import com.google.android.material.tabs.TabLayout;


public class login extends AppCompatActivity implements TabLayout.OnTabSelectedListener{

    ViewPager viewPager;
    TabLayout tabLayout;
    Animation anim;


    //Video Bg
    Uri uri;
    VideoView videoView;
    ConstraintLayout main_layout;

   // Java from package utility
    NetwokChangeListener netwokChangeListener = new NetwokChangeListener();


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.in,R.anim.fade_out);
        setContentView(R.layout.activity_login);

        // Video login background
        videoView = findViewById(R.id.videoView);
        videoBg();

        main_layout = findViewById(R.id.main_layout);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.pager);

        tabLayout.addTab(tabLayout.newTab().setText("Login"));
        tabLayout.addTab(tabLayout.newTab().setText("Register"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(),
                this,tabLayout.getTabCount());

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(this);





    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    //Internet Checker on Start and onStop

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netwokChangeListener,filter);

        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(netwokChangeListener);
        super.onStop();

    }

    @Override
    protected void onPause() {
        videoView.pause();
        super.onPause();
    }

    protected void onRestart(){
        videoView.start();
        super.onRestart();
    }

    public void videoBg(){


            //for the loop
            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    videoView.start();

                }
            });

            uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video_bg);
            videoView.setVideoURI(uri);
            anim = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.video_anim);
            videoView.setAnimation(anim);
            videoView.setVisibility(View.VISIBLE);
            videoView.requestFocus();
            videoView.start();

            //end



    }


}