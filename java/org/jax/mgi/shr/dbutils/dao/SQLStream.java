package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;

/**
 * An abstract class for performing database inserts, updates and deletes
 * using strategy classes which can be plugged in to handle the particular
 * database operations
 * @has an InsertStrategy, UpdateStrategy and a DeleteStrategy for performing
 * the database operations
 * @does routes the DAO object to the correct strategy
 * @company The Jackson Laboratory
 * @author M Walker
 */

public abstract class SQLStream implements DAOPersistent
{

  protected InsertStrategy insertStrategy = null;
  protected UpdateStrategy updateStrategy = null;
  protected DeleteStrategy deleteStrategy = null;

  protected void setInsertStrategy(InsertStrategy insertStrategy)
  {
    this.insertStrategy = insertStrategy;
  }

  protected void setUpdateStrategy(UpdateStrategy updateStrategy)
  {
    this.updateStrategy = updateStrategy;
  }

  protected void setDeleteStrategy(DeleteStrategy deleteStrategy)
  {
    this.deleteStrategy = deleteStrategy;
  }

  public boolean isBCP()
  {
    if (this.insertStrategy instanceof BCPStrategy)
      return true;
    else
      return false;
  }

  /**
   * delete the given DAO object from the database
   * @assumes nothing
   * @effects the given DAO will be deleted from the database
   * or batched up to be deleted from the database, depending on the specific
   * implementation
   * @param dao the object to delete
   * @throws DBException thrown if there is an error executing the delete
   */
  public void delete(DAO dao) throws DBException
  {
    this.deleteStrategy.delete(dao);
  }

  /**
   * update the given DAO object in the database
   * @assumes nothing
   * @effects the given DAO will be updated in the database
   * or batched up to be updated in the database, depending on the specific
   * implementation
   * @param dao the object to update
   * @throws DBException thrown if there is an error accessing the database
   */
  public void update(DAO dao) throws DBException
  {
    this.updateStrategy.update(dao);
  }

  /**
   * insert the given DAO object in the database
   * @assumes nothing
   * @effects the given DAO will be inserted into the database
   * or batched up to be inserted into the database, depending on the specific
   * implementation
   * @param dao the object to insert
   * @throws DBException thrown if there is an error executing the insert
   */
  public void insert(DAO dao) throws DBException
  {
    this.insertStrategy.insert(dao);
  }

  /**
   * abstract method to be implemented by subclasses to handle the execution
   * of any batch SQL in an implementation specific manner
   * @assumes nothing
   * @effects any outstanding batch statements will be executed
   * @throws DBException thrown if there is an error trying to execute any
   * batch statements
   */
  public abstract void close() throws DBException;
}
