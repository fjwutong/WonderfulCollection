package com.android.wonderfulcollection;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class UserFavoritesJsonParser {
	
	private UserFavorites userfavorites = null;

    public UserFavoritesJsonParser() {
    	userfavorites = new UserFavorites();
    }
    
    public UserFavorites GetUserFavoritesByJson(String dataStr) {
        try {
        	Log.i("wutong", dataStr);
            JSONObject jsonObject = new JSONObject(dataStr);
            userfavorites.setTotalNumber(jsonObject.getInt("total_number"));    
        } catch (JSONException e) { 
        	userfavorites = null;
            e.printStackTrace();
        }
        return userfavorites;
    }

}
