import java.util.List;

public class SubGroup {

    private String mSubType;

    private List<Emoji> mEmojis;

    public String getSubType() {
        return mSubType;
    }

    public void setSubType(String subType) {
        mSubType = subType;
    }


    public List<Emoji> getEmojis() {
        return mEmojis;
    }

    public void setEmojis(List<Emoji> emojis) {
        mEmojis = emojis;
    }
}
