package org.jax.mgi.shr.dbutils.dao;

/**
 * @is a SQLStream which can execute sql in a transactional way.
 * @has nothing.
 * @does provides a commit and rollback method and the sql statements for
 * insert, update and delete.
 * @company Jackson Laboratory
 * @author M. Walker
 * @version 1.0
 */


public interface TransactionalSQLStream extends SQLStream
{
  /**
   * set whether or not to use transactions when executing sql.
   * @assumes nothing
   * @effects subsequent execution of sql will be either transactional or
   * not depending upon the given boolean value
   * @param bool true if the sql should be executing using transaction
   * management, false otherwise
   */
  void setTransactionOn(boolean bool);

  /**
   * commit the current transaction
   * @assumes nothing
   * @effects all outstanding transactional sql will be commited to the
   * database
   */
  void commit();

  /**
   * rollback the current transaction
   * @assumes nothing
   * @effects the current transaction will be roolbacked and a new transaction
   * will be started if the class has transactions on.
   */
  void rollback();

  /**
   * delete the given DAO object from the database
   * @assumes nothing
   * @effects the given DAO will be deleted from the database
   * or batched up to be deleted from the database, depending on the specific
   * implementation
   * @param dataInstance the object to delete
   */
  void delete(DAO dataInstance);

  /**
   * update the given DAO object in the database
   * @assumes nothing
   * @effects the given DAO will be updated in the database
   * or batched up to be updated in the database, depending on the specific
   * implementation
   * @param dataInstance the object to update
   */
  void update(DAO dataInstance);

  /**
   * insert the given DAO object in the database
   * @assumes nothing
   * @effects the given DAO will be inserted into the database
   * or batched up to be inserted into the database, depending on the specific
   * implementation

   * @param dataInstance the object to insert
   */
  void insert(DAO dataInstance);

  /**
   * execute any outstanding statements that may have been processed for batch
   * execution
   * @assumes nothing
   * @effects any outstanding batch statements will be executed
   */
  void close();
}
