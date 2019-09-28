package com.andforce.updater;

import java.util.ArrayList;

public class EmojiTest {
    private String date;
    private String version;
    private int count;

    private ArrayList<EmojiItem> emojis;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<EmojiItem> getEmojis() {
        return emojis;
    }

    public void setEmojis(ArrayList<EmojiItem> emojis) {
        this.emojis = emojis;
    }
}
