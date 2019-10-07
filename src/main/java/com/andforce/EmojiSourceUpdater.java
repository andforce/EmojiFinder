package com.andforce;

import com.andforce.retrofit.JsonFormatInterceptor;
import com.andforce.retrofit.managers.InterceptorRetrofitManager;
import com.andforce.retrofit.services.DownloadService;
import com.andforce.updater.EmojiTest;
import com.andforce.utils.FileUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.functions.Consumer;

public class EmojiSourceUpdater {

    public void update() {
        InterceptorRetrofitManager customRetrofitManager = new InterceptorRetrofitManager(
                "http://unicode.org", new JsonFormatInterceptor());

        DownloadService service = customRetrofitManager.create(DownloadService.class);
        service.emoji_test().subscribe(new Consumer<EmojiTest>() {
            @Override
            public void accept(EmojiTest emojiTest) throws Exception {
                Gson mGson = new GsonBuilder().setPrettyPrinting().create();

                String tmp = "package com.andforce;\n" +
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
                        "    public static EmojiSource getInstance() {\n" +
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
                FileUtils.writeToFile("emoji-finder/src/main/java/com/andforce/EmojiSource.java", java);
                System.out.println(String.format("Version:[%s], Date:[%s], Total-Count:[%s], Parse-Count:[%s]",
                        emojiTest.getVersion(), emojiTest.getDate(), emojiTest.getCount(), emojiTest.getEmojis().size()));
            }
        });
    }

}
