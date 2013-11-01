package models;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;

import org.bson.types.ObjectId;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.QueryBuilder;

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
	
	private DBObject reverseSort = QueryBuilder.start("_id").is(-1).get();
	
	/**Class linking from Java to the MongoDB**/
	public MongoLink(boolean devMode) throws UnknownHostException {
		
		mongoClient = new MongoClient( DBURL );
		db = mongoClient.getDB( DBURL.getDatabase() );
		
		if(devMode)
		{
			gitRepos = db.getCollection("DEVgitRepositories");
			newsFeed = db.getCollection("DEVnewsFeed");
			users = db.getCollection("DEVuserAccounts");
		}
		else
		{
			gitRepos = db.getCollection("gitRepositories");
			newsFeed = db.getCollection("newsFeed");
			users = db.getCollection("userAccounts");
		}

	}

	//for testing
	public static void main(String[] args) throws UnknownHostException {
		MongoLink ml = new MongoLink(true);
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
		
	//	long totalTime = 0;
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		list = ml.getTasksByPriority();
	//	for(int i = 0; i < 100; i++) {
	//		long startTime = System.currentTimeMillis();
	//		list = ml.getNewsFeed(20);
	//		totalTime += System.currentTimeMillis() - startTime;
	//	}
		
	//	float avgTime = totalTime / 100000;

		for(ArrayList<String> a : list) {
			for(String o : a) {
				System.out.println(o);
			}
		}
//		System.out.println("Get news feed took " + avgTime + " seconds on average.");
		try {
			System.out.println("REFERENCES");
			for(String a : ml.getReferencedBy("52715499b7608d8e9d710f40")) {
					System.out.println(a);
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	/** 	 * 
	 * @param collection - Database collection on which to perform query
	 * @param key - Criteria to use for search
	 * @param postLimit - Maximum number of items to fetch
	 * @return List of Lists containing news feed posts, with their replies
	 */
	private ArrayList<ArrayList<String>> dbFetch(DBObject searchCriteria, DBObject sortCriteria, int postLimit) {
		
		ArrayList<DBObject> posts = (ArrayList<DBObject>) newsFeed.find(searchCriteria).sort(sortCriteria).limit(postLimit).toArray();
		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		
		try {
			int i = 0;
			while(i < posts.size())
			{
				ArrayList<String> tempList = getReplies(posts.get(i).get("_id").toString());
				
				ActivityModel post = new ActivityModel(posts.get(i).toString());
				post.setID(posts.get(i).get("_id").toString());
				
				tempList.add(0, post.toJSON());
				tempList.addAll(1, getReferences(posts.get(i)));
				retList.add(i, tempList);
				
				i++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retList;
	}
	
	
	/**Returns the a list of the last postLimit items from newsFeed collection with replies**/
	public ArrayList<ArrayList<String>> getNewsFeed(int postLimit) {
		return dbFetch(QueryBuilder.start("target.messageID").is("").get(), reverseSort, postLimit);
		
	}
	
	/**Default method to return last 20 items from newsFeed collection**/
	public ArrayList<ArrayList<String>> getNewsFeed(){
		return getNewsFeed(20);
	}
	
	/**Returns a list of postLimit tasks**/
	public ArrayList<ArrayList<String>> getTasks(int postLimit) {
		return dbFetch(QueryBuilder.start("object.objectType").is("TASK").get(), reverseSort, postLimit);
		
	}
	
	/**Default method to return last 20 tasks**/
	public ArrayList<ArrayList<String>> getTasks(){
		return getTasks(20);
	}
	
	/**Inserts obj into newsFeed collection**/
	public String insertNews(DBObject obj) {
		
		newsFeed.insert(obj);
		return newsFeed.find(obj).toArray().get(0).get("_id").toString();
	}
	
	/**
	 * Adds new user to the database, only if their username is not already
	 * in the database
	 * 
	 * @param obj - Object containing new user's data
	 * @return true if user was added, false if not
	 */
	public boolean registerNewUser(DBObject obj) {
		
		int oldCount = (int) users.getCount();
		
		if(users.find(QueryBuilder.start("username").is(obj.get("username")).get()).hasNext())
			return false;
		
		users.insert(obj);
		
		return (int) users.getCount() == oldCount + 1;
	}
	
	/** Checks whether the username and password supplied in 'obj' match an
	 * entry in the database
	 * 
	 * @param obj - Object containing username and password
	 * @return true if parameters match some entry in the database, false if not
	 */
	public boolean checkLogin(DBObject obj) {
		return checkLogin( obj.get("username").toString() ,obj.get("password").toString());
	}
	
	public boolean checkLogin(String username, String password) {
		return users.find(QueryBuilder.start("username").is(username).and("password").is(password).get()).hasNext();
	}
	
	/**Returns a list of all tasks (only the tasks, no replies or associated objects) 
	 * @throws ParseException **/
	public ArrayList<String> getAllTasksWithoutReplies() throws ParseException{
		ArrayList<String> tasks = getItemsWithoutReferences(QueryBuilder.start("object.objectType").is("TASK").get());
		Collections.reverse(tasks);
		return tasks;
	}
	
	public ArrayList<ArrayList<String>> getTasksByPriority(int postLimit) {
		return dbFetch(QueryBuilder.start("object.objectType").is("TASK").get(), QueryBuilder.start("object.priority").is(-1).get(), postLimit);
	}
	
	public ArrayList<ArrayList<String>> getTasksByPriority() {
		return getTasksByPriority(20);
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
	
	/** 
	 * @param obj - News Feed object
	 * @return ArrayList respresenting all the tasks that are referenced by the given object
	 * @throws ParseException
	 */
	private ArrayList<String> getReferences(DBObject obj) throws ParseException {
		
		BasicDBList taskIDs = (BasicDBList) ((DBObject)obj.get("target")).get("taskIDs");
		
		if(taskIDs.isEmpty()) return new ArrayList<String>();
		
		ObjectId[] taskIDObjs = new ObjectId[taskIDs.size()];
		for(int i = 0; i < taskIDObjs.length; i++)
		{
			taskIDObjs[i] = new ObjectId(taskIDs.get(i).toString());
		}
		
		return getItemsWithoutReferences(QueryBuilder.start("_id").in(taskIDObjs).get());
	}
	
	/**
	 * @param id - ID string of the task
	 * @return ArrayList of all the news feed items that reference the given task
	 * @throws ParseException
	 */
	private ArrayList<String> getReferencedBy(String id) throws ParseException {
		
		return getItemsWithoutReferences(QueryBuilder.start("target.taskIDs").in(new String[]{id}).get());
	}
	
	/**
	 * @param id - ID string of the news feed post
	 * @return ArrayList of all the replies to that post
	 * @throws ParseException
	 */
	private ArrayList<String> getReplies(String id) throws ParseException {
		
		return getItemsWithReferences(QueryBuilder.start("target.messageID").is(id).get());
	}
	
	/** Generic method to find list of objects that satisfy the given query
	 * and any task they reference
	 * 
	 * @param query - DBObject containing the information about the query
	 * @return ArrayList of objects
	 * @throws ParseException
	 */
	private ArrayList<String> getItemsWithReferences(DBObject query) throws ParseException {
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) newsFeed.find(query).toArray();
		ArrayList<String> retList = new ArrayList<String>();		
		
		
		for(DBObject o : list) {
			
			ActivityModel am = new ActivityModel(o.toString());
			am.setID(o.get("_id").toString());
			
			retList.add(am.toJSON());
			retList.addAll(getReferences(o));
		}
		
		return retList;
	}
	
	/** Generic method to find list of objects that satisfy the given query
	 * 
	 * @param query - DBObject containing the information about the query
	 * @return ArrayList of objects
	 * @throws ParseException
	 */
	private ArrayList<String> getItemsWithoutReferences(DBObject query) throws ParseException {
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) newsFeed.find(query).toArray();
		ArrayList<String> retList = new ArrayList<String>();		
		
		
		for(DBObject o : list) {
			
			ActivityModel am = new ActivityModel(o.toString());
			am.setID(o.get("_id").toString());
			
			retList.add(am.toJSON());
		}
		
		return retList;
	}
}
