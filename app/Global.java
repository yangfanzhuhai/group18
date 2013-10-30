import java.net.UnknownHostException;

import models.MongoLink;
import play.Application;
import play.GlobalSettings;


public class Global extends GlobalSettings {
	
	
	  @Override
	  public void onStart(Application app) {
	    try {
			MongoLink.MONGO_LINK = new MongoLink();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
	  }  

}
