package org.jax.mgi.shr.dbutils;

/**
 * @is an interface for parsing database results in a forward only direction
 * @has nothing
 * @does provides forward only navigation methods for parsing query results
 * @company: The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public interface ResultsIterator
{
  /**
   * get the next object from the query results
   * @return the next object from the query results
   * @throws DBException thrown if there is an error accessing the database
   */
   public Object next() throws DBException;

   /**
    * close the query results
    * @throws DBException thrown if there is an error accessing the database
    */
   public void close() throws DBException;

   /**
    * see if there is another object to be retrieved from the query results
    * @return true if there is another object to be retrieved from the
    * query results, false otherwise
    */
   public boolean hasNext();

}