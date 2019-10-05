package com.andforce.retrofit;

import com.andforce.updater.TextJsonConverter;
import okhttp3.*;

import java.io.IOException;

public class JsonFormatInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);

        MediaType mediaType = MediaType.parse("application/json;charset=uft-8");
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            TextJsonConverter textJsonConverter = new TextJsonConverter();
            return response.newBuilder().body(ResponseBody.create(mediaType, textJsonConverter.convert(responseBody.string()))).build();
        } else {
            return response.newBuilder().body(ResponseBody.create(mediaType, "{}")).build();
        }
    }
}
