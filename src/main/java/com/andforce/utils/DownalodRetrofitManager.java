package com.andforce.utils;

import com.andforce.beans.EmojiImage;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DownalodRetrofitManager {

    private static DownalodRetrofitManager sRetrofitManager;
    private Retrofit retrofit;

    private DownalodRetrofitManager() {
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

    public static DownalodRetrofitManager getInstance() {
        if (sRetrofitManager == null) {
            sRetrofitManager = new DownalodRetrofitManager();
        }
        return sRetrofitManager;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
