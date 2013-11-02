package models;

import java.lang.reflect.Field;

/**
 * Abstract class to be extended by models stored in the database.
 * 
 * @author Colin Stannard <colin.stannard@cbsindustries.com>
 * 
 * @param <T>
 */
public abstract class DatabaseBaseModel<T extends DatabaseBaseModel<T>> {

  private String id = null;

  /**
   * The id of the model.
   * 
   * @return Model id as an Integer.
   */
  public String getId() {
    return this.id;
  }

  /**
   * Set the id of the model.
   * 
   * @param id
   */
  private void setId(String id) {
    this.id = id;
  }

  /**
   * Save the current model instance. If the instance already exists - update
   * it. If the instance doesn't exist - create it.
   */
  public void save() throws DatabaseWriteException {

    // No id assigned to model instance - new instance
    if (this.getId() == null) {
      this.setId(this.insertNewInstanceIntoDatabase());
    }

    // Id has been assigned - existing instance.
    else {
      this.updateExistingInstanceInDatabase();
    }
  }

  /**
   * Insert the current model into the database as a new entry. Should return
   * the id of the new insertion.
   */
  protected abstract String insertNewInstanceIntoDatabase()
      throws DatabaseWriteException;

  /**
   * Update an existing instance in the database.
   */
  protected abstract void updateExistingInstanceInDatabase()
      throws DatabaseWriteException;

  /**
   * Find the instance of this model given its id.
   * 
   * @param id
   * 
   * @return The instantiated model (read from database).
   */
  protected abstract T readExistingInstanceFromDatabase(Integer id)
      throws DatabaseReadException;

  /**
   * Merge this instance with another instance of the same type.
   * 
   * @param merger
   * @throws IllegalArgumentException
   * @throws IllegalAccessException
   */
  public void merge(T merger) throws IllegalArgumentException,
      IllegalAccessException {
    Field[] fields = this.getClass().getFields();
    for (Field field : fields) {
      Object value = field.get(merger);
      field.set(this, value);
    }
  }

}
