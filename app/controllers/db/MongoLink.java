package controllers.db;

import java.net.UnknownHostException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import models.GroupMember;
import models.UserModel;
import models.authentication.Session;

import org.bson.types.ObjectId;

import com.google.gson.Gson;
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
	
	/** Public singleton instance of the MongoLink class*/
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
	
	
	/**
	 * MongoLink constructor.
	 * Initialises connection with database, and gets the core collections.
	 * @throws UnknownHostException
	 */
	public MongoLink() throws UnknownHostException {
		
		mongoClient = new MongoClient( DBURL );
		db = mongoClient.getDB( DBURL.getDatabase() );
		
		users = db.getCollection("userAccountsV2");
		groups = db.getCollection("groups");
		sessions = db.getCollection("session");

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
		
		return addNewProject(MongoUtils.createNewEmptyProject(MongoUtils.generateCustomID(groups, name), name, creatorObject));
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
	 * @param user - User to be removed
	 */
	public void removeFromProject(String customID, String user) {
		groups.update(MongoUtils.queryForProject(customID), new BasicDBObject("$pull", new BasicDBObject("members", new BasicDBObject("username", user))));
	}
	
	/** Changes the project's display name
	 * 
	 * @param customID - ID of the project
	 * @param name - The new display name
	 */
	public void changeProjectName(String customID, String name) {
		groups.update(MongoUtils.queryForProject(customID), new BasicDBObject("name", name));
	}
	
	/** Deletes the entire project from the database.
	 * 
	 * @param customID - ID of the project to be removed
	 */
	public void deleteProject(String customID) {
		groups.remove(MongoUtils.queryForProject(customID));
		getGroupColl(customID).drop();
	}
	
	/** Expects information about the user registering or logging in with GitHub or Facebook
	 * 	If the user is registering, makes sure the email is unique and then puts entry in database
	 * 	If the user is logging in with Facebook or GitHub, checks if they have ever logged in using one of those,
	 * 	if not, enters the details into the database. Returns true in both cases.
	 * 
	 * @param obj - Object representing register/login details
	 * @return True if user has been registered in the database and logged in correctly, False otherwise
	 */
	public boolean registerOrLogin(DBObject obj) {
		
		if(obj.containsField("localAccount") && !"{}".equals(obj.get("localAccount").toString().replaceAll("\\s+",""))) {
			
			if(users.findOne(MongoUtils.queryEmail(((DBObject) obj.get("localAccount")).get("email").toString())) == null)
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
	
	/** Checks entered credentials of user.
	 * 
	 * @param userIdentification - Username or Email of user
	 * @param password - Password entered
	 * @return True if credentials are correct, False otherwise
	 */
	public boolean checkLogin(String userIdentification, String password) {
		return checkLoginWithUsername(userIdentification, password) || checkLoginWithEmail(userIdentification, password);
	}
	
	/** Checks the validity of the given username and password
	 * 
	 * @param email - Username entered by user
	 * @param password - Password entered by user
	 * @return True if there is an entry in the database with that exact username and password, False otherwise
	 */
	private boolean checkLoginWithUsername(String username, String password) {
		DBObject user = users.findOne(QueryBuilder.start("username").is(username).get());
		return MongoUtils.checkCredentials(user, password);
	}
	
	/** Checks the validity of the given email and password
	 * 
	 * @param username - Email entered by user
	 * @param password - Password entered by user
	 * @return True if there is an entry in the database with that exact email and password, False otherwise
	 */
	public boolean checkLoginWithEmail(String email, String password) {
		DBObject user = users.findOne(MongoUtils.queryEmail(email));
		return MongoUtils.checkCredentials(user, password);
	}
	
	public void linkAccount(String userID, DBObject obj) {
		users.update(MongoUtils.queryID(userID), new BasicDBObject("$set",obj));
		//TODO check if merge needed on database side
	}
	
	/** Retrieves the username that links to the email used for login
	 * 
	 * @param email
	 * @return username
	 */
	public String getUsernameFromEmail(String email) {
		return (users.findOne(MongoUtils.queryEmail(email)).toString());
	}
	
	public UserModel getUserFromUsername(String userName){
		Gson gson = new Gson();
		String userJson = (users.findOne(QueryBuilder.start("username").is(userName).get())).toString();
		return gson.fromJson(userJson, UserModel.class);
	}
	
	
	/**
	 * @param session - new session
	 * @return True if session was entered into the database correctly, False otherwise
	 */
	public boolean createNewSession(Session session) {

		int oldCount = (int) sessions.getCount();

		DBObject obj = (DBObject) JSON.parse(session.toJSON());
		
		sessions.insert(obj);

		ObjectId token = (ObjectId)obj.get( "_id" );

		session.setToken(token.toString());
		
		return (int) sessions.getCount() == oldCount + 1;
	}
	
	/**
	 * @param Session token
	 * @return A session with the given token
	 * @throws ParseException
	 */
	public Session getSession(String token) throws ParseException {
		DBObject query = MongoUtils.queryID(token);
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
	
	/**
	 * @param username - Username of the current user
	 * @param groupID - GroupID to check against
	 * @return True if user is a member of 'groupID', False otherwise
	 */
	public boolean isMember(String username, String groupID) {
		@SuppressWarnings("unchecked")
		List<DBObject> members = (List<DBObject>) groups.findOne(MongoUtils.queryForProject(groupID)).get("members");
		
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
			MongoMethods.updateStatus(getGroupColl(customID), obj.get("id").toString(), obj.get("status").toString());
		}
		else if(obj.containsField("priority"))
		{
			MongoMethods.updatePriority(getGroupColl(customID), obj.get("id").toString(), (Integer) obj.get("priority"));
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
		return MongoMethods.dbFetch(getGroupColl(customID), MongoUtils.queryReply(""), MongoUtils.reverseSort, postLimit);
		
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
		return MongoMethods.dbFetch(getGroupColl(customID), QueryBuilder.start("target.messageID").is("").and("_id").lessThan(new ObjectId(lastID)).get(), MongoUtils.reverseSort, postLimit);
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
	
	/**
	 * 
	 * @param customID - ID of current group/project
	 * @param newestID - ID of the newest post on news feed
	 * @return - An Array of any news feed items that are newer than the post with 'newestID' 
	 * @throws ParseException 
	 */
	public ArrayList<String> getNewNews(String customID, String newestID) throws ParseException {
		return MongoMethods.getItemsWithoutReferences(getGroupColl(customID), QueryBuilder.start("_id").greaterThan(new ObjectId(newestID)).get(), MongoUtils.reverseSort);
	}

	/** 
	 * @param customID - ID of collection to be used
	 * @param postLimit - Maximum number of items to be fetched
	 * @return List of the last 'postLimit' items from given collection with replies and references
	 */
	public ArrayList<ArrayList<String>> getTasks(String customID, int postLimit) {
		return MongoMethods.dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").get(), MongoUtils.reverseSort, postLimit);
		
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
		return MongoMethods.dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").and("_id").lessThan(new ObjectId(lastID)).get(), MongoUtils.reverseSort, postLimit);
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
		return MongoMethods.getItemsWithoutReferences(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").get(), MongoUtils.reverseSort);
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @return A list of all tasks (only the tasks, no replies or associated objects)
	 *  in alphabetical order (CASE SENSITIVE)
	 * @throws ParseException
	 */
	public ArrayList<String> getAllTasksByName(String customID) throws ParseException {
		return MongoMethods.getItemsWithoutReferences(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").get(), QueryBuilder.start("object.name").is(1).get());
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param postLimit - Number of tasks to fetch from the database
	 * @return List of tasks (with replies and associated objects) sorted by priority in descending order
	 */
	public ArrayList<ArrayList<String>> getTasksByPriority(String customID, int postLimit) {
		return MongoMethods.dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").get(), QueryBuilder.start("object.priority").is(-1).get(), postLimit);
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
		return MongoMethods.dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("TASK").and("object.status").is(status).get(), MongoUtils.reverseSort, noLimit(customID));
	}
	
	/**
	 * @param customID - ID of collection to be used
	 * @param postLimit - Maximum number of git commits to fetch
	 * @return List of git commits (with replies and associated objects) sorted from newest to oldest
	 */
	public ArrayList<ArrayList<String>> getGitCommits(String customID, int postLimit) {
		return MongoMethods.dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("GIT").get(), MongoUtils.reverseSort, postLimit);
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
		return MongoMethods.dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("GIT").and("_id").lessThan(new ObjectId(lastID)).get(), MongoUtils.reverseSort, postLimit);
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
		return MongoMethods.dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("JENKINS").get(), MongoUtils.reverseSort, postLimit);
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
		return MongoMethods.dbFetch(getGroupColl(customID), QueryBuilder.start("object.objectType").is("JENKINS").and("_id").lessThan(new ObjectId(lastID)).get(), MongoUtils.reverseSort, postLimit);
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
	
	/**
	 * @param id - ID of user
	 * @return The user's appropriate display name
	 */
	public String getDisplayName(String id) {
		
		String ret = null;
		DBObject user = users.findOne(MongoUtils.queryID(id));
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
		
		MongoMethods.deletePost(getGroupColl(customID), obj.get("id").toString());
	}
	
	/**
	 * @param groupID - ID of group/project
	 * @param id - ID of task
	 * @return Array of posts and their replies which reference the task with the given id
	 * @throws ParseException
	 */
	public ArrayList<ArrayList<String>> getTaskDetails(String groupID, String id) throws ParseException {
		return getTaskDetails(getGroupColl(groupID), id);
	}
	
	/**
	 * 
	 * @param collection - Collection of news feed items
	 * @param id - ID of task concerned
	 * @return Array of posts and their replies which reference the task with the given id
	 * @throws ParseException
	 */
	private ArrayList<ArrayList<String>> getTaskDetails(DBCollection collection, String id) throws ParseException {
		
		Set<ArrayList<String>> retList = new LinkedHashSet<ArrayList<String>>();
		ArrayList<DBObject> referencingItems = (ArrayList<DBObject>) collection.find(QueryBuilder.start("target.taskIDs").in(new String[]{id}).get()).sort(MongoUtils.reverseSort).toArray();
		
		
		retList.add(MongoMethods.getEntireTopic(collection, collection.findOne(MongoUtils.queryID(id))));
		
		for(DBObject r : referencingItems)
		{
			retList.add(MongoMethods.getEntireTopic(collection, r));
		}
		
		return new ArrayList<ArrayList<String>>(retList);
	}

	/** Shortcut method to pass as a parameter to database methods,
	 * 	indicating that you want to fetch all posts satisfying a given query
	 * 
	 * @param customID - ID of group collection to be used
	 * @return Size of the collection
	 */
	private int noLimit(String customID) {
		return MongoUtils.noLimit(getGroupColl(customID));
	}
	
	/**
	 * @param customID - ID of the current project/group
	 * @return Database collection belonging to that project/group
	 */
	private DBCollection getGroupColl(String customID) {
		return db.getCollection(customID);
	}	

}
