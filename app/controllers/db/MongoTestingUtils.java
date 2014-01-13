package controllers.db;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.QueryBuilder;

class MongoTestingUtils {
	
	private final static String DBUSER = "testUser";
	private final static String DBPASS = "test";
	private final int DBPORT = 49868;
	private final String DBNAME = "heroku_app18716009";
	private final MongoClientURI DBURL = new MongoClientURI("mongodb://" + DBUSER + ":" + DBPASS + "@ds0" + DBPORT + ".mongolab.com:" + DBPORT + "/" + DBNAME);
	private MongoClient mongoClient;
	private static DB db;
	private static DBCollection users;
	private static DBCollection groups;
	private static DBCollection sessions;
	
	public MongoTestingUtils() throws UnknownHostException 
	{
		mongoClient = new MongoClient( DBURL );
		db = mongoClient.getDB( DBURL.getDatabase() );
		
		users = db.getCollection("userAccountsV2");
		groups = db.getCollection("groups");
		sessions = db.getCollection("session");
	}
	
	//for testing
	public static void main(String[] args) throws UnknownHostException, ParseException {
		MongoLink ml = new MongoLink();
		
		
	//	ml.a();
	//	ml.addFieldToAllGroupCollections("actor.objectType", "PERSON");
		
	//	ml.a();
	//	ml.addFieldToAllGroupCollections("actor.username", "");
	//	ml.removeFieldFromCollection(db.getCollection("TestingFields"), "simple");
	//	groups = db.getCollection("DEVgroups");

	//	ml.removeFieldFromAllGroupCollections("alias");
	//	ml.addFieldToAllGroupCollections(QueryBuilder.start("object.objectType").is("TASK").get(),"object.alias", "");
		
		//String json = "{ \"localAccount\": { \"name\": \"Luke\", \"photo_url\" : \"\", \"email\" : \"abc\" , \"password\": \"pass\" } ," +
			//	"\"fbAccount\": {\"name\": \"Luke\", \"photo_url\" : \"\", \"profile_id\" : \"f123\" }, " +
				//"\"ghAccount\" : {\"name\": \"Luke\", \"photo_url\" : \"\", \"email\" : \"abc\" , \"gravatar_id\": \"g321\", \"html_url\": \"www.git.com\"}}";
		
	//	String json = "{ \"localAccount\": {}," +
	//			"\"fbAccount\": {\"name\": \"Piotr\", \"photo_url\" : \"\", \"profile_id\" : \"654123\"}, " +
	//			"\"ghAccount\" : {}}";
		
	//	String fbccount = "{\"fbAccount\": {\"name\": \"Piotr\", \"photo_url\" : \"\", \"profile_id\" : \"654123\"}}";
	//	ml.linkAccount("5287aacfb7606861f8bc37f2", (DBObject) JSON.parse(fbccount));
		
	/*	String json = "{\"ghAccount\" : {\"name\": \"Luke\", \"photo_url\" : \"\", \"email\" : \"abc\" , \"gravatar_id\": \"g321\", \"html_url\": \"www.git.com\"}}";
		
		Gson gson = new Gson();
		UserModel model = gson.fromJson(json, UserModel.class);
			System.out.println(model.toJSON());

		
		System.out.println(((DBObject) JSON.parse(model.toJSON())).toString());
		
		if(ml.registerOrLogin((DBObject) JSON.parse(model.toJSON())))
			System.out.println("Registered/Logged in");
		
	/*	ArrayList<ArrayList<String>> a = ml.getTaskDetails("QuantumCheese", "528002a5e4b0e00e6f371e80");
		
		for(ArrayList<String> x : a)
		{
			System.out.println();
			System.out.println();
			for(String y : x)
			{
				System.out.println(y);
			}
		}
	*/
	/*	
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < 100; i++)
		{
			ml.getGroups("Piotr");
		}
		float totalTime = System.currentTimeMillis() - startTime;
		System.out.println("Total time taken : " + totalTime/1000 + " (average: " + totalTime/100000 + ")");
		
		//boolean auth = db.authenticate(DBUSER, DBPASS.toCharArray());

		//Prints last 20 items of newsFeed
		
	/*	try {
			System.out.println(new ActivityModel("{\"published\" : \""
			          + "234/324/423"
			          + "\", "
			          + "\"actor\" : "
			          + "{\"objectType\" : \"PERSON\", \"displayName\" : "
			          + "\"" + "abc" + "\"}, "
			          + "\"verb\" : \"said\", \"object\" : {\"objectType\""
			          + " : \"MESSAGE\" , \"message\" : " 
			          + "\"" + "Hello" + "\"}, \"target\" : \"\"} ").toJSON());
		} catch (ParseException e) {
			System.out.println("PArse exception");
		}*/
		
	/*	float totalTime = 0;
		
		for(int i = 0; i < 100; i++) {
			long startTime = System.currentTimeMillis();
			ml.getNewsFeed("newsFeed");
			totalTime += System.currentTimeMillis() - startTime;
		}
		
		float avgTime = totalTime / 100000;
		System.out.println("Total time taken : " + totalTime/1000 + " (average: " + avgTime + ")");
/*
		for(ArrayList<String> a : list) {
			for(String o : a) {
				System.out.println(o);
			}
		}
		
		System.out.println("SELECTING NEXT NEWS");
		
	//	list = ml.getNextNews("5273d01c646021e813bcd12a");
		
		for(ArrayList<String> a : list) {
			for(String o : a) {
				System.out.println(o);
			}
		}
		
		System.out.println("GETTING TASKS WITH STATUS TO_DO");
		
	//	list = ml.getTasksWithStatus("TO_DO");
		for(ArrayList<String> a : list) {
			for(String o : a) {
				System.out.println(o);
			}
		}
		
		if(ml.checkLogin(new BasicDBObject("username", "Piotr").append("password","pass")))
			System.out.println("Success");
		
		System.out.println("Adding new project");
		*/
	/*	groups = db.createCollection("temp", null);
		long startTime = System.currentTimeMillis();
		for(int i = 0; i < 200; i++)
		{
			ml.addNewProject("test Hello spaces", new GroupMember("Piotr", "Piotr", "www.dot.com"));
		}
		float totalTime = System.currentTimeMillis() - startTime;
		System.out.println("Total time taken : " + totalTime/1000 + " (average: " + totalTime/200000 + ")");
		groups.drop();
	//	ml.registerNewUser(new BasicDBObject("username", "Rob").append("password", "pass2"));
		
	//	if(ml.checkLogin("Rob", "pass2"))
	//		System.out.println("Success2");
		
		//Checks inserting into newsFeed and prints new latest 20
	//	System.out.println(ml.insertNews(ml.dbFormat("Today", "PERSON", "Luke", "whispered", "MESSAGE", "What is up", "")));
//		if(insertSuccess) {
//			System.out.println("\n Insert Success");
//			list = ml.getNewsFeed(20);
//			for(DBObject o : list) {
//				System.out.println(o);
//			}
//		} else {
//			System.out.println("insert failed");
//		}*/
	}
	
