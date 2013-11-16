package models;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bson.types.BasicBSONList;
import org.bson.types.ObjectId;

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
	
	private DBObject reverseSort = QueryBuilder.start("_id").is(-1).get();
	
	/**Class linking from Java to the MongoDB**/
	public MongoLink(boolean devMode) throws UnknownHostException {
		
		mongoClient = new MongoClient( DBURL );
		db = mongoClient.getDB( DBURL.getDatabase() );
		
		users = db.getCollection("userAccounts");
		groups = db.getCollection("groups");

	}

	//for testing
	public static void main(String[] args) throws UnknownHostException, ParseException {
		
		//String json = "{ \"localAccount\": { \"name\": \"Luke\", \"photo_url\" : \"\", \"email\" : \"abc\" , \"password\": \"pass\" } ," +
			//	"\"fbAccount\": {\"name\": \"Luke\", \"photo_url\" : \"\", \"profile_id\" : \"f123\" }, " +
				//"\"ghAccount\" : {\"name\": \"Luke\", \"photo_url\" : \"\", \"email\" : \"abc\" , \"gravatar_id\": \"g321\", \"html_url\": \"www.git.com\"}}";
		
		String json = "{ \"localAccount\": { \"photo_url\" : \"\", \"email\" : \"abc\" , \"password\": \"pass\" } ," +
				"\"fbAccount\": {}, " +
				"\"ghAccount\" : {}}";
		
		Gson gson = new Gson();
		UserModel model = gson.fromJson(json, UserModel.class);
		if (model.getLocalAccount() != null) {
	//		System.out.println(model.toJSON());
		}
		MongoLink ml = new MongoLink(true);
		
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
	
	/*	for(ArrayList<String> o: ml.getGroups("Piotr"))
		{
			System.out.println(o);
		}
		
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
		
	//	long totalTime = 0;
		
	/*	System.out.println(ml.getGroups("Matt"));
		System.out.println(ml.isMember("Matt", "Progress"));
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
	//	list = ml.getNewsFeed();
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
			ml.addNewProject("test Hello spaces", "Piotr");
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
				
				ActivityModel post = new ActivityModel(posts.get(i).toString());
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
	public boolean addNewProject(String name, String creator) {
		
		return addNewProject(createNewEmptyProject(generateCustomID(name), name, creator));
	}
	
	/** Adds any amount of users to a given project. Any user who is already a
	 * member of the project will not be added again.
	 * 
	 * @param customID - ID of the project
	 * @param users - String or String array of users to be added to the project
	 */
	public void addUsersToProject(String customID, String ... users) {
		
		groups.update(QueryBuilder.start("customID").is(customID).get(),
					new BasicDBObject("$addToSet", new BasicDBObject("members", new BasicDBObject("$each", users))));
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
		
		if(obj.containsField("localAccount") && !"{}".equals(obj.get("localAccount").toString())) {
			
			if(users.findOne(QueryBuilder.start("localAccount.email").is(((DBObject) obj.get("localAccount")).get("email")).get()) == null)
			{
				int oldCount = (int) users.getCount();
				
				users.insert(obj);
				return (int) users.getCount() == oldCount + 1;
			}
			return false;
		}
		else if(obj.containsField("fbAccount") && !"{}".equals(obj.get("fbAccount").toString())) {
			
			if(users.findOne(((DBObject) obj.get("fbAccount")).get("profile_id")) == null)
			{
				int oldCount = (int) users.getCount();
				
				users.insert(obj);
				return (int) users.getCount() == oldCount + 1;
			}
			return true;
		}
		else if(obj.containsField("ghAccount") && !"{}".equals(obj.get("ghAccount").toString())) {
			
			if(users.findOne(((DBObject) obj.get("ghAccount")).get("email")) == null)
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
		return checkLogin( ((DBObject) obj.get("localAccount")).get("email").toString() ,((DBObject) obj.get("localAccount")).get("password").toString());
	}
	
	/** Checks the validity of the given username and password
	 * 
	 * @param email - Username entered by user
	 * @param password - Password entered by user
	 * @return True if there is an entry in the database with that exact username and password, False otherwise
	 */
	public boolean checkLogin(String email, String password) {
		return users.findOne(QueryBuilder.start("localAccount.email").is(email).and("localAccount.password").is(password).get()) != null;
	}
	
	/**
	 * @param username - Username of the current user
	 * @return A list of groups which the member is part of
	 */
	public ArrayList<ArrayList<String>> getGroups(String username) {
		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		List<DBObject> list = groups.find(QueryBuilder.start("members").in(new String[]{username}).get()).toArray();
	
		for(int i = 0; i < list.size(); i++)
		{
			ArrayList<String> temp = getInfoAboutUsers(((BasicDBList) list.get(i).get("members")).toArray(new String[0]));
			temp.add(0, list.get(i).toString());
			retList.add(i, temp);
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
		List<String> members = (List<String>) groups.findOne(queryForProject(groupID)).get("members");
		
		return members.contains(username);
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

	/**Returns a list of postLimit tasks**/
	public ArrayList<ArrayList<String>> getTasks(String customID, int postLimit) {
		return dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").get(), reverseSort, postLimit);
		
	}

	/**Default method to return last 20 tasks**/
	public ArrayList<ArrayList<String>> getTasks(String customID){
		return getTasks(customID, 20);
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
		ActivityModel am = new ActivityModel(tempPost.toString());
		
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
			
			ActivityModel am = new ActivityModel(o.toString());
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
			
			ActivityModel am = new ActivityModel(o.toString());
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
	 * @param creator - creator/owner of the project
	 * @return DBObject containing the given parameters
	 */
	private DBObject createNewEmptyProject(String customID, String name, String creator) {
		return new BasicDBObject("customID", customID).append("name", name).append("members", new String[]{creator});
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
