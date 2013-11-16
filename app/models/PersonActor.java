package models;

public class PersonActor extends ActorModel {

  public PersonActor(String displayName) {
    super(displayName);
    objectType = ActorType.PERSON;
  }

  @Override
  public void setType() {
    objectType = ActorType.PERSON;
  }

}
