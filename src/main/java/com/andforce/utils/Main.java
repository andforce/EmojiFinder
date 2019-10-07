package com.andforce.utils;

import com.andforce.EmojiFinder;
import com.andforce.EmojiSource;
import com.andforce.beans.EmojiBean;
import com.andforce.retrofit.JsonFormatInterceptor;
import com.andforce.retrofit.managers.InterceptorRetrofitManager;
import com.andforce.retrofit.services.DownloadService;
import com.andforce.updater.EmojiTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.functions.Consumer;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {

        InterceptorRetrofitManager customRetrofitManager = new InterceptorRetrofitManager(
                "http://unicode.org", new JsonFormatInterceptor());

        DownloadService service = customRetrofitManager.create(DownloadService.class);
        service.emoji_test().subscribe(new Consumer<EmojiTest>() {
            @Override
            public void accept(EmojiTest emojiTest) throws Exception {
                Gson mGson = new GsonBuilder().setPrettyPrinting().create();

                String tmp = "package com.andforce.utils;\n" +
                        "\n" +
                        "import java.util.HashSet;\n" +
                        "import java.util.Set;\n" +
                        "\n" +
                        "public class EmojiSource {\n" +
                        "\n" +
                        "    private static EmojiSource sEmojiSource = new EmojiSource();\n" +
                        "\n" +
                        "    private EmojiSource() {\n" +
                        "        super();\n" +
                        "    }\n" +
                        "\n" +
                        "    private static final EmojiSource getInstance() {\n" +
                        "        return sEmojiSource;\n" +
                        "    }\n" +
                        "\n" +
                        "    private Set<String> mEmojiSet = new HashSet<String>() {{\n" +
                        "%s" +
                        "    }};\n" +
                        "\n" +
                        "    public Set<String> getEmojiSet() {\n" +
                        "        return mEmojiSet;\n" +
                        "    }\n" +
                        "}";
                String add = "        add(\"%s\");\n";
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < emojiTest.getEmojis().size(); i++) {
                    String emoji = emojiTest.getEmojis().get(i).getContent();
                    String addEmoji = String.format(add, emoji);
                    stringBuilder.append(addEmoji);
                }

                String java = String.format(tmp, stringBuilder.toString());
                FileUtils.writeToFile("src/main/java/com/andforce/EmojiSource.java", java);
                System.out.println(emojiTest.getEmojis().size());
            }
        });

        long start = System.currentTimeMillis();
        EmojiFinder emojiFinder = new EmojiFinder(EmojiSource.getInstance().getEmojiSet());
        ArrayList<EmojiBean> findResult = emojiFinder.find(FileUtils.readString("src/main/resources/emoji-test.txt"));

        System.out.println("Use Time: " + (System.currentTimeMillis() - start) + "Find Count : " + findResult.size());
        System.out.println("Find Result : " + findResult);

    }

}
