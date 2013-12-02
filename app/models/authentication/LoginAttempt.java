package models.authentication;

import play.api.libs.json.JsValue;
import play.api.libs.json.Json;
import models.ActivityModel;
import models.db.MongoLink;

public class LoginAttempt {

  private String username;
  private String ipAddress;
  private Session session;
  
  /**
   * Attempt a login.
   * 
   * @param username
   * @param ipAddress
   * @throws LoginAttemptException 
   */
  public LoginAttempt(String username, String password, String ipAddress)
      throws LoginAttemptException {
    
    this.setUsername(username);
    this.setIpAddress(ipAddress);
    
    // Try to login
    LoginAttempt.validateCredentials(username, password, ipAddress);
    
    // --- Login was successful --- 
    
	//If user login with email, retrieve username that links to the email
	if (MongoLink.MONGO_LINK.checkLoginWithEmail(username, password)) {
		String userJson = MongoLink.MONGO_LINK.getUsernameFromEmail(username);
		JsValue obj = Json.parse(userJson);
		this.setUsername(ActivityModel.cleanJsonStringValue(obj.$bslash("username")
				.toString()));
	}
    
    // Establish a new session.
    this.setSession(new Session(this));
  }
  
  /**
   * Validate the given credentials.
   * 
   * @param email
   * @param password
   * @return
   * @throws LoginAttemptException 
   */
  private static void validateCredentials(String username,
    String password, String ipAddress) throws LoginAttemptException {
	boolean credentialsAreValid = MongoLink.MONGO_LINK.checkLogin(username, password);

    //System.out.println(credentialsAreValid);
    if(!credentialsAreValid) {
      new FailedLoginAttempt(username, ipAddress);
      throw new LoginAttemptException();
    }
  }
  
  /**
   * Get the email associated with this login attempt.
   * 
   * @return
   */
  public String getUsername() {
    return username;
  }


  /**
   * Set the email associated with this login attempt.
   * 
   * @param email
   */
  private void setUsername(String username) {
    this.username = username;
  }
  
  
  /**
   * Get the ip address from which this request originates.
   *  
   * @return
   */
  public String getIpAddress() {
    return ipAddress;
  }

  
  /**
   * Set the ip address from which this request originates.
   * 
   * @param ipAddress
   */
  private void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
  
  
  /**
   * Get the session created by this login attempt.
   * 
   * @return
   */
  public Session getSession() {
    return session;
  }


  /**
   * Set the session created by this login attempt.
   * 
   * @param session
   */
  private void setSession(Session session) {
    this.session = session;
  }

  
}
