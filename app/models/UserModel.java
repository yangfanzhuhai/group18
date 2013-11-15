package models;

public class UserModel extends Model {
	
	private LocalAccount localAccount;
	private FBAccount fbAccount;
	private GHAccount ghAccount;
	
	public UserModel(String jsonString) {
		super();
		
	}

	@Override
	public String toJSON() {
		// TODO Auto-generated method stub
		return null;
	}

}
