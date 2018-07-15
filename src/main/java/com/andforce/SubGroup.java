package com.andforce;

import java.util.List;

public class SubGroup {

    private String subType;

    private List<Emoji> emojis;

    public String getSubType() {
        return subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }


    public List<Emoji> getEmojis() {
        return emojis;
    }

    public void setEmojis(List<Emoji> emojis) {
        this.emojis = emojis;
    }
}
