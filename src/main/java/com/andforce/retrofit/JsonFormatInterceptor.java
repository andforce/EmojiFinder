package com.andforce.retrofit;

import com.andforce.updater.EmojiTest;
import com.andforce.updater.TextJsonConverter;
import com.google.gson.Gson;
import okhttp3.*;

import java.io.IOException;

public class JsonFormatInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);

        String bodyString = null;
        MediaType mediaType = MediaType.parse("application/json;charset=uft-8");
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            bodyString = responseBody.string();
            TextJsonConverter jsonConverter = new TextJsonConverter();
            return response.newBuilder().body(ResponseBody.create(mediaType, jsonConverter.convert(bodyString))).build();
        } else {
            return response.newBuilder().body(ResponseBody.create(mediaType, new Gson().toJson(new EmojiTest()))).build();
        }
    }
}
