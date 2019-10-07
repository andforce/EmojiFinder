package com.andforce;

import com.andforce.beans.EmojiBean;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {

        EmojiSourceUpdater updater = new EmojiSourceUpdater();
        updater.update();

        //Test String from
        //http://unicode.org/Public/emoji/latest/emoji-test.txt
        String toFind = "# subgroup: face-concerned\n" +
                "1F615                                      ; fully-qualified     # \uD83D\uDE15 confused face\n" +
                "1F61F                                      ; fully-qualified     # \uD83D\uDE1F worried face\n" +
                "1F641                                      ; fully-qualified     # \uD83D\uDE41 slightly frowning face\n" +
                "2639 FE0F                                  ; fully-qualified     # ☹️ frowning face\n" +
                "2639                                       ; fully-qualified     # ☹ frowning face\n" +
                "1F62E                                      ; fully-qualified     # \uD83D\uDE2E face with open mouth\n" +
                "1F62F                                      ; fully-qualified     # \uD83D\uDE2F hushed face\n" +
                "1F632                                      ; fully-qualified     # \uD83D\uDE32 astonished face\n" +
                "1F633                                      ; fully-qualified     # \uD83D\uDE33 flushed face\n" +
                "1F97A                                      ; fully-qualified     # \uD83E\uDD7A pleading face\n" +
                "1F626                                      ; fully-qualified     # \uD83D\uDE26 frowning face with open mouth\n" +
                "1F627                                      ; fully-qualified     # \uD83D\uDE27 anguished face\n" +
                "1F628                                      ; fully-qualified     # \uD83D\uDE28 fearful face\n" +
                "1F630                                      ; fully-qualified     # \uD83D\uDE30 anxious face with sweat\n" +
                "1F625                                      ; fully-qualified     # \uD83D\uDE25 sad but relieved face\n" +
                "1F622                                      ; fully-qualified     # \uD83D\uDE22 crying face\n" +
                "1F62D                                      ; fully-qualified     # \uD83D\uDE2D loudly crying face\n" +
                "1F631                                      ; fully-qualified     # \uD83D\uDE31 face screaming in fear\n" +
                "1F616                                      ; fully-qualified     # \uD83D\uDE16 confounded face\n" +
                "1F623                                      ; fully-qualified     # \uD83D\uDE23 persevering face\n" +
                "1F61E                                      ; fully-qualified     # \uD83D\uDE1E disappointed face\n" +
                "1F613                                      ; fully-qualified     # \uD83D\uDE13 downcast face with sweat\n" +
                "1F629                                      ; fully-qualified     # \uD83D\uDE29 weary face\n" +
                "1F62B                                      ; fully-qualified     # \uD83D\uDE2B tired face\n" +
                "1F971                                      ; fully-qualified     # \uD83E\uDD71 yawning face";

        long start = System.currentTimeMillis();

        ArrayList<EmojiBean> findResult = EmojiFinder.getInstance().find(toFind);

        System.out.println("Use Time: " + (System.currentTimeMillis() - start) + ", Find Count : " + findResult.size());
        System.out.println("Find Result : " + findResult);

    }

}
