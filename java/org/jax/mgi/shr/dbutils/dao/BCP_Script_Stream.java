package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.ScriptWriter;
import org.jax.mgi.shr.dbutils.ScriptException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.bcp.BCPManager;

public class BCP_Script_Stream
    implements SQLStream {
  /**
   * the BatchProcessor to use
   */
  private ScriptWriter writer = null;
  /**
   * the BCPManager to use
   */
  private BCPManager bcpMgr = null;
  /**
   * the DeleteStrategy object which performs deletes of a given DAO
   */
  private ScriptStrategy deleteStrategy = null;
  /**
   * the UpdateStrategy object which performs updates of a given DAO
   */
  private ScriptStrategy updateStrategy = null;
  /**
   * the BCPStrategy object which performs inserts of a given DAO
   */
  private BCPStrategy insertStrategy = null;

  // the following constant defintions are exceptions thrown by this class
  private static final String ExecuteScriptErr =
      DBExceptionFactory.ExecuteScriptErr;
  private static String SQLStreamCloseErr =
      DBExceptionFactory.SQLStreamCloseErr;

  /**
   * constructor
   * @param sqlMgr the SQLDataManager to use
   * @param bcpMgr the BCPManager to use
   */
  public BCP_Script_Stream(ScriptWriter writer, BCPManager bcpMgr) throws
      DBException {
    this.writer = writer;
    this.deleteStrategy = new ScriptStrategy(writer);
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
      writer.execute();
    }
    catch (ScriptException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(ExecuteScriptErr, e);
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