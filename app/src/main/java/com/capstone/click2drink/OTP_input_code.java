package com.capstone.click2drink;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.concurrent.TimeUnit;

public class OTP_input_code extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    String SIGN_UP_URL = "http://resources.click2drinkph.store/php/signup.php";
    EditText code1,code2,code3,code4,code5,code6;
    String VerificationId, token,mNumber;
    TextView resend;
    LottieAnimationView progress;

    String activity_credential;

    //update profile otp

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_o_t_p_input_code);

        ImageView back = findViewById(R.id.back);
        TextView txt_number = findViewById(R.id.number);
        resend = findViewById(R.id.resend_otp);
        Button verify = findViewById(R.id.verify);
        progress = findViewById(R.id.progress);


        //Getting number from sharedPref
        Intent intent = getIntent();
        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        mNumber = sharedPreferences.getString("temp_number","");

        txt_number.setText(String.format("+63-%s", mNumber));

        //Codes
        code1 = findViewById(R.id.input_code_1);
        code2 = findViewById(R.id.input_code_2);
        code3 = findViewById(R.id.input_code_3);
        code4 = findViewById(R.id.input_code_4);
        code5 = findViewById(R.id.input_code_5);
        code6 = findViewById(R.id.input_code_6);


        //Activity credential
        activity_credential = sharedPreferences.getString("activity_credential","");


        assert activity_credential != null;

        if(activity_credential.equals("signup")){
            VerificationId = intent.getStringExtra("verificationId");
            token = intent.getStringExtra("token");
        }
        else if (activity_credential.equals("update")){
            //sending otp for profile update
            otpSend();
        }
        else if(activity_credential.equals("forgot_password")){
            VerificationId = intent.getStringExtra("verificationId");
            token = intent.getStringExtra("token");


        }

        //Text shifter
        text_shifter();



        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                        progress.setVisibility(View.VISIBLE);

                        if(code1.getText().toString().trim().isEmpty() ||
                        code2.getText().toString().trim().isEmpty()||
                        code3.getText().toString().trim().isEmpty() ||
                        code4.getText().toString().trim().isEmpty() ||
                        code5.getText().toString().trim().isEmpty() ||
                        code6.getText().toString().trim().isEmpty())
                        {
                            progress.setVisibility(View.GONE);
                            Toast.makeText(OTP_input_code.this,"OTP is not Valid",Toast.LENGTH_SHORT).show();

                        }
                        else {
                            if(VerificationId != null){
                                String code = code1.getText().toString().trim() +
                                        code2.getText().toString().trim()+
                                        code3.getText().toString().trim() +
                                        code4.getText().toString().trim()+
                                        code5.getText().toString().trim()+
                                        code6.getText().toString().trim();

                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(VerificationId, code);
                                FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {

                                        if(task.isSuccessful()){

                                            progress.setVisibility(View.GONE);

                                            Handler handler = new Handler();

                                            if(activity_credential.equals("update")){

                                                handler.post(new Runnable() {
                                                    @Override
                                                    public void run() {

                                                        String UPDATE_URL ="http://resources.click2drinkph.store/php/UpdateProfile.php";

                                                        String[] fields = new String[5];
                                                        fields[0] = "cuser_email";
                                                        fields[1] = "cuser_name";
                                                        fields[2] = "cuser_bday";
                                                        fields[3] = "cuser_phone_number";
                                                        fields[4] = "cuser_current_address";

                                                        Intent intent = getIntent();

                                                        String t_email,t_name,t_bday,t_number,t_address;

                                                        t_email = intent.getStringExtra("update_email");
                                                        t_name = intent.getStringExtra("update_name");
                                                        t_bday = intent.getStringExtra("update_bday");
                                                        t_number = intent.getStringExtra("update_phone");
                                                        t_address = intent.getStringExtra("update_address");

                                                        String[] data = new String[5];
                                                        data[0] = t_email;
                                                        data[1] = t_name;
                                                        data[2] = t_bday;
                                                        data[3] = t_number;
                                                        data[4] = t_address;

                                                        PutData putData = new PutData(UPDATE_URL,"POST",fields,data);

                                                        if(putData.startPut()){

                                                            if(putData.onComplete()){
                                                                String result = putData.getResult();
                                                                progress.setVisibility(View.VISIBLE);
                                                                Toast.makeText(getApplicationContext(),result.trim(),Toast.LENGTH_SHORT).show();
                                                                Intent toMain = new Intent(OTP_input_code.this,MainUi.class);
                                                                startActivity(toMain);
                                                            }

                                                        }
                                                        else{

                                                            Toast.makeText(getApplicationContext(),"Internet Connection Error",Toast.LENGTH_SHORT).show();
                                                        }




                                                    }
                                                });

                                            }
                                            else if (activity_credential.equals("signup")){

                                                //Sign Up OTP
                                                handler.post(new Runnable() {

                                                    @Override
                                                    public void run() {

                                                        String[] field = new String[4];
                                                        field[0] = "cuser_name";
                                                        field[1] = "cuser_email";
                                                        field[2] = "cuser_password";
                                                        field[3] = "cuser_phone_number";
                                                        String[] data = new String[4];

                                                        String txt_fullName,txt_email,txt_password,pref_number;
                                                        txt_fullName = sharedPreferences.getString("temp_fname","");
                                                        txt_email = sharedPreferences.getString("temp_email","");
                                                        txt_password = sharedPreferences.getString("temp_password","");
                                                        pref_number = sharedPreferences.getString("temp_number","");

                                                        data[0] = txt_fullName;
                                                        data[1] = txt_email;
                                                        data[2] = txt_password;
                                                        data[3] = pref_number;

                                                        PutData putData = new PutData(SIGN_UP_URL,"POST",field,data);
                                                        //Start of operation
                                                        if(putData.startPut()){

                                                            if(putData.onComplete()){

                                                                String result = putData.getResult();

                                                                if(result.equals("Sign Up Success")){
                                                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                                                    Intent activity = new Intent(getApplicationContext(),MainUi.class);
                                                                    activity.setFlags(activity.FLAG_ACTIVITY_CLEAR_TASK | activity.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(activity);

                                                                }
                                                                else if(result.equals("Email is Taken")){
                                                                    progress.setVisibility(View.GONE);
                                                                    Intent intent1 = new Intent(getApplicationContext(),login.class);
                                                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                                                    startActivity(intent1);

                                                                }
                                                                else {
                                                                    progress.setVisibility(View.GONE);
                                                                    Toast.makeText(getApplicationContext(),result,Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            else {
                                                                progress.setVisibility(View.GONE);
                                                                Toast.makeText(getApplicationContext(),"Error on registration",Toast.LENGTH_SHORT).show();
                                                            }
                                                        }

                                                        else{

                                                            Toast.makeText(getApplicationContext(),"Error Occur",Toast.LENGTH_SHORT).show();

                                                        }


                                                    }
                                                });

                                            }
                                            else if(activity_credential.equals("forgot_password")){
                                                Intent intent1 = new Intent(getApplicationContext(),Update_password.class);
                                                startActivity(intent1);
                                                finish();
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),"Error on activity Credential",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                        else{

                                            progress.setVisibility(View.GONE);
                                            Toast.makeText(OTP_input_code.this,"OTP is not Valid",Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            }

                        }

            }
        });

        resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ResentOTP();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void ResentOTP(){

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(OTP_input_code.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken token) {
                VerificationId = verificationId;
               Toast.makeText(getApplicationContext(),"OTP SENT",Toast.LENGTH_SHORT).show();
            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+63"+mNumber.trim())       // Phone number to verify
//                        .setPhoneNumber("+11000000000")
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    public void text_shifter(){

        code1.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    code2.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        code2.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    code3.requestFocus();
                }
                else if (s.length() == 0){
                    code1.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        code3.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    code4.requestFocus();
                }
                else if (s.length() == 0){
                    code2.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        code4.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    code5.requestFocus();
                }
                else if (s.length() == 0){
                    code3.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        code5.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {

                if (s.length() ==1) {
                    code6.requestFocus();
                }
                else if (s.length() == 0){
                    code4.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

        code6.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {


                 if (s.length() == 0){
                    code5.requestFocus();
                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
            }
        });

    }

    // FOR UPDATE PROFILE
    public void otpSend(){

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                progress.setVisibility(View.GONE);
                Toast.makeText(OTP_input_code.this,e.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCodeSent(@NonNull String verificationId,
                                   @NonNull PhoneAuthProvider.ForceResendingToken mtoken) {
                progress.setVisibility(View.GONE);
                VerificationId = verificationId;
                token = mtoken.toString();


            }
        };

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber("+63"+mNumber)       // Phone number to verify
//                        .setPhoneNumber("+11000000000")
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks(mCallback)          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);

    }
}