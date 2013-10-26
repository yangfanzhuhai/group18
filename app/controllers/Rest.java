package controllers;

import java.net.UnknownHostException;
import java.util.Map;

import com.mongodb.DBObject;
import com.mongodb.util.JSON;

import models.ActivityModel;
import models.MongoLink;
import play.mvc.Controller;
import play.mvc.Result;

public class Rest extends Controller {

	public static Result createMessage() {
		final Map<String, String[]> values = request().body()
				.asFormUrlEncoded();
		String activityJson = values.get("activity")[0];

		try {
			MongoLink mongoLink = new MongoLink();
			ActivityModel activity = new ActivityModel(activityJson);
			mongoLink.insertNews((DBObject)JSON.parse(activity.toJSON()));
			return ok(activity.toJSON());
		} catch (Exception e) {
			return status(400);
		}
	}
	
	 public static Result getActivities() throws UnknownHostException {
	    	MongoLink mongoLink = new MongoLink();
	        return ok(mongoLink.getNewsFeed(3).toString());
	    }

}
