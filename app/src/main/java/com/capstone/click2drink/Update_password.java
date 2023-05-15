package com.capstone.click2drink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vishnusivadas.advanced_httpurlconnection.PutData;

public class Update_password extends AppCompatActivity {

    private static String UPDATE_URL ="http://resources.click2drinkph.store/php/update_password.php";
    private EditText password, confirm_password;
    SharedPreferences sharedPreferences;
    String txt_number;
    Button submit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        password = findViewById(R.id.password);
        confirm_password = findViewById(R.id.confirm_pass);
        submit = findViewById(R.id.submit);

        sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
        txt_number = sharedPreferences.getString("temp_number", "");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(password.getText().toString().equals(confirm_password.getText().toString())){
                    UpdatePassword();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Password must match", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

    public void UpdatePassword(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[2];
                field[0] = "number";
                field[1] = "password";

                String[] data = new String[2];
                data[0] = txt_number;
                data[1] = password.getText().toString();

                PutData putData = new PutData(UPDATE_URL,"POST", field,data);

                if(putData.startPut()){
                    if(putData.onComplete()){
                        Intent intent = new Intent(getApplicationContext(),login.class);
                        startActivity(intent);
                        finish();
                    }
                }

            }
        });
    }
}