package com.android.wonderfulcollection;

public class UserInfo {
	
	private String uid;//用户UID
    private String screen_name;//用户昵称
    private String name;//友好显示名称
    private String location;//用户所在地
    private String profile_image_url;//用户头像地址，50×50像素
    private String gender;//性别，m：男、f：女、n：未知
    private String avatar_large;//用户大头像地址

    @Override
    public String toString() {
        return "UserInfo [uid=" + uid + ", screen_name=" + screen_name
                + ", name=" + name + ", location=" + location
                + ", profile_image_url=" + profile_image_url + ", gender="
                + gender + ", avatar_large=" + avatar_large + "]";
    }

    public UserInfo() {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getScreen_name() {
        return screen_name;
    }

    public void setScreen_name(String screen_name) {
        this.screen_name = screen_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfile_image_url() {
        return profile_image_url;
    }

    public void setProfile_image_url(String profile_image_url) {
        this.profile_image_url = profile_image_url;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAvatar_large() {
        return avatar_large;
    }

    public void setAvatar_large(String avatar_large) {
        this.avatar_large = avatar_large;
    }

}
