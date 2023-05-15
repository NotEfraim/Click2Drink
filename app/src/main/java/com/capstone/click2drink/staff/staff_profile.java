package com.capstone.click2drink.staff;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.capstone.click2drink.R;
import com.capstone.click2drink.menu_profile;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Calendar;

public class staff_profile extends AppCompatActivity {


    EditText fullName;
    EditText phone_number;
    EditText birth_date;
    EditText current_address;
    Button update;
    ImageView back;
    LottieAnimationView menu_progress;

    //

    private static String STAFF_PROFILE = "http://resources.click2drinkph.store/php/staff_profile.php";
    private static String UPDATE_PROFILE = "http://resources.click2drinkph.store/php/staff_update_profile.php";

    //

    SharedPreferences sharedPreferences;
    String staff_email;
    String fName;
    String birth_date_txt;
    String phone_number_txt;
    String current_address_txt;

    //

    String a,b,c;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile);

        fullName = findViewById(R.id.user_name);
        birth_date = findViewById(R.id.user_bday);
        phone_number = findViewById(R.id.user_phone_number);
        current_address = findViewById(R.id.user_current_address);
        update = findViewById(R.id.update_btn);
        back = findViewById(R.id.back);
        menu_progress = findViewById(R.id.menu_progress);

        sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        staff_email = sharedPreferences.getString("staff_email", "");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        GetProfile();
        UpdateProfile();
        DatePicker();

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
                        staff_profile.this, new DatePickerDialog.OnDateSetListener() {
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
    public void GetProfile(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "email";

                String[] data = new String[1];
                data[0] = staff_email;

                PutData putData = new PutData(STAFF_PROFILE, "POST", field, data);

                if(putData.startPut()){
                    if(putData.onComplete()){

                        String result = putData.getResult();

                        try {

                            JSONArray array = new JSONArray(result);

                            for(int i = 0; i < array.length(); i ++){

                                JSONObject object = array.getJSONObject(i);

                                String f_name = object.getString("fname");
                                String last_name = object.getString("lastname");
                                birth_date_txt = object.getString("birth_date");
                                phone_number_txt = object.getString("phone_number");
                                current_address_txt = object.getString("current_address");

                                fName = f_name+" "+last_name;

                                a = birth_date_txt;
                                b = phone_number_txt;
                                c = current_address_txt;

                            }


                            Loading();

                        }
                        catch (Exception e){

//                            Toast.makeText(getApplicationContext(), e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }

                        fullName.setText(fName);
                        birth_date.setText(birth_date_txt);
                        phone_number.setText(phone_number_txt);
                        current_address.setText(current_address_txt);

                    }
                }

            }
        });
    }
    public void UpdateProfile(){

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                birth_date_txt = birth_date.getText().toString();
                phone_number_txt = phone_number.getText().toString();
                current_address_txt = current_address.getText().toString();

                if(a.equals(birth_date_txt) && b.equals(phone_number_txt) && c.equals(current_address_txt)){
                    Toast.makeText(getApplicationContext(),"No Changes to be Updated",Toast.LENGTH_SHORT).show();
                }
                else{
                    if(!birth_date_txt.isEmpty() && !phone_number_txt.isEmpty() && !current_address_txt.isEmpty()){

                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                String[] field = new String[4];
                                field[0] = "email";
                                field[1] = "phone_number";
                                field[2] = "birth_date";
                                field[3] = "current_address";

                                String[] data = new String[4];
                                data[0] = staff_email;
                                data[1] = phone_number_txt;
                                data[2] = birth_date_txt;
                                data[3] = current_address_txt;

                                PutData putData = new PutData(UPDATE_PROFILE,"POST", field, data);

                                if(putData.startPut()){
                                    if(putData.onComplete()){
                                        String result = putData.getResult();
                                        Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        });

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"All Fields is Required",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });


    }
    public void Loading(){
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menu_progress.setVisibility(View.GONE);
            }
        },1000);

    }
}