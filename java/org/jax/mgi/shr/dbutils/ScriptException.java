package org.jax.mgi.shr.dbutils;

import org.jax.mgi.shr.exception.MGIException;

/**
 * @is An MGIException which represents an error in sql script processing
 * @has an exception message, a data related indicator and a parent
 * exception which can be null.
 * @does nothing
 * @author M Walker
 * @version 1.0
 */


public class ScriptException extends MGIException {

  public ScriptException(String pMessage, boolean pDataRelated) {
    super(pMessage, pDataRelated);
  }

}