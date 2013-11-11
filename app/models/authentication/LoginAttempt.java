package models.authentication;

public class LoginAttempt {

  private String username;
  private String ipAddress;
  private Session session;
  

  /**
   * Validate the given credentials.
   * 
   * @param username
   * @param password
   * @return
   * @throws LoginAttemptException 
   */
  private static void validateCredentials(String username,
      String password, String ipAddress) throws LoginAttemptException {
    
    // TODO: Some database stuff.
    
    boolean credentialsAreValid = false;
    
    if(!credentialsAreValid) {
      new FailedLoginAttempt(username, ipAddress);
      throw new LoginAttemptException();
    }
  }
  
  
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
    
    // Establish a new session.
    this.setSession(new Session(this));
  }
  
  
  
  
  /**
   * Get the username associated with this login attempt.
   * 
   * @return
   */
  public String getUsername() {
    return username;
  }


  /**
   * Set the username associated with this login attempt.
   * 
   * @param username
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
