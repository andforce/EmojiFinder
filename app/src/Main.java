import com.google.gson.Gson;

import java.io.File;

public class Main {

    public static String emoji_test = "./app/src/emoji-test.txt";

    public static void main(String[] args) {

//        File file = new File("./");
//        File[] files = file.listFiles();

        EmojiForm emojiForm = new EmojiFormParser(emoji_test).parse();


        Gson gson = new Gson();
        String s = gson.toJson(emojiForm);


        int count = EmojiFormUtils.emojiCount(emojiForm);
        System.out.println(">>> " + count);

        System.out.println(">>> " + s);

    }

}
