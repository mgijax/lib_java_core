package org.jax.mgi.shr.dbutils.bcp;

import java.util.Hashtable;
import org.jax.mgi.shr.config.BCPManagerCfg;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;

/**
 * A class for accessing BCPManager references by name. The name
 * matches the prefix used for the configuration parameters for configuring
 * a BCPManager. The references accessed can be shared or new.
 * @has a mapping of BCPManager references by name
 * @does provides a either a shared or new reference to a BCPManager
 * @company The Jackson Laboratory
 * @author M Walker
 */

public class BCPManagerFactory {

  /**
   * name for a BCPManager reference to the MGD database
   */
  public static final String MGD = "MGD";
  /**
   * name of a BCPManager reference to the RADAR database
   */
  public static final String RADAR = "RADAR";

  /**
   * the internal mapping of names and references
   */
  private static Hashtable refmap = new Hashtable();

  /**
   * get a new reference to a BCPManager by name
   * @param name the name of the reference which would match some prefix used
   * for a set of BCPManager configuration parameters
   * @return the BCPManager reference
   * @throws ConfigException thrown if there was an error accessing the
   * configuration file
   * @throws DBException thrown if a new BCPManager reference could not be
   * created
   */
  static public BCPManager getNew(String name)
      throws ConfigException, DBException {
    BCPManagerCfg conf = new BCPManagerCfg(name);
    BCPManager mgr = new BCPManager(conf);
    BCPManagerCfg config = new BCPManagerCfg(name);
    mgr = new BCPManager(config);
    return mgr;
  }

  /**
   * get a shared reference to a BCPManager by name
   * @param name the name of the reference which would match some prefix used
   * for a set of BCPManager configuration parameters
   * @return the shared BCPManager reference
   * @throws ConfigException thrown if there was an error accessing the
   * configuration file
   * @throws DBException thrown if a new BCPManager reference could not be
   * created
   */
  static public BCPManager getShared(String name) throws ConfigException,
      DBException {
    BCPManager mgr = (BCPManager) refmap.get(name);
    if (mgr == null) {
      BCPManagerCfg config = new BCPManagerCfg(name);
      mgr = new BCPManager(config);
      refmap.put(name, mgr);
    }
    return mgr;
  }

}
