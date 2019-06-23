package com.andforce.utils;

import com.andforce.beans.EmojiImage;
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

public class Main {


    private static final String EMOJI_TEST = "./src/main/resources/emoji-test.txt";

    public static void main(String[] args) {

//        Set<String> emojies = new HashSet<>();
//
//        File file = new File(EMOJI_TEST);
//
//        BufferedReader bufferedReader = null;
//
//        StringBuilder stringBuilder = new StringBuilder();
//        int i = 0;
//        try {
//            bufferedReader = new BufferedReader(new FileReader(file));
//            String line;
//            while ((line = bufferedReader.readLine()) != null) {
//                if (line.trim().length() != 0 && !line.startsWith("#")) {
//                    String emojiStart = line.substring(line.indexOf(" # ") + " # ".length());
//                    String emoji = emojiStart.substring(0, emojiStart.indexOf(" "));
//                    stringBuilder.append(emoji).append('\n');
//                    emojies.add(emoji);
//                    i++;
//                }
//            }
//            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("\n"));
//            FileUtils.writeToFile("./src/main/resources/emoji.txt", stringBuilder.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (bufferedReader != null) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }


//        SensitiveWordMatcher sensitiveWordMatcher = new SensitiveWordMatcher(EmojiSource.map);
//
//
//        String src = FileUtils.readString(EMOJI_TEST);
//        long start1 = System.currentTimeMillis();
//        Set<String> set = sensitiveWordMatcher.matches(src, false);
//        long end1 = System.currentTimeMillis();
//        System.out.println("\r\nTest Emoji File count: >>> count:" + " time:" + (end1 - start1) + " " + set.size());


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

                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("{");

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
                        if (src == null || src.equals("")){
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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://emojipedia.org/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        EmojiService service = retrofit.create(EmojiService.class);

        DownloadService downloadService = DownalodRetrofitManager.getInstance().create(DownloadService.class);

        service.apple().flatMap(new Function<List<EmojiImage>, Observable<EmojiImage>>() {
            @Override
            public Observable<EmojiImage> apply(List<EmojiImage> emojiImages) throws Exception {
                return Observable.fromIterable(emojiImages);
            }
        }).flatMap(new Function<EmojiImage, ObservableSource<File>>() {
            @Override
            public ObservableSource<File> apply(EmojiImage emojiImage) throws Exception {
                return downloadService.download(emojiImage.getImage()).flatMap(new Function<ResponseBody, ObservableSource<File>>() {
                    @Override
                    public ObservableSource<File> apply(ResponseBody responseBody) throws Exception {
                        return Observable.just(FileUtils.writeToFile(responseBody.byteStream(),
                                "/Users/diyuanwang/github/EmojiFinder/images/" + emojiImage.getEmoji() + ".png"));
                    }
                });
            }
        }).subscribe(new Consumer<File>() {
            @Override
            public void accept(File file) throws Exception {
                System.out.println(file.getAbsoluteFile());
            }
        });


//        String url = "https://emojipedia.org/apple/ios-12.2";
//        OkHttpClient okhttp = new OkHttpClient();
//        final Request request = new Request.Builder()
//                .url(url)
//                .get()//默认就是GET请求，可以不写
//                .build();
//        Call call = okhttp.newCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                System.out.println("onFailure: ");
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String html = null;
//                ResponseBody responseBody = response.body();
//                if (responseBody != null) {
//                    html = responseBody.string();
//                }
//
//                if (html == null){
//                    return;
//                }
//
//                //文档对象，用来接收html页面
//                Document document = Jsoup.parse(html);
//
//                int count = 0;
//                if (document != null) {
//                    Elements elements = document.select("ul.emoji-grid");
//                    Element element = elements.get(0);
//                    Elements emojiAttr = element.children();
//                    for (Element emoji : emojiAttr) {
//                        count++;
//                        String src = emoji.select("img").attr("data-src");
//                        if (src == null || src.equals("")){
//                            src = emoji.select("img").attr("src");
//                        }
//
//                        String imgUrl = src.replace("/72/", "/320/");
//                        System.out.println(">> " + imgUrl);
//                    }
//                    System.out.println(emojiAttr.size() + " real: " + count);
//
//                }
//            }
//        });


    }

}
