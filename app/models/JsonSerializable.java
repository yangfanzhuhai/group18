package models;

import play.api.libs.json.JsValue;

/**
 * Classes implementing this interface can be serialized into a JSON string and
 * reconstructed from a JSON string.
 * 
 * @author Colin Stannard <colin.stannard@cbsindustries.com>
 */
public interface JsonSerializable<T extends JsonSerializable<T>> {

  /**
   * Serialize this model instance into a JSON string.
   * 
   * @return
   * @throws JsonException
   */
  public String toJson() throws JsonException;

  /**
   * Initialize this instance with the given JsValue (parsed JSON).
   * 
   * @param json
   * @return
   * @throws JsonException
   */
  public T initializeFromJson(JsValue json) throws JsonException;

  /**
   * Initialize this instance from the given JSON String.
   * 
   * @param json
   * @return
   * @throws JsonException
   */
  public T initializeFromJson(String json) throws JsonException;
}
