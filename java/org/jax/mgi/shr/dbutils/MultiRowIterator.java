package org.jax.mgi.shr.dbutils;

import java.util.Vector;

/**
 * @is an object which iterates in a forward direction through a given set
 * of query results and returns objects based on multiple rows
 * @has a ResultsNavigator for parsing through the query results and a
 * MultiRowDataInterpreter for creating objects based on multiple rows
 * @does it looks out for a sequential group of rows which contain the same
 * key value and will create a java data object based on that group of rows.
 * The getNext() method is provided for iterating through the results which
 * is iteratively called, returning the next object, until it return nulls
 * indicating that there are no more objects.
 * @author M Walker
 * @version 1.0
 */

public class MultiRowIterator implements ResultsIterator
{
  /**
   * the ResultsNavigator used for iterating through the query results
   */
  private ResultsNavigator nav = null;

  /**
   * the MultiRowInterpreter used for creating java data objects based on
   * multiple rows.
   */
  private MultiRowInterpreter interp = null;

  /**
   * a flag to indicate whether or not the ResultsNavigator has reached
   * the end of its results
   */
  private boolean done = false;

  /**
   * the constructor
   * @assumes nothing
   * @effects nothing
   * @param nav the query results through which to iterate
   * @param interp the MultiRowDataInterpreter used for creating java data
   * objects based on the rows of query results
   * @throws DBException thrown if there is an error within the database
   */
  public MultiRowIterator(ResultsNavigator nav,
                          MultiRowInterpreter interp)
  throws DBException
  {
    this.nav = nav;
    this.interp = interp;
    nav.setInterpreter(null);
    if (!nav.next())
      done = true;
  }

  public boolean hasNext()
  {
	  return !done;
  }

  public void close() throws DBException
  {
    nav.close();
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

    /**
     * the vector which will hold the grouped rows sharing the same key
     */
    Vector v = new Vector();

    if (done) // there are no more rows left in the ResultsNavigator
      return null;

    /**
     * get the current row and add it to the vector
     */
    RowReference initialRef = (RowReference)nav.getCurrent();
    Object initialObject = interp.interpret(initialRef);
    Object initialKey = interp.interpretKey(initialRef);
    v.add(initialObject);

    /**
     * get all the remainning rows which share the same key as the initial
     * row and add them to the vector
     */
    while (nav.next())
    {
      RowReference thisRef = (RowReference)nav.getCurrent();
      Object thisObject = interp.interpret(thisRef);
      Object thisKey = interp.interpretKey(thisRef);
      if (thisKey.toString().equals(initialKey.toString()))
      {
        v.add(thisObject);
      }
      else // the keys dont match so this indicates we are done
      {
        /**
         * call the interpretRows() method and return the created
         * object to the caller
         */
        nextObject = interp.interpretRows(v);
        return nextObject;
      }
    }
    /**
     * there are no more rows in the ResultsNavigator, so set the done
     * flag to true, call the interpretRows() method and return the
     * created object to the caller
     */
    done = true;
    nextObject = interp.interpretRows(v);
    return nextObject;
  }

}