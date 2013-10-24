package models;

import java.awt.List;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;

import org.bson.BasicBSONObject;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.WriteConcern;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.DBCursor;
import com.mongodb.ServerAddress;

public class MongoLink {
	
	private final static String DBUSER = "testUser";
	private final static String DBPASS = "test";
	private final int DBPORT = 49868;
	private final String DBNAME = "heroku_app18716009";
	private final MongoClientURI DBURL = new MongoClientURI("mongodb://" + DBUSER + ":" + DBPASS + "@ds0" + DBPORT + ".mongolab.com:" + DBPORT + "/" + DBNAME);
	private MongoClient mongoClient;
	private static DB db;
	private static DBCollection newsFeed;
	
	//a link from Java to the MongoDB
	public MongoLink() throws UnknownHostException {
		mongoClient = new MongoClient( DBURL );
		db = mongoClient.getDB( DBURL.getDatabase() );
		newsFeed = db.getCollection("newsFeed");
		System.out.println("Connection Complete");
	}

	//for testing
	public static void main(String[] args) throws UnknownHostException {
		MongoLink ml = new MongoLink();
		//boolean auth = db.authenticate(DBUSER, DBPASS.toCharArray());

		//Prints last 20 items of newsFeed
		ArrayList<DBObject> list = ml.getNewsFeed(20);
		for(DBObject o : list) {
			System.out.println(o);
		}
		
		//Checks inserting into newsFeed and prints new latest 20
//		boolean insertSuccess = ml.insertNews(ml.dbFormat("Today", "PERSON", "Yangfan", "whispered", "MESSAGE", "im replying", "notthewall"));
//		if(insertSuccess) {
//			System.out.println("\n Insert Success");
//			list = ml.getNewsFeed(20);
//			for(DBObject o : list) {
//				System.out.println(o);
//			}
//		} else {
//			System.out.println("insert failed");
//		}
	}

	//returns the last postLimit posts
	public static ArrayList<DBObject> getNewsFeed(int postLimit) {

	//	DBCursor cursor = newsFeed.find(new BasicDBObject("target", ""));
	//	cursor.
	//	cursor.length();
	//	int postCount = (int) newsFeed.getCount();
	//	if(postLimit > postCount) {
	//		postLimit = postCount;
	//	}
		ArrayList<DBObject> list = (ArrayList<DBObject>) newsFeed.find(new BasicDBObject("target", "")).sort(new BasicDBObject("_id", -1)).limit(postLimit).toArray();
		int i = 0;
		while(i < list.size())
		{
			ArrayList<DBObject> temp = getReply(list.get(i).get("_id").toString());
			list.addAll(i + 1, temp);
			i += temp.size() + 1;
		}
		
		return list;
	}
	
	public static ArrayList<DBObject> getNewsFeed(){
		return getNewsFeed(20);
	}
	
	//use dbFormat to insert a formed message into the newsFeed DB
	public static boolean insertNews(DBObject obj) {
		int oldCount = (int) newsFeed.getCount();
		
		newsFeed.insert(obj);
		
		return (int) newsFeed.getCount() == oldCount + 1;
	}
	
	//shortcut for testing inserting in correct form
	public BasicDBObject dbFormat(String published, String actorType, String dispName, String verb, String objType, String msg, String tar) {
		BasicDBObject news = new BasicDBObject("published", published);
		news.append("actor", new BasicDBObject("objectType", actorType).append("displayName", dispName));
		news.append("verb", verb);
		news.append("object", new BasicDBObject("objectType", objType).append("message", msg ));
		news.append("target", tar);
		return news;
	}
	
	private static ArrayList<DBObject> getReply(String id) {
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) newsFeed.find(new BasicDBObject("target", id)).toArray();
		
		if(!list.isEmpty())
			list.addAll(getReply(list.get(0).get("_id").toString()));
		
		return list;
	}
}
