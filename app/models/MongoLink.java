package models;

import models.authentication.*;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import com.google.gson.Gson;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.QueryBuilder;
import com.mongodb.util.JSON;

public class MongoLink {
	
	public static MongoLink MONGO_LINK;
	
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
	
	private DBObject reverseSort = QueryBuilder.start("_id").is(-1).get();
	
	/**Class linking from Java to the MongoDB**/
	public MongoLink(boolean devMode) throws UnknownHostException {
		
		mongoClient = new MongoClient( DBURL );
		db = mongoClient.getDB( DBURL.getDatabase() );
		
		users = db.getCollection("userAccountsV2");
		groups = db.getCollection("groups");
		sessions = db.getCollection("session");

	}

	//for testing
	public static void main(String[] args) throws UnknownHostException, ParseException {
		MongoLink ml = new MongoLink(true);
		
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
	
	private static DBObject jenkins() {
		return new BasicDBObject("displayName", "Jenkins").append("objectType", "JENKINS");
	}
	
	private static DBObject actor(String username, String photo_url, String objectType) {
		return new BasicDBObject("displayName", username).append("objectType", objectType).append("username", username).append("photo_url", photo_url);
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
	 * @param newsFeed Collection from which to fetch data
	 * @param searchCriteria - Criteria to use for search
	 * @param sortCriteria - Criteria by which to sort results
	 * @param postLimit - Maximum number of items to fetch
	 * @return List of Lists containing news feed posts, with their replies and references
	 */
	private ArrayList<ArrayList<String>> dbFetch(DBCollection newsFeed, DBObject searchCriteria, DBObject sortCriteria, int postLimit) {
		
		ArrayList<DBObject> posts = (ArrayList<DBObject>) newsFeed.find(searchCriteria).sort(sortCriteria).limit(postLimit).toArray();
		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		
		try {
			int i = 0;
			while(i < posts.size())
			{
				ArrayList<String> tempList = getReplies(newsFeed, posts.get(i).get("_id").toString());
				ActivityModel post = ActivityModel.activityModelGson.fromJson(posts.get(i).toString(), ActivityModel.class);
				post.setID(posts.get(i).get("_id").toString());
				
				tempList.add(0, post.toJSON());
				tempList.addAll(1, getReferences(newsFeed, posts.get(i)));
				retList.add(i, tempList);
				
				i++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return retList;
	}
	
	
	/** Inserts given object into appropriate collection
	 * 
	 * @param customID - ID of group collection
	 * @param obj - Object to be inserted
	 * @return TODO DO WE NEED A RETURN?
	 */
	public String insertNews(String customID, DBObject obj) {
		
		DBCollection newsFeed = getGroupColl(customID);
		
		newsFeed.insert(obj);
		return newsFeed.findOne(obj).get("_id").toString();
	}
	
	/** Creates a new project in the database
	 * 
	 * @param obj - Object containing all the necessary information of the new project
	 * @return True if added correctly, False otherwise
	 */
	public boolean addNewProject(DBObject obj) {
		
		int oldCount = (int) groups.getCount();
		String customID = obj.get("customID").toString();
		
		groups.insert(obj);
		db.createCollection(customID, null);
		
		return (int) groups.getCount() == oldCount + 1 && db.getCollection(customID) != null;
	}
	
	/** Takes the parameters, generates a customID, creates a new project,
	 * adds it to the database, and creates a collection for the project
	 * 
	 * @param name - Name of the project
	 * @param creator - Username of the person who created the project
	 * @return True if added correctly, False otherwise
	 */
	public boolean addNewProject(String name, GroupMember creator) {
		Gson gson = new Gson();
		String creatorJson = gson.toJson(creator);
		BasicDBObject creatorObject = (BasicDBObject) JSON.parse(creatorJson);
		
		return addNewProject(createNewEmptyProject(generateCustomID(name), name, creatorObject));
	}
	
	/** Adds any amount of users to a given project. Any user who is already a
	 * member of the project will not be added again.
	 * 
	 * @param customID - ID of the project
	 * @param users - String or String array of users to be added to the project
	 */
	public void addUsersToProject(String customID, String username) {
		
		UserModel user = getUserFromUsername(username);
		String displayName = user.getLocalAccount().getName();
		String photo_url = user.getLocalAccount().getPhoto_url();
		GroupMember groupMember = new GroupMember(username, displayName, photo_url);
		Gson gson = new Gson();
		String memberJson = gson.toJson(groupMember);
		BasicDBObject memberObject = (BasicDBObject) JSON.parse(memberJson);
		groups.update(QueryBuilder.start("customID").is(customID).get(),
					new BasicDBObject("$addToSet", new BasicDBObject("members", memberObject)));
	}
	
	/** Removes a user from the given project
	 * 
	 * @param customID - ID of the project
	 * @param users - User to be removed
	 */
	public void removeFromProject(String customID, String ... users) {
		groups.update(queryForProject(customID), new BasicDBObject("$pullAll", new BasicDBObject("members", users)));
	}
	
	/** Changes the project's display name
	 * 
	 * @param customID - ID of the project
	 * @param name - The new display name
	 */
	public void changeProjectName(String customID, String name) {
		groups.update(queryForProject(customID), new BasicDBObject("name", name));
	}
	
	/** Deletes the entire project from the database.
	 * 
	 * @param customID - ID of the project to be removed
	 */
	public void deleteProject(String customID) {
		groups.remove(queryForProject(customID));
		getGroupColl(customID).drop();
	}
	
	/**
	 * Adds new user to the database, only if their username is not already
	 * in the database
	 * TODO DELETE
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
	
	public boolean registerOrLogin(DBObject obj) {
		
		if(obj.containsField("localAccount") && !"{}".equals(obj.get("localAccount").toString().replaceAll("\\s+",""))) {
			
			if(users.findOne(QueryBuilder.start("localAccount.email").is(((DBObject) obj.get("localAccount")).get("email")).get()) == null)
			{
				int oldCount = (int) users.getCount();
				
				users.insert(obj);
				return (int) users.getCount() == oldCount + 1;
			}
			return false;
		}
		else if(obj.containsField("fbAccount") && !"{}".equals(obj.get("fbAccount").toString().replaceAll("\\s+",""))) {
			
			if(users.findOne(QueryBuilder.start("fbAccount.profile_id").is(((DBObject) obj.get("fbAccount")).get("profile_id")).get()) == null)
			{
				int oldCount = (int) users.getCount();
				
				users.insert(obj);
				return (int) users.getCount() == oldCount + 1;
			}
			return true;
		}
		else if(obj.containsField("ghAccount") && !"{}".equals(obj.get("ghAccount").toString().replaceAll("\\s+",""))) {
			
			if(users.findOne(QueryBuilder.start("ghAccount.html_url").is(((DBObject) obj.get("ghAccount")).get("html_url")).get()) == null)
			{
				int oldCount = (int) users.getCount();
				
				users.insert(obj);
				return (int) users.getCount() == oldCount + 1;
			}
			
			return true;
		}
		else {
			return false;
		}
	}
	
	/** Checks whether the username and password supplied in 'obj' match an
	 * entry in the database
	 * 
	 * @param obj - Object containing username and password
	 * @return true if parameters match some entry in the database, false if not
	 */
	public boolean checkLogin(DBObject obj) {
		return checkLogin(obj.get("username").toString() ,((DBObject) obj.get("localAccount")).get("password").toString());
	}
	
	public boolean checkLogin(String username, String password) {
		return checkLoginWithUsername(username, password) || checkLoginWithEmail(username, password);
	}
	
	/** Checks the validity of the given username and password
	 * 
	 * @param email - Username entered by user
	 * @param password - Password entered by user
	 * @return True if there is an entry in the database with that exact username and password, False otherwise
	 */
	
		
	public boolean checkLoginWithUsername(String username, String password) {
		DBObject user = users.findOne(QueryBuilder.start("username").is(username).get());
		return checkCredentials(user, password);
	}
	
	/** Checks the validity of the given email and password
	 * 
	 * @param username - Email entered by user
	 * @param password - Password entered by user
	 * @return True if there is an entry in the database with that exact email and password, False otherwise
	 */
	public boolean checkLoginWithEmail(String email, String password) {
		DBObject user = users.findOne(QueryBuilder.start("localAccount.email").is(email).get());
		return checkCredentials(user, password);
	}

	private boolean checkCredentials(DBObject user, String password) {
		if(user != null) {
			String hashedPassword = ((DBObject) user.get("localAccount")).get("password").toString();
			return BCrypt.checkpw(password, hashedPassword);
			//return true;
		}
		return false;
	}
	
	public void linkAccount(String userID, DBObject obj) {
		users.update(QueryBuilder.start("_id").is(new ObjectId(userID)).get(), new BasicDBObject("$set",obj));
		//TODO check if merge needed on database side
	}
	
	/** Retrieves the username that links to the email used for login
	 * 
	 * @param email
	 * @return username
	 */
	public String getUsernameFromEmail(String email) {
		return (users.findOne(QueryBuilder.start("localAccount.email").is(email).get()).toString());
	}
	
	public UserModel getUserFromUsername(String userName){
		Gson gson = new Gson();
		String userJson = (users.findOne(QueryBuilder.start("username").is(userName).get())).toString();
		return gson.fromJson(userJson, UserModel.class);
	}
	
	
	/*
	* Creates a new session entry
	*/ 
	public boolean createNewSession(Session session) {

		int oldCount = (int) sessions.getCount();

		DBObject obj = (DBObject) JSON.parse(session.toJSON());
		
		sessions.insert(obj);

		ObjectId token = (ObjectId)obj.get( "_id" );

		session.setToken(token.toString());
		
		return (int) sessions.getCount() == oldCount + 1;
	}


	/*
	* Returns a session with the given token
	*/ 
	public Session getSession(String token) throws ParseException {
		ObjectId tokenID = new ObjectId(token);
		DBObject query = QueryBuilder.start("_id").is(tokenID).get();
		ArrayList<DBObject> list = (ArrayList<DBObject>) sessions.find(query).toArray();
 
		return new Session(list.get(0).toString());
	}
	
	/**
	 * @param userID - ID of the current user
	 * @return A list of groups which the member is part of
	 */
	public ArrayList<String> getGroups(String username) {
		ArrayList<String> retList = new ArrayList<String>();
		List<DBObject> list = groups.find(QueryBuilder.start("members.username").is(username).get()).toArray();
	
		for(DBObject obj : list){
			retList.add(obj.toString());
		}
			
		return retList;
	}
	
	public ArrayList<String> getInfoAboutUsers(String ... usernames) {
		List<DBObject> list = users.find(QueryBuilder.start("username").in(usernames).get()).toArray();	
		ArrayList<String> retList = new ArrayList<String>();
		
		for(DBObject o : list)
		{
			retList.add(o.toString());
		}
	
		return retList;
	}

	/**
	 * @param username - Username of the current user
	 * @param groupID - GroupID to check against
	 * @return True if user is a member of 'groupID', False otherwise
	 */
	public boolean isMember(String username, String groupID) {
		@SuppressWarnings("unchecked")
		List<DBObject> members = (List<DBObject>) groups.findOne(queryForProject(groupID)).get("members");
		
		for(DBObject member : members){
			if (member.get("username").equals(username)) return true;
		}
		
		return false;
	}
	
	public void addFBImage(String username, String url) {
		users.update(QueryBuilder.start("username").is(username).get(), new BasicDBObject("$set", new BasicDBObject("fb_image", url)));
	}
	
	/** Method which changes either the status or priority of a task
	 * 
	 * @param customID - ID of collection to be used
	 * @param obj - Object containing ID of task to be altered plus a status or priority field
	 * which indicates what should be changed and how
	 * @throws MongoException
	 */
	public void updateStatusOrPriority(String customID, DBObject obj) throws MongoException {
		if(obj.containsField("status"))
		{
			updateStatus(getGroupColl(customID), obj.get("id").toString(), obj.get("status").toString());
		}
		else if(obj.containsField("priority"))
		{
			updatePriority(getGroupColl(customID), obj.get("id").toString(), (Integer) obj.get("priority"));
		}
		else
			throw new MongoException("Neither field status or priority exist. Update failed");
	}
	
	 /** 
	 * @param customID - ID of collection to be used
	 * @param postLimit - Maximum number of items to be fetched
	 * @return List of the last 'postLimit' items from given collection with replies and references
	 */
	public ArrayList<ArrayList<String>> getNewsFeed(String customID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("target.messageID").is("").get(), reverseSort, postLimit);
		
	}

	 /**
	 * @param customID - ID of collection to be used
	 * @return List of the last 20 items from given collection with replies and references
	 */
	public ArrayList<ArrayList<String>> getNewsFeed(String customID){
		return getNewsFeed(customID, 20);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param lastID - String ID of the last post currently in news feed
	 * @param postLimit - Maximum number of posts to fetch
	 * @return An array of news feed posts (with replies and references) 
	 * 			that were posted after the post with the given ID
	 */
	public ArrayList<ArrayList<String>> getNextNews(String customID, String lastID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("target.messageID").is("").and("_id").lessThan(new ObjectId(lastID)).get(), reverseSort, postLimit);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param lastID - String ID of the last post currently in news feed
	 * @return An array of (at most 20) news feed posts (with replies and references) 
	 * 			that were posted after the post with the given ID
	 */
	public ArrayList<ArrayList<String>> getNextNews(String customID, String lastID) {
		return getNextNews(customID, lastID, 20);
	}
	
	public ArrayList<ArrayList<String>> getNewNews(String customID, String newestID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("target.messageID").is("").and("_id").greaterThan(new ObjectId(newestID)).get(), reverseSort, postLimit);
	}
	
	public ArrayList<ArrayList<String>> getNewNews(String customID, String newestID) {
		return getNewNews(customID, newestID, 20);
	}

	 /** 
	 * @param customID - ID of collection to be used
	 * @param postLimit - Maximum number of items to be fetched
	 * @return List of the last 'postLimit' items from given collection with replies and references
	 */
	public ArrayList<ArrayList<String>> getTasks(String customID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").get(), reverseSort, postLimit);
		
	}

	/**
	 * @param customID - ID of collection to be used
	 * @return List of the last 20 items from given collection with replies and references
	 */
	public ArrayList<ArrayList<String>> getTasks(String customID){
		return getTasks(customID, 20);
	}

	/**
	 * @param customID - ID of collection to be used
	 * @param lastID - String ID of the last post currently in task list
	 * @param postLimit - Maximum number of tasks to fetch
	 * @return An array of task posts (with replies and references) 
	 * 			that were posted after the task with the given ID
	 */
	public ArrayList<ArrayList<String>> getNextTasks(String customID, String lastID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").and("_id").lessThan(new ObjectId(lastID)).get(), reverseSort, postLimit);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param lastID - String ID of the last post currently in task list
	 * @return An array of (at most 20) task posts (with replies and references) 
	 * 			that were posted after the task with the given ID
	 */
	public ArrayList<ArrayList<String>> getNextTasks(String customID, String lastID) {
		return getNextTasks(customID, lastID, 20);
	}


	/**
	 * @param customID - ID of collection to be used
	 * @return A list of all tasks (only the tasks, no replies or associated objects)
	 *  in order from newest to oldest 
	 * @throws ParseException **/
	public ArrayList<String> getAllTasksWithoutReplies(String customID) throws ParseException{
		return getItemsWithoutReferences(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").get(), reverseSort);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @return A list of all tasks (only the tasks, no replies or associated objects)
	 *  in alphabetical order (CASE SENSITIVE)
	 * @throws ParseException
	 */
	public ArrayList<String> getAllTasksByName(String customID) throws ParseException {
		return getItemsWithoutReferences(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").get(), QueryBuilder.start("object.name").is(1).get());
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param postLimit - Number of tasks to fetch from the database
	 * @return List of tasks (with replies and associated objects) sorted by priority in descending order
	 */
	public ArrayList<ArrayList<String>> getTasksByPriority(String customID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").get(), QueryBuilder.start("object.priority").is(-1).get(), postLimit);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @return List of 20 tasks (with replies and associated objects) sorted by priority in descending order
	 */
	public ArrayList<ArrayList<String>> getTasksByPriority(String customID) {
		return getTasksByPriority(customID, 20);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param status - Status of task
	 * @return List of all the tasks with the given status, sorted from newest to oldest, with replies and associated objects
	 */
	public ArrayList<ArrayList<String>> getTasksWithStatus(String customID, String status) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").and("object.status").is(status).get(), reverseSort, noLimit(getGroupColl(customID)));
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param postLimit - Maximum number of git commits to fetch
	 * @return List of git commits (with replies and associated objects) sorted from newest to oldest
	 */
	public ArrayList<ArrayList<String>> getGitCommits(String customID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("GIT").get(), reverseSort, postLimit);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @return List of (at most) 20 git commits (with replies and associated objects) sorted from newest to oldest
	 */
	public ArrayList<ArrayList<String>> getGitCommits(String customID) {
		return getGitCommits(customID, 20);
	}

	/**
	 * @param customID - ID of collection to be used
	 * @param lastID - String ID of the last commit currently in git commits page
	 * @param postLimit - Maximum number of commits to fetch
	 * @return An array of git commit posts (with replies and references) 
	 * 			that were posted after the commit with the given ID
	 */
	public ArrayList<ArrayList<String>> getNextGitCommits(String customID, String lastID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("GIT").and("_id").lessThan(new ObjectId(lastID)).get(), reverseSort, postLimit);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param lastID - String ID of the last commit currently in git commits page
	 * @return An array of (at most 20) git commit posts (with replies and references) 
	 * 			that were posted after the commit with the given ID
	 */
	public ArrayList<ArrayList<String>> getNextGitCommits(String customID, String lastID) {
		return getNextGitCommits(customID, lastID, 20);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param postLimit - Maximum number of builds to fetch
	 * @return List of Jenkins builds (with replies and associated objects) sorted from newest to oldest
	 */
	public ArrayList<ArrayList<String>> getJenkinsBuilds(String customID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("JENKINS").get(), reverseSort, postLimit);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @return List of (at most) 20 jenkins builds (with replies and associated objects) sorted from newest to oldest
	 */
	public ArrayList<ArrayList<String>> getJenkinsBuilds(String customID) {
		return getJenkinsBuilds(customID, 20);
	}

	/**
	 * @param customID - ID of collection to be used
	 * @param lastID - String ID of the last build currently in builds page
	 * @param postLimit - Maximum number of builds to fetch
	 * @return An array of build posts (with replies and references) 
	 * 			that were posted after the build with the given ID
	 */
	public ArrayList<ArrayList<String>> getNextJenkinsBuilds(String customID, String lastID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("JENKINS").and("_id").lessThan(new ObjectId(lastID)).get(), reverseSort, postLimit);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param lastID - String ID of the last build currently in builds page
	 * @return An array of (at most 20) build posts (with replies and references) 
	 * 			that were posted after the build with the given ID
	 */
	public ArrayList<ArrayList<String>> getNextJenkinsBuilds(String customID, String lastID) {
		return getNextJenkinsBuilds(customID, lastID, 20);
	}
	
	/**
	 * @return List of all the usernames in the Database
	 */
	public ArrayList<String> getUsers() {
		List<DBObject> allusers = users.find().toArray();
		ArrayList<String> retList = new ArrayList<String>();
		
		for(DBObject obj : allusers)
		{
			retList.add(obj.get("username").toString());
		}
		return retList;
	}
	
	public String getDisplayName(String id) {
		
		String ret = null;
		DBObject user = users.findOne(QueryBuilder.start("_id").is(new ObjectId(id)));
		if(!"{}".equals(user.get("localAccount").toString().replaceAll("\\s+","")))
		{
			ret = ((DBObject) user.get("localAccount")).get("name").toString();
		}
		else if(!"{}".equals(user.get("fbAccount").toString().replaceAll("\\s+","")))
		{
			ret = ((DBObject) user.get("fbAccount")).get("name").toString();
		}
		else
		{
			ret = ((DBObject) user.get("ghAccount")).get("name").toString();
		}
		
		return ret;
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param obj - Object representing the news feed post to be deleted.
	 * All replies corresponding to that object will also be deleted
	 */
	public void deletePost(String customID, DBObject obj) {
		
		deletePost(getGroupColl(customID), obj.get("id").toString());
	}
	
	public ArrayList<ArrayList<String>> getTaskDetails(String groupID, String id) throws ParseException {
		return getTaskDetails(getGroupColl(groupID), id);
	}
	
	/** 
	 * @param coll - Collection to be used
	 * @param obj - News Feed object
	 * @return ArrayList representing all the tasks that are referenced by the given object
	 * @throws ParseException
	 */
	private ArrayList<String> getReferences(DBCollection coll, DBObject obj) throws ParseException {
		
		BasicDBList taskIDs = (BasicDBList) ((DBObject)obj.get("target")).get("taskIDs");
		
		if(taskIDs.isEmpty()) return new ArrayList<String>();
		
		ObjectId[] taskIDObjs = new ObjectId[taskIDs.size()];
		for(int i = 0; i < taskIDObjs.length; i++)
		{
			taskIDObjs[i] = new ObjectId(taskIDs.get(i).toString());
		}
		
		return getItemsWithoutReferences(coll, QueryBuilder.start("_id").in(taskIDObjs).get(), null);
	}
	
	public ArrayList<String> getReferencedBy(String groupID, String id) throws ParseException {
		
		return getReferencedBy(getGroupColl(groupID), id);
	}
	
	/**
	 * @param coll - Collection to be used
	 * @param id - ID string of the task
	 * @return ArrayList of all the news feed items that reference the given task
	 * @throws ParseException
	 */
	private ArrayList<String> getReferencedBy(DBCollection coll, String id) throws ParseException {
		
		return getItemsWithoutReferences(coll, QueryBuilder.start("target.taskIDs").in(new String[]{id}).get(), reverseSort);
	}
	
	private ArrayList<ArrayList<String>> getTaskDetails(DBCollection coll, String id) throws ParseException {
		
		Set<ArrayList<String>> retList = new LinkedHashSet<ArrayList<String>>();
		ArrayList<DBObject> referencingItems = (ArrayList<DBObject>) coll.find(QueryBuilder.start("target.taskIDs").in(new String[]{id}).get()).sort(reverseSort).toArray();
		
		for(DBObject r : referencingItems)
		{
			retList.add(getEntireTopic(coll, r));
		}
		
		return new ArrayList<ArrayList<String>>(retList);
	}
	
	private ArrayList<String> getEntireTopic(DBCollection coll, DBObject referencingPost) throws ParseException {
		
		ArrayList<String> retList = new ArrayList<String>();
		
		String targetPost = ((DBObject) referencingPost.get("target")).get("messageID").toString();
		DBObject tempPost;
		
		if("".equals(targetPost))
		{
			tempPost = referencingPost;
		}
		else
		{
			tempPost = coll.findOne(QueryBuilder.start("_id").is(new ObjectId(targetPost)).get());
		}
		
		String id = tempPost.get("_id").toString();
		ActivityModel am = ActivityModel.activityModelGson.fromJson(tempPost.toString(), ActivityModel.class);
		
		// TODO This sort of thing should not be needed once activity model is refactored
		am.setID(id);
		retList.add(am.toJSON());
		retList.addAll(getReplies(coll, id));
		
		return retList;
	}
	
	/**
	 * @param coll - Collection to be used
	 * @param id - ID string of the news feed post
	 * @return ArrayList of all the replies to that post
	 * @throws ParseException
	 */
	private ArrayList<String> getReplies(DBCollection coll, String id) throws ParseException {
		
		return getItemsWithoutReferences(coll, QueryBuilder.start("target.messageID").is(id).get(), null);
	}
	
	/** Generic method to find list of objects that satisfy the given query
	 * and any task they reference
	 * @param collection - Collection to be used
	 * @param query - DBObject containing the information about the query
	 * 
	 * @return ArrayList of objects
	 * @throws ParseException
	 */
	private ArrayList<String> getItemsWithReferences(DBCollection collection, DBObject query) throws ParseException {
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) collection.find(query).toArray();
		ArrayList<String> retList = new ArrayList<String>();		
		
		
		for(DBObject o : list) {
			
			ActivityModel am = ActivityModel.activityModelGson.fromJson(o.toString(), ActivityModel.class);
			am.setID(o.get("_id").toString());
			
			retList.add(am.toJSON());
			retList.addAll(getReferences(collection, o));
		}
		
		return retList;
	}
	
	/** Generic method to find list of objects that satisfy the given query
	 * @param collection - Collection to be used
	 * @param query - DBObject containing the information about the query
	 * 
	 * @return ArrayList of objects
	 * @throws ParseException
	 */
	private ArrayList<String> getItemsWithoutReferences(DBCollection collection, DBObject query, DBObject sortKey) throws ParseException {
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) collection.find(query).sort(sortKey).toArray();
		ArrayList<String> retList = new ArrayList<String>();		
		
		
		for(DBObject o : list) {
			
			ActivityModel am = ActivityModel.activityModelGson.fromJson(o.toString(), ActivityModel.class);
			am.setID(o.get("_id").toString());
			
			retList.add(am.toJSON());
		}
		
		return retList;
	}
	
	/**
	 * @param coll - Collection be used
	 * @param id - ID of the object to be deleted, along with all its replies
	 */
	private void deletePost(DBCollection coll, String id) {
		deleteReplies(coll, id);
		coll.remove(new BasicDBObject("_id", new ObjectId(id)));
	}

	/**
	 * @param coll - Collection be used
	 * @param id - ID of the object which will have its replies deleted
	 */
	private void deleteReplies(DBCollection coll, String id) {
		coll.remove(QueryBuilder.start("target.messageID").is(id).get());
	}
	
	/** Updates the status of the task with ID id
	 * @param coll - Collection be used
	 * @param id - ID of task to be updated
	 * @param status - New status value to be set
	 */
	private void updateStatus(DBCollection coll, String id, String status) {
		updateObject(coll, id, new BasicDBObject("$set", new BasicDBObject("object.status", status)));
	}

	/** Updates the priority of the task with ID id
	 * @param coll - Collection be used
	 * @param id - ID of task to be updated
	 * @param priority - New priority value to be set
	 */
	private void updatePriority(DBCollection coll, String id, int priority) {
		updateObject(coll, id, new BasicDBObject("$set", new BasicDBObject("object.priority", priority)));
	}

	/** Generic method which updates object with ID 'id' using the parameters in 'updateWith'
	 * @param collection - Collection be used
	 * @param id - ID of object to be updated
	 * @param updateWith - Information on what the update should change
	 */
	private void updateObject(DBCollection collection, String id, DBObject updateWith) {
		collection.update(QueryBuilder.start("_id").is(new ObjectId(id)).get(), updateWith);
	}

	private int noLimit(DBCollection collection) {
		return (int) collection.getCount();
	}
	
	/**
	 * @param customID - ID of the current project/group
	 * @return Database collection belonging to that project/group
	 */
	private DBCollection getGroupColl(String customID) {
		return db.getCollection(customID);
	}
	
	/**
	 * 
	 * @param customID - ID of the current project/group
	 * @return DBObject to be used for querying this project/groups
	 */
	private DBObject queryForProject(String customID) {
		return QueryBuilder.start("customID").is(customID).get();
	}
	
	/** Generates a DBObject representing a project with the given parameters
	 * 
	 * @param customID - ID of the project
	 * @param name - name of the project
	 * @param creatorObject - creator/owner of the project
	 * @return DBObject containing the given parameters
	 */
	private DBObject createNewEmptyProject(String customID, String name, BasicDBObject creatorObject) {
		return new BasicDBObject("customID", customID).append("name", name).append("members", new BasicDBObject[]{creatorObject});
	}
	
	/** Generates a unique customID for the project with the given name,
	 * using the name as a basis for the customID
	 * 
	 * @param name - Display name of the project
	 * @return A unique customID for the project
	 */
	private String generateCustomID(String name) {
		
		name = removeBlanks(name);
		String temp = name;
		Random random = new Random();
		
		while(groups.findOne(queryForProject(temp)) != null)
		{
			temp = name + Math.round(random.nextFloat() * 1000);
		}
		return temp;
	}
	
	private String removeBlanks(String input) {
		
		return input.replaceAll("\\s+","");
	}
}
