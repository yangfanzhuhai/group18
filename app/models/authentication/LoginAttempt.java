package models.authentication;

import java.util.Date;

import models.DatabaseBaseModel;

/**
 * Represents a login attempt.
 * 
 * @author Colin Stannard <colin.stannard@cbsindustries.com>
 */
public class LoginAttempt extends DatabaseBaseModel<LoginAttempt> {

  private String ipAddress;
  private String userAgent;
  private Date time;
  private String username;
  private Boolean successful;

  /**
   * Try to login.
   * 
   * @param username
   * @param password
   * @param ipAddress
   * @param userAgent
   */
  public LoginAttempt(String username, String password, String ipAddress,
      String userAgent) {

    this.setIpAddress(ipAddress);
    this.setUserAgent(userAgent);
    this.setTime(new Date());

    this.attemptLogin(username, password);
    this.save();
  }

  /**
   * Check with the database if the given credentials are valid.
   * 
   * @param username
   * @param password
   */
  private void attemptLogin(String username, String password) {
    this.setUsername(username);

    // TODO: Link up with database.

    // Result of database query must either result in:
    // - True -> The credentials are valid.
    // - False -> The credentials are invalid.
    boolean credentialsOk = true;

    this.setSuccessful(credentialsOk);
  }

  /**
   * The IP address of the client who issued the login request.
   * 
   * @return The IP address of the client as a String.
   */
  public String getIpAddress() {
    return ipAddress;
  }

  /**
   * Set the IP address of the client making the login attempt.
   * 
   * @param ipAddress
   */
  private void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

  /**
   * The user agent of the client who issued the login attempt.
   * 
   * @return The user agent of the client as a String.
   */
  public String getUserAgent() {
    return userAgent;
  }

  /**
   * Set the user agent of the client who issued the login attempt.
   * 
   * @param userAgent
   */
  private void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  /**
   * The time the login attempt was issued.
   * 
   * @return The date and time of the login attempt.
   */
  public Date getTime() {
    return time;
  }

  /**
   * Set the time of the login attempt.
   * 
   * @param time
   */
  private void setTime(Date time) {
    this.time = time;
  }

  /**
   * The username of the login attempt.
   * 
   * @return Username as a String.
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the username of the login attempt.
   * 
   * @param username
   */
  private void setUsername(String username) {
    this.username = username;
  }

  /**
   * Was the login attempt successful?
   * 
   * @return Was the login successful?
   */
  public Boolean getSuccessful() {
    return successful;
  }

  /**
   * Set the result of a login attempt. True - success, False - fail
   * 
   * @param successful
   */
  private void setSuccessful(Boolean successful) {
    this.successful = successful;
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
  protected LoginAttempt readExistingInstanceFromDatabase(Integer id) {
    // TODO Auto-generated method stub
    return null;
  }

}
