package models.db;

import java.util.Random;

import org.bson.types.ObjectId;
import org.mindrot.jbcrypt.BCrypt;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

class MongoUtils {
	

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
	
	/** Generic method which updates object with ID 'id' using the parameters in 'updateWith'
	 * @param collection - Collection be used
	 * @param id - ID of object to be updated
	 * @param updateWith - Information on what the update should change
	 */
	static void updateObject(DBCollection collection, String id, DBObject updateWith) {
		collection.update(QueryBuilder.start("_id").is(new ObjectId(id)).get(), updateWith);
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
	
	/** 
	 * @param input - String with spaces
	 * @return The given String without white-spaces
	 */
	private static String removeBlanks(String input) {
		return input.replaceAll("\\s+","");
	}
	
	

}
