package models;


public abstract class Model {
	
	private MongoID _id;
	
	public Model() {
		_id = null;
	}
	
	public MongoID getID() {
		return _id;
	}
	
	public void setID(MongoID id) {
		_id = id;
	}
	
	public String toJSON() {
		return getID() == null ? "" : "\"id\" : \"" + getID().get$oid() + "\", ";
	}

}
