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
	
	private final static String DBUSER = "testUser";
	private final static String DBPASS = "test";
	private final int DBPORT = 49868;
	private final String DBNAME = "heroku_app18716009";
	private final MongoClientURI DBURL = new MongoClientURI("mongodb://" + DBUSER + ":" + DBPASS + "@ds0" + DBPORT + ".mongolab.com:" + DBPORT + "/" + DBNAME);
	private MongoClient mongoClient;
	private static DB db;
	private static DBCollection newsFeed;
	private static DBCollection users;
	
	//a link from Java to the MongoDB
	public MongoLink() throws UnknownHostException {
		mongoClient = new MongoClient( DBURL );
		db = mongoClient.getDB( DBURL.getDatabase() );
		newsFeed = db.getCollection("newsFeed");
		users = db.getCollection("userAccounts");
		System.out.println("Connection Complete");
	}

	//for testing
	public static void main(String[] args) throws UnknownHostException {
		MongoLink ml = new MongoLink();
		//boolean auth = db.authenticate(DBUSER, DBPASS.toCharArray());

		//Prints last 20 items of newsFeed
		ArrayList<ArrayList<String>> list = ml.getNewsFeed(20);
		for(ArrayList<String> a : list) {
			for(String o : a) {
				System.out.println(o);
			}
		}
		
		if(ml.checkLogin("Piotr","pass"))
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

	//returns the last postLimit posts with replies
	public ArrayList<ArrayList<String>> getNewsFeed(int postLimit) {

		ArrayList<DBObject> posts = (ArrayList<DBObject>) newsFeed.find(new BasicDBObject("target", "")).sort(new BasicDBObject("_id", -1)).limit(postLimit).toArray();
		ArrayList<ArrayList<String>> list = new ArrayList<ArrayList<String>>();
		
		try {
			int i = 0;
			while(i < posts.size())
			{
				ArrayList<String> replies = getReplies(posts.get(i).get("_id").toString());
				replies.add(0, new ActivityModel(posts.get(i).toString()).toJSON());
				list.add(i, replies);
				i++;
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return list;
	}
	
	public ArrayList<ArrayList<String>> getNewsFeed(){
		return getNewsFeed(20);
	}
	
	//use dbFormat to insert a formed message into the newsFeed DB
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
	
	public boolean checkLogin(String username, String pass) {
		return users.find(new BasicDBObject("username", username).append("password", pass)).hasNext();
	}
	
	//shortcut for testing inserting in correct form
	private BasicDBObject dbFormat(String published, String actorType, String dispName, String verb, String objType, String msg, String tar) {
		BasicDBObject news = new BasicDBObject("published", published);
		news.append("actor", new BasicDBObject("objectType", actorType).append("displayName", dispName));
		news.append("verb", verb);
		news.append("object", new BasicDBObject("objectType", objType).append("message", msg ));
		news.append("target", tar);
		return news;
	}
	
	private static ArrayList<String> getReplies(String id) throws ParseException {
		
		ArrayList<DBObject> list = (ArrayList<DBObject>) newsFeed.find(new BasicDBObject("target", id)).toArray();
		ArrayList<String> retList = new ArrayList<String>();		
		
		
		for(DBObject o : list) {
			retList.add(new ActivityModel(o.toString()).toJSON());
		}
		
		return retList;
	}
}
