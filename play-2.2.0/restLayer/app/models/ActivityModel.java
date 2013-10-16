package models;

import java.util.Date;


public class ActivityModel {
	
	private Date published;
	private ActorModel actor;
	private String verb;
	private ObjectModel object;
	private TargetModel target;
	
	public ActivityModel(String jsonString){
		
	}
	
	public Date getPublished() {
		return published;
	}
	private void setPublished(Date published) {
		this.published = published;
	}
	public ActorModel getActor() {
		return actor;
	}
	private void setActor(ActorModel actor) {
		this.actor = actor;
	}
	public String getVerb() {
		return verb;
	}
	private void setVerb(String verb) {
		this.verb = verb;
	}
	public ObjectModel getObject() {
		return object;
	}
	private void setObject(ObjectModel object) {
		this.object = object;
	}
	public TargetModel getTarget() {
		return target;
	}
	private void setTarget(TargetModel target) {
		this.target = target;
	}
	
	
	
}
