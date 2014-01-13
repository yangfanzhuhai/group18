package controllers;

import controllers.db.MongoLink;
import models.LocalAccount;
import models.UserModel;
import play.mvc.Controller;
import play.mvc.Result;
import scala.collection.mutable.Seq;
import views.html.about;
import views.html.feed;
import views.html.login;
import views.html.profile;
import views.html.register;
import views.html.profile;

public class Application extends Controller {
	
	/**
	 * 
	 * @param toggle 0 for News Feed, 1 for Task View
	 * @return News Feed/Task View if logged in.
	 * 		   Else render login screen.
	 */
	public static Result feed(String groupID, String toggle) {
		
		if (loggedIn() && isValidToggle(toggle)) {
			String userName = Rest.getUsernameFromSession();
			if(MongoLink.MONGO_LINK.isMember(userName, groupID)) {
				UserModel user = MongoLink.MONGO_LINK.getUserFromUsername(userName);
				LocalAccount localAccount = user.getLocalAccount();
				String displayName = localAccount.getName();
				String photo_url = localAccount.getPhoto_url();
				session("groupid", groupID);
				return ok(feed.render(groupID, toggle, userName, displayName, photo_url));
			} else {
				return redirect(controllers.routes.Application.profile());
			}
			
		} else {
			return redirect(controllers.routes.Application.login());
		}
	}
	
	private static boolean isValidToggle(String toggle) {
		//waiting on db methods to implment
		return true;
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
			UserModel user = MongoLink.MONGO_LINK.getUserFromUsername(userName);
			LocalAccount localAccount = user.getLocalAccount();
			String displayName = localAccount.getName();
			String photo_url = localAccount.getPhoto_url();
			return ok(profile.render(userName, displayName, photo_url));
		} else {
			return redirect(controllers.routes.Application.login());
		}
		
	}

}
