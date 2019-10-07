package com.andforce;

import com.andforce.beans.EmojiBean;

import java.util.ArrayList;

public class Main {


    public static void main(String[] args) {

        EmojiSourceUpdater updater = new EmojiSourceUpdater();
        updater.update();


        //Test String from
        //http://unicode.org/Public/emoji/latest/emoji-test.txt

        String toFind = "# subgroup: person-activity" +
                "1F486                                      ; fully-qualified     # 💆 person getting massage" +
                "1F486 1F3FB                                ; fully-qualified     # 💆🏻 person getting massage: light skin tone" +
                "1F486 1F3FC                                ; fully-qualified     # 💆🏼 person getting massage: medium-light skin tone" +
                "1F486 1F3FD                                ; fully-qualified     # 💆🏽 person getting massage: medium skin tone" +
                "1F486 1F3FE                                ; fully-qualified     # 💆🏾 person getting massage: medium-dark skin tone" +
                "1F486 1F3FF                                ; fully-qualified     # 💆🏿 person getting massage: dark skin tone " +
                "1F486 200D 2642 FE0F                       ; fully-qualified     # 💆‍♂️ man getting massage" +
                "1F486 200D 2642                            ; minimally-qualified # 💆‍♂ man getting massage " +
                "1F486 1F3FB 200D 2642 FE0F                 ; fully-qualified     # 💆🏻‍♂️ man getting massage: light skin tone" +
                "1F486 1F3FB 200D 2642                      ; minimally-qualified # 💆🏻‍♂ man getting massage: light skin tone " +
                "1F486 1F3FC 200D 2642 FE0F                 ; fully-qualified     # 💆🏼‍♂️ man getting massage: medium-light skin tone" +
                "1F486 1F3FC 200D 2642                      ; minimally-qualified # 💆🏼‍♂ man getting massage: medium-light skin tone" +
                "1F486 1F3FD 200D 2642 FE0F                 ; fully-qualified     # 💆🏽‍♂️ man getting massage: medium skin tone" +
                "1F486 1F3FD 200D 2642                      ; minimally-qualified # 💆🏽‍♂ man getting massage: medium skin tone" +
                "1F486 1F3FE 200D 2642 FE0F                 ; fully-qualified     # 💆🏾‍♂️ man getting massage: medium-dark skin tone" +
                "1F486 1F3FE 200D 2642                      ; minimally-qualified # 💆🏾‍♂ man getting massage: medium-dark skin tone " +
                "1F486 1F3FF 200D 2642 FE0F                 ; fully-qualified     # 💆🏿‍♂️ man getting massage: dark skin tone " +
                "1F486 1F3FF 200D 2642                      ; minimally-qualified # 💆🏿‍♂ man getting massage: dark skin tone";

        long start = System.currentTimeMillis();

        ArrayList<EmojiBean> findResult = EmojiFinder.getInstance().find(toFind);

        System.out.println("Use Time: " + (System.currentTimeMillis() - start) + ", Find Count : " + findResult.size());
        System.out.println("Find Result : " + findResult);

    }

}
