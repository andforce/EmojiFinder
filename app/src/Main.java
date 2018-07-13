import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EmojiForm emojiForm = new EmojiFormParser("./app/src/emoji-test.txt").parse();

        List<Emoji> emojis = new EmojiFormParser("./app/src/emoji-test.txt").parseEmojis();

        String fileName = "./app/src/emoji-reg.txt";
        try {
            FileWriter writer = new FileWriter(fileName);

            List<List<String>> codePoints = new ArrayList<>();

            List<String> toAppend = null;

            for (Emoji emoji : emojis) {
                if (codePoints.isEmpty()){
                    List<String> list = new ArrayList<>();
                    list.add(emoji.getCodePoints());
                    codePoints.add(list);
                } else {
                    for (List<String> list : codePoints){
                        for (String s : list){
                            if (UnicodeUtils.isNeighbor(s, emoji.getCodePoints())){
                                toAppend = list;
                            }
                        }
                    }
                    if (toAppend == null) {
                        List<String> list = new ArrayList<>();
                        list.add(emoji.getCodePoints());
                        codePoints.add(list);
                    } else {
                        toAppend.add(emoji.getCodePoints());
                        toAppend = null;
                    }
                }
            }


            int count = 0;
            for (int i = 0; i < codePoints.size(); i++) {
                List<String> group = codePoints.get(i);
                if (group.size() == 1){
                    writer.write(group.get(0));
                } else {
                    writer.write("[" + group.get(0) + "-" + group.get(group.size() - 1) + "]");
                }
                if (i != codePoints.size() -1){
                    writer.write("|");
                }
            }

            System.out.println("Check Count: >>> " + count);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new Gson();
        String s = gson.toJson(emojiForm);


        int count = EmojiFormUtils.emojiCount(emojiForm);
        System.out.println(">>> " + count + "\uD83D\uDE00 \uD83D\uDE01");

        System.out.println(s);

    }

}
