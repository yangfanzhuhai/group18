package controllers;

import java.net.UnknownHostException;
import java.util.Date;

import models.ActivityModel;
import models.MessageObject;
import models.MongoLink;
import models.PersonActor;
import models.TargetModel;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.index;

public class Application extends Controller {

    public static Result index() {
        return ok(index.render("Jenkins and Heroku are working!"));
    }
    
    public static Result getActivities() throws UnknownHostException {
    	MongoLink mongoLink = new MongoLink();
        return ok(mongoLink.getNewsFeed(3).toString());
    }

}
