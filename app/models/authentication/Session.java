package models.authentication;

import java.util.Calendar;
import java.util.Date;

public class Session {
  
  private final Integer hoursUntilSessionExpires = 24;
  
  private Date expiryTime;
  private String token;
  private LoginAttempt loginAttempt;
  
  
  /**
   * Construct a new session from a login attempt.
   * 
   * @param loginAttempt
   */
  public Session(LoginAttempt loginAttempt) {
    this.setLoginAttempt(loginAttempt);
    
    this.refreshExpiryTime();
  }
  
  
  /**
   * Find a session given the token assigned to the session.
   * 
   * @param token
   * @return
   */
  public static Session findSessionFromToken(String token) {
    
    // TODO: Some database stuff
    
    return null;
  }



  /**
   * Get the token assigned to the session.
   * 
   * @return
   */
  public String getToken() {
    return token;
  }
  
  
  /**
   * Get the time this session expires.
   * 
   * @return
   */
  public Date getExpiryTime() {
    return expiryTime;
  }


  /**
   * Set the time this session expires.
   * 
   * @param expiryTime
   */
  private void setExpiryTime(Date expiryTime) {
    this.expiryTime = expiryTime;
  }

  
  /**
   * Refresh the time this session will expire.
   */
  public void refreshExpiryTime() {
    
    // Calculate new expiry time;
    Date now = new Date();
    Calendar cal = Calendar.getInstance();
    cal.setTime(now);
    cal.add(Calendar.HOUR_OF_DAY, this.hoursUntilSessionExpires);
    
    this.setExpiryTime(cal.getTime());
  }
  
  
  /**
   * Has this session expired?
   * 
   * @return
   */
  public boolean expired() {
    Date now = new Date();
    return this.getExpiryTime().before(now);
  }

  
  /**
   * Get the login attempt that initialized this session.
   * 
   * @return
   */
  public LoginAttempt getLoginAttempt() {
    return loginAttempt;
  }


  /**
   * Set the login attempt that initialized this session.
   * 
   * @param loginAttempt
   */
  private void setLoginAttempt(LoginAttempt loginAttempt) {
    this.loginAttempt = loginAttempt;
  }
  
  
}
