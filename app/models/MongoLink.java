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
import com.mongodb.MongoException;
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
		list = ml.getNewsFeed();
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
		
		System.out.println("DELETING A POST");
		
		ml.deletePost("52743c59c2e69027b2302da2");
		
		System.out.println("NEW NEWS FEED");
		list = ml.getNewsFeed();
		for(ArrayList<String> a : list) {
			for(String o : a) {
				System.out.println(o);
			}
		}
		
		System.out.println("GETTING TASKS WITH STATUS TO_DO");
		
		list = ml.getTasksWithStatus("TO_DO");
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
	
	
	/**Inserts obj into newsFeed collection**/
	public String insertNews(DBObject obj) {
		
		newsFeed.insert(obj);
		return newsFeed.findOne(obj).get("_id").toString();
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
		
		if(users.findOne(QueryBuilder.start("username").is(obj.get("username")).get()) != null)
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
	
	/** Checks the validity of the given username and password
	 * 
	 * @param username - Username entered by user
	 * @param password - Password entered by user
	 * @return True if there is an entry in the database with that exact username and password, False otherwise
	 */
	public boolean checkLogin(String username, String password) {
		return users.findOne(QueryBuilder.start("username").is(username).and("password").is(password).get()) != null;
	}
	
	public void updateStatusOrPriority(DBObject obj) throws MongoException {
		if(obj.containsField("status"))
		{
			updateStatus(obj.get("id").toString(), obj.get("status").toString());
		}
		else if(obj.containsField("priority"))
		{
			updatePriority(obj.get("id").toString(), (Integer) obj.get("priority"));
		}
		else
			throw new MongoException("Neither field status or priority exist. Update failed");
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

	/**Returns a list of all tasks (only the tasks, no replies or associated objects) 
	 * @throws ParseException **/
	public ArrayList<String> getAllTasksWithoutReplies() throws ParseException{
		ArrayList<String> tasks = getItemsWithoutReferences(QueryBuilder.start("object.objectType").is("TASK").get());
		Collections.reverse(tasks);
		return tasks;
	}
	
	/**
	 * @param postLimit - Number of tasks to fetch from the database
	 * @return List of tasks (with replies and associated objects) sorted by priority in descending order
	 */
	public ArrayList<ArrayList<String>> getTasksByPriority(int postLimit) {
		return dbFetch(QueryBuilder.start("object.objectType").is("TASK").get(), QueryBuilder.start("object.priority").is(-1).get(), postLimit);
	}
	
	/**
	 * @return List of 20 tasks (with replies and associated objects) sorted by priority in descending order
	 */
	public ArrayList<ArrayList<String>> getTasksByPriority() {
		return getTasksByPriority(20);
	}
	
	/**
	 * @param status - Status of task
	 * @return List of all the tasks with the given status, sorted from newest to oldest, with replies and associated objects
	 */
	public ArrayList<ArrayList<String>> getTasksWithStatus(String status) {
		return dbFetch(QueryBuilder.start("object.objectType").is("TASK").and("object.status").is(status).get(), reverseSort, noLimit());
	}
	
	/**
	 * @param postLimit - Maximum number of git commits to fetch
	 * @return List of git commits (with replies and associated objects) sorted from newest to oldest
	 */
	public ArrayList<ArrayList<String>> getGitCommits(int postLimit) {
		return dbFetch(QueryBuilder.start("object.objectType").is("GIT").get(), reverseSort, postLimit);
	}
	
	/**
	 * @return List of (at most) 20 git commits (with replies and associated objects) sorted from newest to oldest
	 */
	public ArrayList<ArrayList<String>> getGitCommits() {
		return getGitCommits(20);
	}
	
	public void deletePost(DBObject obj) {
		deletePost(obj.get("id").toString());
	}
	
	private void deletePost(String id) {
		deleteReplies(id);
		newsFeed.remove(new BasicDBObject("_id", new ObjectId(id)));
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
	
	private void deleteReplies(String id) {
		newsFeed.remove(QueryBuilder.start("target.messageID").is(new ObjectId(id)).get());
	}
	
	/** Updates the status of the task with ID id
	 * 
	 * @param id - ID of task to be updated
	 * @param status - New status value to be set
	 */
	private void updateStatus(String id, String status) {
		updateObject(id, new BasicDBObject("$set", new BasicDBObject("object.status", status)));
	}

	/** Updates the priority of the task with ID id
	 * 
	 * @param id - ID of task to be updated
	 * @param priority - New priority value to be set
	 */
	private void updatePriority(String id, int priority) {
		updateObject(id, new BasicDBObject("$set", new BasicDBObject("object.priority", priority)));
	}

	/** Generic method which updates object with ID 'id' using the parameters in 'updateWith'
	 * 
	 * @param id - ID of object to be updated
	 * @param updateWith - Information on what the update should change
	 */
	private void updateObject(String id, DBObject updateWith) {
		newsFeed.update(QueryBuilder.start("_id").is(new ObjectId(id)).get(), updateWith);
	}

	private int noLimit() {
		return (int) newsFeed.getCount();
	}
}
