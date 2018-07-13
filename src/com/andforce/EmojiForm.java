package com.andforce;

import java.util.Date;
import java.util.List;

public class EmojiForm {
    private Date mDate;

    private String mVersion;

    private String mDocumentation;

    private List<Group> mGroups;

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public String getVersion() {
        return mVersion;
    }

    public void setVersion(String version) {
        mVersion = version;
    }

    public String getDocumentation() {
        return mDocumentation;
    }

    public void setDocumentation(String documentation) {
        mDocumentation = documentation;
    }

    public List<Group> getGroups() {
        return mGroups;
    }

    public void setGroups(List<Group> groups) {
        mGroups = groups;
    }
}
