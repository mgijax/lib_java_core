package org.jax.mgi.shr.ioutils;

import org.jax.mgi.shr.exception.MGIException;

/**
 * @is An MGIException that represents an encounter with an unexpected
 * record format
 * @has An error message with a variable field for binding the
 * record string at runtime
 * @does Binds the given runtime record string to the error messsage
 * @author M Walker
 * @version 1.0
 */

public class RecordFormatException extends MGIException {

  public RecordFormatException() {
    super("Unexpected record format for the following record: ??", true);
  }

  public void bindRecord(String s) {
    bind(s);
  }

}