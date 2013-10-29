package models.activity.actor;

import play.api.libs.json.JsValue;
import models.JsonUtility;
import models.user.User;

public class Person extends Actor {

  private User user;

  public Person(User user) {
    this.setUser(user);
  }

  /**
   * Get the User that represents this Person.
   * 
   * @return The User associated with this Person.
   */
  public User getUser() {
    return user;
  }

  /**
   * Set the User that represents this Person.
   * 
   * @param user
   */
  public void setUser(User user) {
    this.setUser(user);
  }

  /**
   * Get the type of this Actor -> Person.
   */
  @Override
  public ActorType getType() {
    return ActorType.PERSON;
  }

  /**
   * Serialize this Person to JSON.
   */
  @Override
  public String toJson() {
    return "{\"objectType\" : \"" + getType() + "\", \"userId\" : \""
        + this.getUserId() + "\"}";
  }

  /**
   * Initialize a new Person from JSON.
   * 
   * @param json
   * 
   * @return Initialized Person
   */
  public static Actor fromJson(JsValue json) {
    Integer userId = Integer.parseInt(JsonUtility.cleanJsonStringValue(json
        .$bslash("userId").toString()));

    return new Person(new User(userId));
  }

  /**
   * The id of the User this Person represents.
   * 
   * @return User's Id as an Integer.
   */
  public Integer getUserId() {
    return this.getUser().getId();
  }

  /**
   * The display name of the User this Person represents.
   * 
   * @return User's display name as a String.
   */
  public String getDisplayName() {
    return this.getUser().getDisplayName();
  }

}
