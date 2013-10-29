package models;

/**
 * An exception relating to JSON.
 * 
 * @author Colin Stannard <colin.stannard@cbsindustries.com>
 */
public class JsonException extends Exception {

  private static final long serialVersionUID = -5116665422097852165L;
  private String message;

  /**
   * Insist on a message being provided.
   * 
   * @param message
   */
  public JsonException(String message) {
    this.message = message;
  }
  
  /**
   * Override message with our custom message.
   */
  @Override
  public String getMessage() {
    return this.message;
  }
  
}
