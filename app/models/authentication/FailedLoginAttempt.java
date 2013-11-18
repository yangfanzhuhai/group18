package models.authentication;

public class FailedLoginAttempt {

  private String username;
  private String ipAddress;
  
  
  /**
   * Construct and save a new failed login attempt.
   * 
   * @param username
   * @param ipAddress
   */
  public FailedLoginAttempt(String username, String ipAddress) {
    this.setUsername(username);
    this.setIpAddress(ipAddress);
    this.save();
  }
  
  
  /**
   * Get the username used in the failed login attempt.
   * 
   * @return
   */
  public String getUsername() {
    return username;
  }

  /**
   * Set the username used in the failed login attempt.
   * 
   * @param username
   */
  private void setUsername(String username) {
    this.username = username;
  }

  
  /**
   * Get the ip address from where the bad login attempt originates.
   * 
   * @return
   */
  public String getIpAddress() {
    return ipAddress;
  }

  
  /**
   * Set the ip address from where the base login attempt originates.
   * 
   * @param ipAddress
   */
  private void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }
  
  private void save() {
    //TODO: Some database stuff to save this bad login attempt.
  }
  
}
