package models;

public class GroupMember {
	
	public String username;
	public String displayName;
	public String photo_url;
	
	public GroupMember(String username, String displayName, String photo_url) {
		this.username = username;
		this.displayName = displayName;
		this.photo_url = photo_url;
	}

}
