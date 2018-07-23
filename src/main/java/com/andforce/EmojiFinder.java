package com.andforce;

import com.andforce.beans.EmojiBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmojiFinder {

    private static final boolean DEBUG = false;

    /**
     * @param src 需要查找的字符串
     * @param regexs 自定义的正则表达式
     * @return 查找到的结果，如果没有返回空的List，不会返回null
     */
    public static List<EmojiBean> find(String src, String[] regexs) {
        List<EmojiBean> list = new ArrayList<>();

        if (src == null || src.trim().equals("")) {
            return list;
        }

        // 先查看所要查找的src是否只是由中文和英文，以及数字组成的，
        // 如果是的话，就不需要再进行emoji的查找了
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
        if (pattern.matcher(src).find()) {
            return list;
        }

        long startTime = System.currentTimeMillis();

        for (String aRegex : regexs) {
            src = find(src, aRegex, list);
        }

        if (DEBUG) {
            System.out.println("ALL Time Usage: " + (System.currentTimeMillis() - startTime));
        }
        return list;
    }

    public static List<EmojiBean> find(String src) {
        List<EmojiBean> list = new ArrayList<>();

        if (src == null || src.trim().equals("")) {
            return list;
        }

        // 先查看所要查找的src是否只是由中文和英文，以及数字组成的，
        // 如果是的话，就不需要再进行emoji的查找了
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_\\u4e00-\\u9fa5]+$");
        if (pattern.matcher(src).find()) {
            return list;
        }

        String[] specialForIndex = {
                "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC65\uDB40\uDC6E\uDB40\uDC67\uDB40\uDC7F",
                "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC73\uDB40\uDC63\uDB40\uDC74\uDB40\uDC7F",
                "\uD83C\uDFF4\uDB40\uDC67\uDB40\uDC62\uDB40\uDC73\uDB40\uDC63\uDB40\uDC74\uDB40\uDC7F",
                "\uD83D\uDC68\u200D\u2764\uFE0F\u200D\uD83D\uDC8B\u200D\uD83D\uDC68",
                "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66",
                "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66",
                "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC67",
                "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66",
                "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66",
                "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67",
                "\uD83D\uDC69\u200D\u2764\uFE0F\u200D\uD83D\uDC8B\u200D\uD83D\uDC68",
                "\uD83D\uDC69\u200D\u2764\uFE0F\u200D\uD83D\uDC8B\u200D\uD83D\uDC69",
                "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66",
                "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66",
                "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67",
                "\uD83D\uDC68\u200D\u2764\u200D\uD83D\uDC8B\u200D\uD83D\uDC68",
                "\uD83D\uDC69\u200D\u2764\u200D\uD83D\uDC8B\u200D\uD83D\uDC68",
                "\uD83D\uDC69\u200D\u2764\u200D\uD83D\uDC8B\u200D\uD83D\uDC69",
                "\uD83D\uDC68\u200D\u2764\uFE0F\u200D\uD83D\uDC68",
                "\uD83D\uDC68\u200D\uD83D\uDC66\u200D\uD83D\uDC66",
                "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC66",
                "\uD83D\uDC68\u200D\uD83D\uDC67\u200D\uD83D\uDC67",
                "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC66",
                "\uD83D\uDC68\u200D\uD83D\uDC68\u200D\uD83D\uDC67",
                "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC66",
                "\uD83D\uDC68\u200D\uD83D\uDC69\u200D\uD83D\uDC67",
                "\uD83D\uDC69\u200D\u2764\uFE0F\u200D\uD83D\uDC68",
                "\uD83D\uDC69\u200D\u2764\uFE0F\u200D\uD83D\uDC69",
                "\uD83D\uDC69\u200D\uD83D\uDC66\u200D\uD83D\uDC66",
                "\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC66",
                "\uD83D\uDC69\u200D\uD83D\uDC67\u200D\uD83D\uDC67",
                "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC66",
                "\uD83D\uDC69\u200D\uD83D\uDC69\u200D\uD83D\uDC67",
                "\uD83D\uDC68\u200D\u2764\u200D\uD83D\uDC68",
                "\uD83D\uDC69\u200D\u2764\u200D\uD83D\uDC68",
                "\uD83D\uDC69\u200D\u2764\u200D\uD83D\uDC69"
        };

        if (src.length() >= 7) {
            StringBuilder stringBuilder = new StringBuilder(src);
            long findLong = System.currentTimeMillis();
            int findCount = 0;
            for (String toFind : specialForIndex) {
                int index = stringBuilder.indexOf(toFind);
                if (index == -1) {
                    continue;
                }
                do {
                    stringBuilder.replace(index, index + toFind.length(), generateEmptyString(toFind.length()));

                    EmojiBean regexEmoji = new EmojiBean();

                    int end = index + toFind.length();

                    String emoji = src.substring(index, end);

                    regexEmoji.setStart(index);
                    regexEmoji.setEnd(end);
                    regexEmoji.setEmoji(emoji);

                    list.add(regexEmoji);

                    findCount++;
                } while ((index = stringBuilder.indexOf(toFind, index + toFind.length())) != -1);
            }

            src = stringBuilder.toString();

            if (DEBUG) {
                System.out.println("Index Find Time Usage: " + (System.currentTimeMillis() - findLong) + "\t\tfindCount:" + findCount);
            }
        }


        //通过正则表达式查找
        // 目前没有找到任何一个正则表达式，能一次查找出所有的emoji。
        // 因此我尝试根据emoji的长度写了下面的正则，可能还需要优化
        // 规则是先查找较长的emoji，原因是长的emoji可能包含短的emoji
        String[] regexArr = {
                "[\\uD83C\\uDFC3-\\uD83E\\uDDDD][\\uD83C\\uDFFB-\\uD83C\\uDFFF|\\uFE0F]\\u200D[\\u2640-\\u2708|\\uD83C\\uDF3E-\\uD83E\\uDDB3]\\uFE0F?",
                "\\u26F9?[\\uD83C\\uDFC3-\\uD83E\\uDDDD][\\uD83C\\uDFFB-\\uD83C\\uDFFF]?\\uFE0F?\\u200D[\\u2640-\\u2708|\\uD83C\\uDF08-\\uD83D\\uDDE8]\\uFE0F?",
                "[\\uD83C\\uDFF3-\\uD83D\\uDC69|\\u26F9]\\uFE0F?\\u200D[\\uD83C\\uDF08-\\uD83E\\uDDB3|\\u2620-\\u2708]\\uFE0F?",
                "[\\uD83C\\uDFC3-\\uD83E\\uDDDF]\\uFE0F?\\u200D[\\u2640-\\u2642]\\uFE0F?",
                "[\\uD83C\\uDF85-\\uD83E\\uDDDF][\\uD83C\\uDFFB-\\uD83C\\uDFFF|\\u200D[\\u2620-\\u2642]]",
                "[\\uD83C\\uDDE6-\\uD83C\\uDDFF][\\uD83C\\uDDE6-\\uD83C\\uDDFF]",
                "[\\u261D-\\u270D]?[\\uD83C\\uDC04-\\uD83E\\uDEF9]\\uFE0F?",
                "[\\u0023-\\u0039]\\uFE0F?\\u20E3|\\u00A9\\uFE0F?|\\u00AE\\uFE0F?",
                "[\\u203C-\\u3299]\\uFE0F?"
        };


        long startTime = System.currentTimeMillis();

        for (String aRegex : regexArr) {
            src = find(src, aRegex, list);
        }

        if (DEBUG) {
            System.out.println("ALL Time Usage: " + (System.currentTimeMillis() - startTime));
        }

        //FileUtils.writeToFile("./src/main/resources/emoji-removed.txt", src);

        return list;
    }

    private static String find(String src, String patternStr, List<EmojiBean> list) {

        StringBuffer stringBuffer = null;

        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(src);

        long startTime = System.currentTimeMillis();

        int count = 0;
        while (matcher.find()) {
            EmojiBean regexEmoji = new EmojiBean();

            int start = matcher.start();
            int end = matcher.end();

            String emoji = src.substring(start, end);

            regexEmoji.setStart(start);
            regexEmoji.setEnd(end);
            regexEmoji.setEmoji(emoji);

            if (DEBUG && list.contains(regexEmoji)) {
                System.out.println("Error: " + regexEmoji);
            }

            list.add(regexEmoji);
            count++;

            if (stringBuffer == null) {
                stringBuffer = new StringBuffer(src.length());
            }
            matcher.appendReplacement(stringBuffer, generateEmptyString(end - start));
        }
        if (stringBuffer != null) {
            matcher.appendTail(stringBuffer);
            if (DEBUG) {
                System.out.println("Time Usage: " + (System.currentTimeMillis() - startTime) + "\t\tfindCount:" + count);
            }
            return stringBuffer.toString();
        } else {
            if (DEBUG) {
                System.out.println("Time Usage: " + (System.currentTimeMillis() - startTime) + "\t\tfindCount:" + count);
            }
            return src;
        }
    }

    private static String generateEmptyString(int len) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            stringBuilder.append(" ");
        }
        return stringBuilder.toString();
    }
}
