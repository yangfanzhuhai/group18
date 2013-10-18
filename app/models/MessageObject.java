package models;

public class MessageObject extends ObjectModel {

	private String message;
	
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public ObjectType getType() {
		return ObjectType.MESSAGE;
	}

	@Override
	public String toJSON() {
		return "{\"objectType\" : \"" + getType() + "\", \"message\" : \"" + getMessage() + "\"}";
	}

}
