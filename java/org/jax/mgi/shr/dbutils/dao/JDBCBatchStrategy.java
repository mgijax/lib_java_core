package org.jax.mgi.shr.dbutils.dao;

/**
 * @is a class which implements the InsertStrategy, UpdateStrategy,
 * and DeleteStrategy using a JDBC batch strategy
 * @has a SQLDataManager.
 * @does casts the given DAO to a SQLTranslatable from which to
 * obtain the sql and executes the sql for either insert, update or
 * delete.
 * @Copyright Jackson Lab
 * @author M Walker
 * @version 1.0
 */


public class JDBCBatchStrategy
    implements DeleteStrategy, InsertStrategy, UpdateStrategy
{
  /**
   * update the given DAO object
   * @assumes nothing
   * @effects the given DAO will be batched up for update into the
   * database
   * @param dbComponent the object to update
   */
  public void update(DAO dbComponent){ }

  /**
   * delete the given DAO object
   * @assumes nothing
   * @effects the given DAO will be batched up for delete from the
   * database
   * @param dbComponent the object to delete
   */
  public void delete(DAO dbComponent){ }

  /**
   * insert the given DAO object
   * @assumes nothing
   * @effects the given DAO will be batched up for insert into the
   * database
   * @param dbComponent the object to insert
   */
  public void insert(DAO dbComponent){ }
}
