package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.exception.MGIException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;
import org.jax.mgi.shr.dbutils.ScriptWriter;
import org.jax.mgi.shr.dbutils.ScriptException;


/**
 * @is a SQLStream for doing inserts, updates and deletes with isql
 * @has a ScriptStrategy for performing updates, inserts and deletes
 * @does inserts, updates and deletes DAO objects onto an SQLStream
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class Script_Stream extends SQLStream
{
    
    private ScriptWriter writer = null;
    
    // the following constant defintions are exceptions thrown by this class
    private static final String ExecuteScriptErr =
        DBExceptionFactory.ExecuteScriptErr;
    private static String SQLStreamCloseErr =
        DBExceptionFactory.SQLStreamCloseErr;
    
  /**
   * constructor
   * @assumes nothing
   * @effects nothing
   * @param sqlMgr
   * @param bcpMgr
   */
  public Script_Stream(ScriptWriter writer)
  {
    super();
    this.writer = writer;
    ScriptStrategy scriptStrategy = new ScriptStrategy(writer);
    super.setInsertStrategy(scriptStrategy);
    super.setUpdateStrategy(scriptStrategy);
    super.setDeleteStrategy(scriptStrategy);
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
   * execute the script
   * @assumes nothing
   * @effects isql is executed through the ScriptWriter
   * @throws DBException thrown if there is an error executing the isql command
   */
  public void close() throws DBException
  {
      try {
        writer.execute();
      }
      catch (ScriptException e) {
        DBExceptionFactory eFactory = new DBExceptionFactory();
        DBException e2 = (DBException)
            eFactory.getException(ExecuteScriptErr, e);
        throw e2;
      }
      
  }
}

