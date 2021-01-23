package com.ats.royalagro.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ats.royalagro.R;
import com.ats.royalagro.bean.LoginData;
import com.ats.royalagro.retroint.ApiInterface;
import com.ats.royalagro.util.CheckNetwork;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

import dmax.dialog.SpotsDialog;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText edMobile, edOTP;
    private Button btnLogin, btnSubmitOTP;
    private LinearLayout llForm, llOTP;

    private String randomNumber;

    private LoginData loginData = new LoginData();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("Sign In");

        edMobile = findViewById(R.id.edMobile);
        edOTP = findViewById(R.id.edOTP);
        btnLogin = findViewById(R.id.btnLogin);
        btnSubmitOTP = findViewById(R.id.btnSubmitOTP);
        llForm = findViewById(R.id.llLogin_form);
        llOTP = findViewById(R.id.llLogin_OTP);

        btnLogin.setOnClickListener(this);
        btnSubmitOTP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btnLogin) {
            // startActivity(new Intent(LoginActivity.this, HomeActivity.class));
            if (edMobile.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            } else if (edMobile.getText().toString().length() != 10) {
                Toast.makeText(this, "Please Enter 10 Digit Mobile Number", Toast.LENGTH_SHORT).show();
                edMobile.requestFocus();
            } else {
               // randomNumber = random(6);
                randomNumber = "123456";
                Log.e("RANDOM NUMBER : ", "----" + randomNumber);
                doLogin(edMobile.getText().toString(), "Dear User, Your OTP for RoyalAgro app is " + randomNumber + " Please do not share this OTP.");
                // send(edMobile.getText().toString(), "Your OTP number for RoyalAgro app is " + randomNumber + " Please do not share this OTP.");
            }
        } else if (v.getId() == R.id.btnSubmitOTP) {
            if (edOTP.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please Enter OTP Number", Toast.LENGTH_SHORT).show();
                edOTP.requestFocus();
            } else if (edOTP.getText().toString().equalsIgnoreCase("1234")) {//randomNumber.equals(edOTP.getText().toString())
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();

                SharedPreferences pref = getApplicationContext().getSharedPreferences(ApiInterface.MY_PREF, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                Gson gson = new Gson();
                String json = gson.toJson(loginData);
                editor.putString("loginData", json);
                editor.apply();
                editor.apply();

                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                finish();
            } else {
                Toast.makeText(this, "Please Enter Correct OTP Number", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static String random(int size) {
        StringBuilder generatedToken = new StringBuilder();
        try {
            SecureRandom number = SecureRandom.getInstance("SHA1PRNG");
            for (int i = 0; i < size; i++) {
                generatedToken.append(number.nextInt(9));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedToken.toString();
    }

    public void send(String mobile, String message) {
        if (CheckNetwork.isInternetAvailable(LoginActivity.this)) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();
                            Request request = original.newBuilder()
                                    .header("Accept", "application/json")
                                    .method(original.method(), original.body())

                                    .build();

                            Response response = chain.proceed(request);
                            return response;
                        }
                    })
                    .readTimeout(20000, TimeUnit.SECONDS)
                    .connectTimeout(20000, TimeUnit.SECONDS)
                    .writeTimeout(20000, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://control.bestsms.co.in/api/")
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create()).build();

            ApiInterface myInterface = retrofit.create(ApiInterface.class);

            Call<JsonObject> call = myInterface.sendSMS("140742AbB1cy8zZt589c06d5", mobile, message, "RCONNT", "4", "91", "json");

            final AlertDialog progressDialog = new SpotsDialog(LoginActivity.this);
            progressDialog.show();

            call.enqueue(new Callback<JsonObject>() {
                @Override
                public void onResponse(Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                    progressDialog.dismiss();
                    // Log.e("Json Object ", " is " + response.body().toString());
                    //Toast.makeText(RegistrationActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    llForm.setVisibility(View.GONE);
                    llOTP.setVisibility(View.VISIBLE);

                }

                @Override
                public void onFailure(Call<JsonObject> call, Throwable t) {
                    progressDialog.dismiss();
                    // Log.e("call failed ", " .. " + t.getMessage());
                    Toast.makeText(LoginActivity.this, "Unable To Login!\nPlease Try Again.", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

    public void doLogin(final String mobile, final String message) {

        if (CheckNetwork.isInternetAvailable(getApplicationContext())) {

            final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .build();

            Retrofit retrofit = new Retrofit.Builder().baseUrl(ApiInterface.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
            ApiInterface api = retrofit.create(ApiInterface.class);
            Call<LoginData> loginDataCall = api.doLogin(mobile);

            final AlertDialog progressDialog = new SpotsDialog(LoginActivity.this);
            progressDialog.show();


            loginDataCall.enqueue(new Callback<LoginData>() {
                @Override
                public void onResponse(Call<LoginData> call, retrofit2.Response<LoginData> response) {
                    if (response.body() != null) {
                        LoginData data = response.body();
                        if (data.getError()) {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "User Is Not Registered", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            loginData = data;


                            send(mobile, message);
                        }

                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(LoginActivity.this, "Unable To Login", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<LoginData> call, Throwable t) {
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Unable To Login", Toast.LENGTH_SHORT).show();
                }
            });


        } else {
            Toast.makeText(this, "Please Connect To Internet", Toast.LENGTH_SHORT).show();
        }
    }

}
