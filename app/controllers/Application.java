package controllers;

import models.MongoLink;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.about;
import views.html.feed;
import views.html.login;
import views.html.register;
import views.html.profile;

public class Application extends Controller {
	
	/**
	 * 
	 * @param toggle 0 for News Feed, 1 for Task View
	 * @return News Feed/Task View if logged in.
	 * 		   Else render login screen.
	 */

	public static Result feed(String groupID, Integer toggle) {
		if (loggedIn()) {
			String userName = Rest.getUsernameFromSession();
			if(MongoLink.MONGO_LINK.isMember(userName, groupID)) {
				return ok(feed.render(groupID, toggle, userName));
			} else {
				return redirect(controllers.routes.Application.profile());
			}
			
		} else {
			return redirect(controllers.routes.Application.login());
		}
	}

	/**
	 * 
	 * @return Render the about page regardless
	 * 		   of login status.
	 */
	
	public static Result about() {
		return ok(about.render());
	}
	
	/**
	 * 
	 * @return Clear the current session.
	 * 		   and render login screen.
	 */
	public static Result login() {
		session().clear();
		return ok(login.render());
	}

	/**
	 * 
	 * @return Render the register page
	 * 		   regardless of login status.
	 */
	public static Result register() {
		return ok(register.render());
	}
	
	/**
	 * 
	 * @return Check if there is currently
	 * 		   a session value
	 */
	private static boolean loggedIn(){
		return Rest.getUsernameFromSession() != null;
	}

	public static Result profile() {
		if (loggedIn()) {
			String userName = Rest.getUsernameFromSession();
			return ok(profile.render(userName));
		} else {
			return redirect(controllers.routes.Application.login());
		}
		
	}

}
