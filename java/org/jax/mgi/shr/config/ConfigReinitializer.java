package org.jax.mgi.shr.config;

/**
 * @is a class for reinitializing the ConfigurationManager class and having it
 * re-read the configuration files
 * @author M Walker
 * @version 1.0
 */

public class ConfigReinitializer {

  public static void reinit() throws ConfigException
  {
    ConfigurationManager cm = ConfigurationManager.getInstance();
    cm.reinit();
  }

}