package com.capstone.click2drink;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.vishnusivadas.advanced_httpurlconnection.FetchData;
import com.vishnusivadas.advanced_httpurlconnection.PutData;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginTabFragment extends Fragment {

    SharedPreferences sharedPreferences;
    final String LOGIN_URL = "http://resources.click2drinkph.store/php/login.php";
    TextInputEditText email;
    TextInputEditText password;
    TextView forgot_pass;
    Button login_btn;
    float v = 0;
    LottieAnimationView prograssBar;

//    private FirebaseAuth auth;

    // For multi ui
    String userType;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_fragment,container,false);

        email = root.findViewById(R.id.email);
        password = root.findViewById(R.id.password);
        forgot_pass = root.findViewById(R.id.forgot_pass);
        login_btn = root.findViewById(R.id.Login_btn);
        prograssBar = root.findViewById(R.id.progress);

        sharedPreferences = getActivity()
                .getSharedPreferences("SharedPref", Context.MODE_PRIVATE);

//        auth = FirebaseAuth.getInstance();


        email.setAlpha(v);
        password.setAlpha(v);
        forgot_pass.setAlpha(v);
        login_btn.setAlpha(v);


        email.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(400).start();
        password.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(600).start();
        forgot_pass.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(500).start();
        login_btn.animate().translationX(0).alpha(1).setDuration(800).setStartDelay(700).start();

        forgot_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),forgot_password.class);
                startActivity(intent);
            }
        });

        login_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                prograssBar.setVisibility(View.VISIBLE);
                String txt_email,txt_password;
                txt_email = email.getText().toString();
                txt_password = password.getText().toString();

                //Check User Type Start
                // need to load early
                CheckUserType();

                //For Loading purposes
                Handler mhandler = new Handler();
                mhandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        if(!TextUtils.isEmpty(txt_email) && !TextUtils.isEmpty(txt_password))
                        {

                            if(isEmailValid(txt_email)){

                                Handler handler = new Handler();
                                if(userType.equals("customer")){

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            String[] field = new String[2];
                                            field[0] = "cuser_email";
                                            field[1] = "cuser_password";
                                            String[] data = new String[2];
                                            data[0] = txt_email;
                                            data[1] = txt_password;

                                            PutData putData = new PutData(LOGIN_URL,"POST",field,data);
                                            //Start of operation
                                            if(putData.startPut()){
                                                if(putData.onComplete()){
                                                    String result = putData.getResult();
                                                    if(result.equals("Login Success")){

                                                        //inserting new data
                                                        sharedPreferences.edit().putString("temp_email",txt_email).apply();
                                                        //
                                                        Handler xhandler = new Handler();
                                                        xhandler.postDelayed(new Runnable() {
                                                            @Override
                                                            public void run() {

                                                                Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                                                                Intent activity = new Intent(getActivity(),MainUi.class);
                                                                startActivity(activity);
                                                                getActivity().finish();

                                                            }
                                                        },2000);


                                                    }
                                                    else {
                                                        Toast.makeText(getContext(),"Email or password is incorrect",Toast.LENGTH_SHORT).show();
                                                        prograssBar.setVisibility(View.GONE);
                                                    }
                                                }
                                            }


                                        }
                                    });  //handler Ending

                                }

                                else{

                                    // logging in the user as staff

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {

                                            String[] field = new String[2];
                                            field[0] = "email";
                                            field[1] = "password";
                                            String[] data = new String[2];
                                            data[0] = txt_email;
                                            data[1] = txt_password;

                                            String url = "http://resources.click2drinkph.store/php/staff_login.php";

                                            PutData staff = new PutData(url,"POST",field,data);

                                            if(staff.startPut()){
                                                if(staff.onComplete()){

                                                    String result = staff.getResult();
                                                    // Start Login
                                                    if(result.equals("Login Success")){

                                                        Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                                                        sharedPreferences.edit().putString("staff_email",txt_email).apply();
                                                        Intent intent = new Intent(getActivity(),com.capstone.click2drink.staff.staff_main_ui.class);
                                                        startActivity(intent);
                                                        getActivity().finish();

                                                    }
                                                    else{
                                                        prograssBar.setVisibility(View.GONE);
                                                        Toast.makeText(getContext(),result,Toast.LENGTH_SHORT).show();
                                                    }

                                                }
                                                else{

                                                    Toast.makeText(getContext(),"Can't Complete Your Request",Toast.LENGTH_SHORT).show();

                                                }

                                            }
                                            else{
                                                Toast.makeText(getContext(),"Error in Request",Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });

                                } //staff else ending
                            }
                            else{
                                prograssBar.setVisibility(View.GONE);
                                Toast.makeText(getContext(),"Use Valid Email",Toast.LENGTH_SHORT).show();
                            }

                        }
                        else{
                            prograssBar.setVisibility(View.GONE);
                            Toast.makeText(getContext(),"All fields must not empty",Toast.LENGTH_SHORT).show();
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

    public void CheckUserType(){
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {

                String url = "http://resources.click2drinkph.store/php/CheckUserType.php";
                String[] field = new String[1];
                field[0] = "email";
                String[] data = new String[1];
                data[0] = email.getText().toString();

                PutData putData = new PutData(url,"POST",field,data);

                if(putData.startPut()){
                    if(putData.onComplete()){
                        //Getting the result into global variable userType
                        userType = putData.getResult();


                    }
                }
                else{
                    Toast.makeText(getContext(),"Error in Connection code: C2D-01",Toast.LENGTH_SHORT).show();
                }


            }
        });

    }

}
