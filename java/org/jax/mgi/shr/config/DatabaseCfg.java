// $Header$
// $Name$

package org.jax.mgi.shr.config;


/**
 * @is an object for configuring a database connection.
 * @has a set of database connection parameters and a reference to
 * a ConfigurationManager
 * @does provides methods for getting configuration paramaters
 * for a database connection and for providing default values for those
 * parameters that are not configured
 * @company Jackson Laboratory
 * @author M. Walker
 * @version 1.0
 */

public class DatabaseCfg
    extends Configurator implements DatabaseConfigurator {

  private String DEFAULT_SERVER = "DEV_MGI";
  private String DEFAULT_DATABASE = "mgd";
  private String DEFAULT_USER = "mgd_dbo";
  private String DEFAULT_URL = "rohan.informatics.jax.org:4100";
  private String DEFAULT_PWFILE =
      "/usr/local/mgi/dbutils/mgidbutilities/.mgd_dbo_password";
  private String DEFAULT_DBSCHEMA_DIR =
      "/usr/local/mgi/dbutils/mgd/mgddbschema";
  private String DEFAULT_CONNECTION_MANAGER =
      "org.jax.mgi.shr.dbutils.MGIDriverManager";

  /**
   * default constructor which obtains a reference to the ConfigurationManager
   * singleton class.
   * @throws ConfigException
   */
  public DatabaseCfg() throws ConfigException {
    super();
  }

  /**
   * constructor which accepts a prefix string that will be prepended to
   * all lookup parameter names
   * @param pParameterPrefix the given prefix string
   * @throws ConfigException throws if there is a configuration error
   */
  public DatabaseCfg(String pParameterPrefix) throws ConfigException {
    super();
    super.parameterPrefix = pParameterPrefix;
  }


  /**
   * get the server name that hosts the database. The name of the
   * configuration parameter is DBSERVER. The default value is
   * DEV_MGI. If prefixing is used and the value is not found, then a
   * second lookup will be performed on the non-prefixed value. If the
   * second lookup fails then the default value is used.
   * @return server name
   */
  public String getServer() {
    return dualLookup("DBSERVER", DEFAULT_SERVER);
  }

  /**
   * get the name of the database. The name of the configuration
   * parameter is DBNAME. The default value if not set is mgd.
   * @return database name
   */
  public String getDatabase() {
    return dualLookup("DBNAME", DEFAULT_DATABASE);
  }

  /**
   * get the login user name for the database connection. The name of the
   * configuration parameter is DBUSER. The default value if not set is
   * mgd_dbo.
   * @return login user
   */
  public String getUser() {
    return dualLookup("DBUSER", DEFAULT_USER);
  }

  /**
   * get the login password for the database connection. The name of the
   * configuration parameter is DBPASSWORD. There is no default value for
   * this parameter. If it is not specified in the configuration file then
   * its value will be null. It is recommended to use the DBPASSWORDFILE
   * parameter instead to point to file which contains the passowrd
   * @return login password
   */
  public String getPassword() {
    return dualLookup("DBPASSWORD", null);
  }

  /**
   * get the database url. The name of the configuration parameter is DBURL.
   * The default value if not set is rohan.informatics.jax.org:4100.
   * @return database url
   */
  public String getUrl() {
    return dualLookup("DBURL", DEFAULT_URL);
  }

  /**
   * get the name of a file that holds the login password for the
   * database connection. The name of the configuration
   * parameter is DBPASSWORDFILE. The default value if not set is
   * /usr/local/mgi/dbutils/mgidbutilities/.mgd_dbo_password
   * @return password file name
   */
  public String getPasswordFile() {
    return dualLookup("DBPASSWORDFILE", DEFAULT_PWFILE);
  }

  /**
   * get the directory name for the installation of the database schema
   * product. The name of the configuration parameter is DBSCHEMADIR.
   * The default value if not set is
   * /usr/local/mgi/dbutils/mgidbutilities/mgd/mgddbschema.
   * @return database schema product installation directory
   */
  public String getDBSchemaDir() {
    return dualLookup("DBSCHEMADIR", DEFAULT_DBSCHEMA_DIR);
  }
 

  /**
   * get the name of the ConnectionManager class used for instantiating
   * a ConnectionManager instance which is used for obtaining a connection
   * to the database. The name of the configuration parameter is
   * DBCONNECTION_MANAGER. The default value if not set is
   * org.jax.mgi.shr.dbutils.MGIDriverManager
   * @return the name of the ConnectionManager class
   */
  public String getConnectionManagerClass() {
    return dualLookup("DBCONNECTION_MANAGER",
                           DEFAULT_CONNECTION_MANAGER);
  }

  /**
   * get the name of the LoggerFactory class used for instantiating a
   * LoggerFactory which is used for creating a general purpose Logger
   * or return null if not found
   * @return the name of the LoggerFactory class
   */
  public String getLoggerFactoryClass() {
    return dualLookup("LOGGER_FACTORY", null);
  }

  /**
   * looks up a parameter and if not found and prefixing is being used
   * then lookup the value again as un-prefixed before applying the
   * default value
   * @param param the parameter to lookup on
   * @param defaultValue the default value to return if the value
   * could not be found either by using the prefix or not
   * @return the determined value
   */
  private String dualLookup(String param, String defaultValue)
  {
    String value = this.getConfigStringNull(param);
    if (value == null)
    {
      if (this.parameterPrefix != null) {
        // parameter was not found using prefixing.
        // try and find it as an unprefixed value before
        // applying the default value
        this.setApplyPrefix(false);
        value = this.getConfigString(param, defaultValue);
        this.setApplyPrefix(true);
      }
      else { // value was not found and we are not using prefixing
        value = defaultValue;
      }
    }
    return value;
  }


}
// $Log$
// Revision 1.4  2003/12/09 22:48:39  mbw
// merged jsam branch onto the trunk
//
// Revision 1.3.2.19  2003/10/22 15:38:12  mbw
// no change
//
// Revision 1.3.2.18  2003/09/10 21:07:52  mbw
// now doing a second lookup if prefixing is being used for the non-prefixed term before applying the default
//
// Revision 1.3.2.17  2003/06/23 19:36:34  mbw
// added methods for obtaining a connection_manager and a logger
//
// Revision 1.3.2.16  2003/06/04 14:51:44  mbw
// javadoc edits
//
// Revision 1.3.2.15  2003/05/22 16:15:34  mbw
// now implements DatabaseConfigurator interface
//
// Revision 1.3.2.14  2003/05/22 15:52:02  mbw
// javadocs edits
//
// Revision 1.3.2.13  2003/05/16 15:11:15  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.3.2.12  2003/04/25 17:17:44  mbw
// changed to reflect new design of exception handling
//
// Revision 1.3.2.11  2003/04/18 01:53:51  mbw
// changed classes to extend Configurator
//
// Revision 1.3.2.10  2003/04/15 12:00:22  mbw
// javadoc edits
//
// Revision 1.3.2.9  2003/04/09 21:18:21  mbw
// removed setter methods; replaced code in getter methods to use ConfigLookup class; added parameter name prefixing feature
//
// Revision 1.3.2.8  2003/04/04 00:07:00  mbw
// added clone method
//
// Revision 1.3.2.7  2003/04/03 18:58:46  mbw
// javadocs
//
/**************************************************************************
*
* Warranty Disclaimer and Copyright Notice
*
*  THE JACKSON LABORATORY MAKES NO REPRESENTATION ABOUT THE SUITABILITY OR
*  ACCURACY OF THIS SOFTWARE OR DATA FOR ANY PURPOSE, AND MAKES NO WARRANTIES,
*  EITHER EXPRESS OR IMPLIED, INCLUDING MERCHANTABILITY AND FITNESS FOR A
*  PARTICULAR PURPOSE OR THAT THE USE OF THIS SOFTWARE OR DATA WILL NOT
*  INFRINGE ANY THIRD PARTY PATENTS, COPYRIGHTS, TRADEMARKS, OR OTHER RIGHTS.
*  THE SOFTWARE AND DATA ARE PROVIDED "AS IS".
*
*  This software and data are provided to enhance knowledge and encourage
*  progress in the scientific community and are to be used only for research
*  and educational purposes.  Any reproduction or use for commercial purpose
*  is prohibited without the prior express written permission of The Jackson
*  Laboratory.
*
* Copyright \251 1996, 1999, 2002 by The Jackson Laboratory
*
* All Rights Reserved
*
**************************************************************************/
