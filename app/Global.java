import java.net.UnknownHostException;

import models.MongoLink;
import play.Application;
import play.GlobalSettings;
import play.api.Play;


public class Global extends GlobalSettings {
	
	
	  @Override
	  public void onStart(Application app) {
	    try {
			MongoLink.MONGO_LINK = new MongoLink(Play.isDev(Play.current()));
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	  }  

}