package org.jax.mgi.shr.config;

//import org.jax.mgi.shr.ioutils.OutputFormatter;

/**
 * A class for configuring an OutputDataFile object.
 * @has OutputDataFile configuration parameters with default values.
 * @does
 *   <UL>
 *   <LI> Provides methods for getting configuration paramaters for an
 *        OutputDataFile object.
 *   <LI> Provides the ability of prefix the configuration parameters with a
 *        string within the configuration file or system properties.
 *   </UL>
 * @company The Jackson Laboratory
 * @author D Miers, M Walker
 *
 */

public class OutputDataCfg extends Configurator {

    private String DEFAULT_DELIMITER = "\t";

    /**
     * Default constructor.
     * @assumes Nothing
     * @effects Nothing
     * @throws ConfigException thrown if there is a configuration error
     */
    public OutputDataCfg()
        throws ConfigException
    {
        super();
    }

    /**
     * Constructor that accepts a prefix string that will be prepended to
     * all configuration parameter names on lookup.
     * @assumes Nothing
     * @effects Nothing
     * @param pParameterPrefix the prefix to apply to all configuration
     * parameter names
     * @throws ConfigException thrown if there is a configuration error
     */
    public OutputDataCfg(String pParameterPrefix)
        throws ConfigException
    {
        super();
        super.parameterPrefix = pParameterPrefix;
    }

    /**
     * Gets the name of the output file.
     * @assumes Nothing
     * @effects Nothing
     * @return The name of the output file or null if the name is not found.
     */
      public String getOutputFileName()
      {
          return getConfigStringNull("OUTFILE_NAME");
      }

    /**
     * get the value of the option which designates whether to automatically
     * flush a buffer after each write.
     * The parameter name read from the configuration file
     * for setting this option is OUTFILE_AUTO_FLUSH and its default is false.
     * @return true or false
     * @throws ConfigException throws if configuration value does not represent
     * a boolean
     */
    public Boolean getOkToAutoFlush() throws ConfigException {
      return super.getConfigBoolean("OUTFILE_AUTO_FLUSH",
                                    new Boolean(false));
    }

}

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
* Copyright \251 1996, 1999, 2002, 2004 by The Jackson Laboratory
*
* All Rights Reserved
*
**************************************************************************/
