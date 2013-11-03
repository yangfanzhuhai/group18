package models;

import java.util.List;

import models.utils.ListPrinter;

public class TargetModel {

	private String messageID;
	private List<String> taskIDs;

	public TargetModel(String messageID, List<String> taskIDs) {
		this.messageID = messageID;
		this.taskIDs = taskIDs;
	}

	public List<String> getTaskIDs() {
		return taskIDs;
	}

	public void setTaskIDs(List<String> taskIDs) {
		this.taskIDs = taskIDs;
	}

	public String getMessageID() {
		return messageID;
	}

	public void setMessageID(String id) {
		this.messageID = id;
	}

	public String toJSON() {
		return "{\"messageID\" : \"" + getMessageID() + "\", \"taskIDs\" : "
				+ ListPrinter.print(getTaskIDs()) + "}";
	}

}
