package com.andforce.utils;

import com.andforce.beans.EmojiBean;
import com.andforce.beans.EmojiImage;
import com.andforce.retrofit.JsonFormatInterceptor;
import com.andforce.retrofit.managers.DownloadRetrofitManager;
import com.andforce.retrofit.managers.InterceptorRetrofitManager;
import com.andforce.retrofit.managers.RetrofitManager;
import com.andforce.retrofit.services.DownloadService;
import com.andforce.retrofit.services.EmojiService;
import com.andforce.updater.EmojiTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Main {


    public static void main(String[] args) {

        Set<String> emojiSet = new HashSet<String>();

        if (true){
            InterceptorRetrofitManager customRetrofitManager = new InterceptorRetrofitManager(
                    "http://unicode.org", new JsonFormatInterceptor());

            DownloadService service = customRetrofitManager.create(DownloadService.class);
            service.emoji_test().subscribe(new Consumer<EmojiTest>() {
                @Override
                public void accept(EmojiTest emojiTest) throws Exception {
                    Gson mGson = new GsonBuilder().setPrettyPrinting().create();

                    FileUtils.writeToFile("src/main/resources/emoji-test.json", mGson.toJson(emojiTest));

                    System.out.println(emojiTest.getEmojis().size());

                    for (int i = 0; i < emojiTest.getEmojis().size(); i++) {
                        emojiSet.add(emojiTest.getEmojis().get(i).getContent());
                    }
                }
            });

        }

        System.out.println("Dic: " + emojiSet.size());

        long start = System.currentTimeMillis();
        SensitiveWordMatcher sensitiveWordMatcher = new SensitiveWordMatcher(emojiSet);
        ArrayList<EmojiBean> set = sensitiveWordMatcher.matches(FileUtils.readString("/Users/diyuanwang/github/EmojiFinder/src/main/resources/emoji-test.txt"), false);

        System.out.println("Use Time: " + (System.currentTimeMillis() - start));
        System.out.println("Find Result : " + set);

        System.out.println("Find Result Count : " + set.size());



//        RetrofitManager retrofitManager = RetrofitManager.getInstance();
//        EmojiService emojiService = retrofitManager.create(EmojiService.class);
//
//        DownloadService downloadService = DownloadRetrofitManager.getInstance().create(DownloadService.class);
//
//        emojiService.apple().flatMap(new Function<List<EmojiImage>, Observable<EmojiImage>>() {
//            @Override
//            public Observable<EmojiImage> apply(List<EmojiImage> emojiImages) throws Exception {
//                return Observable.fromIterable(emojiImages);
//            }
//        }).flatMap(new Function<EmojiImage, ObservableSource<File>>() {
//            @Override
//            public ObservableSource<File> apply(EmojiImage emojiImage) throws Exception {
//                return downloadService.download(emojiImage.getImage()).flatMap(new Function<ResponseBody, ObservableSource<File>>() {
//                    @Override
//                    public ObservableSource<File> apply(ResponseBody responseBody) throws Exception {
//
//                        final String dir = System.getProperty("user.dir") + File.separator;
//                        File file = new File(dir, "images");
//                        if (!file.exists()) {
//                            file.mkdirs();
//                        }
//
//                        return Observable.just(FileUtils.writeToFile(responseBody.byteStream(),
//                                new File(file, emojiImage.getEmoji() + ".png")));
//                    }
//                });
//            }
//        }).subscribe(new Consumer<File>() {
//            @Override
//            public void accept(File file) throws Exception {
//                System.out.println(file.getAbsoluteFile());
//            }
//        });

    }

}
