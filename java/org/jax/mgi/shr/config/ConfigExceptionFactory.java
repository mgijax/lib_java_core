package org.jax.mgi.shr.config;

import org.jax.mgi.shr.exception.ExceptionFactory;

/**
 * @is An ExceptionFactory.
 * @has a hashmap of predefined ConfigExceptions stored by a name key
 * @does looks up ConfigExceptions by name
 * @company The Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class ConfigExceptionFactory extends ExceptionFactory {

  /**
   * A requested parameter was not found
   */
  public static final String ParameterNotFound =
      "org.jax.mgi.shr.config.ParameterNotFound";
  static {
    exceptionsMap.put(ParameterNotFound, new ConfigException(
        "Could not find configuration parameter ??", false));
  }
  /**
   * A requested parameter could not be converted to the desired type
   */
  public static final String ParameterTypeError =
      "org.jax.mgi.shr.config.ParameterTypeError";
  static {
    exceptionsMap.put(ParameterTypeError, new ConfigException(
        "Could not convert parameter named ?? to type ?? with value ??",
        false));
  }

  /**
   * An error occurred when trying to read the configuration file
   */
  public static final String MgrLoadErr =
      "org.jax.mgi.shr.config.MgrLoadErr";
  static {
    exceptionsMap.put(MgrLoadErr, new ConfigException(
        "Error loading configuration manager from file ??", false));
  }
  /**
   * the configuration file could not be found
   */
  public static final String FileNotFound =
      "org.jax.mgi.shr.config.FileNotFound";
  static {
    exceptionsMap.put(FileNotFound, new ConfigException(
        "Could not find configuration file ??", false));
  }
  
	/**
	 * invalid delimiter value found in configuration
	 */
	public static final String InvalidDelimiter =
			"org.jax.mgi.shr.config.InvalidDelimiter";
	static {
		exceptionsMap.put(InvalidDelimiter, new ConfigException(
				"Invalid delimiter: ??", false));
	}

  /**
   * Could not create a new instance of a class
   */
  public static final String NewInstanceFailed =
      "org.jax.mgi.shr.config.NewInstanceFailed";
  static {
    exceptionsMap.put(NewInstanceFailed, new ConfigException(
        "Could not create a new instance of class ??", false));
  }
  
	/**
	 * the user could not be found in the MGI_USER table when trying to
	 * lookup the key for the configured job stream name
	 */
	public static final String UserNotFound =
			"org.jax.mgi.shr.config.UserNotFound";
	static {
		exceptionsMap.put(UserNotFound, new ConfigException(
				"Could not access the user ?? in the MGI_USER table. " +
				"This could be that the user was not found in the table or there " +
				"was an exception accessing the database or configuration file",
				false));
	}


}
