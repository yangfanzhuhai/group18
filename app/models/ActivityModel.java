package models;

import models.db.MongoLink;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;

public class ActivityModel extends Model {

	public static Gson activityModelGson = new GsonBuilder()
			.registerTypeAdapter(ActorModel.class, new ActorModelAdaptor())
			.registerTypeAdapter(ObjectModel.class, new ObjectModelAdaptor())
			.create();

	private String published;
	private ActorModel actor;
	private String verb;
	private ObjectModel object;
	private TargetModel target;

	public ActivityModel(String published, ActorModel actor, String verb,
			ObjectModel object, TargetModel target) {
		super();
		this.setPublished(published);
		this.setActor(actor);
		this.setVerb(verb);
		this.setObject(object);
		this.setTarget(target);
	}

	public static String cleanJsonStringValue(String stringValue) {
		return stringValue.replaceAll("^\"|\"$", "");
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

	/**
	 * Converts the ActivityModel to a JSON object. Optionally has an ID field
	 * (if the ActivityModel has an ID set)
	 * 
	 * @return String JSON
	 */
	@Override
	public String toJSON() {
		Gson gson = new Gson();
		return "{" + super.toJSON() + "\"published\" : \""
				+ getPublished().toString() + "\", \"actor\" : "
				+ gson.toJson(getActor()) + ", \"verb\" : \"" + getVerb()
				+ "\", \"object\" : " + gson.toJson(getObject())
				+ ", \"target\" : " + gson.toJson(getTarget()) + " }";
	}

	public void save(String groupID) {
		MongoLink.MONGO_LINK.insertNews(groupID,
				(DBObject) JSON.parse(this.toJSON()));
	}

}
