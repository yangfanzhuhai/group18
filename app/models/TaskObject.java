package models;

public class TaskObject extends ObjectModel {

	private String name;

	  public TaskObject(String name) {
	    this.setName(name);
	  }

	  public String getName() {
	    return name;
	  }

	  private void setName(String name) {
	    this.name = name;
	  }
	
	
	@Override
	public ObjectType getType() {
		return ObjectType.TASK;
	}

	@Override
	public String toJSON() {
		return "{\"objectType\" : \"" + getType() + "\", \"name\" : \""
		        + getName() + "\"}";
	}

}
