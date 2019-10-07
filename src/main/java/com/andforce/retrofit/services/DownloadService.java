package com.andforce.retrofit.services;

import com.andforce.updater.EmojiTest;
import io.reactivex.Observable;
import retrofit2.http.GET;

public interface DownloadService {
    @GET("http://unicode.org/Public/emoji/latest/emoji-test.txt")
    Observable<EmojiTest> emoji_test();
}
