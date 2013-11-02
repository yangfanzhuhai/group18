package models;

public abstract class ActorModel {

  protected String displayName;

  public ActorModel(String displayName) {
    this.setDisplayName(displayName);
  }
  
  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public abstract ActorType getType();

  public abstract String toJSON();

}
