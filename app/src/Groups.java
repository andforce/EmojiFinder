package com.andforce;

import org.json.*;
import java.util.ArrayList;

public class Groups {
	
    private String type;
    private ArrayList<SubGroups> subGroups;
    
    
	public Groups () {
		
	}	
        
    public Groups (JSONObject json) {
    
        this.type = json.optString("type");

        this.subGroups = new ArrayList<SubGroups>();
        JSONArray arraySubGroups = json.optJSONArray("subGroups");
        if (null != arraySubGroups) {
            int subGroupsLength = arraySubGroups.length();
            for (int i = 0; i < subGroupsLength; i++) {
                JSONObject item = arraySubGroups.optJSONObject(i);
                if (null != item) {
                    this.subGroups.add(new SubGroups(item));
                }
            }
        }
        else {
            JSONObject item = json.optJSONObject("subGroups");
            if (null != item) {
                this.subGroups.add(new SubGroups(item));
            }
        }


    }
    
    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<SubGroups> getSubGroups() {
        return this.subGroups;
    }

    public void setSubGroups(ArrayList<SubGroups> subGroups) {
        this.subGroups = subGroups;
    }


    
}
