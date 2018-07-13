package com.andforce;

import org.json.*;


public class Emojis {
	
    private String status;
    private String emoji;
    private String codePoints;
    private String name;
    
    
	public Emojis () {
		
	}	
        
    public Emojis (JSONObject json) {
    
        this.status = json.optString("status");
        this.emoji = json.optString("emoji");
        this.codePoints = json.optString("codePoints");
        this.name = json.optString("name");

    }
    
    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getEmoji() {
        return this.emoji;
    }

    public void setEmoji(String emoji) {
        this.emoji = emoji;
    }

    public String getCodePoints() {
        return this.codePoints;
    }

    public void setCodePoints(String codePoints) {
        this.codePoints = codePoints;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    
}
