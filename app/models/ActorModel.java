package models;

public abstract class ActorModel {

	private String displayName;
	protected ActorType objectType;

	public ActorModel(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public ActorType getType() {
		return objectType;
	}
	
	public abstract void setType();


}
