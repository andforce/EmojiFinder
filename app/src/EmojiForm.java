package com.andforce;

import org.json.*;
import java.util.ArrayList;

public class EmojiForm {
	
    private String document;
    private String version;
    private ArrayList<Groups> groups;
    private String date;
    
    
	public EmojiForm () {
		
	}	
        
    public EmojiForm (JSONObject json) {
    
        this.document = json.optString("document");
        this.version = json.optString("version");

        this.groups = new ArrayList<Groups>();
        JSONArray arrayGroups = json.optJSONArray("groups");
        if (null != arrayGroups) {
            int groupsLength = arrayGroups.length();
            for (int i = 0; i < groupsLength; i++) {
                JSONObject item = arrayGroups.optJSONObject(i);
                if (null != item) {
                    this.groups.add(new Groups(item));
                }
            }
        }
        else {
            JSONObject item = json.optJSONObject("groups");
            if (null != item) {
                this.groups.add(new Groups(item));
            }
        }

        this.date = json.optString("date");

    }
    
    public String getDocument() {
        return this.document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getVersion() {
        return this.version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public ArrayList<Groups> getGroups() {
        return this.groups;
    }

    public void setGroups(ArrayList<Groups> groups) {
        this.groups = groups;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }


    
}
