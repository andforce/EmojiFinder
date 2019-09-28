package com.andforce.retrofit.services;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

public interface DownloadService {
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);

    @GET("http://unicode.org/Public/emoji/latest/emoji-test.txt")
    Observable<ResponseBody> emoji_test();
}
