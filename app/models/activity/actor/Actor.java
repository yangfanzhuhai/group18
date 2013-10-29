package models.activity.actor;

import play.api.libs.json.JsValue;
import play.api.libs.json.Json;
import models.JsonException;
import models.JsonUtility;

public abstract class Actor {

  /**
   * The display name of this Actor.
   * 
   * @return The display name of this Actor.
   */
  public abstract String getDisplayName();

  /**
   * Get the type of this Actor.
   * 
   * @return The type of the Actor.
   */
  public abstract ActorType getType();

  /**
   * Serialize this Actor instance into a String.
   * 
   * @return This Actor serialized as a String.
   */
  public abstract String toJson();
  
  /**
   * Initialize a new Actor given a JsValue.
   * 
   * @param json
   * 
   * @return A new Actor initialized from the given JsValue.
   * 
   * @throws JsonException
   */
  public static Actor initializeFromJson(JsValue json) throws JsonException {

    // Read the type from JSON.
    String actorObjectType = JsonUtility.cleanJsonStringValue(json.$bslash(
        "objectType").toString());

    switch (ActorType.valueOf(actorObjectType)) {
    case PERSON:
      return Person.fromJson(json);
    }

    // Invalid Actor type.
    throw new JsonException("Invalid Actor type '" + actorObjectType + "'");
  }

  /**
   * Initialize a new Actor given a JSON String.
   * 
   * @param json
   * 
   * @return A new Actor initialized from the given JSON String.
   * 
   * @throws JsonException
   */
  public static Actor initializeFromJson(String json) throws JsonException {
    try {
      return Actor.initializeFromJson(Json.parse(json));
    } catch (JsonException e) {
      throw new JsonException("Parse error");
    }
  }

}
