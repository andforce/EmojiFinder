package com.andforce.retrofit.managers;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class DownloadRetrofitManager {

    private static DownloadRetrofitManager sRetrofitManager;
    private Retrofit retrofit;

    private DownloadRetrofitManager() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        if (true) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        OkHttpClient okHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://emojipedia.org/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static DownloadRetrofitManager getInstance() {
        if (sRetrofitManager == null) {
            sRetrofitManager = new DownloadRetrofitManager();
        }
        return sRetrofitManager;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
