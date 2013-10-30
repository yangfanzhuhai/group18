package models;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

public class MongoLink {
	
	public static MongoLink MONGO_LINK;
	
	private final static String DBUSER = "testUser";
	private final static String DBPASS = "test";
	private final int DBPORT = 49868;
	private final String DBNAME = "heroku_app18716009";
	private final MongoClientURI DBURL = new MongoClientURI("mongodb://" + DBUSER + ":" + DBPASS + "@ds0" + DBPORT + ".mongolab.com:" + DBPORT + "/" + DBNAME);
	private MongoClient mongoClient;
	private static DB db;
	private static DBCollection newsFeed;
	private static DBCollection users;
	private static DBCollection gitRepos;
	
	/**Class linking from Java to the MongoDB**/
	public MongoLink() throws UnknownHostException {
		
		mongoClient = new MongoClient( DBURL );
		db = mongoClient.getDB( DBURL.getDatabase() );
		
		gitRepos = db.getCollection("gitRepositories");
		newsFeed = db.getCollection("newsFeed");
		users = db.getCollection("userAccounts");
	}

	//for testing
	public static void main(String[] args) throws UnknownHostException {
		MongoLink ml = new MongoLink();
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
	//	test();
		
		ArrayList<ArrayList<String>> list = ml.getNewsFeed(20);
		for(ArrayList<String> a : list) {
			for(String o : a) {
				System.out.println(o);
			}
		}
		
		if(ml.checkLogin(new BasicDBObject("username", "Piotr").append("password","pass")))
			System.out.println("Success");
		
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

	private static void test(){
		System.out.println(newsFeed.find(new BasicDBObject("object.objectType", "TASK")).toArray());
	}
	
	/**Returns a list of postLimit items containing key from collection**/
	private ArrayList<ArrayList<String>> dbFetch(DBCollection collection, BasicDBObject key, int postLimit) {
		
		ArrayList<DBObject> posts = (ArrayList<DBObject>) collection.find(key).sort(new BasicDBObject("_id", -1)).limit(postLimit).toArray();
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		try {
			int i = 0;
			while(i < posts.size())
			{
				ArrayList<String> replies = getReplies(posts.get(i).get("_id").toString());
				
				ActivityModel post = new ActivityModel(posts.get(i).toString());
				post.setID(posts.get(i).get("_id").toString());
				
				replies.add(0, post.toJSON());
				list.add(i, replies);
				
				i++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	
	/**Returns the a list of the last postLimit items from newsFeed collection with replies**/
	public ArrayList<ArrayList<String>> getNewsFeed(int postLimit) {
		return dbFetch(newsFeed, new BasicDBObject("target.messageID", ""), postLimit);
		
	}
	
	/**Default method to return last 20 items from newsFeed collection**/
	public ArrayList<ArrayList<String>> getNewsFeed(){
		return getNewsFeed(20);
	}
	
	/**Returns a list of postLimit tasks**/
	public ArrayList<ArrayList<String>> getTasks(int postLimit) {
		return dbFetch(newsFeed, new BasicDBObject("object.objectType", "TASK"), postLimit);
		
	}
	
	/**Default method to return last 20 tasks**/
	public ArrayList<ArrayList<String>> getTasks(){
		return getTasks(20);
	}
	
	/**Returns a list of all tasks **/
	public ArrayList<ArrayList<String>> getAllTasks(){
		return getTasks((int) newsFeed.count());
	}
	
	private ArrayList<String> getReferences(String id) throws ParseException {
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) newsFeed.find(new BasicDBObject("target.taskIDs", id)).toArray();
		ArrayList<String> retList = new ArrayList<String>();		
		
		
		for(DBObject o : list) {
			retList.add(new ActivityModel(o.toString()).toJSON());
		}
		
		return retList;
	}
	
	
	/**Inserts obj into newsFeed collection**/
	public String insertNews(DBObject obj) {
		
		newsFeed.insert(obj);
		return newsFeed.find(obj).toArray().get(0).get("_id").toString();
	}
	
	public boolean registerNewUser(DBObject obj) {
		
		int oldCount = (int) users.getCount();
		
		if(users.find(new BasicDBObject("username", obj.get("username"))).hasNext())
			return false;
		
		users.insert(obj);
		
		return (int) users.getCount() == oldCount + 1;
	}
	
	public boolean checkLogin(DBObject obj) {
		return checkLogin( obj.get("username").toString() ,obj.get("password").toString());
	}
	
	public boolean checkLogin(String username, String password) {
		return users.find(new BasicDBObject("username", username).append("password", password)).hasNext();
	}
	
	/**Shortcut to JSON format for testing inserts**/
	private BasicDBObject dbFormat(String published, String actorType, String dispName, String verb, String objType, String msg, String tar) {
		BasicDBObject news = new BasicDBObject("published", published);
		news.append("actor", new BasicDBObject("objectType", actorType).append("displayName", dispName));
		news.append("verb", verb);
		news.append("object", new BasicDBObject("objectType", objType).append("message", msg ));
		news.append("target", tar);
		return news;
	}
	
	private static ArrayList<String> getReplies(String id) throws ParseException {
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) newsFeed.find(new BasicDBObject("target.messageID", id)).toArray();
		ArrayList<String> retList = new ArrayList<String>();		
		
		
		for(DBObject o : list) {
			retList.add(new ActivityModel(o.toString()).toJSON());
		}
		
		return retList;
	}
}
