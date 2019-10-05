package com.andforce.retrofit.managers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.internal.platform.Platform;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

import static okhttp3.internal.platform.Platform.INFO;
import static okhttp3.internal.platform.Platform.WARN;

public class InterceptorRetrofitManager {
    private Retrofit retrofit;

    public InterceptorRetrofitManager(String baseUrl, Interceptor customInterceptor) {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        if (false) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new PrettyLogger());
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(loggingInterceptor);
        }

        builder.addInterceptor(customInterceptor);

        OkHttpClient okHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    class PrettyLogger implements HttpLoggingInterceptor.Logger {
        private Gson mGson = new GsonBuilder().setPrettyPrinting().create();
        private JsonParser mJsonParser = new JsonParser();

        @Override
        public void log(String message) {
            String trimMessage = message.trim();
            if ((trimMessage.startsWith("{") && trimMessage.endsWith("}"))
                    || (trimMessage.startsWith("[") && trimMessage.endsWith("]"))) {
                try {
                    String prettyJson = mGson.toJson(mJsonParser.parse(message));
                    Platform.get().log(INFO, prettyJson, null);
                } catch (Exception e) {
                    Platform.get().log(WARN, message, e);
                }
            } else {
                Platform.get().log(INFO, message, null);
            }
        }
    }
}
