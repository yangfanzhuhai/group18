package models;

public class LocalAccount {
	
	private String username;
	private String password;
	private String email;
	private String emailHash;
	
	
	public String getEmailHash() {
		return emailHash;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
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
		emailHash = hashEmail();
	}
	private String hashEmail() {
		// TODO Auto-generated method stub
		return null;
	}
	

}
