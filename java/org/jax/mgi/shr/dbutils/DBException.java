package org.jax.mgi.shr.dbutils;

import org.jax.mgi.shr.exception.MGIException;

/**
 * @is A MGIException which represents an error occuring during a
 * process involving a database resource
 * @has an exception message, a data related indicator and a parent
 * exception which can be null.
 * @does nothing
 * @company
 * @author M Walker
 * @version 1.0
 */

public class DBException extends MGIException {
  public DBException(String pMessage, boolean pDataRelated) {
    super(pMessage, pDataRelated);
  }
}