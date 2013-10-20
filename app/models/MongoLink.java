package models;

import java.awt.List;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Set;

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
	
	
	public MongoLink() throws UnknownHostException {
		mongoClient = new MongoClient( DBURL );
		db = mongoClient.getDB( DBURL.getDatabase() );
		newsFeed = db.getCollection("newsFeed");
		System.out.println("Complete");
		
		/*Set<String> colls = db.getCollectionNames();

		for (String s : colls) {
		    System.out.println(s);
		}
		
		DBCursor cursor = newsFeed.find();
		try {
		   while(cursor.hasNext()) {
		       System.out.println(cursor.next());
		   }
		} 
		finally {
			   cursor.close();
		   }
		
		
		BasicDBObject testNews = new BasicDBObject("published", "Now");
		testNews.append("actor", new BasicDBObject("objectType", "PERSON").append("displayName", "Piotr"));
		testNews.append("verb", "shouted");
		testNews.append("object", new BasicDBObject("objectType", "MESSAGE").append("message", "MONGOMONGO" ));
		testNews.append("target", "");
		
		newsFeed.insert(testNews);
		
		System.out.println("\n Inserted");
		
		cursor=newsFeed.find();
		
		cursor = newsFeed.find().skip((int) (newsFeed.getCount() - 1));
		
		try {
		   while(cursor.hasNext()) {
		       System.out.println(cursor.next());
		   }
		} finally {
		   cursor.close();
		} */
		
		
	}

	
	public static void main(String[] args) throws UnknownHostException {
		MongoLink ml = new MongoLink();
		//boolean auth = db.authenticate(DBUSER, DBPASS.toCharArray());
		ArrayList<DBObject> list = ml.getNewsFeed(20);
		
		for(DBObject o : list) {
			System.out.println(o);
		}
		
		boolean insertSuccess = ml.insertNews(ml.dbFormat("Today", "PERSON", "Rob", "yelled", "MESSAGE", "Databases!!", ""));
		if(insertSuccess) {
			System.out.println("\n Insert Success");
			list = ml.getNewsFeed(20);
			for(DBObject o : list) {
				System.out.println(o);
			}
		} else {
			System.out.println("insert failed");
		}
	}
	
	public ArrayList<DBObject> getNewsFeed(int postLimit) {
		int postCount = (int) newsFeed.getCount();
		if(postLimit > postCount) {
			postLimit = postCount;
		}
		return (ArrayList<DBObject>) newsFeed.find().skip(postCount - postLimit).toArray();
	}
	
	public boolean insertNews(DBObject obj) {
		int oldCount = (int) newsFeed.getCount();
		
		newsFeed.insert(obj);
		
		int newCount = (int) newsFeed.getCount();
		
		if (newCount == oldCount + 1) {
			return true;
		} else 
			return false;
	}
	
	public BasicDBObject dbFormat(String published, String actorType, String dispName, String verb, String objType, String msg, String tar) {
		BasicDBObject news = new BasicDBObject("published", published);
		news.append("actor", new BasicDBObject("objectType", actorType).append("displayName", dispName));
		news.append("verb", verb);
		news.append("object", new BasicDBObject("objectType", objType).append("message", msg ));
		news.append("target", tar);
		return news;
	}
}
