package org.jax.mgi.shr.types;

import org.jax.mgi.shr.exception.MGIException;

/**
 * @is An MGIException which represents exceptions occuring during
 * data conversion or data type handling.
 * @has nothing
 * @does nothing
 * @company Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class TypesException extends MGIException {
  public TypesException(String pMessage, boolean pDataRelated) {
    super(pMessage, pDataRelated);
  }
}