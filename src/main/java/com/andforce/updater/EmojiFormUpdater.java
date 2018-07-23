package com.andforce.updater;

import com.andforce.utils.FileUtils;
import com.andforce.utils.HttpUtils;

public class EmojiFormUpdater {


    private static final String EMOJI_TEST = "./src/main/resources/emoji-test.txt";
    private static final String EMOJI_LAST_URL = "http://unicode.org/Public/emoji/latest/emoji-test.txt";

    private static String LAST_TEST_TEXT;
    private static String LOCAL_TEXT;

    public static void checkForUpdate() {


        CheckUpdater checkUpdater = new CheckUpdater();
        checkUpdater.start();

        System.out.println("Start Checking ...");

        while (checkUpdater.isAlive()) {
            System.out.print(".");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (LAST_TEST_TEXT == null) {
            System.out.println("Read remote text file error.");
        } else if (LOCAL_TEXT == null) {
            System.out.println("Read local text file error.");
        } else {
            System.out.println("\r\nIt's have new emoji version: " + !LAST_TEST_TEXT.equals(LOCAL_TEXT));
            FileUtils.writeToFile("./src/main/resources/emoji-test.txt", LAST_TEST_TEXT);
        }
    }

    static class CheckUpdater extends Thread {
        @Override
        public void run() {
            super.run();

            LAST_TEST_TEXT = HttpUtils.doGet(EMOJI_LAST_URL);

            LOCAL_TEXT = FileUtils.readString(EMOJI_TEST);
        }
    }
}
