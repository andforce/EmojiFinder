import java.util.List;

public class Group {

    private String type;

    private List<SubGroup> subGroups;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<SubGroup> getSubGroups() {
        return subGroups;
    }

    public void setSubGroups(List<SubGroup> subGroups) {
        this.subGroups = subGroups;
    }
}
