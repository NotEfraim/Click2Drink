package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;

public class view_promo extends AppCompatActivity {
    String promo_id,description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_promo);

        //Image of promo
        Intent intent = getIntent();
        ImageView promo_image = findViewById(R.id.promo_image);
        TextView promo_description = findViewById(R.id.promo_description);
        Button avail_promo = findViewById(R.id.avail_promo);
        String image_url = intent.getStringExtra("image_url");
        promo_id = intent.getStringExtra("promo_id");
        description = intent.getStringExtra("description");
        LottieAnimationView promo_progress = findViewById(R.id.promo_progress);

        promo_progress.setVisibility(View.VISIBLE);
        Glide.with(this).load(image_url).into(promo_image);
        promo_description.setText(description);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                promo_progress.setVisibility(View.GONE);
                promo_image.setVisibility(View.VISIBLE);
            }
        },1000);

        if(promo_id.equals("404")){
            avail_promo.setVisibility(View.GONE);
        }


        //

        // Back
        ImageView back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        //


    }
}