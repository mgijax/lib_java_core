// $Header$
// $Name$

package org.jax.mgi.shr.config;

import java.util.Properties;
import java.util.Vector;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.lang.reflect.Array;
import java.io.FileNotFoundException;
import java.io.IOException;


/**
 * An object that looks up configuration parameters in the java system
 * properties along with a set of configuration files. It implements the
 * Singleton pattern.
 * @has The collection of java system properties and a set of
 * Configuration classes (each of which represent a configuration file).
 * @does looks up parameters in the system properties. If they are not
 * found then it looks them up within the set of Configuration classes. The
 * system properties will override properties found in configuration files.
 * Configuration files are determined by the CONFIG system property. This
 * property is a comma separated list of configuration filenames ordered by
 * precedence.
 * @company The Jackson Laboratory
 * @author M Walker
 */

public class ConfigurationManager {

  private Configuration config = null;
  // these properties are searched first in order to override configurations
  private Properties systemProperties = null;
  // the singleton instance of the ConfigurationManager. It is returned by
  // the getInstance() method
  private static ConfigurationManager instance = null;
  // the exception factory for throwing ConfigExceptions
  private ConfigExceptionFactory eFactory = new ConfigExceptionFactory();

  /**
   * get the singleton instance of the ConfigurationManager.
   * @assumes nothing
   * @effects the Singleton instance is created if it didnt already
   * exist and the configuration files and system properties are read into
   * memory
   * @return a reference to the ConfigurationManagement instance
   * @throws ConfigException thrown if an exception occurs while reading the
   * configuration file.
   */
  protected static ConfigurationManager getInstance()
  throws ConfigException {
    if (instance == null) {
      instance = new ConfigurationManager();
    }
    return instance;
  }

  /**
   * look up a value of a named parameter in the system properties
   * and if not found then lookup within the set of Configuration classes.
   *
   * @assumes nothing
   * @effects nothing
   * @param name the named parameter to search on
   * @return the value of the named parameter or null if not found
   */
  protected String get(String name) {
    String value;
    value = (String)systemProperties.get(name);
    if (value == null && config != null)
      value = config.get(name);
    return value;
  }

  /**
   * get the key values for all the configuration parameters from the
   * configuration file(s) and the java system properties
   * @assumes nothing
   * @effects nothing
   * @return an array of configuration parameter names as Strings
   */
  protected String[] getKeys()
  {
      int counter = 0;
      ArrayList list = new ArrayList();
      Enumeration e = this.systemProperties.propertyNames();
      while (e.hasMoreElements())
      {
          list.add(e.nextElement());
          counter++;
      }
      if (config != null)
      {
          Iterator it = this.config.keys();
          while (it.hasNext())
          {
              list.add(it.next());
              counter++;
          }
      }
      String[] keys = new String[counter];
      for (int i = 0; i < list.size(); i++)
      {
          keys[i] = (String)list.get(i);
      }
      return keys;
  }

  /**
   * reread system properties and configuration files
   * @assumes nothing
   * @effects the configuration files and system properties are
   * reread into memory obtaining any runtime changes
   * @throws ConfigException thrown if an exception occurs reading the
   * configuration files
   */
  protected void reinit() throws ConfigException {
    instance.init();
  }

  /**
   * private constructor
   * @assumes nothing
   * @effects the ConfigurationInstance will be created and the
   * configuration files and system properties are read into memory
   * @throws ConfigException thrown if an exception occures reading the
   * configuration file.
   */
  private ConfigurationManager() throws ConfigException {
    init();
  }

  /**
   * read configuration files and system properties
   * @assumes nothing
   * @effects the ConfigurationInstance will be created and the
   * configuration files and system properties are read into memory
   * @throws ConfigException thrown if there is an exception reading the
   * configuration files
   */
  private void init() throws ConfigException {
    systemProperties = System.getProperties();
    String value = (String)systemProperties.get("CONFIG");

    if (value != null)
    {
        String[] configfiles = value.split(",");
        int len = Array.getLength(configfiles);
        String filename = configfiles[0];
        try
        {
            // load first file and do not use previously cached file
            config = Configuration.load(filename, false);
            // include the remaining
            for (int i = 1; i < len; i++)
            {
                filename = configfiles[i];
                config.include(filename);
            }
        }
        catch (FileNotFoundException e) {
          ConfigException e2 = (ConfigException)eFactory.getException(
                   ConfigExceptionFactory.FileNotFound, e);
          e2.bind(filename);
          throw e2;
        }
        catch (IOException e) {
          ConfigException e2 = (ConfigException)eFactory.getException(
                   ConfigExceptionFactory.MgrLoadErr, e);
          e2.bind(filename);
          throw e2;
        }

    }
    else // null out the instance in case the reinit() method was called
        config = null;
  }


}
// $Log$
// Revision 1.4  2004/08/25 20:24:14  mbw
// fixed so that the Configuration instanace is nulled out if reinit() is called and the CONFIG parameter is not set
//
// Revision 1.3  2004/07/21 18:30:05  mbw
// javadocs only
//
// Revision 1.2  2004/04/14 20:18:08  mbw
// now reads multiple config files and handles parameters set in one to be refered by another
//
// Revision 1.1  2003/12/30 16:50:06  mbw
// imported into this product
//
// Revision 1.4  2003/12/09 22:47:23  mbw
// merged jsam branch onto the trunk
//
// Revision 1.3.2.16  2003/10/02 19:39:13  mbw
// clearing the Vector of configuration instances on init now
//
// Revision 1.3.2.15  2003/06/04 18:28:34  mbw
// javadoc edits
//
// Revision 1.3.2.14  2003/05/22 15:48:47  mbw
// javadocs edits
//
// Revision 1.3.2.13  2003/04/22 22:17:27  mbw
// added casting of MGIException to ConfigException
//
// Revision 1.3.2.12  2003/04/22 00:11:22  mbw
// no longer throwing KnownException type
//
// Revision 1.3.2.11  2003/04/18 01:32:41  mbw
// fixed constructor so not to return void
//
// Revision 1.3.2.10  2003/04/18 01:03:09  mbw
// renamed ConfigLookup to Configurator
//
// Revision 1.3.2.9  2003/04/18 00:11:42  mbw
// updated to support coding standards
//
// Revision 1.3.2.8  2003/03/21 14:32:53  mbw
// javadcos
//
// Revision 1.3.2.7  2003/03/21 14:25:30  mbw
// added standard header/footer
//
// Revision 1.3.2.6  2003/03/21 14:16:35  mbw
// added standard header/footer
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
