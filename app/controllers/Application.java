package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.about;
import views.html.builds;
import views.html.gits;
import views.html.index;
import views.html.login;
import views.html.register;
import views.html.tasks;

public class Application extends Controller {

	public static Result index() {
		if (session("connected") != null) {
			return ok(index.render());
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

	public static Result tasks() {
		if (session("connected") != null) {
			return ok(tasks.render());
		} else {
			return ok(login.render());
		}
	}

	public static Result register() {
		return ok(register.render());
	}

}
