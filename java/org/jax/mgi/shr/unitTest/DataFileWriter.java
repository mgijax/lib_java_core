package org.jax.mgi.shr.unitTest;

import java.io.IOException;

/**
 * <p>IS: An interface which defines a data file create method. The
 * DataFileClassCreator creates a class that implements this interface.</p>
 * <p>HAS: nothing</p>
 * <p>DOES: nothing</p>
 * <p>Company: Jackson Laboratory</p>
 * @author M Walker
 * @version 1.0
 */

public interface DataFileWriter {
  public void createDataFile(String name) throws IOException;
}