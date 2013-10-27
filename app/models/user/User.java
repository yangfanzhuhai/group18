package models.user;

import java.util.Date;

import models.DatabaseBaseModel;

/**
 * Represents a user.
 * 
 * @author Colin Stannard <colin.stannard@cbsindustries.com>
 */
public class User extends DatabaseBaseModel<User> {
  
  private String email;
  private String displayName;
  private String firstName;
  private String lastName;
  private Date dateJoined;

  /**
   * Construct a User from the id of the User (read from database).
   * 
   * @param id
   */
  public User(Integer id) {
    try {
      this.merge(this.readExistingInstanceFromDatabase(id));

      // Should never throw an exception unless id is invalid.
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }

  /**
   * Construct a new User from the given details (creates a new user).
   * 
   * @param email
   * @param displayName
   * @param firstName
   * @param lastName
   */
  public User(String email, String displayName, String firstName,
      String lastName) {
    this.setEmail(email);
    this.setDisplayName(displayName);
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setDateJoined(new Date());
    this.save();
  }

  /**
   * The email address of the user.
   * 
   * @return
   */
  public String getEmail() {
    return email;
  }

  private void setEmail(String email) {
    this.email = email;
  }

  /**
   * The display name of the user.
   * 
   * @return
   */
  public String getDisplayName() {
    return displayName;
  }

  private void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  /**
   * The first name of the user.
   * 
   * @return
   */
  public String getFirstName() {
    return firstName;
  }

  private void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  /**
   * The last name of the user.
   * 
   * @return
   */
  public String getLastName() {
    return lastName;
  }

  private void setLastName(String lastName) {
    this.lastName = lastName;
  }

  /**
   * The date the user joined.
   * 
   * @return
   */
  public Date getDateJoined() {
    return dateJoined;
  }

  /**
   * 
   * @param dateJoined
   */
  private void setDateJoined(Date dateJoined) {
    this.dateJoined = dateJoined;
  }

  @Override
  protected Integer insertNewInstanceIntoDatabase() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void updateExistingInstanceInDatabase() {
    // TODO Auto-generated method stub

  }

  @Override
  protected User readExistingInstanceFromDatabase(Integer id) {
    // TODO Auto-generated method stub
    return null;
  }

}
