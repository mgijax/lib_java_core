package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.BatchProcessor;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.BatchException;
import org.jax.mgi.shr.dbutils.bcp.BCPManager;

/**
 * @is a SQLStream for doing inserts with bcp and updates and deletes with
 * JDBC batch
 * @has a JDBCBatchStrategy for performing updates and deletes and a
 * BCPStrategy for perfoming inserts
 * @does provides a set of update, insert and delete strategies for updating
 * a given DAO objects in a database
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class BCP_Batch_Stream
    implements SQLStream {
  /**
   * the BatchProcessor to use
   */
  private BatchProcessor batch = null;
  /**
   * the BCPManager to use
   */
  private BCPManager bcpMgr = null;
  /**
   * the DeleteStrategy object which performs deletes of a given DAO
   */
  private BatchStrategy deleteStrategy = null;
  /**
   * the UpdateStrategy object which performs updates of a given DAO
   */
  private BatchStrategy updateStrategy = null;
  /**
   * the BCPStrategy object which performs inserts of a given DAO
   */
  private BCPStrategy insertStrategy = null;

  // the following constant defintions are exceptions thrown by this class
  private static final String ExecuteBatchErr =
      DBExceptionFactory.ExecuteBatchErr;
  private static String SQLStreamCloseErr =
      DBExceptionFactory.SQLStreamCloseErr;

  /**
   * constructor
   * @param sqlMgr the SQLDataManager to use
   * @param bcpMgr the BCPManager to use
   */
  public BCP_Batch_Stream(SQLDataManager sqlMgr, BCPManager bcpMgr) throws
      DBException {
    this.batch = sqlMgr.getBatchProcessor();
    this.deleteStrategy = new BatchStrategy(batch);
    this.updateStrategy = this.deleteStrategy;
    this.insertStrategy = new BCPStrategy(bcpMgr);
    this.bcpMgr = bcpMgr;
  }

  /**
   * delete the given DAO object from the database
   * @param dataInstance the object to delete
   */
  public void delete(DAO dataInstance) throws DBException {
    this.deleteStrategy.delete(dataInstance);
  }

  /**
   * update the given DAO object in the database
   * @param dataInstance the object to update
   */
  public void update(DAO dataInstance) throws DBException {
    this.updateStrategy.update(dataInstance);
  }

  /**
   * insert the given DAO object in the database
   * @param dataInstance the object to insert
   */
  public void insert(DAO dataInstance) throws DBException {
    this.insertStrategy.insert(dataInstance);
  }

  /**
   * execute the bcp commands followed by the batch statements
   */
  public void close() throws DBException {
    try {
      batch.executeBatch();
    }
    catch (BatchException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(ExecuteBatchErr, e);
      throw e2;
    }
    try {
      bcpMgr.executeBCP();
    }
    catch (MGIException e)
    {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(SQLStreamCloseErr, e);
      e2.bind(this.getClass().getName());
      throw e2;
    }

  }

}