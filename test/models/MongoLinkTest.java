package models;

import static org.junit.Assert.fail;

import java.net.UnknownHostException;

import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoLinkTest {

	private final static String DBUSER = "testUser";
	private final static String DBPASS = "test";
	private final int DBPORT = 49868;
	private final String DBNAME = "heroku_app18716009";
	private final MongoClientURI DBURL = new MongoClientURI("mongodb://" + DBUSER + ":" + DBPASS + "@ds0" + DBPORT + ".mongolab.com:" + DBPORT + "/" + DBNAME);
	
	@Test
	public void testConnection()
	{
		try {
			new MongoClient( DBURL ).getDB(DBURL.getDatabase());
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		}
	}
	
	@Test
	public void testInsert()
	{
		try {
			DB db = new MongoClient( DBURL ).getDB(DBURL.getDatabase());
			db.createCollection("testCollection", null);
			DBCollection coll = db.getCollection("testCollection");
			
			coll.insert(new BasicDBObject("parametername", "value"));
			
			if(coll.count() != 1)
				fail("Item was not inserted correctly");
			
			coll.drop();
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		}
	}
	
	@Test
	public void testInsertedCorrectly()
	{
		try {
			DB db = new MongoClient( DBURL ).getDB(DBURL.getDatabase());
			db.createCollection("testCollection", null);
			DBCollection coll = db.getCollection("testCollection");
			
			coll.insert(new BasicDBObject("name", "Bob"));
			
			if(!"Bob".equals(coll.findOne().get("name")))
				fail("Item was not inserted correctly");
			
			coll.drop();
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		}
	}
	
	@Test
	public void testManyInsertedCorrectly()
	{
		try {
			DB db = new MongoClient( DBURL ).getDB(DBURL.getDatabase());
			db.createCollection("testCollection", null);
			DBCollection coll = db.getCollection("testCollection");
			
			coll.insert(new BasicDBObject("name", "Bob"));
			coll.insert(new BasicDBObject("name", "Fred"));
			coll.insert(new BasicDBObject("name", "Bob"));
			coll.insert(new BasicDBObject("name", "Ted"));
			coll.insert(new BasicDBObject("name", "Bob"));
			
			if(coll.find(new BasicDBObject("name", "Bob")).count() != 3)
				fail("Items were not inserted correctly");
			
			coll.drop();
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		}
	}
}