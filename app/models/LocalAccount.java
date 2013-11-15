package models;

import models.utils.MD5Util;

public class LocalAccount extends Account {
	
	private String password;
	private String email;
	
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		updatePhotoUrl();
	}
	@Override
	protected void updatePhotoUrl() {
		setPhoto_url("http://www.gravatar.com/avatar/" + MD5Util.md5Hex(email) + ".jpg?s=250" + default_img);
	}

}
