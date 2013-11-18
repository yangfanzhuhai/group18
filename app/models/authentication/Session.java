package models.authentication;

import java.util.Calendar;
import java.util.Date;

public class Session {
  
  private final Integer hoursUntilSessionExpires = 24;
  
  private Date expiryTime;
  private String token;
  private String email; 
  
  /**
   * Construct a new session from a login attempt.
   * 
   * @param loginAttempt
   */
  public Session(LoginAttempt loginAttempt) {
    setEmail(loginAttempt.getEmail());   
    this.refreshExpiryTime();
    MongoLink.MONGO_LINK.createNewSession(this);
  }
  
  
  public Session(String jsonString) {
    JsValue obj = Json.parse(jsonString);
    Date expirytime = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy",
                     Locale.ENGLISH).parse(ActivityModel.cleanJsonStringValue(
                     obj.$bslash("expiryTime").toString()));
    setExpiryTime(expirytime);
    setEmail(ActivityModel.cleanJsonStringValue(obj.$bslash("email").toString()));
    setToken(ActivityModel.cleanJsonStringValue(obj.$bslash("_id").toString()));
  }

  /** Converts the Session to a JSON object.
   * 
   * @return String JSON
   */
  public String toJSON() {
    
    return "{" + "\"expiryTime\" : \"" + getExpiryTime().toString()
        + "\", \"email\" : " + getEmail() + ", \"_id\" : \""
        + getToken() + "\"}";
  }


  /**
   * Find a session given the token assigned to the session.
   * 
   * @param token
   * @return
   */
  public static Session findSessionFromToken(String token) {
    return MongoLink.MONGO_LINK.getSession(token);
  }


  /**
   * Set the token assigned to the session.
   * 
   * 
   */
  public void setToken(String token) {
    this.token = token;
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
   * Get the email that initialized this session.
   * 
   * @return
   */
  public String getEmail() {
    return email;
  }


  /**
   * Set the email that initialized this session.
   * 
   * @param String
   */
  private void setEmail(String email) {
    this.email = email;
  }
  
  
}
