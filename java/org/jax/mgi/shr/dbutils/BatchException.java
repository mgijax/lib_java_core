package org.jax.mgi.shr.dbutils;

import java.sql.Statement;

import org.jax.mgi.shr.exception.MGIException;

/**
 * @is An MGIException which represents an error in bcp processing
 * @has an exception message, a data related indicator and a parent
 * exception which can be null.
 * @does nothing
 * @author M Walker
 * @version 1.0
 */

public class BatchException extends MGIException {

  private int [] updateCounts;
  
  public static final int UNAVAILABLE = Statement.SUCCESS_NO_INFO;
  public static final int ERROR = Statement.EXECUTE_FAILED;

  public BatchException(String pMessage, boolean pDataRelated) {
    super(pMessage, pDataRelated);
  }

  public int [] getUpdateCounts()
  {
    return updateCounts;
  }

  public void setUpdateCounts(int [] counts)
  {
    updateCounts = counts;
  }
}
