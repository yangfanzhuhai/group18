package controllers.db;

import java.text.ParseException;
import java.util.ArrayList;

import org.bson.types.ObjectId;

import models.ActivityModel;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

/** Class containing some more generic methods used by the back-end.
 *  It acts as a helper for the MongoLink class.
 * 
 * @author Piotr Tokaj
 */
class MongoMethods {
	
	/**
	 * @param newsFeed Collection from which to fetch data
	 * @param searchCriteria - Criteria to use for search
	 * @param sortCriteria - Criteria by which to sort results
	 * @param postLimit - Maximum number of items to fetch
	 * @return List of Lists containing news feed posts, with their replies and references
	 */
	static ArrayList<ArrayList<String>> dbFetch(DBCollection newsFeed, DBObject searchCriteria, DBObject sortCriteria, int postLimit) {
		
		ArrayList<DBObject> posts = (ArrayList<DBObject>) newsFeed.find(searchCriteria).sort(sortCriteria).limit(postLimit).toArray();
		ArrayList<ArrayList<String>> retList = new ArrayList<ArrayList<String>>();
		
		try {
			int i = 0;
			while(i < posts.size())
			{
				ArrayList<String> tempList = getReplies(newsFeed, posts.get(i).get("_id").toString());
				
				tempList.add(0, ActivityModel.activityModelGson.fromJson(posts.get(i).toString(), ActivityModel.class).toJSON());
				tempList.addAll(1, getReferences(newsFeed, posts.get(i)));
				retList.add(i, tempList);
				
				i++;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return retList;
	}
	
	/**
	 * @param coll - Collection to be used
	 * @param id - ID string of the news feed post
	 * @return ArrayList of all the replies to that post
	 * @throws ParseException
	 */
	private static ArrayList<String> getReplies(DBCollection coll, String id) throws ParseException {
		return getItemsWithoutReferences(coll, MongoUtils.queryReply(id), null);
	}
	
	
	/**
	 * @param collection - Collection to use
	 * @param referencingPost - Post to get entire topic of.
	 * @return Array of posts and their replies (topics) that contain the given post, which is either a post or a reply
	 * @throws ParseException
	 */
	static ArrayList<String> getEntireTopic(DBCollection collection, DBObject referencingPost) throws ParseException {
		
		ArrayList<String> retList = new ArrayList<String>();
		
		String targetPost = ((DBObject) referencingPost.get("target")).get("messageID").toString();
		DBObject tempPost;
		
		if("".equals(targetPost))
		{
			tempPost = referencingPost;
		}
		else
		{
			tempPost = collection.findOne(MongoUtils.queryID(targetPost));
		}
		
		String id = tempPost.get("_id").toString();
		
		retList.add(ActivityModel.activityModelGson.fromJson(tempPost.toString(), ActivityModel.class).toJSON());
		retList.addAll(getReplies(collection, id));
		
		return retList;
	}

	/**
	 * @param coll - Collection be used
	 * @param id - ID of the object to be deleted, along with all its replies
	 */
	static void deletePost(DBCollection coll, String id) {
		deleteReplies(coll, id);
		coll.remove(new BasicDBObject("_id", new ObjectId(id)));
	}

	/**
	 * @param coll - Collection be used
	 * @param id - ID of the object which will have its replies deleted
	 */
	static void deleteReplies(DBCollection coll, String id) {
		coll.remove(MongoUtils.queryReply(id));
	}

	/** Generic method to find list of objects that satisfy the given query
	 * @param collection - Collection to be used
	 * @param query - DBObject containing the information about the query
	 * 
	 * @return ArrayList of objects
	 * @throws ParseException
	 */
	static ArrayList<String> getItemsWithoutReferences(DBCollection collection, DBObject query, DBObject sortKey) throws ParseException {
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) collection.find(query).sort(sortKey).toArray();
		ArrayList<String> retList = new ArrayList<String>();		
		
		for(DBObject o : list)
		{			
			retList.add(ActivityModel.activityModelGson.fromJson(o.toString(), ActivityModel.class).toJSON());
		}
		
		return retList;
	}
	
	/** 
	 * @param coll - Collection to be used
	 * @param obj - News Feed object
	 * @return ArrayList representing all the tasks that are referenced by the given object
	 * @throws ParseException
	 */
	private static ArrayList<String> getReferences(DBCollection coll, DBObject obj) throws ParseException {
		
		BasicDBList taskIDs = (BasicDBList) ((DBObject)obj.get("target")).get("taskIDs");
		
		if(taskIDs.isEmpty()) return new ArrayList<String>();
		
		ObjectId[] taskIDObjs = new ObjectId[taskIDs.size()];
		for(int i = 0; i < taskIDObjs.length; i++)
		{
			taskIDObjs[i] = new ObjectId(taskIDs.get(i).toString());
		}
		
		return getItemsWithoutReferences(coll, QueryBuilder.start("_id").in(taskIDObjs).get(), null);
	}
	
	/** Updates the status of the task with ID id
	 * @param coll - Collection be used
	 * @param id - ID of task to be updated
	 * @param status - New status value to be set
	 */
	static void updateStatus(DBCollection coll, String id, String status) {
		updateObject(coll, id, new BasicDBObject("$set", new BasicDBObject("object.status", status)));
	}

	/** Updates the priority of the task with ID id
	 * @param coll - Collection be used
	 * @param id - ID of task to be updated
	 * @param priority - New priority value to be set
	 */
	static void updatePriority(DBCollection coll, String id, int priority) {
		updateObject(coll, id, new BasicDBObject("$set", new BasicDBObject("object.priority", priority)));
	}
	
	/** Generic method which updates object with ID 'id' using the parameters in 'updateWith'
	 * @param collection - Collection be used
	 * @param id - ID of object to be updated
	 * @param updateWith - Information on what the update should change
	 */
	private static void updateObject(DBCollection collection, String id, DBObject updateWith) {
		collection.update(MongoUtils.queryID(id), updateWith);
	}

}
