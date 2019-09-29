package com.andforce.retrofit;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import okhttp3.*;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JsonFormatInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Response response = chain.proceed(request);

        String bodyString;
        MediaType mediaType = MediaType.parse("application/json;charset=uft-8");
        ResponseBody responseBody = response.body();
        if (responseBody != null) {
            bodyString = responseBody.string();

            int count = 0;

            String[] lines = bodyString.split("\n");

            JsonObject jsonObject = new JsonObject();

            JsonArray items = new JsonArray();

            for (String line : lines) {
                if (line.trim().length() == 0) {
                    continue;
                }
                if (!line.startsWith("#")) {
                    count++;

                    //找出CodePoints
                    Pattern utf16Pattern = Pattern.compile("[\\S{4}\\s{1}]+(?=\\s+;)");
                    Matcher utf16Matcher = utf16Pattern.matcher(line);
                    String utf16 = "\\u" + (utf16Matcher.find() ? line.substring(utf16Matcher.start(), utf16Matcher.end()) : "").trim().replace(" ", "\\u");


                    // 找出画质
                    Pattern statusPattern = Pattern.compile("fully-qualified|minimally-qualified|unqualified|component");
                    Matcher statusMatcher = statusPattern.matcher(line);
                    String status = statusMatcher.find() ? line.substring(statusMatcher.start(), statusMatcher.end()) : "";

                    // 找出Emoji
                    Pattern emojiPattern = Pattern.compile("(?<=\\s#\\s)\\S+");
                    Matcher emojiMatcher = emojiPattern.matcher(line);
                    String emoji = emojiMatcher.find() ? line.substring(emojiMatcher.start(), emojiMatcher.end()) : "";

                    // 找出描述
                    Pattern desPattern = Pattern.compile("(?<=\\s#\\s\\S{1,14}\\s).*");
                    Matcher desMatcher = desPattern.matcher(line);
                    String des = desMatcher.find() ? line.substring(desMatcher.start(), desMatcher.end()) : "";

                    JsonObject oneEmoji = new JsonObject();

                    oneEmoji.addProperty("content", emoji);
                    oneEmoji.addProperty("description", des);
                    oneEmoji.addProperty("status", status);
                    oneEmoji.addProperty("utf16", utf16);
                    oneEmoji.addProperty("length", emoji.length());

                    if (items.size() < 5) {
                        items.add(oneEmoji);
                    }

                } else if (line.startsWith("#")) {
                    if (!jsonObject.has("date")) {
                        Matcher matcher = Pattern.compile("(?<=# Date: )\\d{4}-\\d{2}-\\d{2}.*").matcher(line);
                        if (matcher.find()) {
                            jsonObject.addProperty("date", line.substring(matcher.start(), matcher.end()));
                        }
                    }

                    if (!jsonObject.has("version")) {
                        Matcher matcher = Pattern.compile("(?<=# Version: ).*").matcher(line);
                        if (matcher.find()) {
                            jsonObject.addProperty("version", line.substring(matcher.start(), matcher.end()));
                        }
                    }
                }
            }

            jsonObject.addProperty("count", count);
            jsonObject.add("emojis", items);

            return response.newBuilder().body(ResponseBody.create(mediaType, jsonObject.toString())).build();
        } else {
            return response.newBuilder().body(ResponseBody.create(mediaType, "{}")).build();
        }
    }
}
