package com.andforce.utils;

import com.andforce.EmojiFinder;
import com.andforce.beans.EmojiBean;
import com.andforce.updater.EmojiFormUpdater;

import java.util.List;

public class Main {


    private static final String EMOJI_TEST = "./src/main/resources/emoji-test.txt";

    private static final String EMOJI_LIST_ORG_SORT = "./src/main/resources/emoji-test_not_sort.txt";

    public static void main(String[] args) {

//        EmojiFormUpdater.checkForUpdate();
//
//        List<Emoji> emojis = new EmojiFormParser(EMOJI_TEST).parseEmojis(false);
//
//        EmojiFormUtils.writeEmojis2Files(emojis, EMOJI_LIST_ORG_SORT);
//
        String testLocal = FileUtils.readString(EMOJI_LIST_ORG_SORT);

        List<EmojiBean> regexEmojis = EmojiFinder.find(testLocal);

        System.out.println("Find emoji count is: >>> " + regexEmojis.size());

    }

}
