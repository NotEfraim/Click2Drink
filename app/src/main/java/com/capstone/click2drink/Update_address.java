package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

import org.json.JSONArray;
import org.json.JSONObject;

public class Update_address extends AppCompatActivity {
    //
    private String DELETE_URL = "http://resources.click2drinkph.store/php/DeleteAddress.php";
    private String UPDATE_URL = "http://resources.click2drinkph.store/php/UpdateAddress.php";
    private String GETADDRESS = "http://resources.click2drinkph.store/php/getDefaultAddress.php";
    private EditText address_title, phone_number, house_number, zipcode;

    //Spinner
    private Spinner province;
    String[] province_src = {"Pangasinan"};

    private Spinner municipality;
    String temp_municipality; // For updating data
    String[] municipality_src = {"SELECT MUNICIPALITY","Lingayen", "Binmaley"};

    private Spinner brgy;
    String temp_brgy;
    ArrayAdapter arrayAdapter2;

    String[] final_brgy;

    String[] binmaley_brgy = {"SELECT BARANGAY","Amancoro","Balagan","Balogo","Basing","Baybay Lopez","Baybay Polong","Biec",
    "Buenlag","Calit","Caloocan Dupo","Caloocan Norte","Caloocan Sur","Camaley","Canaoalan","Dulag",
    "Gayaman","Linoc","Nagpalangan","Malindong","Manat","Naguilayan","Pallas","Pallas","Parayao","Poblacion",
            "Pototan","Sabangan","Salapingao","San Isidro Norte","San Isidro Sur","Santa Rosa","Tombor"};

    String[] lingayen_brgy ={"SELECT BARANGAY","Aliwekwek","Baay","Balangobong","Balangobong","Bantayan","Basing","Capandanan",
    "Capandanan","Domalandan East","Domalandan West","Dorongan","Dulag","Estanza","Lasip","Libsong East",
    "Libsong West","Malawa","Malimpuec","Maniboc","Matalava","Naguelguel","Namolan","Pangapisan North",
            "Pangapisan Sur","Poblacion","Quibaol","Rosario","Sabangan","Talogtog","Talogtog","Tumbar","Wawa"};



    ImageView back;
    Button delete_address, update_address;
    SwitchCompat default_add_bttn;
    // Shared pref
    SharedPreferences sharedPreferences;
    private String cuser_email;
    Intent intent;

    // Address ID
    String temp_id;

