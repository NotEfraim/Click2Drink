package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.w3c.dom.Text;

public class view_partner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_partner);

        Intent intent = getIntent();

        ImageView partner_img = findViewById(R.id.partner_img);
        TextView partner_address = findViewById(R.id.partner_address);
        TextView partner_number = findViewById(R.id.partner_number);
        TextView partner_fbpage = findViewById(R.id.partner_fbpage);
        TextView partner_status = findViewById(R.id.status);
        ImageView back = findViewById(R.id.back);
        Button order_now = findViewById(R.id.order_now);

        String shop_id = intent.getStringExtra("shop_id");
        String image_url = intent.getStringExtra("image_url");
        String address = intent.getStringExtra("address");
        String number = intent.getStringExtra("contact_number");
        String fb_page = intent.getStringExtra("fb_page");
        String status = intent.getStringExtra("status");

        Glide.with(this).load(image_url).into(partner_img);
        partner_address.setText(address);
        partner_number.setText(number);
        partner_fbpage.setText(fb_page);

//        if (status.equals("inactive")){
//            partner_status.setVisibility(View.VISIBLE);
//            order_now.setVisibility(View.GONE);
//        }
//        else {
//            partner_status.setVisibility(View.INVISIBLE);
//        }


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();

            }
        });

        order_now.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(view_partner.this,"Shop is Active",Toast.LENGTH_SHORT).show();
                    Intent activity = new Intent(view_partner.this,specific_prod_viewer.class);
                    activity.putExtra("shop_id", shop_id);
                    startActivity(activity);
                    finish();
            }
        });



    }
}