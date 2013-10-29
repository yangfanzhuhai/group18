package models.database;

import java.net.UnknownHostException;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import models.DatabaseException;
import models.DatabaseReadException;
import models.DatabaseWriteException;
import models.activity.Activity;

public class MongoDriver implements DatabaseDriver {

  private MongoClient mongoClient;

  /**
   * Create a new MongoDB database driver.
   * 
   * @param username
   * @param password
   * @param port
   * @param databaseName
   * @throws DatabaseException
   */
  public MongoDriver(String username, String password, Integer port,
      String databaseName) throws DatabaseException {

    MongoClientURI databaseURI = new MongoClientURI("mongodb://" + username
        + ":" + password + "@ds0" + port + ".mongolab.com:" + port + "/"
        + databaseName);

    try {
      mongoClient = new MongoClient(databaseURI);
    } catch (UnknownHostException e) {
      throw new DatabaseException();
    }
  }

  @Override
  public void insertNewActivity(Activity activity)
      throws DatabaseWriteException {
    // TODO Auto-generated method stub

  }

  @Override
  public void updateExistingActivity(Activity activity)
      throws DatabaseWriteException {
    // TODO Auto-generated method stub

  }

  @Override
  public Activity readExistingActivity(Integer id) throws DatabaseReadException {
    // TODO Auto-generated method stub
    return null;
  }

}
