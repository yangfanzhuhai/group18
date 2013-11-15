package models;

public class FBAccount extends Account {
	
	private String profile_id;

	
	public String getProfile_id() {
		return profile_id;
	}

	public void setProfile_id(String profile_id) {
		this.profile_id = profile_id;
	}

	@Override
	protected Account updatePhotoUrl() {
		if(profile_id != null) setPhoto_url("http://graph.facebook.com/" + profile_id + "/picture?type=square");
		return this;
	}

}
