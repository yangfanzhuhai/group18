package models.db;

import java.util.Random;

import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

class MongoUtils {
	
	static DBObject reverseSort = QueryBuilder.start("_id").is(-1).get();

	/**
	 * @param user - Object representing user
	 * @param password - Password entered by user
	 * @return True if login credentials are correct, False otherwise
	 */
	static boolean checkCredentials(DBObject user, String password) {
		if(user != null) {
			String hashedPassword = ((DBObject) user.get("localAccount")).get("password").toString();
			return BCrypt.checkpw(password, hashedPassword);
		}
		return false;
	}
	
	/** Generates a unique customID for the project with the given name,
	 * using the name as a basis for the customID
	 * 
	 * @param name - Display name of the project
	 * @return A unique customID for the project
	 */
	static String generateCustomID(DBCollection groups, String name) {
		
		name = removeBlanks(name);
		String temp = name;
		Random random = new Random();
		
		while(groups.findOne(queryForProject(temp)) != null)
		{
			temp = name + Math.round(random.nextFloat() * 1000);
		}
		return temp;
	}
	
	/**
	 * @param customID - ID of the current project/group
	 * @return DBObject to be used for querying this project/groups
	 */
	static DBObject queryForProject(String customID) {
		return QueryBuilder.start("customID").is(customID).get();
	}

	/** Shortcut method to pass as a parameter to database methods,
	 * 	indicating that you want to fetch all posts satisfying a given query
	 * 
	 * @param collection - Collection being used
	 * @return Size of the collection
	 */
	static int noLimit(DBCollection collection) {
		return (int) collection.getCount();
	}
	
	/** Generates a DBObject representing a project with the given parameters
	 * 
	 * @param customID - ID of the project
	 * @param name - name of the project
	 * @param creatorObject - creator/owner of the project
	 * @return DBObject containing the given parameters
	 */
	static DBObject createNewEmptyProject(String customID, String name, BasicDBObject creatorObject) {
		return new BasicDBObject("customID", customID).append("name", name).append("members", new BasicDBObject[]{creatorObject});
	}
	
	static DBObject queryID(String ID) {
		return QueryBuilder.start("_id").is(new ObjectId(ID)).get();
	}
	
	static DBObject queryEmail(String email) {
		return QueryBuilder.start("localAccount.email").is(email).get();
	}

	/** 
	 * @param input - String with spaces
	 * @return The given String without white-spaces
	 */
	private static String removeBlanks(String input) {
		return input.replaceAll("\\s+","");
	}

}
