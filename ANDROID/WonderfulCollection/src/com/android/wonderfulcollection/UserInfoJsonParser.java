package com.android.wonderfulcollection;

import org.json.JSONException;
import org.json.JSONObject;
import com.android.wonderfulcollection.UserInfo;


public class UserInfoJsonParser {
	
	private UserInfo userInfo = null;

    public UserInfoJsonParser() {
    	userInfo = new UserInfo();
    }
    
    public UserInfo GetUserInfoByJson(String dataStr) {
        try {
            JSONObject jsonObject = new JSONObject(dataStr);
            userInfo.setUid(jsonObject.getString("id")); 
            userInfo.setScreen_name(jsonObject.getString("screen_name"));
            userInfo.setName(jsonObject.getString("name"));
            userInfo.setLocation(jsonObject.getString("location"));
            userInfo.setProfile_image_url(jsonObject.getString("profile_image_url"));
            userInfo.setGender(jsonObject.getString("gender"));
            userInfo.setAvatar_large(jsonObject.getString("avatar_large")); 
        } catch (JSONException e) { 
        	userInfo = null;
            e.printStackTrace();
        }
        return userInfo;
    }

}
