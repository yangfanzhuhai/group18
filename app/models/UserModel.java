package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserModel extends Model {

	private String username;
	private LocalAccount localAccount;
	private FBAccount fbAccount;
	private GHAccount ghAccount;
	

	public UserModel(GHAccount ghAccount) {
		super();
		this.username = "";
		this.localAccount = new LocalAccount();
		this.fbAccount = new FBAccount();
		this.ghAccount = ghAccount;
	}

	public UserModel(FBAccount fbAccount) {
		super();
		this.username = "";
		this.localAccount = new LocalAccount();
		this.fbAccount = fbAccount;
		this.ghAccount = new GHAccount();
	}

	@Override
	public String toJSON() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return "{" + super.toJSON() + "\"username\" : " + username 
									+ ",\"localAccount\" : " + gson.toJson(localAccount.updatePhotoUrl())
							  		+ ",\"fbAccount\" : " + gson.toJson(fbAccount.updatePhotoUrl())
							  		+ ",\"ghAccount\" : " + gson.toJson(ghAccount.updatePhotoUrl())
							  		+ "}";
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public LocalAccount getLocalAccount() {
		return localAccount;
	}

	public void setLocalAccount(LocalAccount localAccount) {
		this.localAccount = localAccount;
	}

	public FBAccount getFbAccount() {
		return fbAccount;
	}

	public void setFbAccount(FBAccount fbAccount) {
		this.fbAccount = fbAccount;
	}

	public GHAccount getGhAccount() {
		return ghAccount;
	}

	public void setGhAccount(GHAccount ghAccount) {
		this.ghAccount = ghAccount;
	}
	
	
}
