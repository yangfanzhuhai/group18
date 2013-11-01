package models;

public class TaskObject extends ObjectModel {

	private String name;
	private Status status;
	private int priority;
	
	public TaskObject(String name, Status status, int priority) {
		this.setName(name);
		this.setStatus(status);
		this.setPriority(priority);
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}
	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}


	@Override
	public ObjectType getType() {
		return ObjectType.TASK;
	}

	@Override
	public String toJSON() {
		return "{\"objectType\" : \"" + getType() + "\", \"name\" : \""
				+ getName() + "\", \"status\" : \"" + getStatus() + "\", \"priority\" : \"" + getPriority() + "\"}";
	}

}
