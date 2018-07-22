package com.andforce.utils;

import com.andforce.EmojiFinder;
import com.andforce.beans.EmojiBean;
import com.andforce.utils.EmojiFormParser;

import java.util.List;

public class FindMain {


    public static void main(String[] args) {


        String s = new EmojiFormParser("./src/main/resources/emoji-list.txt").parseEmojiString();

        System.out.println("Find String length : >>> " + s.length());

        List<EmojiBean> regexEmojis = EmojiFinder.find(s);

        System.out.println("Find emoji count is: >>> " + regexEmojis.size());


    }

}
