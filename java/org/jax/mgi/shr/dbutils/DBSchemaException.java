package org.jax.mgi.shr.dbutils;

import org.jax.mgi.shr.exception.MGIException;

/**
 * @is A MGIException which represents an error occuring while
 * reading files from the dbSchema product
 * @has an exception message, a data related indicator and a parent
 * exception which can be null.
 * @does nothing
 * @company
 * @author M Walker
 * @version 1.0
 */

public class DBSchemaException extends MGIException {
  public DBSchemaException(String pMessage, boolean pDataRelated) {
    super(pMessage, pDataRelated);
  }


}