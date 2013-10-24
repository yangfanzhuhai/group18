package controllers;

import java.util.Date;

import models.ActivityModel;
import models.MessageObject;
import models.PersonActor;
import models.TargetModel;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Jenkins and Heroku are working!"));
    }
    
    public static Result getActivities() {
    	PersonActor actor = new PersonActor("Martin Smith");
    	MessageObject message = new MessageObject("Hello JSON");
    	TargetModel target = new TargetModel();
    	ActivityModel activity = new ActivityModel(new Date(), actor, "said",
    	    message, target);
    	
    	
        return ok(activity.toJSON());
    }

}
