package org.jax.mgi.shr.dbutils;

import java.util.Vector;

/**
 * @is an interface which defines an object that can interpret query results
 * which span multiple rows and is able to create a java data object based
 * on the conjunction of data from the these rows.
 * @has nothing
 * @does provides a method to create a new java data object based
 * on a given vector of objects.
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public interface MultiRowInterpreter extends RowDataInterpreter
{
  /**
   * returns an object that represents the key to the given RowReference
   * @assumes nothing
   * @effects nothing
   * @param row the RowReference object from which to derive the key
   * @return the key of the given RowReference
   * @throws DBException thrown if there is an error accessing data
   */
  public Object interpretKey(RowReference row)
      throws DBException, InterpretException;

  /**
   * create a new single java object which represents the given
   * vector of java objects
   * @assumes nothing
   * @effects nothing
   * @param v the vector of objects which is to be used in creating the
   * new java object
   * @return the new java object
   * @throws DBException thrown if there is an error accessing data
   */
  public Object interpretRows(Vector v) throws InterpretException;
}

