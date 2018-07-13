package com.andforce;

import org.json.*;
import java.util.ArrayList;

public class SubGroups {
	
    private String subType;
    private ArrayList<Emojis> emojis;
    
    
	public SubGroups () {
		
	}	
        
    public SubGroups (JSONObject json) {
    
        this.subType = json.optString("subType");

        this.emojis = new ArrayList<Emojis>();
        JSONArray arrayEmojis = json.optJSONArray("emojis");
        if (null != arrayEmojis) {
            int emojisLength = arrayEmojis.length();
            for (int i = 0; i < emojisLength; i++) {
                JSONObject item = arrayEmojis.optJSONObject(i);
                if (null != item) {
                    this.emojis.add(new Emojis(item));
                }
            }
        }
        else {
            JSONObject item = json.optJSONObject("emojis");
            if (null != item) {
                this.emojis.add(new Emojis(item));
            }
        }


    }
    
    public String getSubType() {
        return this.subType;
    }

    public void setSubType(String subType) {
        this.subType = subType;
    }

    public ArrayList<Emojis> getEmojis() {
        return this.emojis;
    }

    public void setEmojis(ArrayList<Emojis> emojis) {
        this.emojis = emojis;
    }


    
}
