package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.bcp.BCPManager;


/**
 * @is a SQLStream for doing inserts with bcp and updates and deletes with
 * a inline sql
 * @has a InlineSQLStrategy for performing updates and deletes and a
 * BCPStrategy for perfoming inserts
 * @does provides a set of update, insert and delete strategies for updating
 * a given DAO objects in a database
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class BCP_Inline_Stream extends SQLStream
{

  /**
   * the BCPManager class for performing database inserts with bcp
   */
  private BCPManager bcpManager = null;
  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String SQLStreamCloseErr =
      DBExceptionFactory.SQLStreamCloseErr;


  /**
   * constructor
   * @assumes nothing
   * @effects nothing
   * @param sqlMgr
   * @param bcpMgr
   */
  public BCP_Inline_Stream(SQLDataManager sqlMgr, BCPManager bcpMgr)
  {
    super();
    this.bcpManager = bcpMgr;
    InlineStrategy inlineStrategy = new InlineStrategy(sqlMgr);
    BCPStrategy bcpStrategy = new BCPStrategy(bcpMgr);
    super.setInsertStrategy(bcpStrategy);
    super.setUpdateStrategy(inlineStrategy);
    super.setDeleteStrategy(inlineStrategy);
  }

  /**
   * execute the bcp commands followed by the batch statements
   * @assumes nothing
   * @effects the bcp command is executed for all BCPWriters created
   * through the BCPManager
   * @throws DBException thrown if there is an error executing the bcp command
   */
  public void close() throws DBException
  {
    try {
      bcpManager.executeBCP();
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
