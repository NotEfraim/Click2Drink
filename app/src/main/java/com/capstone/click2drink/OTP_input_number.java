package com.capstone.click2drink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.executor.TaskExecutor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.concurrent.TimeUnit;


public class OTP_input_number extends AppCompatActivity {

    EditText txt_number;
    private FirebaseAuth mAuth;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private LottieAnimationView progress;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_input_number);

        ImageView back = findViewById(R.id.back);
        txt_number =(EditText) findViewById(R.id.number);
        Button send_code = (Button) findViewById(R.id.send_code);
        progress = findViewById(R.id.progress);

        mAuth = FirebaseAuth.getInstance();

        //Remove Zero
        removeZero();

        //Buttons
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        send_code.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progress.setVisibility(View.VISIBLE);

                if(txt_number.getText().toString().isEmpty()){
                    progress.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Mobile number is required",Toast.LENGTH_SHORT).show();
                }
                else {

                    // Phone checker view HTTP Request
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            String[] field = new String[1];
                            field[0] = "number";
                            String[] data = new String[1];
                            data[0] = txt_number.getText().toString();

                            String email_url = "http://resources.click2drinkph.store/php/phoneChecker.php";
                            PutData putData = new PutData(email_url,"POST",field,data);

                            if(putData.startPut()){
                                if(putData.onComplete()){

                                    String result = putData.getResult();

                                    if(result.equals("proceed")){
                                        otpSend();
                                        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
                                        String toPref = txt_number.getText().toString();
                                        sharedPreferences.edit().putString("temp_number",toPref).apply();

                                    }
                                    else{
                                        progress.setVisibility(View.GONE);
                                        Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                    }


                                }

                            }
                            else{
                                progress.setVisibility(View.GONE);
                                Toast.makeText(OTP_input_number.this,"Connection Error",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }); //handler

                } // else
            } // onclick
        });
        //end of buttons

    }


    public void otpSend(){

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(OTP_input_number.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                progress.setVisibility(View.GONE);
                Intent intent = new Intent(OTP_input_number.this,OTP_input_code.class);
                intent.putExtra("verificationId", verificationId);
                intent.putExtra("token",token);
                sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
                sharedPreferences.edit().putString("activity_credential","signup").apply();
                startActivity(intent);
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+63"+txt_number.getText().toString().trim())       // Phone number to verify
//                        .setPhoneNumber("+13000000000")
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }


    public void removeZero(){
        txt_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().trim().length() == 1 && s.toString().trim().equals("0")) {

                    txt_number.setText("");

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}