package models;

public abstract class ObjectModel {
	
	protected ObjectType objectType; 
	
	public ObjectType getType() {
		return this.objectType;
	}
	
	public abstract void setType();
	

}
