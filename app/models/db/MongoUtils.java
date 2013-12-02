package models.db;

import java.util.Random;

import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;

class MongoUtils {
	
	
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
	 * 
	 * @param customID - ID of the current project/group
	 * @return DBObject to be used for querying this project/groups
	 */
	static DBObject queryForProject(String customID) {
		return QueryBuilder.start("customID").is(customID).get();
	}
	
	/** 
	 * @param input - String with spaces
	 * @return The given String without whitespaces
	 */
	private static String removeBlanks(String input) {
		return input.replaceAll("\\s+","");
	}

}
