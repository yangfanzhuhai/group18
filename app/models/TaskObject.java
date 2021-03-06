package models;

/**
 * Models a Task.
 */
public class TaskObject extends ObjectModel {

  private String name;
  private String status;
  private int priority;
  private String assignedUsername = null;
  private String alias = "";

  /**
   * Construct a new Task with the given name, status and priority.
   * 
   * @param name
   * @param status
   * @param priority
   */
  public TaskObject(String name, String status, int priority, String alias) {
    this.setName(name);
    this.setStatus(status);
    this.setPriority(priority);
    this.setAlias(alias);
  }

  /**
   * Get the name of this Task.
   * 
   * @return
   */
  public String getName() {
    return name;
  }

  /**
   * Set the name of this Task.
   * 
   * @param name
   */
  private void setName(String name) {
    this.name = name;
  }

  /**
   * Get the status of this Task.
   * 
   * @return
   */
  public String getStatus() {
    return status;
  }

  /**
   * Set the status of this Task.
   * 
   * @param status
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * Get the priority of this Task.
   * 
   * @return
   */
  public int getPriority() {
    return priority;
  }

  /**
   * Set the priority of this Task.
   * 
   * @param priority
   */
  public void setPriority(int priority) {
    this.priority = priority;
  }
  
  public String getAlias() {
	  return this.alias;
  }
  
  public void setAlias(String alias) {
	  this.alias=alias;
  }

  /**
   * Get the user name assigned to this Task.
   * 
   * @return
   */
  public String getAssignedUsername() {
    return this.assignedUsername;
  }

  /**
   * The the user name assigned to this Task.
   * 
   * @param username
   */
  public void setAssignedUsername(String username) {
    this.assignedUsername = username;
  }

  /**
   * Has this Task been assigned to a user?
   * 
   * @return
   */
  public boolean assignedToUser() {
    return this.getAssignedUsername() != null;
  }

  /**
   * Assign this Task to a user.
   * 
   * @param username
   */
  public void assignToUser(String username) {
    this.setAssignedUsername(username);
  }

  /**
   * Get the type of this Object -> TASK.
   * 
   * @return
   */
  @Override
  public void setType() {
    this.objectType = ObjectType.TASK;
  }

  /**
   * Serialize this Task as JSON.
   * 
   * @return
   */
  public String toJSON() {

    String assigned = "";
    if (this.assignedToUser()) {
      assigned = "\"assignedUsername\" : \"" + this.getAssignedUsername() + 
          "\"";
    }

    return "{" + 
      "\"objectType\" : \"" + getType() + "\"," +
      "\"alias\" : \"" + getAlias() + "\"," +
      "\"name\" : \"" + getName() + "\"," + 
      "\"status\" : \"" + getStatus() + "\"," + 
      "\"priority\" : \"" + getPriority() + "\"" + 
      assigned + 
      "}";
    
  }

}
