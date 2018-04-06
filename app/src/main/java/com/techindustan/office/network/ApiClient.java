package com.techindustan.office.network;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by binod on 27/3/18.
 */

public class ApiClient {
    public static final String BASE_URL = "http://techindustan.in/office/app/api/";
    public static final String BASE_URL_FP = "https://techindustan.in/pms/index.php/";
    private static Retrofit retrofit = null;
    private static Retrofit retrofit2 = null;


    public static Retrofit getClient() {

        if (retrofit == null) {
            //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            //httpClient.addInterceptor(logging);
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static Retrofit getClientFP() {

        if (retrofit2 == null) {
            //  HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            //logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            //httpClient.addInterceptor(logging);
            retrofit2 = new Retrofit.Builder()
                    .baseUrl(BASE_URL_FP)
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit2;
    }
}
