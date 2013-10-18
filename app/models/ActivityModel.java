package models;

import java.util.Date;

public class ActivityModel {

	private Date published;
	private ActorModel actor;
	private String verb;
	private ObjectModel object;
	private TargetModel target;
	
	public ActivityModel(){}

	public ActivityModel(String jsonString) {

	}

	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public ActorModel getActor() {
		return actor;
	}

	public void setActor(ActorModel actor) {
		this.actor = actor;
	}

	public String getVerb() {
		return verb;
	}

	public void setVerb(String verb) {
		this.verb = verb;
	}

	public ObjectModel getObject() {
		return object;
	}

	public void setObject(ObjectModel object) {
		this.object = object;
	}

	public TargetModel getTarget() {
		return target;
	}

	public void setTarget(TargetModel target) {
		this.target = target;
	}

	public String toJSON() {
		return "{\"published\" : \"" + getPublished() + "\", \"actor\" : "
				+ getActor().toJSON() + ", \"verb\" : \"" + getVerb()
				+ "\", \"object\" : " + getObject().toJSON()
				+ ", \"target\" : " + getTarget().toJSON() + " }";
	}

}
