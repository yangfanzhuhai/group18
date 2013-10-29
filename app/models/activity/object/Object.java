package models.activity.object;

import play.api.libs.json.JsValue;
import play.api.libs.json.Json;
import models.JsonException;
import models.JsonUtility;

public abstract class Object {

  /**
   * Get the type of this Object.
   * 
   * @return The type of this Object.
   */
  public abstract ObjectType getType();

  /**
   * Serialize this Object instance into a String.
   * 
   * @return This Object serialized as a String.
   */
  public abstract String toJson();
  
  /**
   * Initialize a new Object given a JsValue.
   * 
   * @param json
   * 
   * @return A new Object initialized from the given JsValue.
   * 
   * @throws JsonException
   */
  public static Object initializeFromJson(JsValue json) throws JsonException {

    // Read the type from JSON.
    String actorObjectType = JsonUtility.cleanJsonStringValue(json.$bslash(
        "objectType").toString());

    switch (ObjectType.valueOf(actorObjectType)) {
    case MESSAGE:
      return Message.fromJson(json);
    }

    // Invalid Actor type.
    throw new JsonException("Invalid Actor type '" + actorObjectType + "'");
  }

  /**
   * Initialize a new Object given a JSON String.
   * 
   * @param json
   * 
   * @return A new Object initialized from the given JSON String.
   * 
   * @throws JsonException
   */
  public static Object initializeFromJson(String json) throws JsonException {
    try {
      return Object.initializeFromJson(Json.parse(json));
    } catch (JsonException e) {
      throw new JsonException("Parse error");
    }
  }

}
