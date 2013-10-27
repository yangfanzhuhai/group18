package models;

import static org.junit.Assert.fail;

import java.net.UnknownHostException;

import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoLinkTest {

	
	@Test
	public void testConnection()
	{
		String DBUSER = "testUser";
		String DBPASS = "test";
		int DBPORT = 49868;
		String DBNAME = "heroku_app18716009";
		MongoClientURI DBURL = new MongoClientURI("mongodb://" + DBUSER + ":" + DBPASS + "@ds0" + DBPORT + ".mongolab.com:" + DBPORT + "/" + DBNAME);
		try {
			MongoClient mongoClient = new MongoClient( DBURL );
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		}
	}
}
