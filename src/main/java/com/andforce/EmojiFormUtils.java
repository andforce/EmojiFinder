package com.andforce;

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
}
