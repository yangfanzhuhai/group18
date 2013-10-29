package controllers;

import java.net.UnknownHostException;
import java.util.Map;
import models.DatabaseWriteException;
import models.JsonException;
import models.MongoLink;
import models.activity.Activity;
import play.mvc.Controller;
import play.mvc.Result;

public class Rest extends Controller {

  /**
   * 
   * @return
   */
  public static Result createMessage() {
    final Map<String, String[]> values = request().body().asFormUrlEncoded();

    // Construct an activity from the given JSON.
    Activity activity;
    try {
      String activityJson = values.get("activity")[0];
      activity = Activity.initializeFromJson(activityJson);
    } catch (JsonException e) {
      // Bad JSON request.
      return status(400);
    }
    
    // Save activity.
    try {
      activity.save();
    } catch (DatabaseWriteException e) {
      return status(500);
    }
    
    return ok(activity.toJson());
  }

  public static Result getActivities() throws UnknownHostException {
    MongoLink mongoLink = new MongoLink();
    return ok(mongoLink.getNewsFeed(3).toString());
  }

}
