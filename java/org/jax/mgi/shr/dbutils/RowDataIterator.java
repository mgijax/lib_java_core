package org.jax.mgi.shr.dbutils;

import java.util.Vector;

/**
 * An object which iterates in a forward direction through a given set
 * of query results and returns objects based on multiple rows
 * @has a ResultsNavigator for parsing through the query results and a
 * MultiRowDataInterpreter for creating objects based on multiple rows
 * @does it looks out for a sequential group of rows which contain the same
 * key value and will create a java data object based on that group of rows.
 * The getNext() method is provided for iterating through the results which
 * is iteratively called, returning the next object, until the call to hasNext()
 * returns false.
 * @author M Walker
 */

public class RowDataIterator implements DataIterator
{
  /**
   * the ResultsNavigator used for iterating through the query results
   */
  private ResultsNavigator nav = null;

  /**
   * a flag to indicate whether or not the ResultsNavigator has reached
   * the end of its results
   */
  private boolean done = false;

  private Object cacheObject = null;

  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String InterpretErr = DBExceptionFactory.InterpretErr;


  /**
   * the constructor
   * @assumes nothing
   * @effects nothing
   * @param nav the query results through which to iterate
   * @param interp the MultiRowDataInterpreter used for creating java data
   * objects based on the rows of query results
   * @throws DBException thrown if there is an error within the database
   */
  public RowDataIterator(ResultsNavigator nav)
  throws DBException
  {
    this.nav = nav;
    if (!nav.next())
      done = true;
    else
        cacheObject = nav.getCurrent();
  }

  /**
   * return an indicator of whether or not there are any more results to process
   * @return true if thjere are more results to process, false otherwise
   */
  public boolean hasNext()
  {
      return !done;
  }

  /**
   * close the ResultsNavigator
   * @assumes nothing
   * @effects the ResultsNavigator will be closed
   * @throws DBException
   */
  public void close() throws DBException
  {
    nav.close();
  }

  public void setInterpreter(RowDataInterpreter interpreter)
  {
      nav.setInterpreter(interpreter);
  }

  /**
   * returns a java data object interpreted from the next group of rows
   * which share the same key value
   * @assumes nothing
   * @effects nothing
   * @return the created java data object
   * @throws DBException thrown if there is an error within the database
   */
  public Object next() throws DBException
  {
      /**
       * the object that will get created based on the rows
       */
      Object nextObject = null;

      if (done) // there are no more rows left in the ResultsNavigator
          return null;

      if (cacheObject != null)
      {
          nextObject = cacheObject;
          if (!nav.next())
          {
              done = true;
              cacheObject = null;
          }
          else
              cacheObject = nav.getCurrent();
      }

      return nextObject;
  }
}
