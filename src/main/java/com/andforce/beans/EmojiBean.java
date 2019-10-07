package com.andforce.beans;

import com.andforce.utils.UnicodeUtils;

public class EmojiBean {
    private int start = -1;
    private int end = -1;

    private String emoji;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getEmoji() {
        return emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof EmojiBean && ((EmojiBean) obj).emoji.equals(emoji);
    }

    @Override
    public String toString() {
        return emoji + ":[" + start + "-" + end + "]-" + UnicodeUtils.convertUnicode(emoji);
    }
}
