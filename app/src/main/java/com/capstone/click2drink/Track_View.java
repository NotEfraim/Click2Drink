package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Track_View extends AppCompatActivity {

    private String VIEW_INVOICE = "http://resources.click2drinkph.store/php/view_invoices.php";
    private List<Invoice_Fetcher> invoice_fetchers;
    SharedPreferences sharedPreferences;
    RecyclerView recyclerView;
    Invoice_Adapter invoice_adapter;
    RecyclerView.LayoutManager layoutManager;
    LottieAnimationView progress;
    TextView empty_alert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track__view);

        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        recyclerView = findViewById(R.id.view_invoices);
        ImageView back = findViewById(R.id.back);
        progress = findViewById(R.id.x_progress);
        empty_alert = findViewById(R.id.empty_alert);

        getInvoices();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent main = new Intent(getApplicationContext(),MainUi.class);
                startActivity(main);
                finish();
            }
        });
    }

    public void getInvoices() {
        String ID = sharedPreferences.getString("login_user_id", "");
        layoutManager = new LinearLayoutManager(Track_View.this,
                LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        invoice_fetchers = new ArrayList<>();

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {


                    String[] field = new String[1];
                    field[0] = "cuser_id";

                    String[] data = new String[1];
                    data[0] = ID;

                    PutData putData = new PutData(VIEW_INVOICE,"POST",field,data);

                    if(putData.startPut()){

                        if(putData.onComplete()) {
                            String result = putData.getResult();

                            try {

                                //Getting Response form api (Base Url)
                                JSONArray array = new JSONArray(result);


                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);

                                    String db_id = object.getString("id");
                                    String order_number = object.getString("order_number");
                                    String total_price = object.getString("total_price");
                                    String total_gallons = object.getString("total_gallons");
                                    String order_status = object.getString("order_status");
                                    String date_ordered = object.getString("date_ordered");

                                    Invoice_Fetcher newData = new Invoice_Fetcher(db_id,order_number,total_price,total_gallons,order_status,date_ordered);
                                    invoice_fetchers.add(newData);

                                }

                            } catch (Exception e) {
//                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                                    empty_alert.setVisibility(View.VISIBLE);
                            }

                            invoice_adapter = new Invoice_Adapter(Track_View.this,invoice_fetchers);
                            recyclerView.setAdapter(invoice_adapter);
                            progress.setVisibility(View.GONE);

                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Unstable internet Connection",Toast.LENGTH_SHORT).show();
                    }


            }
        });
    }
}