package org.jax.mgi.shr.dbutils.dao;

import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBExceptionFactory;


/**
 * @is a class which implements the InsertStrategy, UpdateStrategy,
 * and DeleteStrategy using a direct in-line SQL strategy
 * @has a SQLDataManager.
 * @does casts the given DAO to a SQLTranslatable from which to
 * obtain the sql and executes the sql for either insert, update or
 * delete.
 * @Copyright Jackson Lab
 * @author M Walker
 * @version 1.0
 */


public class InlineStrategy
    implements InsertStrategy, DeleteStrategy, UpdateStrategy
{

  /**
   * the database resource
   */
  private SQLDataManager sqlDataManager;

  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String DataInstanceErr =
      DBExceptionFactory.DataInstanceErr;

  /**
   * constructor
   * @assumes nothing
   * @effects nothing
   * @param sqlMgr the SQLDataManager for performing inserts, updates and
   * deletes
   */
  public InlineStrategy(SQLDataManager sqlMgr)
  {
    sqlDataManager = sqlMgr;
  }

  /**
   * update the given DAO using direct inline sql
   * @assumes nothing
   * @effects the given DAO will be updated in the database
   * @param dbComponent the object to update
   * @throws DBException thrown if an exception occurs when executing
   * the update
   */
  public void update(DAO dbComponent) throws DBException
  {
    SQLTranslatable translator = (SQLTranslatable)dbComponent;
    String sql = translator.getUpdateSQL();
    try {
      sqlDataManager.execute(sql);
    }
    catch (DBException e)
    {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(DataInstanceErr, e);
      e2.bind(dbComponent.toString());
      throw e2;
    }
  }

  /**
   * delete the given DAO using direct inline sql
   * @assumes nothing
   * @effects the given DAO will be deleted from the database
   * @param dbComponent the object to delete
   * @throws DBException thrown if an exception occurs when executing
   * the delete
   */
  public void delete(DAO dbComponent) throws DBException
  {
    SQLTranslatable translator = (SQLTranslatable)dbComponent;
    String sql = translator.getDeleteSQL();
    try {
      sqlDataManager.execute(sql);
    }
    catch (DBException e)
    {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(DataInstanceErr, e);
      e2.bind(dbComponent.toString());
      throw e2;
    }

  }

  /**
   * insert the given DAO using direct inline sql
   * @assumes nothing
   * @effects the given DAO will be inserted into the database
   * @param dbComponent the object to insert
   * @throws DBException thrown if an exception occurs when executing
   * the insert
   */
  public void insert(DAO dbComponent) throws DBException
  {
    SQLTranslatable translator = (SQLTranslatable)dbComponent;
    String sql = translator.getInsertSQL();
    try {
      sqlDataManager.execute(sql);
    }
    catch (DBException e)
    {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(DataInstanceErr, e);
      e2.bind(dbComponent.toString());
      throw e2;
    }

  }


}
