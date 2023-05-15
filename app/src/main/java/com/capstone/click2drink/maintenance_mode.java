package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class maintenance_mode extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.in,R.anim.fade_out);
        setContentView(R.layout.activity_maintenance_mode);
    }
}