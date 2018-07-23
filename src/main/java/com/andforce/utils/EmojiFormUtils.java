package com.andforce.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class EmojiFormUtils {

    public static int emojiCount(EmojiForm emojiForm){

        int count = 0;
        List<Group> groups = emojiForm.getGroups();
        for (Group group : groups){
            List<SubGroup> subGroups = group.getSubGroups();
            for (SubGroup subGroup : subGroups){
                List<Emoji> emojis = subGroup.getEmojis();
                count += emojis.size();
            }
        }
        return count;
    }

    public static int printHex(EmojiForm emojiForm){

        int count = 0;
        List<Group> groups = emojiForm.getGroups();
        for (Group group : groups){
            List<SubGroup> subGroups = group.getSubGroups();
            for (SubGroup subGroup : subGroups){
                List<Emoji> emojis = subGroup.getEmojis();
                for (Emoji emoji : emojis){
                    String hex = emoji.getCodePoints().replace(" ", "");
                    Integer x = Integer.parseInt(hex,16);
                    System.out.println("HEX: " + hex + " TEN:"  + x);
                }
            }
        }
        return count;
    }


    public static void writeEmojis2Files(List<Emoji> emojis, String filePath) {

        try {
            FileWriter writer = new FileWriter(filePath);

            for (int i = 0; i < emojis.size() - 1; i++) {
                int j = i + 1;

                String unicode0 = emojis.get(i).getUnicode();
                String unicode1 = emojis.get(j).getUnicode();

                String emoji0 = emojis.get(i).getEmoji();
                String emoji1 = emojis.get(j).getEmoji();
                writer.write(unicode0 + "\t\t");
                writer.write("[" + emoji0 + "]\t\t");
                writer.write(emoji0.length() + "\r\n");

                if (j == emojis.size() - 1) {
                    writer.write(unicode1 + "\t\t[" + emoji0 + "] length:" + emoji0.length());
                }
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
