package models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import play.api.libs.json.JsValue;

/**
 * Utility functions relating to JSON.
 * 
 * @author Colin Stannard <colin.stannard@cbsindustries.com>
 */
public class JsonUtility {

  /**
   * Remove quotes from JSON string.
   * 
   * @param stringValue
   * 
   * @return String without quotes.
   */
  public static String cleanJsonStringValue(String stringValue) {
    return stringValue.replaceAll("^\"|\"$", "");
  }

  public static JsValue readJsValue(JsValue json, String attribute) {
    return json.$bslash(attribute);
  }

  public static String readString(JsValue json, String attribute) {
    return JsonUtility.cleanJsonStringValue(JsonUtility.readJsValue(json,
        attribute).toString());
  }

  public static Integer readInteger(JsValue json, String attribute) {
    return Integer.parseInt(JsonUtility.readString(json, attribute));
  }

  public static Date readDate(JsValue json, String attribute)
      throws JsonException {
    String dateString = JsonUtility.readString(json, attribute);
    String dateFormat = "EEE MMM dd HH:mm:ss zzz yyyy";
    try {
      return new SimpleDateFormat(dateFormat, Locale.ENGLISH).parse(dateString);
    } catch (ParseException e) {
      throw new JsonException("Date parse error");
    }
  }
}
