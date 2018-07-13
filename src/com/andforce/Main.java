package com.andforce;

public class Main {

    public static String emoji_test = "./emoji-test.txt";

    public static void main(String[] args) {

        EmojiForm emojiForm = new EmojiFormParser(emoji_test).parse();


        int count = EmojiFormUtils.emojiCount(emojiForm);
        System.out.println(">>> " + count);

    }

}
