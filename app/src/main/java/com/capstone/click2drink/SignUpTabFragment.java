package com.capstone.click2drink;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SignUpTabFragment extends Fragment{

    String SIGN_UP_URL = "http://resources.click2drinkph.store/php/signup.php";
    TextInputEditText fullName,email,password,confirm_pass;
    Button button;
    LottieAnimationView progressBar;
    private FirebaseAuth auth;
    ViewPager viewPager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.signup_fragment,container,false);

        SharedPreferences sharedPreferences = getActivity().
                getSharedPreferences("SharedPref", Context.MODE_PRIVATE);

        fullName = root.findViewById(R.id.fullName);
        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        confirm_pass = root.findViewById(R.id.confirm_pass);
        progressBar = root.findViewById(R.id.progress);
        button = root.findViewById(R.id.signUp_btn);

        auth = FirebaseAuth.getInstance();
        //Hint
        password.setHint("Password");
        confirm_pass.setHint("Confirm Password");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                String txt_fullName,txt_email,txt_password,txt_confirmPass;
                txt_fullName = fullName.getText().toString();
                txt_email = email.getText().toString();
                txt_password = password.getText().toString();
                txt_confirmPass = confirm_pass.getText().toString();

                // For Loading
                Handler mhandler = new Handler();
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        // Start of register

                        if(isEmailValid(txt_email)){

                            if(txt_password.equals(txt_confirmPass)){

                                if(txt_password.length() > 6){

                                    // Main

                                    if(!TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password))
                                    {
                                        // Email check via HTTP Request
                                        Handler handler = new Handler();
                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {

                                                String[] field = new String[1];
                                                field[0] = "cuser_email";
                                                String[] data = new String[1];
                                                data[0] = txt_email;

                                                String email_url = "http://resources.click2drinkph.store/php/emailChecker.php";
                                                PutData putData = new PutData(email_url,"POST",field,data);

                                                if(putData.startPut()){

                                                    if(putData.onComplete()){

                                                        String result = putData.getResult();

                                                        if(result.equals("proceed")){

                                                            progressBar.setVisibility(View.GONE);
                                                            Intent activity = new Intent(getActivity(),OTP_input_number.class);
                                                            sharedPreferences.edit().putString("temp_fname",txt_fullName).apply();
                                                            sharedPreferences.edit().putString("temp_email",txt_email).apply();
                                                            sharedPreferences.edit().putString("temp_password",txt_password).apply();
                                                            startActivity(activity);

                                                        }

                                                        else {
                                                            progressBar.setVisibility(View.GONE);
                                                            Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                }

                                                else{

                                                    Toast.makeText(getContext(),"Connection Error",Toast.LENGTH_SHORT).show();

                                                }



                                            }
                                        });


                                    }

                                    //end
                                }

                                else{
                                    Toast.makeText(getContext(),"Set Strong Password",Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);

                                }

                            }

                            else{
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(),"Password must match",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(),"Email is invalid",Toast.LENGTH_SHORT).show();
                        }





                    }
                },1000);




            }

        });



        return root;
    }


    public boolean isEmailValid(String email)
    {
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        final Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

//    private void registerUser(String email, String password) {
//
//        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//
//                Handler handler = new Handler(Looper.getMainLooper());
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        if(task.isSuccessful()){
//                            progressBar.setVisibility(View.GONE);
//                            Toast.makeText(getContext(),"Verify your email first",Toast.LENGTH_LONG).show();
//                            Intent activity = new Intent(getContext(),login.class);
//                            startActivity(activity);
//                        }
//
//                    }
//                });
//            }
//        });

    //}
}
