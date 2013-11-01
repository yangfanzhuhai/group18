package models;

import java.util.List;

public class TargetModel {

	private String messageID;
	private List<String> taskIDs;
	
	public TargetModel(String messageID, List<String> taskIDs){
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
		        + printTaskIDs() + "}";
	}
	
	private String printTaskIDs() {
		String s = "[";
		
		if (!taskIDs.isEmpty()) {
			int i;
			for (i = 0; i < taskIDs.size() - 1; i++) {
				s += "\"" + taskIDs.get(i) + "\",";
			}
			s += "\"" + taskIDs.get(i) + "\"";
		}
		
		return s += "]";
	}

}
