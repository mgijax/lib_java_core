package org.jax.mgi.shr.dbutils;

import org.jax.mgi.shr.exception.ExceptionFactory;

/**
 * @is An ExceptionFactory.
 * @has a hashmap of predefined DBSchemaExceptions stored by a name key
 * @does looks up DBSchemaExceptions by name
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class DBSchemaExceptionFactory extends ExceptionFactory {
  /**
   * Could not obtain ddl statements from dbSchema file
   */
  public static final String FileNotFoundErr =
      "org.jax.mgi.shr.shrdbutils.FileNotFoundErr";
  static {
    exceptionsMap.put(FileNotFoundErr, new DBSchemaException(
        "DBSchema file ?? does not exist", false));
  }
  /**
   * Could not obtain ddl statements from dbSchema file
   */
  public static final String FileReadErr =
      "org.jax.mgi.shr.shrdbutils.FileReadErr";
  static {
    exceptionsMap.put(FileReadErr, new DBSchemaException(
        "Error reading from file ??", false));
  }
  /**
   * Could not obtain ddl statements from dbSchema file
   */
  public static final String FileCloseErr =
      "org.jax.mgi.shr.shrdbutils.FileCloseErr";
  static {
    exceptionsMap.put(FileCloseErr, new DBSchemaException(
        "Error trying to close file ??", false));
  }
  /**
   * Could not obtain ddl statements from dbSchema file
   */
  public static final String NoRegexMatch =
      "org.jax.mgi.shr.shrdbutils.NoRegexMatch";
  static {
    exceptionsMap.put(NoRegexMatch, new DBSchemaException(
        "Could not locate regular expression ?? within " +
        "file ??", false));
  }
  /**
   * Could not obtain ddl statements from dbSchema file
   */
  public static final String UnexpectedString =
      "org.jax.mgi.shr.shrdbutils.UnexpectedString";
  static {
    exceptionsMap.put(UnexpectedString, new DBSchemaException(
        "File search returned unexpected string value " +
        "'??' from file ??. Could be an internal bug", false));
  }



}