package org.jax.mgi.shr.dbutils;

import org.jax.mgi.shr.types.Converter;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @is a group of sql results resulting from a call to a stored procedure
 * @has A collection of update counts and ResultsNavigators
 * @does iterates through the collection of results
 * @company Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */
public class MultipleResults {

  // the internal Statement object for obtaining results from
  Statement s = null;
  // indicator of whether the execute statement returned a ResultSet
  boolean isResultSet;
  // the last update count returned
  int updateCount = 0;
  // the sql string executed for this statement
  String sql = null;
  // indicator that the call to getNextResults is the first call
  boolean firstTime = true;
  // the following constant definitions are exceptions thrown by this class
  private static final String JDBCException = DBExceptionFactory.JDBCException;
   private static final String CloseErr = DBExceptionFactory.CloseErr;

  /**
   * constructor
   * @param pStatement
   * @param sql
   */
  protected MultipleResults(Statement pStatement,
                            boolean pIsResultSet, String pSql) {
    s = pStatement;
    sql = pSql;
    isResultSet = pIsResultSet;
  }
  /**
   * get the next result which may be an update count or a query results.
   * @assumes nothing
   * @effects any previous ResultsNavigator objects returned to the
   * caller are closed
   * @return next results which would need to be cast to either an Integer
   * if the results represents an update count or a ResultsNavigator if
   * the results represents a query results
   */
  public Object getNextResults() throws DBException {
    try {
      // do not call getMoreResults() on the first call
      if (!firstTime)
        isResultSet = s.getMoreResults(); // this closes previous ResultSets
      firstTime = false;
      if (!isResultSet && updateCount == -1)
        // no more results
        return null;
      if (isResultSet) // the results represents query results
      {
          ResultSet rs = s.getResultSet();
          ResultsNavigator nav = new ResultsNavigator(rs);
          return nav;
      }
      else // the results represents an uodate count
      {
          updateCount = s.getUpdateCount();
          if (updateCount == -1)
            return null;
          else
            return Converter.wrap(updateCount);
      }
    }
    catch (SQLException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(JDBCException, e);
      e2.bind("obtain results from executing the following:\n" + sql);
      throw e2;
    }
  }

  /**
   * close this object and free JDBC resources
   * @assumes nothing
   * @effects the internal JDBC resources will be closed
   */
  public void close() throws DBException
  {
      try
      {
          s.close();
      }
      catch (SQLException e)
      {
          DBExceptionFactory eFactory = new DBExceptionFactory();
          DBException e2 = (DBException)
              eFactory.getException(CloseErr, e);
          throw e2;
      }
  }
}