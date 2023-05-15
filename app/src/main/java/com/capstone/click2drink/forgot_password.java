package com.capstone.click2drink;

import androidx.annotation.NonNull;
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

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.concurrent.TimeUnit;

public class forgot_password extends AppCompatActivity {

    String phone_checker_url ="http://resources.click2drinkph.store/php/phoneChecker.php";
    EditText Phone_number;
    Button submit;
    ImageView back;
    String txt_number;

    private FirebaseAuth mAuth;
    private LottieAnimationView progress;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mAuth = FirebaseAuth.getInstance();

        progress = findViewById(R.id.progress);
        back = findViewById(R.id.back);
        Phone_number = findViewById(R.id.phone_number);
        submit = findViewById(R.id.submit);

        txt_number = Phone_number.getText().toString();

        //Functions
        removeZero();


        //Buttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);

                if(Phone_number.getText().toString().isEmpty()){
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Mobile number is required",Toast.LENGTH_SHORT).show();
                }
                else{

                    PhoneChecker();


                }

            }
        });

    }

    public void PhoneChecker(){

        txt_number = Phone_number.getText().toString();


        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String[] field = new String[1];
                field[0] = "number";

                String[] data = new String[1];
                data[0] = txt_number;

                PutData putData = new PutData(phone_checker_url, "POST", field, data);
                if(putData.startPut()){
                    if(putData.onComplete()){
                        String result = putData.getResult();
                        if(result.equals("Phone Number is Already Registered")){
                            sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
                            sharedPreferences.edit().putString("temp_number",Phone_number.getText().toString()).apply();
                            otpSend();

                        }
                        else{

                            Toast.makeText(getApplicationContext(),"Phone number is not registered",Toast.LENGTH_SHORT).show();
                        }
                    }
                }

            }
        });
    }

    public void otpSend(){

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(forgot_password.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progress.setVisibility(View.GONE);
                Intent intent = new Intent(forgot_password.this, OTP_input_code.class);
                intent.putExtra("verificationId", verificationId);
                intent.putExtra("token", token);
                sharedPreferences = getSharedPreferences("SharedPref", MODE_PRIVATE);
                sharedPreferences.edit().putString("activity_credential", "forgot_password").apply();
                startActivity(intent);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+63"+Phone_number.getText().toString().trim())       // Phone number to verify
//                        .setPhoneNumber("+12000000000")
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }

    public void removeZero(){

        Phone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 1 && s.toString().trim().equals("0")) {

                    Phone_number.setText("");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


}