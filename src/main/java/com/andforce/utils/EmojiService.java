package com.andforce.utils;

import com.andforce.beans.EmojiImage;
import io.reactivex.Observable;
import retrofit2.http.GET;

import java.util.List;

public interface EmojiService {
    @GET("apple/ios-12.2")
    Observable<List<EmojiImage>> apple();

    @GET("google")
    Observable<List<EmojiImage>> google();
}
