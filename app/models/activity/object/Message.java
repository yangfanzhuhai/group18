package models.activity.object;

import models.JsonUtility;
import play.api.libs.json.JsValue;

public class Message extends Object {

  private String message;

  /**
   * Construct a new Message given the message text.
   * 
   * @param message
   */
  public Message(String message) {
    this.setMessage(message);
  }

  /**
   * Get the message text of this Message.
   * 
   * @return The text of this Message.
   */
  public String getMessage() {
    return message;
  }

  /**
   * Set the message text of this Message.
   * 
   * @param message
   */
  private void setMessage(String message) {
    this.message = message;
  }

  /**
   * The type of the Message Object is MESSAGE.
   * 
   * @return The type of the Message model.
   */
  @Override
  public ObjectType getType() {
    return ObjectType.MESSAGE;
  }

  /**
   * Serialize this Message to a String.
   * 
   * @return Message as a JSON string.
   */
  @Override
  public String toJson() {
    return "{\"objectType\" : \"" + getType() + "\", \"message\" : \""
        + getMessage() + "\"}";
  }

  /**
   * Initialize a new Message from a JsValue.
   * 
   * @param json
   * 
   * @return Initialized Message.
   */
  public static Message fromJson(JsValue json) {
    String message = JsonUtility.readString(json, "message");
    
    return new Message(message);
  }
}
