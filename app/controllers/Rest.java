package controllers;

import java.util.Map;

import models.ActivityModel;
import play.mvc.Controller;
import play.mvc.Result;

public class Rest extends Controller {
  
  public static Result createMessage() {
    final Map<String, String[]> values = request().body().asFormUrlEncoded();
    String activityJson = values.get("activity")[0];
    
    try {
      ActivityModel activity = new ActivityModel(activityJson);
      return ok(activity.toJSON());
    } catch (Exception e){
      return status(400);
    }
  }
  
}
