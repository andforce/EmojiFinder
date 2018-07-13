import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        EmojiForm emojiForm = new EmojiFormParser("./app/src/emoji-test.txt").parse();


        List<Emoji> emojis = new EmojiFormParser("./app/src/emoji-test.txt").parseEmojis();

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

        String fileName = "./app/src/emoji-sort.txt";
        try {
            //使用这个构造函数时，如果存在kuka.txt文件，
            //则先把这个文件给删除掉，然后创建新的kuka.txt
            FileWriter writer = new FileWriter(fileName);

            for (Emoji emoji : emojis){
                writer.write(emoji.getCodePoints() + "\r\n");
            }

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Gson gson = new Gson();
        String s = gson.toJson(emojiForm);


        int count = EmojiFormUtils.emojiCount(emojiForm);
        System.out.println(">>> " + count);

        System.out.println(s);

    }

}
