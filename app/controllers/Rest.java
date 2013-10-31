package controllers;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.Map;


import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import com.google.gson.Gson;

import models.ActivityModel;
import models.MongoLink;
import models.User;
import play.mvc.Controller;
import play.mvc.Result;

public class Rest extends Controller {

	public static Result createMessage() {
		String activityJson = getValueFromRequest("activity");

		try {
			ActivityModel activity = new ActivityModel(activityJson);
			MongoLink.MONGO_LINK.insertNews((DBObject)JSON.parse(activity.toJSON()));
			return ok();
		} catch (Exception e) {
			return status(400);
		}
	}
	
	public static Result getUser(){
		return ok(session("connected"));
	}
	
	 public static Result getActivities() throws UnknownHostException {
	        return ok(MongoLink.MONGO_LINK.getNewsFeed().toString());
	    }
	 
	 public static Result getTasks() throws UnknownHostException {
	        return ok(MongoLink.MONGO_LINK.getTasks().toString());
	    }
	 
	 public static Result getAllTasks() throws UnknownHostException, ParseException {
		 	return ok(MongoLink.MONGO_LINK.getAllTasksWithoutReplies().toString());
	 }
	 
	 public static Result registerUser(){
		 String credentialsJson = getValueFromRequest("credentials");

			try {
				if (MongoLink.MONGO_LINK.registerNewUser((DBObject) JSON.parse(credentialsJson))){
				return ok();
				} else {
					return status(400);
				}
			} catch (Exception e) {
				return status(422);
			}
		 
	 }
	 
	 public static Result loginUser(){
		 String credentialsJson = getValueFromRequest("credentials");

			try {
				Gson gson = new Gson();
				User user = gson.fromJson(credentialsJson, User.class);
				String username = user.username;
				String password = user.password;
				if (MongoLink.MONGO_LINK.checkLogin(username, password)){
					session("connected", username);
					return ok();
				} else {
					return status(400);
				}
			} catch (Exception e) {
				return status(422);
			}
		 
	 }

	/**
	 * @return
	 */
	private static String getValueFromRequest(String value) {
		final Map<String, String[]> values = request().body()
					.asFormUrlEncoded();
			String stringValue = values.get(value)[0];
		return stringValue;
	}

}
