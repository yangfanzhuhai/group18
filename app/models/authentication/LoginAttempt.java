package models.authentication;

import models.MongoLink;

public class LoginAttempt {

  private String email;
  private String ipAddress;
  private Session session;
  

  /**
   * Validate the given credentials.
   * 
   * @param email
   * @param password
   * @return
   * @throws LoginAttemptException 
   */
  private static void validateCredentials(String email,
    String password, String ipAddress) throws LoginAttemptException {
    
    boolean credentialsAreValid = MongoLink.MONGO_LINK.checkLogin(email, password);
    
    if(!credentialsAreValid) {
      new FailedLoginAttempt(email, ipAddress);
      throw new LoginAttemptException();
    }
  }
  
  
  /**
   * Attempt a login.
   * 
   * @param email
   * @param ipAddress
   * @throws LoginAttemptException 
   */
  public LoginAttempt(String email, String password, String ipAddress)
      throws LoginAttemptException {
    
    this.setEmail(email);
    this.setIpAddress(ipAddress);
    
    // Try to login
    LoginAttempt.validateCredentials(email, password, ipAddress);
    
    // --- Login was successful --- 
    
    // Establish a new session.
    this.setSession(new Session(this));
  }
  
  
  
  
  /**
   * Get the email associated with this login attempt.
   * 
   * @return
   */
  public String getEmail() {
    return email;
  }


  /**
   * Set the email associated with this login attempt.
   * 
   * @param email
   */
  private void setEmail(String email) {
    this.email = email;
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
