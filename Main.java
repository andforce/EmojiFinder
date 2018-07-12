package com.andforce;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static String TEMP_DIR = "/Users/diyuanwang/github/Sort/src/com/andforce/emoji-test.txt";

    public static void main(String[] args) {
        File file = new File(TEMP_DIR);

        BufferedReader bufferedReader = null;

        EmojiForm emojiForm = new EmojiForm();
        List<Group> groups = new ArrayList<>();

        Group group = null;
        List<SubGroup> subGroups = null;
        SubGroup subGroup;
        List<Emoji> emojis = null;

        int i = 0;
        try {
            bufferedReader = new BufferedReader(new FileReader(TEMP_DIR));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {

                if (i < 3) {
                    if (line.startsWith("# Date: ")) {
                        line = line.replace("# Date: ", "").replace(" GMT", "");
                        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd, HH:mm:ss");
                        try {
                            Date date = formatter.parse(line);
                            emojiForm.setDate(date);
                            System.out.println(date);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        i++;
                    } else if (line.startsWith("# Version: ")) {
                        String version = line.replace("# Version: ", "");
                        emojiForm.setVersion(version);
                        i++;
                    } else if (line.startsWith("# For documentation and usage, see ")) {
                        String doc = line.replace("# For documentation and usage, see ", "");
                        emojiForm.setDocumentation(doc);
                        i++;
                    }
                } else {

                    if (line.startsWith("# group: ")) {

                        group = new Group();
                        String type = line.replace("# group: ", "");
                        group.setType(type);
                        groups.add(group);

                        subGroups = new ArrayList<>();
                        group.setSubGroups(subGroups);

                    } else if (line.startsWith("# subgroup: ")) {
                        subGroup = new SubGroup();

                        emojis = new ArrayList<>();

                        String type = line.replace("# subgroup: ", "");
                        subGroup.setType(type);
                        subGroup.setEmojis(emojis);

                        subGroups.add(subGroup);
                    } else if (!line.equals("") && !line.startsWith("#")) {
                        Emoji emoji = new Emoji();
                        String[] tmps = line.split(" ; ");
                        String codePoints = "\\u" + tmps[0].trim().replace(" " ,"\\u");

                        String[] tmps2 = tmps[1].split(" # ");
                        String status = tmps2[0].trim();

                        String tmps3 = tmps2[1];

                        int spaceIndex = tmps3.indexOf(" ");

                        String emojiStr = tmps3.substring(0, spaceIndex);

                        String name = tmps3.substring(spaceIndex, tmps3.length()).trim();

                        emoji.setCodePoints(codePoints);
                        emoji.setStatus(status);
                        emoji.setEmoji(emojiStr);
                        emoji.setName(name);
                        emojis.add(emoji);

                    } else if (line.startsWith("#EOF")) {
                        emojiForm.setGroups(groups);
                        break;
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        int count = EmojiFormUtils.emojiCount(emojiForm);
        System.out.println(">>> " + count);

        EmojiFormUtils.printHex(emojiForm);

    }

}
