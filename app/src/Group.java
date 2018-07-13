import java.util.List;

public class Group {

    private String mType;

    private List<SubGroup> mSubGroups;

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public List<SubGroup> getSubGroups() {
        return mSubGroups;
    }

    public void setSubGroups(List<SubGroup> subGroups) {
        mSubGroups = subGroups;
    }
}
