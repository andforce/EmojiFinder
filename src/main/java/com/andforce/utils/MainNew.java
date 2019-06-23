package com.andforce.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class MainNew {
    public static void main(String[] args) {

        String mkTable = "| Emoji | Length |  UTF-16 |Summary|\n" +
                         "| :---: | :----: | ------ | ------ |\n";
        String template = "| [%s] | %d | %s | %s |\n";

        File file = new File("./src/main/resources/emoji-test.txt");

        BufferedReader bufferedReader = null;

        StringBuilder stringBuilder = new StringBuilder(mkTable);
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().length() != 0 && !line.startsWith("#")) {
                    String emojiStart = line.substring(line.indexOf(" # ") + " # ".length());
                    String emoji = emojiStart.substring(0, emojiStart.indexOf(" ")).trim();
                    stringBuilder.append(String.format(template, emoji, emoji.length(),UnicodeUtils.convertUnicode(emoji),  "Summary"));
                }
            }
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
