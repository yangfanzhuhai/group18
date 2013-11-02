package models;

import java.text.ParseException;

import play.api.libs.json.JsValue;
import play.api.libs.json.Json;

import com.google.gson.Gson;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class ActivityModel {

	private String ID;
	private String published;
	private ActorModel actor;
	private String verb;
	private ObjectModel object;
	private TargetModel target;

	public ActivityModel(String published, ActorModel actor, String verb,
			ObjectModel object, TargetModel target) {
		this.ID = null;
		this.setPublished(published);
		this.setActor(actor);
		this.setVerb(verb);
		this.setObject(object);
		this.setTarget(target);
	}

	private static String cleanJsonStringValue(String stringValue) {
		return stringValue.replaceAll("^\"|\"$", "");
	}

	public ActivityModel(String jsonString) throws ParseException {
		JsValue obj = Json.parse(jsonString);
		Gson gson = new Gson();
		// Date published = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
		// Locale.ENGLISH).parse(ActivityModel.cleanJsonStringValue(
		// obj.$bslash("published").toString()));

		ID = null;
		 
		String published = ActivityModel.cleanJsonStringValue(obj.$bslash(
				"published").toString());

		String verb = ActivityModel.cleanJsonStringValue(obj.$bslash("verb")
				.toString());

		ActorModel actorModel = null;
		JsValue actor = obj.$bslash("actor");
		String actorObjectType = ActivityModel.cleanJsonStringValue(actor
				.$bslash("objectType").toString());
		String actorDisplayName = ActivityModel.cleanJsonStringValue(actor
				.$bslash("displayName").toString());
		switch (ActorType.valueOf(actorObjectType)) {
		case PERSON:
			actorModel = new PersonActor(actorDisplayName);
		}

		ObjectModel objectModel = null;
		JsValue object = obj.$bslash("object");
		String objectObjectType = ActivityModel.cleanJsonStringValue(object
				.$bslash("objectType").toString());
		switch (ObjectType.valueOf(objectObjectType)) {
		case MESSAGE:
			String message = ActivityModel.cleanJsonStringValue(object
					.$bslash("message").toString());
			objectModel = new MessageObject(message);
			break;
		case TASK:
			String name = ActivityModel.cleanJsonStringValue(object
					.$bslash("name").toString());
			Status status = Status.valueOf(ActivityModel.cleanJsonStringValue(object
					.$bslash("status").toString()));
			int priority = Integer.parseInt(ActivityModel.cleanJsonStringValue(object
					.$bslash("priority").toString()));
			objectModel = new TaskObject(name, status, priority);
			break;
		case GIT:
			objectModel = gson.fromJson(object.toString(), GitObject.class);
		}

		String target = obj.$bslash("target").toString();

		TargetModel targetModel = gson.fromJson(target, TargetModel.class);
		// JsValue target = obj.$bslash("target");

		this.setPublished(published);
		this.setActor(actorModel);
		this.setVerb(verb);
		this.setObject(objectModel);
		this.setTarget(targetModel);
	}

	public String getPublished() {
		return published;
	}

	public void setPublished(String published) {
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

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		this.ID = iD;
	}

	/** Converts the ActivityModel to a JSON object.
	 * Optionally has an ID field (if the ActivityModel has an ID set)
	 * @return String JSON
	 */
	public String toJSON() {
		String prefix = ID == null ? "" : "\"id\" : \"" + getID() + "\", ";
		
		return "{" + prefix + "\"published\" : \"" + getPublished().toString()
				+ "\", \"actor\" : " + getActor().toJSON() + ", \"verb\" : \""
				+ getVerb() + "\", \"object\" : " + getObject().toJSON()
				+ ", \"target\" : " + getTarget().toJSON() + " }";
	}
	
	public void save(){
		MongoLink.MONGO_LINK.insertNews((DBObject) JSON.parse(this
				.toJSON()));
	}
	

}
