package controllers;

import java.net.UnknownHostException;
import java.util.Map;
<<<<<<< HEAD
import models.DatabaseWriteException;
import models.JsonException;
import models.MongoLink;
import models.activity.Activity;
=======

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import com.google.gson.Gson;

import models.ActivityModel;
import models.MongoLink;
import models.User;
>>>>>>> master
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

<<<<<<< HEAD
  public static Result getActivities() throws UnknownHostException {
    MongoLink mongoLink = new MongoLink();
    return ok(mongoLink.getNewsFeed(3).toString());
  }
=======
		try {
			MongoLink mongoLink = new MongoLink();
			ActivityModel activity = new ActivityModel(activityJson);
			mongoLink.insertNews((DBObject)JSON.parse(activity.toJSON()));
			return ok();
		} catch (Exception e) {
			return status(400);
		}
	}
	
	public static Result getUser(){
		return ok(session("connected"));
	}
	
	 public static Result getActivities() throws UnknownHostException {
	    	MongoLink mongoLink = new MongoLink();
	        return ok(mongoLink.getNewsFeed().toString());
	    }
	 
	 public static Result getTasks() throws UnknownHostException {
	    	MongoLink mongoLink = new MongoLink();
	        return ok(mongoLink.getTasks().toString());
	    }
	 
	 public static Result registerUser(){
		 final Map<String, String[]> values = request().body()
					.asFormUrlEncoded();
			String credentialsJson = values.get("credentials")[0];

			try {
				MongoLink mongoLink = new MongoLink();
				if (mongoLink.registerNewUser((DBObject) JSON.parse(credentialsJson))){
				return ok();
				} else {
					return status(400);
				}
			} catch (Exception e) {
				return status(422);
			}
		 
	 }
	 
	 public static Result loginUser(){
		 final Map<String, String[]> values = request().body()
					.asFormUrlEncoded();
			String credentialsJson = values.get("credentials")[0];

			try {
				Gson gson = new Gson();
				User user = gson.fromJson(credentialsJson, User.class);
				String username = user.username;
				String password = user.password;
				MongoLink mongoLink = new MongoLink();
				if (mongoLink.checkLogin(username, password)){
					session("connected", username);
					return ok();
				} else {
					return status(400);
				}
			} catch (Exception e) {
				return status(422);
			}
		 
	 }
>>>>>>> master

}
