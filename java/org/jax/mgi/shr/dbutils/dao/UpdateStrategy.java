package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.DBException;

/**
 * @is an interface which defines a method to uopdate the given  DAO
 * object within the database. This could be implemented in a number of
 * different ways, including inline sql, batch JDBC or through Transact-SQL
 * scripts.
 * @has a resource for updating the database. The type of resource depends on
 * on the way in which the update method is implemented.
 * @does casts the given DAO to a SQLTranslatable object from which
 * the  delete sql is obtained and then performs the delete.
 * @Copyright Jackson Lab
 * @author M Walker
 * @version 1.0
 */


public interface UpdateStrategy {

  /**
   * update the given DAO object into the database
   * @assumes nothing
   * @effects the given DAO will be updated in the database or
   * batched up to be updated in the database, depending on the
   * specific implementation.
   * @param dbComponent the object to update
   * @throws DBException thrown if an exception occurs when executing
   * the update
   */
  void update(DAO dbComponent) throws DBException;
}
