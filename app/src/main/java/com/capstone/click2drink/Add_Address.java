package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Add_Address extends AppCompatActivity {

    //
    private String ADD_URL ="http://resources.click2drinkph.store/php/AddAddress.php";
    private EditText address_title, phone_number , house_number, brgy, municipality, province, zipcode;
    ImageView back;
    Button add_address;
    // Shared pref
    SharedPreferences sharedPreferences;
    private String cuser_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__address);

        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        cuser_email = sharedPreferences.getString("temp_email","");

        address_title = findViewById(R.id.address_title);
        phone_number = findViewById(R.id.phone_number);
        house_number = findViewById(R.id.house_number);
        brgy = findViewById(R.id.brgy);
        municipality = findViewById(R.id.municipality);
        province = findViewById(R.id.province);
        zipcode = findViewById(R.id.zipcode);

        add_address = findViewById(R.id.add_address);
        back = findViewById(R.id.back);
        OnBackPressed();
        NumberValidator();
        AddButton();


    }

    public void OnBackPressed(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    public void NumberValidator(){
        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() ==1 && !s.toString().trim().equals("9")) {

                    phone_number.setText("");


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void AddButton(){
        add_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAddress();
            }
        });
    }

    //BACKEND
    public void AddAddress(){

        String temp_number = phone_number.getText().toString().trim();
        String temp_house_number = house_number.getText().toString().trim();
        String temp_brgy = brgy.getText().toString().trim();
        String temp_municipality = municipality.getText().toString().trim();
        String temp_province = province.getText().toString().trim();
        String temp_title = address_title.getText().toString().trim();
        String temp_zip = zipcode.getText().toString().trim();

        if(!temp_number.isEmpty() && !temp_house_number.isEmpty() && !temp_brgy.isEmpty()
        && !temp_municipality.isEmpty() && !temp_province.isEmpty() && !temp_title.isEmpty()
        && !temp_zip.isEmpty()){

            Handler handler= new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    String[] field = new String[8];
                    field[0] = "cuser_email";
                    field[1] = "cuser_number";
                    field[2] = "cuser_house_number";
                    field[3] = "cuser_brgy";
                    field[4] = "cuser_municipality";
                    field[5] = "cuser_province";
                    field[6] = "address_title";
                    field[7] = "zipcode";

                    String[] data = new String[8];
                    data[0] = cuser_email;
                    data[1] = temp_number;
                    data[2] = temp_house_number;
                    data[3] = temp_brgy;
                    data[4] = temp_municipality;
                    data[5] = temp_province;
                    data[6] = temp_title;
                    data[7] = temp_zip;

                    PutData putData = new PutData(ADD_URL,"POST",field,data);

                    if(putData.startPut()){

                        if(putData.onComplete()){

                            String result = putData.getResult();
                            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Add_Address.this,menu_address.class);
                            startActivity(intent);
                            finish();
                        }

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Error on Internet Connection",Toast.LENGTH_SHORT).show();
                    }




                }
            });

        }
        else {
            Toast.makeText(getApplicationContext(),"All Fields is Required",Toast.LENGTH_SHORT).show();
        }




    }
}