package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is an interface for performing database inserts, upadtes and deletes using
 * a InsertStrategy, UpdateStrategy and DeleteStrategy
 * @has an InsertStrategy, UpdateStrategy and a DeleteStrategy
 * @does sends the DAO object to the correct strategy
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public interface SQLStream
{
  /**
   * delete the given DAO object from the database
   * @assumes nothing
   * @effects the given DAO will be deleted from the database
   * or batched up to be deleted from the database, depending on the specific
   * implementation
   * @param dataInstance the object to delete
   * @throws DBException thrown if there is an error executing the delete
   */
  void delete(DAO dataInstance) throws DBException;

  /**
   * update the given DAO object in the database
   * @assumes nothing
   * @effects the given DAO will be updated in the database
   * or batched up to be updated in the database, depending on the specific
   * implementation
   * @param dataInstance the object to update
   */
  void update(DAO dataInstance) throws DBException;

  /**
   * insert the given DAO object in the database
   * @assumes nothing
   * @effects the given DAO will be inserted into the database
   * or batched up to be inserted into the database, depending on the specific
   * implementation
   * @param dataInstance the object to insert
   * @throws DBException thrown if there is an error executing the insert
   */
  void insert(DAO dataInstance) throws DBException;

  /**
   * execute any outstanding statements that may have been processed for batch
   * execution
   * @assumes nothing
   * @effects any outstanding batch statements will be executed
   * @throws DBException thrown if there is an error trying to execute any
   * batch statements
   */
  void close() throws DBException;
}
