package org.jax.mgi.shr.dbutils;

import org.jax.mgi.shr.exception.ExceptionFactory;

/**
 * @is An ExceptionFactory.
 * @has a hashmap of predefined DBExceptions stored by a name key
 * @does looks up DBExceptions by name
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class DBExceptionFactory
    extends ExceptionFactory {

  /**
   * an error occurred when trying to connect to the database
   */
  public static final String ConnectErr =
      "org.jax.mgi.shr.shrdbutils.ConnectErr";
  static {
    exceptionsMap.put(ConnectErr, new DBException(
        "Could not open database connection", false));
  }

  /**
   * an error occurred when trying to close a database connection
   */
  public static final String CloseErr =
      "org.jax.mgi.shr.shrdbutils.CloseErr";
  static {
    exceptionsMap.put(CloseErr, new DBException(
        "Could not close database connection", false));
  }

  /**
   * a read was attempted past the end of a ResultSet
   */
  public static final String PastEndOfResultSet =
      "org.jax.mgi.shr.shrdbutils.PastEndOfResultSet";
  static {
    exceptionsMap.put(PastEndOfResultSet, new DBException(
        "Attempt to access beyond the end of result set", false));
  }

  /**
   * the database password could not be retrived from the password file
   */
  public static final String PasswordNotRetrieved =
      "org.jax.mgi.shr.shrdbutils.PasswordNotRetrieved";
  static {
    exceptionsMap.put(PasswordNotRetrieved, new DBException(
        "Could not retrieve password from password file ??", false));
  }

  /**
   * Metadata could not be accessed
   */
  public static final String NoTableDefinitions =
      "org.jax.mgi.shr.shrdbutils.NoTableDefinitions";
  static {
    exceptionsMap.put(NoTableDefinitions, new DBException(
        "Could not retrieve metadata for table ?? in " +
        "database ??", false));
  }

  /**
   * Primary key type is not an incremental type
   */
  public static final String UnexpectedKeyCount =
      "org.jax.mgi.shr.shrdbutils.UnexpectedKeyCount";
  static {
    exceptionsMap.put(UnexpectedKeyCount, new DBException(
        "Expected a single primary key for table ?? " +
        "in database ?? when trying to calculate next key " +
        "value. Check that a primary key was defined, " +
        "it is an integer and is only a single part key.", false));
  }

  /**
   * Primary key type is not an incremental type
   */
  public static final String UnexpectedKeyType =
      "org.jax.mgi.shr.shrdbutils.UnexpectedKeyType";
  static {
    exceptionsMap.put(UnexpectedKeyType, new DBException(
        "Expected a primary key of type Interger " +
        "for table ?? when trying to calculate next key", false));
  }

  /**
   * A SQLException was thrown from a JDBC call
   */
  public static final String JDBCException =
      "org.jax.mgi.shr.shrdbutils.JDBCException";
  static {
    exceptionsMap.put(JDBCException, new DBException(
        "An SQLException was thrown during a JDBC call " +
        "while trying to ??", false));
  }

  /**
   * An unexpected condition occurred which should be reported as a bug
   */
  public static final String UnexpectedCondition =
      "org.jax.mgi.shr.shrdbutils.UnexpectedCondition";
  static {
    exceptionsMap.put(UnexpectedCondition, new DBException(
        "An unexpected error occured while trying to " +
        "??. Probably should be reported as a bug", false));
  }

  /**
   * An unhandled data type was attempted to be processed
   */
  public static final String UnhandledDataType =
      "org.jax.mgi.shr.shrdbutils.UnhandledDataType";
  static {
    exceptionsMap.put(UnhandledDataType, new DBException(
        "Unhandled datatype: ??", false));
  }

  /**
   * An unhandled Sybase column type was found in meta data
   */
  public static final String UnhandledSybaseType =
      "org.jax.mgi.shr.shrdbutils.UnhandledSybaseType";
  static {
    exceptionsMap.put(UnhandledSybaseType, new DBException(
        "Unhandled Sybase data type found in meta data: type number ??",
        false));
  }

  /**
   * Operation attempted on a closed connection
   */
  public static final String ConnectionClosed =
      "org.jax.mgi.shr.shrdbutils.ConnectionClosed";
  static {
    exceptionsMap.put(ConnectionClosed, new DBException(
        "Connection is closed: cannot ??",
        false));
  }

  /**
   * Operation attempted on a closed resource
   */
  public static final String ResourceClosed =
      "org.jax.mgi.shr.shrdbutils.ResourceClosed";
  static {
    exceptionsMap.put(ResourceClosed, new DBException(
        "Resource is closed: cannot ??", false));
  }

  /**
   * A strored procedure call results in a thrown exception
   */
  public static final String StoredProcedureErr =
      "org.jax.mgi.shr.dbutils.StoredProcedureErr";
  static {
    exceptionsMap.put(StoredProcedureErr, new DBException(
        "A call to the following stored procedure resulted in an " +
        "thrown exception\n ??",
        false));
  }

  /**
   * Could not run Class.forname for the given class name
   */
  public static final String ClassFornameErr =
      "org.jax.mgi.shr.dbutils.ClassFornameErr";
  static {
    exceptionsMap.put(ClassFornameErr, new DBException(
        "Could not run Class.forname for class ??",
        false));
  }

  /**
   * Binding a vector of data to a BindableStatement with an inconsistent
   * size of binding places.
   */
  public static final String BindCountErr =
      "org.jax.mgi.shr.dbutils.BindCountErr";
  static {
    exceptionsMap.put(BindCountErr, new DBException(
        "Size of ? for the given binding vector is inconsistent with the " +
        "count of ? for the binding places in the statement", false));
  }

  /**
   * A given DataInstance could not be inserted, updated or deleted in the
   * database due to some nested exception.
   */
  public static final String DataInstanceErr =
      "org.jax.mgi.shr.dbutils.DataInstanceErr";
  static {
    exceptionsMap.put(DataInstanceErr, new DBException(
        "The DAO class ?? could not be updated, deleted or " +
        "inserted into the database. See following exception message:", false));
  }

  /**
   * A SQLStream failed during a close.
   */
  public static final String SQLStreamCloseErr =
      "org.jax.mgi.shr.dbutils.SQLStreamCloseErr";
  static {
    exceptionsMap.put(SQLStreamCloseErr, new DBException(
        "The SQLStream class ?? failed during a close", false));
  }

  /**
   * Could not add statements to batch.
   */
  public static final String AddBatchErr =
      "org.jax.mgi.shr.dbutils.AddBatchErr";
  static {
    exceptionsMap.put(AddBatchErr, new DBException(
        "Could not add the following sql to batch: ??", false));
  }

  /**
   * Could not execute batch.
   */
  public static final String ExecuteBatchErr =
      "org.jax.mgi.shr.dbutils.ExecuteBatchErr";
  static {
    exceptionsMap.put(ExecuteBatchErr, new DBException(
        "Could not execute batch", false));
  }

  /**
   * Could not execute batch.
   */
  public static final String ExecuteScriptErr =
      "org.jax.mgi.shr.dbutils.ExecuteScriptErr";
  static {
    exceptionsMap.put(ExecuteScriptErr, new DBException(
        "Could not execute script", false));
  }

  /**
   * This JDBC driver does not support batch operations.
   */
  public static final String BatchNotSupported =
      "org.jax.mgi.shr.dbutils.BatchNotSupported";
  static {
    exceptionsMap.put(BatchNotSupported, new DBException(
        "This JDBC driver does not support batch operations", false));
  }

  /**
   * wrong type returned on call to getBCPSupportedTables()
   */
  public static final String UnexpectedType =
      "org.jax.mgi.shr.dbutils.UnexpectedType";
  static {
    exceptionsMap.put(UnexpectedType, new DBException(
        "Did not expect type found in the Vector returned from the method " +
        "getBCPSupportedTables(). Only Strings and Tables are expected",
        false));
  }

  /**
   * the Table class failed during record validation
   */
  public static final String ValidationErr =
      "org.jax.mgi.shr.dbutils.ValidationErr";
  static {
    exceptionsMap.put(ValidationErr, new DBException(
        "Table ?? failed validation on a given record", true));
  }

  /**
   * An error occured trying to access ResultSetMetaData
   */
  public static final String RSMetaDataErr =
      "org.jax.mgi.shr.dbutils.RSMetaDataErr";
  static {
    exceptionsMap.put(RSMetaDataErr, new DBException(
        "An error occured trying to access ResultSetMetadata from " +
        "query string ??", false));
  }

  /**
   * there was a failure when trying to auto-record-stamp
   */
  public static final String RecordStampErr =
      "org.jax.mgi.shr.dbutils.RecordStampErr";
  static {
    exceptionsMap.put(RecordStampErr, new DBException(
        "There was a failure when trying to auto record stamp " +
        "for table ??", false));
  }

  /**
   * there was a failure when trying to access the configuration
   */
  public static final String ConfigErr =
      "org.jax.mgi.shr.dbutils.ConfigErr";
  static {
    exceptionsMap.put(ConfigErr, new DBException(
        "There was a failure when trying to access the configuration", false));
  }


}
