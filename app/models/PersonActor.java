package models;

public class PersonActor extends ActorModel {

	private String username;
	private String photo_url;
	
	public PersonActor(String displayName, String username, String photo_url) {
		super(displayName);
		this.username = username;
		this.photo_url = photo_url;
		objectType = ActorType.PERSON;
	}

	@Override
	public void setType() {
		objectType = ActorType.PERSON;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPhoto_url() {
		return photo_url;
	}

	public void setPhoto_url(String photo_url) {
		this.photo_url = photo_url;
	}

}
