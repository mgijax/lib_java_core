package org.jax.mgi.shr.dbutils.dao;

import java.util.Vector;
import org.jax.mgi.shr.dbutils.bcp.BCPTranslatable;
import org.jax.mgi.shr.dbutils.Table;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.bcp.BCPException;

/**
 * @is an abstract class which represents a record in the database. It
 * implements the BCPTranslatable and SQLTranslatable interfaces which
 * requires all subclasses to support inserts, updates and deletes through
 * both bcp and sql.
 * @has a couple of indicators to indicate whether or not the record exists
 * in the database and whether the record has been batched for insert
 * @does provides the implementations for BCPTranslatable and SQLTranslatable
 * @abstract this abstract class implements the accessors for the in-database
 * and batched indicators. The subclass is responsible for implementing
 * the BCPTranslatable and SQLTranslatable interfaces.
 * @author not attributable
 * @version 1.0
 */

abstract public class DAO
    implements BCPTranslatable, SQLTranslatable
{

  /**
   * the flag which indicates whether this record exists in the database or
   * not
   */
  private boolean inDatabase;
  /**
   * the flag which indicates whether this record has been bathced up for
   * insert into the database
   */
  private boolean inBatch;

  /**
   * get the vector which represent column values for a row or a vector of
   * vectors for multiple rows which is called by a BCPWriter when writing
   * to a bcp file
   * @assumes nothing
   * @effects nothing
   * @param table the target Table object which is being bcped into
   * @return the vector of column values used for wriying a line to a bcp
   * file or a vector of vectors if multiple lines are to be written to
   * the bcp file.
   */
  public abstract Vector getBCPVector(Table table) throws BCPException;

  /**
   * get the list of supported tables when inserting via bcp
   * @assumes nothing
   * @effects nothing
   * @return the list of supported tables for bcping
   */
  public abstract Vector getBCPSupportedTables();

  /**
   * get the sql string for performing an update into the database
   * @assumes nothing
   * @effects nothing
   * @return the sql string for performing an update into the database
   */
  public abstract String getUpdateSQL() throws DBException;

  /**
   * get the sql string for performing a delete from the database
   * @assumes nothing
   * @effects nothing
   * @return the sql string for performing a delete from the database
   */
  public abstract String getDeleteSQL() throws DBException;

  /**
   * get the sql string for performing an insert into the database
   * @assumes nothing
   * @effects nothing
   * @return the sql string for performing an insert into the database
   */
  public abstract String getInsertSQL() throws DBException;

  /**
   * return whether the record has been batched for insert into the database
   * @assumes nothing
   * @effects nothing
   * @return true if the record has been batched for insert in the database
   * or false otherwise
   */
  public boolean inBatch()
  {
    return inBatch;
  }

  /**
   * return whether the record exists or not in the database
   * @assumes nothing
   * @effects nothing
   * @return true if the record exists in the database or false otherwise
   */
  public boolean inDatabase()
  {
    return inDatabase;
  }

  /**
   * set whether or not this record exists in the database
   * @assumes nothing
   * @effects nothing
   * @param bool true if the record exists in the database, false otherwise
   */
  public void setInBatch(boolean bool)
  {
    inBatch = bool;
  }

  /**
   * set whether or not this record has been batched for insert into the
   * database
   * @assumes nothing
   * @effects nothing
   * @param bool true if this record has been batched for insert into the
   * database, false otherwise
   */
  public void setInDatabase(boolean bool)
  {
    inDatabase = bool;
  }

  /**
   * return a java.lang.UnsupportedOperationException with a conventional
   * message attached
   * @assumes nothing
   * @effects nothing
   * @param classname the name of the class that will be throwing the
   * exception which is used in the exception message.
   */
  protected RuntimeException getUnsupportedMethodException(String classname)
  {
    String message = "Class " + classname +
                     " does not support the method getPartialInitQuery";
    return new java.lang.UnsupportedOperationException(message);
  }

}
