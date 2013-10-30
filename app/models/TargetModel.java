package models;

import java.util.List;

public class TargetModel {

	private String messageID;
	private List<String> taskIDs;
	
	public List<String> getTaskIDs() {
		return taskIDs;
	}

	public void setTaskIDs(List<String> taskIDs) {
		this.taskIDs = taskIDs;
	}

	public TargetModel(String id){
		this.messageID = id;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String id) {
		this.messageID = id;
	}

	public String toJSON() {
		return "{\"messageID\" : \"" + getMessageID() + "\", \"taskIDs\" : \""
		        + getTaskIDs() + "\"}";
	}

}
