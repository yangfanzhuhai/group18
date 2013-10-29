package models.database;

import models.DatabaseReadException;
import models.DatabaseWriteException;
import models.activity.Activity;

/**
 * All Database drivers must implement this interface.
 * 
 * @author Colin Stannard <colin.stannard@cbsindustries.com>
 */
public interface DatabaseDriver {

  /**
   * Insert a new Activity into the database.
   * 
   * @param activity
   * @throws DatabaseWriteException
   */
  public void insertNewActivity(Activity activity)
      throws DatabaseWriteException;

  /**
   * Update an existing Activity.
   * 
   * @param activity
   * @throws DatabaseWriteException
   */
  public void updateExistingActivity(Activity activity)
      throws DatabaseWriteException;

  /**
   * Read an existing Activity from the Database.
   * 
   * @param id
   * @return
   * @throws DatabaseReadException
   */
  public Activity readExistingActivity(Integer id) throws DatabaseReadException;

}
