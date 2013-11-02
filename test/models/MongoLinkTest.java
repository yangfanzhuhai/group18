package models;

import static org.junit.Assert.fail;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;
import org.junit.Test;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;

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
		DBCollection coll = null;
		try {
			DB db = new MongoClient( DBURL ).getDB(DBURL.getDatabase());
			db.createCollection("testCollection", null);
			coll = db.getCollection("testCollection");
			
			coll.insert(new BasicDBObject("parametername", "value"));
			
			if(coll.count() != 1)
				fail("Item was not inserted correctly");
			
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		} finally {
			if(coll != null)
				coll.drop();
		}
	}
	
	@Test
	public void testInsertedCorrectly()
	{
		DBCollection coll = null;
		try {
			DB db = new MongoClient( DBURL ).getDB(DBURL.getDatabase());
			db.createCollection("testCollection", null);
			coll = db.getCollection("testCollection");
			
			coll.insert(new BasicDBObject("name", "Bob"));
			
			if(!"Bob".equals(coll.findOne().get("name")))
				fail("Item was not inserted correctly");
			
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		} finally {
			if(coll != null)
				coll.drop();
		}
	}
	
	@Test
	public void testManyInsertedCorrectly()
	{
		DBCollection coll = null;
		try {
			DB db = new MongoClient( DBURL ).getDB(DBURL.getDatabase());
			db.createCollection("testCollection", null);
			coll = db.getCollection("testCollection");
			
			coll.insert(new BasicDBObject("name", "Bob"));
			coll.insert(new BasicDBObject("name", "Fred"));
			coll.insert(new BasicDBObject("name", "Bob"));
			coll.insert(new BasicDBObject("name", "Ted"));
			coll.insert(new BasicDBObject("name", "Bob"));
			
			if(coll.find(new BasicDBObject("name", "Bob")).count() != 3)
				fail("Items were not inserted correctly");
			
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		} finally {
			if(coll != null)
				coll.drop();
		}
	}
	
	/**
	 * Tests that a newly registered user gets recorded
	 * in the database and can login
	 */
	@Test
	public void testUserRegisterAndLogin()
	{
		DBCollection coll = null;
		try {
			DB db = new MongoClient( DBURL ).getDB(DBURL.getDatabase());
			db.createCollection("testCollection", null);
			coll = db.getCollection("testCollection");
			
			DBObject obj = new BasicDBObject("username", "Bob").append("password", "pass1");
			
			if(!registerNewUser(coll, obj))
					fail("Registering failed");
			
			if(!checkLogin(coll, obj))
				fail("Register and Login test failed");
			
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		} finally {
			if(coll != null)
				coll.drop();
		}
	}
	
	@Test
	public void testChangingTaskPriority()
	{
		DBCollection coll = null;
		try {
			DB db = new MongoClient( DBURL ).getDB(DBURL.getDatabase());
			db.createCollection("testCollection", null);
			coll = db.getCollection("testCollection");
			
			
			coll.insert(createNewTask());
			
			DBObject addedTask = coll.findOne();
			if((Integer) ((DBObject) addedTask.get("object")).get("priority") != 1)
				fail("Task was not added correctly");
			
			updatePriority(coll, addedTask.get("_id").toString(), 2);
			
			if((Integer) ((DBObject) coll.findOne().get("object")).get("priority") != 2)
				fail("Task priority was not updated correctly");
			
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		} finally {
			if(coll != null)
				coll.drop();
		}
	}
	
	@Test
	public void testSortingByPriority()
	{
		DBCollection coll = null;
		try {
			DB db = new MongoClient( DBURL ).getDB(DBURL.getDatabase());
			db.createCollection("testCollection", null);
			coll = db.getCollection("testCollection");
			
			coll.insert(createNewTask(1));
			coll.insert(createNewTask(5));
			coll.insert(createNewTask(1));
			coll.insert(createNewTask(3));
			coll.insert(createNewTask(2));
			coll.insert(createNewTask(4));
			coll.insert(createNewTask(1));
			
			List<DBObject> byPrio = getTasksByPriority(coll);
			
			if((Integer) ((DBObject) byPrio.get(6).get("object")).get("priority") != 1
					|| (Integer) ((DBObject) byPrio.get(3).get("object")).get("priority") != 2
					|| (Integer) ((DBObject) byPrio.get(0).get("object")).get("priority") != 5)
				fail("Tasks weren't sorted by priority correctly");
				
			
		} catch (UnknownHostException e) {
			fail("Connection to database failed");
		} finally {
			if(coll != null)
				coll.drop();
		}
	}
	
	private boolean registerNewUser(DBCollection coll, DBObject obj) {
		
		int oldCount = (int) coll.getCount();
		
		if(coll.find(new BasicDBObject("username", obj.get("username"))).hasNext())
			return false;
		
		coll.insert(obj);
		
		return (int) coll.getCount() == oldCount + 1;
	}
	
	private boolean checkLogin(DBCollection coll, DBObject obj) {
		return coll.find(new BasicDBObject("username", obj.get("username")).append("password", obj.get("password"))).hasNext();
	}
	
	private void updatePriority(DBCollection coll, String id, int priority) {
		coll.update(QueryBuilder.start("_id").is(new ObjectId(id)).get(), new BasicDBObject("object", new BasicDBObject("priority", priority)));
	}
	
	private DBObject createNewTask() {
		return new BasicDBObject("object", new BasicDBObject("priority", 1));
	}
	
	private DBObject createNewTask(int priority) {
		return new BasicDBObject("object", new BasicDBObject("priority", priority));
	}
	
	private List<DBObject> getTasksByPriority(DBCollection coll) {
		return coll.find().sort(QueryBuilder.start("object.priority").is(-1).get()).toArray();
	}
}
