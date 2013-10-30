package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.about;
import views.html.builds;
import views.html.gits;
import views.html.feed;
import views.html.login;
import views.html.register;

public class Application extends Controller {

	public static Result feed(Integer toggle) {
		if (session("connected") != null) {
			String userName = session("connected");
			return ok(feed.render(toggle, userName));
		} else {
			return ok(login.render());
		}
	}

	public static Result about() {
		return ok(about.render());
	}

	public static Result builds() {
		if (session("connected") != null) {
			return ok(builds.render());
		} else {
			return ok(login.render());
		}
	}

	public static Result gits() {
		if (session("connected") != null) {
			return ok(gits.render());
		} else {
			return ok(login.render());
		}
	}

	public static Result login() {
		session().clear();
		return ok(login.render());
	}


	public static Result register() {
		return ok(register.render());
	}

}
