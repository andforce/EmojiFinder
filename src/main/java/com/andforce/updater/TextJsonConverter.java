package com.andforce.updater;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextJsonConverter {

    public String convert(String testTxt) {


        EmojiTest emojiTest = new EmojiTest();

        int count = 0;
        ArrayList<EmojiItem> emojiItems = new ArrayList<>();

        String[] lines = testTxt.split("\n");

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

                EmojiItem emojiItem = new EmojiItem();
                emojiItem.setContent(emoji);
                emojiItem.setDescription(des);
                emojiItem.setStatus(status);
                emojiItem.setUtf16(utf16);
                emojiItem.setLength(emoji.length());

                if (emojiItems.size() < 5) {
                    emojiItems.add(emojiItem);
                }

            } else if (line.startsWith("#")) {
                if (emojiTest.getDate() == null) {
                    Matcher matcher = Pattern.compile("(?<=# Date: )\\d{4}-\\d{2}-\\d{2}.*").matcher(line);
                    if (matcher.find()) {
                        emojiTest.setDate(line.substring(matcher.start(), matcher.end()));
                    }
                }

                if (emojiTest.getVersion() == null) {
                    Matcher matcher = Pattern.compile("(?<=# Version: ).*").matcher(line);
                    if (matcher.find()) {
                        emojiTest.setVersion(line.substring(matcher.start(), matcher.end()));
                    }
                }
            }
        }
        emojiTest.setCount(count);
        emojiTest.setEmojis(emojiItems);

        return new Gson().toJson(emojiTest);
    }
}
