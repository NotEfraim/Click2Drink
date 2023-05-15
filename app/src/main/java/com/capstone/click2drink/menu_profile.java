package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class menu_profile extends AppCompatActivity {

    //Layout
    ImageView back;
    EditText name,birth_date,phone_number,current_address;
    Button update;

    //BACKEND
    String PROFILE_URL = "http://resources.click2drinkph.store/php/Profile.php";
    private List<Profile_Fetcher> profiles;
    LottieAnimationView progress;
    Profile_Fetcher data;
    String cuser_name;
    String cuser_email;
    String cuser_phone_number;
    String cuser_bday;
    String cuser_current_address;

    // Shared Pref
    SharedPreferences sharedPreferences;
    String cuser_email_txt;

    //Holder Validator
    String a,b,c,d;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_profile);

        back = findViewById(R.id.back);
        back();

        // Layout
        name = findViewById(R.id.user_name);
        birth_date = findViewById(R.id.user_bday);
        phone_number = findViewById(R.id.user_phone_number);
        current_address = findViewById(R.id.user_current_address);
        progress = findViewById(R.id.menu_progress);
        update = findViewById(R.id.update_btn);
        //Shared pref
        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        cuser_email_txt = sharedPreferences.getString("temp_email","");
        //inserting new value of temp_number for otp



        NumberValidator();
        //For Birthday
        DatePicker();
        //For Profile info
        getProfileInfo();
        updateProfile();


    }
    //BACKEND FUNCTIONS!

    public void getProfileInfo() {
        progress.setVisibility(View.VISIBLE);


        Handler handler = new Handler();
        handler.post(new Runnable() {


            @Override
            public void run() {


                if(cuser_email_txt!=null && !cuser_email_txt.isEmpty()){

                    String[] field = new String[1];
                    field[0] = "cuser_email";

                    String[] data = new String[1];
                    data[0] = cuser_email_txt;

                    PutData putData = new PutData(PROFILE_URL,"POST",field,data);

                    if(putData.startPut()){

                        if(putData.onComplete()) {
                            String result = putData.getResult();

                            try {

                                //Getting Response form api (Base Url)
                                JSONArray array = new JSONArray(result);


                                for (int i = 0; i < array.length(); i++) {

                                    JSONObject object = array.getJSONObject(i);



                                    cuser_name = object.getString("cuser_name");
                                    cuser_email = object.getString("cuser_email");
                                    cuser_phone_number = object.getString("cuser_phone_number");
                                    cuser_bday = object.getString("cuser_bday");
                                    cuser_current_address = object.getString("cuser_current_address");

                                    a = cuser_name;
                                    b = cuser_phone_number;
                                    c = cuser_bday;
                                    d = cuser_current_address;

                                }

                            } catch (Exception e) {
                                Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_LONG).show();

                            }



                            if(cuser_bday.equals("null") && cuser_current_address.equals("null")){

                                progress.setVisibility(View.GONE);
                                name.setText(cuser_name);
                                birth_date.setText("");
                                phone_number.setText(cuser_phone_number);
                                current_address.setText("");


                            }
                            else{

                                progress.setVisibility(View.GONE);
                                name.setText(cuser_name);
                                birth_date.setText(cuser_bday);
                                phone_number.setText(cuser_phone_number);
                                current_address.setText(cuser_current_address);

                            }



                        }



                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Unstable internet Connection",Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });

    }


    //Functions

    public void updateProfile(){


        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                cuser_name = name.getText().toString();
                cuser_bday = birth_date.getText().toString();
                cuser_phone_number = phone_number.getText().toString();
                cuser_current_address = current_address.getText().toString();

                if(a.equals(cuser_name) && b.equals(cuser_phone_number) && c.equals(cuser_bday)
                        && d.equals(cuser_current_address))
                {
                    Toast.makeText(getApplicationContext(),"No Changes to be Updated",Toast.LENGTH_SHORT).show();
                } else {

                    if(!cuser_name.isEmpty() && !cuser_bday.isEmpty()
                            && !cuser_phone_number.isEmpty() && !cuser_current_address.isEmpty()){

                        //Activity Credential
                        sharedPreferences.edit().putString("activity_credential","update").apply();
                        //For otp
                        sharedPreferences.edit().putString("temp_number",cuser_phone_number).apply();

                        Intent intent = new Intent(getApplicationContext(),OTP_input_code.class);
                        intent.putExtra("update_email",cuser_email);
                        intent.putExtra("update_name",cuser_name);
                        intent.putExtra("update_bday",cuser_bday);
                        intent.putExtra("update_phone",cuser_phone_number);
                        intent.putExtra("update_address",cuser_current_address);
                        startActivity(intent);
                        finish();

                    }
                    else {
                        Toast.makeText(getApplicationContext(),"All Fields is Required",Toast.LENGTH_SHORT).show();
                    }

                }


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


    public void DatePicker(){

        birth_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                final int year = calendar.get(Calendar.YEAR);


                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        menu_profile.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;

                        birth_date.setText(date);

                    }
                },year,month,day);

                datePickerDialog.show();

            }
        });

    }


    public void back(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MainUi.class);
                startActivity(intent);
                finish();
            }
        });
    }


}