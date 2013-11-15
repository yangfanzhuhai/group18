package models;

public class GHAccount extends Account {
	
	private String email;
	private String gravatar_id;
	private String html_url;
	
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getGravatar_id() {
		return gravatar_id;
	}
	public void setGravatar_id(String gravatar_id) {
		this.gravatar_id = gravatar_id;
	}
	public String getHtml_url() {
		return html_url;
	}
	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
	
	@Override
	protected Account updatePhotoUrl() {
		if(gravatar_id != null) setPhoto_url("http://www.gravatar.com/avatar/" + gravatar_id + ".jpg?s=250" + default_img);
		return this;
	}

}
