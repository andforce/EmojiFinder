package com.andforce.utils;

import com.andforce.beans.VendorEmojiImage;
import com.andforce.retrofit.managers.DownloadRetrofitManager;
import com.andforce.retrofit.services.DownloadService;
import com.andforce.retrofit.services.EmojiService;
import com.google.gson.Gson;
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
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FetchOne {


    private EmojiService service;

    private DownloadService downloadService;

    public FetchOne() {
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

                List<VendorEmojiImage> emojiImages = new ArrayList<>();

                //文档对象，用来接收html页面
                Document document = Jsoup.parse(bodyString);
                int count = 0;
                if (document != null) {
                    Elements elements = document.select("body > div.container > div.content > article > section.vendor-list");
                    Element element = elements.get(0);
                    Elements emojiAttr = element.children().get(0).children();
                    for (Element emoji : emojiAttr) {

                        Elements vendorInfos = emoji.select("div.vendor-info");
                        // 先取出地一个吧
                        Element vendorInfo = vendorInfos.first();
                        count++;
                        String src = emoji.select("img").attr("src");
                        if (src == null || src.equals("")) {
                            src = emoji.select("img").attr("data-src");
                        }

                        String imgUrl = src.replace("/120/", "/320/");
                        System.out.println(">> " + imgUrl);

                        VendorEmojiImage image = new VendorEmojiImage();
                        image.setVersion("11");
                        image.setVendor(vendorInfo.text().trim());
                        image.setUrl(imgUrl);

                        emojiImages.add(image);
                    }
                    System.out.println(emojiAttr.size() + " real: " + count);
                }

                MediaType mediaType = MediaType.parse("application/json;charset=uft-8");
                return response.newBuilder().body(ResponseBody.create(mediaType, new Gson().toJson(emojiImages))).build();
            }
        });

        OkHttpClient okHttpClient = builder.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://emojipedia.org/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        service = retrofit.create(EmojiService.class);

        downloadService = DownloadRetrofitManager.getInstance().create(DownloadService.class);
    }

    public void fetchOneEmpji(String emoji) {

        service.fetchOne("https://emojipedia.org/" + emoji.trim()).flatMap(new Function<List<VendorEmojiImage>, Observable<VendorEmojiImage>>() {
            @Override
            public Observable<VendorEmojiImage> apply(List<VendorEmojiImage> emojiImages) throws Exception {
                return Observable.fromIterable(emojiImages);
            }
        }).flatMap(new Function<VendorEmojiImage, ObservableSource<File>>() {
            @Override
            public ObservableSource<File> apply(VendorEmojiImage emojiImage) throws Exception {
                return downloadService.download(emojiImage.getUrl()).flatMap(new Function<ResponseBody, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(ResponseBody responseBody) throws Exception {

                        final String dir = System.getProperty("user.dir") + File.separator;
                        File file = new File(dir, "images_one");
                        if (!file.exists()) {
                            file.mkdirs();
                        }

                        return Observable.just(FileUtils.writeToFile(responseBody.byteStream(),
                                new File(file, emojiImage.getVendor() + ".png")));
                    }
                });
            }
        }).subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                System.out.println(file.getAbsoluteFile());
            }
        });
    }
}
