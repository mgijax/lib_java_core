package org.jax.mgi.shr.dbutils;

import org.jax.mgi.shr.exception.ExceptionFactory;

/**
 * @is An ExceptionFactory.
 * @has a hashmap of predefined BCPExceptions stored by a name key
 * @does looks up BCPExceptions by name
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
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
