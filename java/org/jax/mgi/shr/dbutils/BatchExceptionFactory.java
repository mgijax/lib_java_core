package org.jax.mgi.shr.dbutils;

import org.jax.mgi.shr.exception.ExceptionFactory;

/**
 * An ExceptionFactory for JDBC batch processing errors.
 * @has a hashmap of predefined BatchExceptions stored by a name key
 * @does looks up BCPExceptions by name
 * @company The Jackson Laboratory
 * @author M Walker
 */

public class BatchExceptionFactory extends ExceptionFactory {

  /**
   * One of the batch commands failed
   */
  public static final String BatchCommandFailed =
      "org.jax.mgi.shr.shrdbutils.BatchCommandFailed";
  static {
    exceptionsMap.put(BatchCommandFailed, new BatchException(
        "One of the batch commands failed", false));
  }

}
