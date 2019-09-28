package com.andforce.retrofit.services;

import com.andforce.beans.EmojiImage;
import com.andforce.beans.VendorEmojiImage;
import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

import java.util.List;

public interface EmojiService {
    @GET("apple/ios-12.2")
    Observable<List<EmojiImage>> apple();

    @GET("google")
    Observable<List<EmojiImage>> google();

    @GET
    Observable<List<VendorEmojiImage>> fetchOne(@Url String url);
}
