import java.util.List;

public class SubGroup {

    private String mType;

    private List<Emoji> mEmojis;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }


    public List<Emoji> getEmojis() {
        return mEmojis;
    }

    public void setEmojis(List<Emoji> emojis) {
        mEmojis = emojis;
    }
}
