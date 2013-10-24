package models;

public class PersonActor extends ActorModel {

  public PersonActor(String displayName) {
    super(displayName);

  }

  @Override
  public ActorType getType() {
    return ActorType.PERSON;
  }

  @Override
  public String toJSON() {
    return "{\"objectType\" : \"" + getType() + "\", \"displayName\" : \""
        + getDisplayName() + "\"}";
  }
}
