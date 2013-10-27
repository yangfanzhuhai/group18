package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.about;
import views.html.builds;
import views.html.gits;
import views.html.index;
import views.html.login;
import views.html.tasks;
import views.html.register;


public class Application extends Controller {

    public static Result index() {
        return ok(index.render());
    }
    
    public static Result about() {
        return ok(about.render());
    }
    
    public static Result builds() {
        return ok(builds.render());
    }
    
    public static Result gits() {
        return ok(gits.render());
    }
    
    public static Result login() {
        return ok(login.render());
    }
    
    public static Result tasks() {
        return ok(tasks.render());
    }
    
    public static Result register() {
        return ok(register.render());
    }
    

}
