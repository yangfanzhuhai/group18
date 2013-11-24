package models;

import org.mindrot.jbcrypt.BCrypt;

import models.utils.MD5Util;

public class LocalAccount extends Account {
	
	private String password;
	private String email;
	
	public LocalAccount(){}
	
	public LocalAccount(String email, String password){
		this.email = email;
		this.password =  BCrypt.hashpw(password, BCrypt.gensalt());
	}
	
	
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
	}
	@Override
	protected Account updatePhotoUrl() {
		if(email != null) setPhoto_url("http://www.gravatar.com/avatar/" + MD5Util.md5Hex(email) + ".jpg?s=250" + default_img);
		return this;
	}

}
