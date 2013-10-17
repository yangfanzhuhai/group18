package models;

public abstract class ActorModel {
	
	protected String displayName;

	
	public String getDisplayName() {
		return displayName;
	}
	protected void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public abstract ActorType getType();

}
