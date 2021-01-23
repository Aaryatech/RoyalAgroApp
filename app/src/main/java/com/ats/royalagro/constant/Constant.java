package com.ats.royalagro.constant;

import com.ats.royalagro.retroint.ApiInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Constant {

    public static OkHttpClient client = new OkHttpClient.Builder()
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
            .readTimeout(10000, TimeUnit.SECONDS)
            .connectTimeout(10000, TimeUnit.SECONDS)
            .writeTimeout(10000, TimeUnit.SECONDS)
            .build();


    public static Retrofit retrofit = new Retrofit.Builder()
            //.baseUrl("http://97.74.228.55:8080/RoyalAgroWebapi/")
            .baseUrl(ApiInterface.BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create()).build();


    //http://132.148.143.124:8080/agrowebapi/
}
