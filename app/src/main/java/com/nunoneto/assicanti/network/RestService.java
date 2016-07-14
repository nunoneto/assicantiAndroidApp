package com.nunoneto.assicanti.network;

import com.facebook.stetho.inspector.protocol.module.Network;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by NB20301 on 14/07/2016.
 */
public class RestService {

    private static RestService instance;

    private AssicantiService assicantiService;

    public static RestService getInstance(){
        return instance == null? (instance = new RestService()) : instance;
    }

    public RestService(){

        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("http://assicanti.pt")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        assicantiService = retrofit.create(AssicantiService.class);
    }

    public AssicantiService getAssicantiService() {
        return assicantiService;
    }
}