    // Holder
    String a,b,c,d,e,f,g;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_address);

        sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        cuser_email = sharedPreferences.getString("temp_email", "");

        address_title = findViewById(R.id.address_title);
        phone_number = findViewById(R.id.phone_number);
        house_number = findViewById(R.id.house_number);
        brgy = findViewById(R.id.brgy);
        municipality = findViewById(R.id.municipality);
        province = findViewById(R.id.province);
        zipcode = findViewById(R.id.zipcode);

        back = findViewById(R.id.back);
        default_add_bttn = findViewById(R.id.default_address);
        delete_address = findViewById(R.id.delete_address);
        update_address = findViewById(R.id.update_address);

        // Temporary Holder
        intent = getIntent();
        getIntentExtra();
        OnBackPressed();
        NumberValidator();
        DeleteButton();
        UpdateButton();
        DefaultAddress();

        //Spinner Province
        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(), R.layout.address_display_spinner,province_src);
        arrayAdapter.setDropDownViewResource(R.layout.address_spinner);
        province.setAdapter(arrayAdapter);

        //Spinner Municipality

        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getApplicationContext(), R.layout.address_display_spinner,municipality_src);
        arrayAdapter1.setDropDownViewResource(R.layout.address_spinner);
        municipality.setAdapter(arrayAdapter1);

        //Spinner Brangay
        temp_brgy = intent.getStringExtra("brgy");
        String val = intent.getStringExtra("municipality");
        // Para ma fix kung anong array resource ang need sa brgy
        if(val.equals("Binmaley")){
            arrayAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.address_display_spinner,binmaley_brgy);
            arrayAdapter2.setDropDownViewResource(R.layout.address_spinner);
            brgy.setAdapter(arrayAdapter2);

            final_brgy = binmaley_brgy;
        }
        else {

            arrayAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.address_display_spinner,lingayen_brgy);
            arrayAdapter2.setDropDownViewResource(R.layout.address_spinner);
            brgy.setAdapter(arrayAdapter2);

            final_brgy = lingayen_brgy;
        }


        //Spinner
        municipality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temp_municipality = municipality_src[position];

                if(temp_municipality.equals("Binmaley")){
                    arrayAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.address_display_spinner,binmaley_brgy);
                    arrayAdapter2.setDropDownViewResource(R.layout.address_spinner);
                    brgy.setAdapter(arrayAdapter2);

                    final_brgy = binmaley_brgy;
                }
                else {

                    arrayAdapter2 = new ArrayAdapter(getApplicationContext(), R.layout.address_display_spinner,lingayen_brgy);
                    arrayAdapter2.setDropDownViewResource(R.layout.address_spinner);
                    brgy.setAdapter(arrayAdapter2);

                    final_brgy = lingayen_brgy;

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brgy.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                temp_brgy = final_brgy[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });

        //


    }

    public void getIntentExtra() {
        String temp_mun = intent.getStringExtra("municipality");
        String temp_add = intent.getStringExtra("address_title");
        String temp_num = intent.getStringExtra("phone_number");
        String temp_hNum = intent.getStringExtra("house_number");
        String temp_prov = intent.getStringExtra("province");
        String temp_zip = intent.getStringExtra("zipcode");
        temp_id = intent.getStringExtra("address_id");

        address_title.setText(temp_add);
        phone_number.setText(temp_num);
        house_number.setText(temp_hNum);
        zipcode.setText(temp_zip);

        //Default Province is selection 0
        province.setSelection(0);

        if(temp_mun.equals("Binmaley")){
            municipality.setSelection(1);
        }
        else{
            municipality.setSelection(2);
        }

        //Display the data from data base by getting the position og that value in array by accessing the adapter
//        int SpinnerPosition = arrayAdapter2.getPosition(temp_brgy);
//        brgy.setSelection(SpinnerPosition);

        Handler xhandler = new Handler();
        xhandler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "address_id";

                String[] data = new String[1];
                data[0] = temp_id;

                PutData putData = new PutData(GETADDRESS, "POST", field, data);

                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();



                        try {

                            JSONArray array = new JSONArray(result);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                String cuser_brgy = object.getString("cuser_brgy");
                                String cuser_municipality = object.getString("cuser_municipality");

                                d = cuser_brgy;
                                e = cuser_municipality;

                            }
                        }
                        catch (Exception e){
                            Toast.makeText(getApplicationContext(),e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                else {
                    Toast.makeText(getApplicationContext(),"ERROR 000", Toast.LENGTH_SHORT).show();
                }

            }
        });


        a = temp_add;
        b = temp_num;
        c = temp_hNum;
        f = temp_prov;
        g = temp_zip;

    }

    public void NumberValidator() {
        phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 1 && !s.toString().trim().equals("9")) {

                    phone_number.setText("");


                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void OnBackPressed() {

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(R.anim.slide_fade_in, R.anim.slide_fade_out);
                onBackPressed();
            }
        });

    }

    public void DefaultAddress(){

        String Default_address = sharedPreferences.getString("default_address","");

        if(temp_id.equals(Default_address)){
            default_add_bttn.setChecked(true);
        }

        default_add_bttn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                final String combined_address = c+" "+d+" "+e+" "+f+" "+ g;

                if(default_add_bttn.isChecked()){
                    sharedPreferences.edit().putString("default_address",temp_id).apply();
                    sharedPreferences.edit().putString("receipt_number","+63"+b).apply();
                    sharedPreferences.edit().putString("string_address",combined_address).apply();
                }
                else {
                    sharedPreferences.edit().remove("default_address").apply();
                }
            }
        });
    }

    public void UpdateButton() {
        update_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UpdateAddress();

            }
        });
    }

    public void DeleteButton() {
        delete_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeleteAddress();
            }
        });
    }

    //BACKEND

    public void DeleteAddress() {

        if (!temp_id.isEmpty()) {

            Handler handler = new Handler();
            handler.post(new Runnable() {
                @Override
                public void run() {

                    String[] field = new String[1];
                    field[0] = "address_id";

                    String[] data = new String[1];
                    data[0] = temp_id;

                    PutData putData = new PutData(DELETE_URL, "POST", field, data);

                    if (putData.startPut()) {

                        if (putData.onComplete()) {
                            String result = putData.getResult();

                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), menu_address.class);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "Error on Internet Connection", Toast.LENGTH_SHORT).show();
                    }

                }
            });

        } else {

            Toast.makeText(getApplicationContext(), "Address ID not found", Toast.LENGTH_SHORT).show();
        }


    }

    public void UpdateAddress() {

        String temp_number = phone_number.getText().toString().trim();
        String temp_house_number = house_number.getText().toString().trim();

        String temp_province = province_src[0];
        String temp_title = address_title.getText().toString().trim();
        String temp_zip = zipcode.getText().toString().trim();

        if (!temp_number.isEmpty() && !temp_house_number.isEmpty() && !temp_brgy.isEmpty()
                && !temp_municipality.isEmpty() && !temp_province.isEmpty() && !temp_title.isEmpty()
                && !temp_zip.isEmpty() && !temp_brgy.equals("SELECT BARANGAY")
                && !temp_municipality.equals("SELECT MUNICIPALITY")) {

            if(a.equals(temp_title) && b.equals(temp_number) && c.equals(temp_house_number) &&
                    d.equals(temp_brgy) && e.equals(temp_municipality) && f.equals(temp_province)
                        && g.equals(temp_zip)){
                Toast.makeText(getApplicationContext(),"No Changes to Update",Toast.LENGTH_SHORT).show();
            }

            else{

                Handler handler = new Handler();
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        String[] field = new String[8];
                        field[0] = "address_id";
                        field[1] = "cuser_number";
                        field[2] = "cuser_house_number";
                        field[3] = "cuser_brgy";
                        field[4] = "cuser_municipality";
                        field[5] = "cuser_province";
                        field[6] = "address_title";
                        field[7] = "zipcode";

                        String[] data = new String[8];
                        data[0] = temp_id;
                        data[1] = temp_number;
                        data[2] = temp_house_number;
                        data[3] = temp_brgy;
                        data[4] = temp_municipality;
                        data[5] = temp_province;
                        data[6] = temp_title;
                        data[7] = temp_zip;


                        PutData putData = new PutData(UPDATE_URL, "POST", field, data);

                        if (putData.startPut()) {

                            if (putData.onComplete()) {

                                String result = putData.getResult();
                                Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Update_address.this, menu_address.class);
                                startActivity(intent);
                            }

                        } else {
                            Toast.makeText(getApplicationContext(), "Error on Internet Connection", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

            }




        }

        else {
            Toast.makeText(getApplicationContext(),"All fields is Required",Toast.LENGTH_SHORT).show();
        }
    }

}