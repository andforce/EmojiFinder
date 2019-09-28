package com.andforce.utils;

import com.andforce.beans.EmojiImage;
import com.andforce.retrofit.managers.DownloadRetrofitManager;
import com.andforce.retrofit.managers.GetRetrofitManager;
import com.andforce.retrofit.managers.InterceptorRetrofitManager;
import com.andforce.retrofit.managers.RetrofitManager;
import com.andforce.retrofit.services.DownloadService;
import com.andforce.retrofit.services.EmojiService;
import com.andforce.updater.TextJsonConverter;
import com.google.gson.Gson;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainNew {
    public static void main(String[] args) {

        new TextJsonConverter().convert("");

        InterceptorRetrofitManager customRetrofitManager = new InterceptorRetrofitManager("http://unicode.org", new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();

                Response response = chain.proceed(request);

                String bodyString = response.body().string();

                TextJsonConverter jsonConverter = new TextJsonConverter();

                MediaType mediaType = MediaType.parse("application/json;charset=uft-8");
                return response.newBuilder().body(ResponseBody.create(mediaType, jsonConverter.convert(bodyString))).build();
            }
        });

        DownloadService service = customRetrofitManager.create(DownloadService.class);
        service.emoji_test().map(new Function<ResponseBody, String>() {
            @Override
            public String apply(ResponseBody responseBody) throws Exception {
                return responseBody.string();
            }
        }).subscribe(new Consumer<String>() {
            @Override
            public void accept(String s) throws Exception {
                System.out.println(s);
            }
        });

        if (true){
            return;
        }

        //# subgroup: face-smiling
        //1F600                                      ; fully-qualified     # 😀 grinning face
        //1F603                                      ; fully-qualified     # 😃 grinning face with big eyes
        //1F604                                      ; fully-qualified     # 😄 grinning face with smiling eyes
        //1F601                                      ; fully-qualified     # 😁 beaming face with smiling eyes
        //1F606                                      ; fully-qualified     # 😆 grinning squinting face
        //1F605                                      ; fully-qualified     # 😅 grinning face with sweat
        //1F923                                      ; fully-qualified     # 🤣 rolling on the floor laughing
        //1F602                                      ; fully-qualified     # 😂 face with tears of joy
        //1F642                                      ; fully-qualified     # 🙂 slightly smiling face
        //1F643                                      ; fully-qualified     # 🙃 upside-down face
        //1F609                                      ; fully-qualified     # 😉 winking face
        //1F60A                                      ; fully-qualified     # 😊 smiling face with smiling eyes
        //1F607                                      ; fully-qualified     # 😇 smiling face with halo

        String mkTable = "| Emoji | Length |  UTF-16 | quality | Summary |\n" +
                "| :---: | :----: |  ------ |  ------ |  ------ |\n";
        String template = "| [%s] |   %d   |    %s   |    %s   |   %s    |\n";

        File file = new File("./src/main/resources/emoji-test.txt");

        BufferedReader bufferedReader = null;

        StringBuilder stringBuilder = new StringBuilder(mkTable);

        int i = 0;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().length() != 0 && !line.startsWith("#")) {

                    i++;

                    //找出CodePoints
                    Pattern pattern = Pattern.compile("[\\S{4}\\s{1}]+(?=\\s+;)");
                    Matcher matcher = pattern.matcher(line);
                    String codePoints = "\\u" + (matcher.find() ? line.substring(matcher.start(), matcher.end()) : "").trim().replace(" ", "\\u");


                    // 找出画质
                    Pattern qualityPattern = Pattern.compile("fully-qualified|minimally-qualified|unqualified|component");
                    Matcher qualityMatcher = qualityPattern.matcher(line);
                    String quality = qualityMatcher.find() ? line.substring(qualityMatcher.start(), qualityMatcher.end()) : "";

                    // 找出Emoji
                    Pattern emojiPattern = Pattern.compile("(?<=\\s#\\s)\\S+");
                    Matcher emojiMatcher = emojiPattern.matcher(line);
                    String emoji = emojiMatcher.find() ? line.substring(emojiMatcher.start(), emojiMatcher.end()) : "";

                    // 找出描述
                    Pattern desPattern = Pattern.compile("(?<=\\s#\\s\\S{1,14}\\s).*");
                    Matcher desMatcher = desPattern.matcher(line);
                    String des = desMatcher.find() ? line.substring(desMatcher.start(), desMatcher.end()) : "";

                    stringBuilder.append(String.format(template, emoji, emoji.length(),/*UnicodeUtils.convertUnicode(emoji)*/codePoints, quality, des));
                }
            }

            System.out.println("Total: " + i);
            stringBuilder.deleteCharAt(stringBuilder.lastIndexOf("\n"));
            FileUtils.writeToFile("./src/main/resources/emoji.md", stringBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
