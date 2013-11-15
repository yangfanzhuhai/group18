package models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class UserModel extends Model {

	private LocalAccount localAccount;
	private FBAccount fbAccount;
	private GHAccount ghAccount;

	@Override
	public String toJSON() {
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		return "{" + super.toJSON() + "\"localAccount\" : " + gson.toJson(localAccount.updatePhotoUrl())
							  		+ ",\"fbAccount\" : " + gson.toJson(fbAccount.updatePhotoUrl())
							  		+ ",\"ghAccount\" : " + gson.toJson(ghAccount.updatePhotoUrl())
							  		+ "}";
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