	/** TODO TEST
	 * 
	 *  Deletes any replies that are in the database, but the post to which they reply has been deleted
	 *  This is a bug that can occur because of two people replying and deleting at the same time
	 *  I imagine this will be run quite rarely as a part of a scheduled clean-up of DB
	 */
	private void deleteFloatingReplies()
	{
		List<DBObject> groupList = groups.find().toArray();
		
		for(DBObject group : groupList)
		{
			DBCollection c = getGroupColl((String) group.get("customID"));
			List<DBObject> list = c.find(QueryBuilder.start("target.messageID").notEquals("").get()).toArray();
			
			for(DBObject reply : list)
			{
				if(c.findOne(MongoUtils.queryID(((DBObject)reply.get("target")).get("messageID").toString())) == null) {
					c.remove(reply);
				}
			}
			
		}
	}
	
	private static DBObject jenkins() {
		return new BasicDBObject("displayName", "Jenkins").append("objectType", "JENKINS");
	}
	
	private static DBObject actor(String username, String photo_url, String objectType) {
		return new BasicDBObject("displayName", username).append("objectType", objectType).append("username", username).append("photo_url", photo_url);
	}
	
	private void a() {
		List<DBObject> groupList = groups.find().toArray();
		
		for(DBObject group : groupList)
		{
		//	DBCollection c = getGroupColl((String) group.get("customID"));
			DBCollection c = getGroupColl("QuantumCheese");
			List<DBObject> list = c.find(QueryBuilder.start("object.objectType").notEquals("JENKINS").get()).toArray();
			for(DBObject o : list)
			{				
				o.put("actor", actor(((DBObject) o.get("actor")).get("username").toString(),((DBObject) o.get("actor")).get("photo_url").toString(),((DBObject) o.get("actor")).get("objectType").toString()));
				c.save(o);
			//	o.put("actor", jenkins());
			//	c.save(o);
			}
		}
	}
	
	private void addFieldToCollection(DBCollection coll, String fieldName, Object defaultValue) {
		addFieldToCollection(coll, new BasicDBObject(), fieldName, defaultValue);
	}
	
	private void addFieldToCollection(DBCollection coll, DBObject query, String fieldName, Object defaultValue) {
		coll.update(query, new BasicDBObject("$set", new BasicDBObject(fieldName, defaultValue)), false, true);
	}
	
	private void addFieldToAllGroupCollections(String fieldName, Object defaultValue) {
		addFieldToAllGroupCollections(new BasicDBObject(), fieldName, defaultValue);	
	}
	
	private void addFieldToAllGroupCollections(DBObject query, String fieldName, Object defaultValue) {
		List<DBObject> groupList = groups.find().toArray();
		
		for(DBObject group : groupList)
		{
			addFieldToCollection(getGroupColl((String) group.get("customID")), query, fieldName, defaultValue);
		}
	}
	
	private void removeFieldFromCollection(DBCollection coll, String fieldName) {
		removeFieldFromCollection(coll, new BasicDBObject(), fieldName);
	}
	
	private void removeFieldFromCollection(DBCollection coll, DBObject query, String fieldName) {
		coll.update(query, new BasicDBObject("$unset", new BasicDBObject(fieldName, "")), false, true);
	}
	
	private void removeFieldFromAllGroupCollections(String fieldName) {
		removeFieldFromAllGroupCollections(new BasicDBObject(), fieldName);	
	}
	
	private void removeFieldFromAllGroupCollections(DBObject query, String fieldName) {
		List<DBObject> groupList = groups.find().toArray();
		
		for(DBObject group : groupList)
		{
			removeFieldFromCollection(getGroupColl((String) group.get("customID")), query, fieldName);
		}
	}
	
	/**
	 * @param customID - ID of the current project/group
	 * @return Database collection belonging to that project/group
	 */
	private DBCollection getGroupColl(String customID) {
		return db.getCollection(customID);
	}

}
