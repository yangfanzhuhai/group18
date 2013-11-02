package models.activity;

import java.net.UnknownHostException;
import java.util.Date;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import models.DatabaseBaseModel;
import models.DatabaseReadException;
import models.DatabaseWriteException;
import models.JsonException;
import models.JsonUtility;
import models.MongoLink;
import models.activity.actor.Actor;
import models.activity.object.Object;
import models.activity.target.Target;
import play.api.libs.json.JsValue;
import play.api.libs.json.Json;

public class Activity extends DatabaseBaseModel<Activity> {

  private Date published;
  private Actor actor;
  private String verb;
  private Object object;
  private Target target;

  /**
   * Initialize a new Activity given the Date, Actor, Verb, Object and Target.
   * 
   * @param published
   * @param actor
   * @param verb
   * @param object
   * @param target
   */
  public Activity(Date published, Actor actor, String verb, Object object,
      Target target) {
    this.setPublished(published);
    this.setActor(actor);
    this.setVerb(verb);
    this.setObject(object);
    this.setTarget(target);
  }
  
  public Activity(String id, Date published, Actor actor, String verb,
      Object object, Target target) {
    this.setPublished(published);
    this.setActor(actor);
    this.setVerb(verb);
    this.setObject(object);
    this.setTarget(target);
  }

  /**
   * Get the date this Activity was published.
   * 
   * @return The date this activity was published.
   */
  public Date getPublished() {
    return published;
  }

  /**
   * Set the date this Activity was published.
   * 
   * @param published
   */
  public void setPublished(Date published) {
    this.published = published;
  }

  /**
   * Get this Activity's Actor.
   * 
   * @return Activity Actor.
   */
  public Actor getActor() {
    return actor;
  }

  /**
   * Set the Actor of this Activity.
   * 
   * @param actor
   */
  public void setActor(Actor actor) {
    this.actor = actor;
  }

  /**
   * Get the verb of this Activity.
   * 
   * @return Activity as a String.
   */
  public String getVerb() {
    return verb;
  }

  /**
   * Set the verb of this activity.
   * 
   * @param verb
   */
  public void setVerb(String verb) {
    this.verb = verb;
  }

  /**
   * Get the Object this Activity carries.
   * 
   * @return The Object used by this Activity.
   */
  public Object getObject() {
    return object;
  }

  /**
   * Set the Object this Activity carries.
   * 
   * @param object
   */
  public void setObject(Object object) {
    this.object = object;
  }

  /**
   * Get the Target of this Activity.
   * 
   * @return The Target of this Activity.
   */
  public Target getTarget() {
    return target;
  }

  /**
   * Set the Target of this Activity.
   * 
   * @param target
   */
  public void setTarget(Target target) {
    this.target = target;
  }

  @Override
  protected String insertNewInstanceIntoDatabase()
      throws DatabaseWriteException {
    
    MongoLink mongoLink;
    try {
      mongoLink = new MongoLink();
    } catch (UnknownHostException e1) {
      // Connection failed.
      throw new DatabaseWriteException();
    }
    
    mongoLink.insertNews((DBObject) JSON.parse(this.toJsonWithoutId()));
    
    return "";
  }

  @Override
  protected void updateExistingInstanceInDatabase()
      throws DatabaseWriteException {
    // TODO Auto-generated method stub

  }

  @Override
  protected Activity readExistingInstanceFromDatabase(Integer id)
      throws DatabaseReadException {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Serialize this Activity to JSON without the id.
   * 
   * @return
   */
  public String toJsonWithoutId() {
    return toJson(false);
  }
  
  
  /**
   * Serialize this Activity to JSON with the id.
   * @return
   */
  public String toJsonWithId() {
    return toJson(true);
  }
  
  
  /**
   * Serialize this Activity to JSON (with / without the id).
   * 
   * @param withId
   * @return
   */
  private String toJson(Boolean withId) {
    String prefix = withId ? "\"id\" : \"" + getId() + "\", " : "";
    
    return "{" + prefix + "\"published\" : \"" + getPublished().toString()
            + "\", \"actor\" : " + getActor().toJson() + ", \"verb\" : \""
            + getVerb() + "\", \"object\" : " + getObject().toJson()
            + ", \"target\" : " + getTarget().toJson() + " }";
  }

  public static Activity initializeFromJson(JsValue json) throws JsonException {

    String id
    
    // Establish published date.
    Date published;
    try {
      published = JsonUtility.readDate(json, "published");
    } catch (JsonException e) {
      published = new Date();
    }

    // Establish verb.
    String verb = JsonUtility.readString(json, "verb");

    // Establish Actor.
    Actor actor = Actor.initializeFromJson(JsonUtility.readJsValue(json,
        "actor"));

    // Establish Object.
    Object object = Object.initializeFromJson(JsonUtility.readJsValue(json,
        "object"));
    
    // Establish Target.
    Target target = new Target();
    
    return new Activity(published, actor, verb, object, target);
  }


  public static Activity initializeFromJson(String json) throws JsonException {
    try {
      return Activity.initializeFromJson(Json.parse(json));
    } catch (JsonException e) {
      throw new JsonException("Parse error");
    }
  }

}
