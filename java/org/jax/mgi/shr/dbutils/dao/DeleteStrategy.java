package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is an interface which defines a method to delete the given DAO
 * object from the database. This could be implemented in a number of
 * different ways, including inline sql, batch JDBC or through Transact-SQL
 * scripts.
 * @has a resource for deleting from the database. The type of resource
 * depends on the way in which the delete method is implemented.
 * @does casts the given DAO to a SQLTranslatable object from which
 * the  delete sql is obtained and then performs the delete.
 * @Copyright Jackson Lab
 * @author M Walker
 * @version 1.0
 */

public interface DeleteStrategy {

  /**
   * delete the given DAO object from the database
   * @assumes nothing
   * @effects the given DAO will be deleted from the database or
   * batched up to be deleted from the database, depending on the
   * specific implementation.
   * @param dbComponent the object to delete
   * @throws DBException thrown if an exception occurs when executing
   * the delete
   */
  void delete(DAO dbComponent) throws DBException;
}
