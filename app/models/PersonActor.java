package models;

public class PersonActor extends ActorModel {
	
	@Override
	public ActorType getType() {
		return ActorType.PERSON;
	}

	@Override
	public String toJSON() {
		return "{\"objectType\" : \""+ getType() + "\", \"displayName\" : \""+ getDisplayName() + "\"}";
	}
}
