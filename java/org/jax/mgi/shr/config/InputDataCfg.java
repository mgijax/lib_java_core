// $Header$
// $Name$

package org.jax.mgi.shr.config;


/**
 * @is an object for configuring an InputDataFile object.
 * @has a set of InputDataFile configuration parameters and a
 * reference to a ConfigurationManager singleton object.
 * @does provides methods for getting configuration paramaters
 * for an InputDataFile object. It also provides default values for parameters
 * that were not configured. A prefix string can be provided to the
 * constructor which would cause a lookup of configuration parameter names
 * within the configuration files or system properties that are prefixed by
 * the given string.
 * @company Jackson Laboratory
 * @author M. Walker
 * @version 1.0
 */

public class InputDataCfg extends Configurator {

  // default delimiter
  private String DEFAULT_END_DELIMITER = "$";

  //default bufferSize
  private int DEFAULT_BUFFER_SIZE = 512000;

  /**
   * default constructor
   * throws ConfigException thrown if there is a configuration error
   */
  public InputDataCfg() throws ConfigException {
    super();
  }
  /**
   * constructor which accepts a prefix string that will be prepended to
   * all configuration parameter names on lookup. For example, if the prefix
   * value is the string 'SECONDARY', then a call to getInputFileName()
   * will lookup the parameter SECONDARY_INFILE_NAME instead of INFILE_NAME.
   * @param pParameterPrefix the given prefix string
   * @throws KnownException throws if there is a configuration error
   */
  public InputDataCfg(String pParameterPrefix) throws ConfigException {
    super();
    super.parameterPrefix = pParameterPrefix;
  }


  /**
   * get the name of the input file. The parameter name read from the
   * configuration file or system properties is INFILE_NAME. If the
   * parameter is not found then null is returned.
   * @return the name of the input file
   */
  public String getInputFileName() {
    return getConfigStringNull("INFILE_NAME");
  }

  /**
   * get the record delimeter for the input file. The delimiter is a java
   * based regular expression. The parameter name read from the
   * configuration file or system properties is INFILE_END_DELIMITER.
   * The default value is newline
   * @return the regular expression used as record delimiter
   */
  public String getEndDelimiter() {
    return getConfigString("INFILE_END_DELIMITER", this.DEFAULT_END_DELIMITER);
  }

  /**
   * get the size of the internal memory buffer. The parameter name read
   * from the configuration file or system properties is INFILE_BUFFERSIZE.
   * The default value = 512000.
   * @return the configured buffer size
   */
  public Integer getBufferSize() throws ConfigException {
    return getConfigInteger("INFILE_BUFFERSIZE",
                            new Integer(this.DEFAULT_BUFFER_SIZE));
  }

  /**
   * get the value of the option which designates whether to use
   * regular expressions when doing delimiter matching.  The parameter
   * name read from the configuration file for setting this option is
   * INFILE_USE_REGEX and its default is true. This value provides the
   * initial parameter setting for an instance of the InputDataFile class
   * and can be overriden by setter methods within that class
   * @return true or false
   * @throws ConfigException throws if configuration value does not represent
   * a boolean
   */
  public Boolean getOkToUseRegex() throws ConfigException {
    return getConfigBoolean("INFILE_USE_REGEX", new Boolean(true));
  }




}
// $Log$
// Revision 1.1  2003/12/30 16:50:09  mbw
// imported into this product
//
// Revision 1.2  2003/11/05 16:39:08  dbm
// Changed INFILE_FILENAME to INFILE_NAME
//
// Revision 1.1  2003/11/04 15:55:30  mbw
// renamed InputDataFileCfg to InputDataCfg.
// also added new parameters for turning on/off use of regular expression matching
//
// Revision 1.4  2003/06/04 15:06:27  mbw
// javadoc edits
//
// Revision 1.3  2003/05/22 15:50:13  mbw
// javadocs edits
//
// Revision 1.2  2003/05/20 15:17:36  mbw
// renamed from FileInputStreamCfg.java
//
// Revision 1.1.2.3  2003/05/16 15:10:32  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.1.2.2  2003/04/29 21:16:55  mbw
// getBufferSize() now returns Integer
//
// Revision 1.1.2.1  2003/04/29 17:27:09  mbw
// renamed InputDataSourceCfg.java to InputDataFileCfg.java
//
// Revision 1.1.2.3  2003/04/28 16:25:20  mbw
// changed due to impact of new exception handling model
//
// Revision 1.1.2.2  2003/04/22 22:23:22  mbw
// now using the Configurator base class
//
// Revision 1.1.2.1  2003/04/10 17:13:25  mbw
// renamed from FileInputStreamCfg.java
//
// Revision 1.2.2.4  2003/03/21 16:10:47  mbw
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
