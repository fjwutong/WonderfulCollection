package com.android.wonderfulcollection;

import java.util.Vector;

public class UserFavorites {
	
	public class Status {
		public String created_at;
		public String id;
		public String text;
		public String source;
		public Boolean favorited;
		public Boolean truncated;	
		public UserInfo user;
	}
	
	public class Tags {
		public String id;
		public String tag;
	}
	
	public class Favorites {
		public Status status;
		public Tags tags;
		public String favorited_time;		
	}
	
	private Vector<Favorites> favorites;
	private int total_number;
	
	@Override
    public String toString() {	
		
        return "UserFavorites [total_number=" + Integer.toString(total_number) + "]";
    }
	

	public int getTotalNumber() {
        return total_number;
    }

    public void setTotalNumber(int total_number) {
        this.total_number = total_number;
    }
	
	

	
}
