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

public class BCP_Inline_Stream implements SQLStream
{

  /**
   * the BCPManager class for performing database inserts with bcp
   */
  private BCPManager bcpManager = null;
  /**
   * the DeleteStrategy object which performs deletes of a given DAO
   */
  private InlineStrategy deleteStrategy = null;
  /**
   * the UpdateStrategy object which performs updates of a given DAO
   */
  private InlineStrategy updateStrategy = null;
  /**
   * the BCPStrategy object which performs inserts of a given DAO
   */
  private BCPStrategy insertStrategy = null;

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
    this.deleteStrategy = new InlineStrategy(sqlMgr);
    this.updateStrategy = this.deleteStrategy;
    this.insertStrategy = new BCPStrategy(bcpMgr);
    this.bcpManager = bcpMgr;
  }


  /**
   * delete the given DAO object from the database
   * @assumes nothing
   * @effects deletes the given DAO from the database using inline
   * sql
   * @param dataInstance the object to delete
   */
  public void delete(DAO dataInstance) throws DBException
  {
    deleteStrategy.delete(dataInstance);
  }

  /**
   * update the given DAO object in the database
   * @assumes nothing
   * @effects updates the given DAO in the database using inline
   * sql
   * @param dataInstance the object to update
   * @throws DBException thrown if there is a database error when executing
   * the update sql
   */
  public void update(DAO dataInstance) throws DBException
  {
    updateStrategy.update(dataInstance);
  }

  /**
   * insert the given DAO object in the database
   * @assumes nothing
   * @effects adds the given DAO to the appropriate bcp file or files
   * @param dataInstance the object to insert
   * @throws DBException thrown if there is an error when trying to add
   * the given object to the bcp file
   */
  public void insert(DAO dataInstance) throws DBException
  {
    insertStrategy.insert(dataInstance);
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
