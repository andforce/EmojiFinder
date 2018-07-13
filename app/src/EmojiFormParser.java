import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class EmojiFormParser {

    private String mEmojiFile;

    public EmojiFormParser(String emojiFile){
        mEmojiFile = emojiFile;
    }

    public EmojiForm parse(){
        File file = new File(mEmojiFile);

        BufferedReader bufferedReader = null;

        EmojiForm emojiForm = new EmojiForm();
        List<Group> groups = new ArrayList<>();

        Group group = null;
        List<SubGroup> subGroups = null;
        SubGroup subGroup;
        List<Emoji> emojis = null;

        int i = 0;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
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
                        emojiForm.setDocument(doc);
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
                        subGroup.setSubType(type);
                        subGroup.setEmojis(emojis);

                        subGroups.add(subGroup);
                    } else if (!line.equals("") && !line.startsWith("#")) {
                        parseEmoji(emojis, line);

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
        return emojiForm;
    }

    public List<Emoji> parseEmojis(){
        File file = new File(mEmojiFile);

        BufferedReader bufferedReader = null;

        List<Emoji> emojis = new ArrayList<>();

        int i = 0;
        try {
            bufferedReader = new BufferedReader(new FileReader(file));
            String line = null;
            while ((line = bufferedReader.readLine()) != null) {

                if (!line.equals("") && !line.startsWith("#")) {
                    parseEmoji(emojis, line);

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
        emojis.sort(new Comparator<Emoji>() {
            @Override
            public int compare(Emoji emoji, Emoji t1) {
                String codePoints0 = emoji.getCodePoints();
                String codePoints1 = t1.getCodePoints();


                if (codePoints0.equals(codePoints1)){
                    return 0;
                } else if (codePoints0.length() > codePoints1.length()) {
                    return 1;
                } else if (codePoints0.length() < codePoints1.length()) {
                    return -1;
                } else {

                    int len = codePoints0.length();

                    for (int i = 0; i < len; i++) {
                        char c0 = codePoints0.charAt(i);
                        char c1 = codePoints1.charAt(i);
                        if (c0 > c1) {
                            return 1;
                        } else if (c0 < c1) {
                            return -1;
                        }
                    }
                    return 0;
                }
            }
        });
        return emojis;
    }

    private void parseEmoji(List<Emoji> emojis, String line) {
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
    }
}
