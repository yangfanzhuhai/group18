package controllers.db;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.QueryBuilder;

/** Class used for testing database methods.
 *  Contains some additional methods which are used to test
 *  database methods or manage the database itself.
 *  None of the code in this class is ever used
 *  to service the end-user.
 * 
 * @author Piotr Tokaj
 */
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
	 * Deletes 'number' of the newest posts (and their replies) in a specific collection
	 * @param collection - Collection to delete from
	 * @param number - Number of posts to delete
	 */
	private void deletePosts(DBCollection collection, int number) {
		List<DBObject> list = collection.find(QueryBuilder.start("target.messageID").is("").get()).sort(MongoUtils.reverseSort).limit(number).toArray();
		
		
		for (DBObject obj : list) {
			MongoMethods.deletePost(collection, obj.get("_id").toString());
		}
	}
	
	private void deleteAllPosts(DBCollection collection) {
		deletePosts(collection, (int) collection.getCount());
	}
	
	/**
	 * @param customID - ID of the current project/group
	 * @return Database collection belonging to that project/group
	 */
	private DBCollection getGroupColl(String customID) {
		return db.getCollection(customID);
	}

}
