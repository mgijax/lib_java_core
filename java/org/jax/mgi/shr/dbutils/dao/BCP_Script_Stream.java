package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.ScriptWriter;
import org.jax.mgi.shr.dbutils.ScriptException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.bcp.BCPManager;

public class BCP_Script_Stream extends SQLStream {
  /**
   * the BatchProcessor to use
   */
  private ScriptWriter writer = null;
  /**
   * the BCPManager to use
   */
  private BCPManager bcpMgr = null;

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
    super();
    this.writer = writer;
    this.bcpMgr = bcpMgr;
    ScriptStrategy scriptStrategy = new ScriptStrategy(writer);
    BCPStrategy bcpStrategy = new BCPStrategy(bcpMgr);
    super.setInsertStrategy(bcpStrategy);
    super.setUpdateStrategy(scriptStrategy);
    super.setDeleteStrategy(scriptStrategy);
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