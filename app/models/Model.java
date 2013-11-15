package models;

public abstract class Model {
	
	private String ID;
	
	public Model() {
		ID = null;
	}
	
	public String getID() {
		return ID;
	}
	
	public void setID(String id) {
		ID = id;
	}
	
	public String toJSON() {
		return getID() == null ? "" : "\"id\" : \"" + getID() + "\", ";
	}

}
