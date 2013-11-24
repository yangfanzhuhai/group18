package models;

public abstract class Account {
	
	protected static String default_img = "&d=http://assets.eschools.co.uk/images/default_user_300x300.png";

	private String name;
	private String photo_url;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhoto_url() {
		return photo_url;
	}
	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}
	
	protected abstract Account updatePhotoUrl();	
	
}