package com.andforce.retrofit.managers;

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

public class RetrofitManager {

    private static RetrofitManager sRetrofitManager;
    private Retrofit retrofit;

    private RetrofitManager() {
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.connectTimeout(9, TimeUnit.SECONDS);

        if (true) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            builder.addInterceptor(interceptor);
        }

        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Response response = chain.proceed(request);

                String bodyString = response.body().string();

                List<EmojiImage> emojiImages = new ArrayList<>();

                //文档对象，用来接收html页面
                Document document = Jsoup.parse(bodyString);
                int count = 0;
                if (document != null) {
                    Elements elements = document.select("ul.emoji-grid");
                    Element element = elements.get(0);
                    Elements emojiAttr = element.children();
                    for (Element emoji : emojiAttr) {
                        count++;
                        String src = emoji.select("img").attr("data-src");
                        if (src == null || src.equals("")) {
                            src = emoji.select("img").attr("src");
                        }

                        String imgUrl = src.replace("/72/", "/320/");
                        System.out.println(">> " + imgUrl);

                        EmojiImage image = new EmojiImage();
                        image.setEmoji("" + count);
                        image.setName("456");
                        image.setVersion("11");
                        image.setImage(imgUrl);

                        emojiImages.add(image);
                    }
                    System.out.println(emojiAttr.size() + " real: " + count);
                }

                MediaType mediaType = MediaType.parse("application/json;charset=uft-8");
                return response.newBuilder().body(ResponseBody.create(mediaType, new Gson().toJson(emojiImages))).build();
            }
        });

        OkHttpClient okHttpClient = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl("https://emojipedia.org/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public static RetrofitManager getInstance() {
        if (sRetrofitManager == null) {
            sRetrofitManager = new RetrofitManager();
        }
        return sRetrofitManager;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }
}
