package com.andforce;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

public class Main {


    private static String addSpace(String emoji){
        int emojiLen = emoji.length();


        StringBuilder t = new StringBuilder("  ");
        for (int i = 0; i < 8 - emojiLen; i++) {
            t.append("    ");
        }
        return t.toString();
    }
    public static void main(String[] args) {


        String debug = "aaaa\t\t        abbbb";

        debug = debug.replaceFirst("a","2");
        System.out.println("------->" + "✅" + UnicodeUtils.convertUnicode("©️"));

        if (false){
            return;
        }

        Pattern pattern = Pattern.compile("");

//        String s = new EmojiFormParser("./src/main/resources/emoji-test.txt").parseEmojiString();
//
//        System.out.println("Find String length : >>> " + s.length());
//
//        List<RegexEmoji> regexEmojis = RegexUtils.findEmoji(s, null);
//
//        System.out.println("Find emoji count is: >>> " + regexEmojis.size());


//        new EmojiFormParser("./src/main/resources/emoji-test.txt").parse();

        List<Emoji> emojis = new EmojiFormParser("./src/main/resources/emoji-test.txt").parseEmojis(false);







        String emojiListFile = "./src/main/resources/emoji-list_org_sort.txt";
        try {
            FileWriter writer = new FileWriter(emojiListFile);

            for (int i = 0; i < emojis.size() - 1; i++) {

                String emoji = emojis.get(i).getEmoji();
                writer.write(emojis.get(i).getUnicode() + "\t\t");
                writer.write("[" + emoji + "]\t\t");
                writer.write(emoji.length() + "\r\n");

            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }



        List<String> regex = new ArrayList<>();



        String fileName = "./src/main/resources/emoji-reg.txt";
        boolean DEBUG = true;
        try {

            List<List<Emoji>> emojiList = new EmojiFormParser().classifyEmojisByLength(emojis);

            FileWriter writer = new FileWriter(fileName);
            StringBuilder stringBuilder = new StringBuilder();

            int count = 0;
            for (int i = 0; i < emojiList.size(); i++) {
                List<Emoji> group = emojiList.get(i);
                System.out.println("GROUP:" + (i + 1) + "\t\tLENGTH:\t" + group.get(0).getEmoji().length() + "\t\tCount:\t" + group.size());

                count += group.size();

                List<List<Emoji>> reGroup = new EmojiFormParser().classifyEmojisByNeighbor(group);

                for (List<Emoji> childList: reGroup){
                    if (childList.size() == 1){
                        stringBuilder.append(childList.get(0).getUnicode()).append("|");
                    } else {

                        if (DEBUG){
                            for (Emoji emoji:childList) {
                                stringBuilder.append(emoji.getUnicode()).append("|");
                            }
                        } else {
                            Emoji fst = childList.get(0);
                            Emoji lst = childList.get(childList.size() - 1);

                            StringBuilder append = new StringBuilder();

                            StringBuilder first = null;
                            StringBuilder last = null;

                            boolean isHaveDiff = false;

                            int emojiLen = fst.getEmoji().length();

                            for (int j = 0; j < emojiLen; j++) {

                                char charInFst = fst.getEmoji().charAt(j);
                                char charInLst = lst.getEmoji().charAt(j);
                                if (charInFst == charInLst) {
                                    if (append != null) {
                                        append.append(UnicodeUtils.convertUnicode(charInFst));
                                    } else {
                                        first.append(UnicodeUtils.convertUnicode(charInFst));
                                        last.append(UnicodeUtils.convertUnicode(charInFst));
                                    }
                                } else {
                                    if (!isHaveDiff && (j == 0 || j == emojiLen -1) && append != null) {
                                        append.append("[").append(UnicodeUtils.convertUnicode((char) Math.min(charInFst, charInLst))).append("-").append(UnicodeUtils.convertUnicode((char) Math.max(charInFst, charInLst))).append("]");
                                        isHaveDiff = true;
                                    } else {
                                        if (first == null){
                                            first = new StringBuilder();
                                            first.append(append.toString());
                                        }

                                        if (last == null){
                                            last = new StringBuilder();
                                            last.append(append.toString());

                                            append = null;
                                        }
                                        first.append(UnicodeUtils.convertUnicode(charInFst));
                                        last.append(UnicodeUtils.convertUnicode(charInLst));

                                    }
                                }
                            }

                            if (first != null){
                                first.append('|');
                            }

                            if (last != null){
                                last.append('|');
                            }
                            if (append != null){
                                append.append("|");
                                stringBuilder.append(append.toString());
//                                stringBuilder.append("|");
                            } else {
//                                first.append("|");
//                                last.append("|");

                                stringBuilder.append(first.toString());
                                stringBuilder.append(last.toString());
                            }

                        }
                    }
                }
                if (stringBuilder.toString().endsWith("|")){
                    stringBuilder.deleteCharAt(stringBuilder.length() - 1);
                }

                regex.add(stringBuilder.toString());

                stringBuilder.append("\r\n\r\n");
                writer.write(group.get(0).getEmoji().length() + " -> " + group.size() + "\t" + stringBuilder.toString());
                stringBuilder = new StringBuilder();
            }


            System.out.println("Emoji count is: >>> " + count);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String s = new EmojiFormParser("./src/main/resources/emoji-test.txt").parseEmojiString();

        System.out.println("Find String length : >>> " + s.length());

        Collections.reverse(regex);

        List<RegexEmoji> regexEmojis = RegexUtils.findEmoji(s, regex);

        System.out.println("Find emoji count is: >>> " + regexEmojis.size());


    }

}
