//  $Header$
//  $Name$

package org.jax.mgi.shr.ioutils;

import java.io.IOException;
import java.io.FileWriter;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;

import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.config.LogCfg;
import org.jax.mgi.shr.config.OutputDataCfg;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.log.LoggerFactory;

/**
 * @is An object that represents an output file.
 * @has Configurable parameters
 * @does Provides the ability to write to the output file.
 * @company The Jackson Laboratory
 * @author dbm
 */

public class OutputDataFile
{
    // String constants.
    //
    private static final String TAB = "\t";
    private static final String CRT = "\n";

    // The name of the output file to write to.
    //
    private String filename = null;

    // The delimiter to add between fields of the output file.
    //
    private String delimiter = TAB;

    // The name of the output file to write to.
    //
    private FileWriter writer = null;

    // A logger for logging messages.
    //
    private Logger logger = null;

    // The exception factory.
    //
    private IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();

    // The following are the exceptions that are thrown.
    //
    private static final String NullFilename =
        IOUExceptionFactory.NullFilename;
    private static final String FileOpenErr =
        IOUExceptionFactory.FileOpenErr;
    private static final String FileWriteErr =
        IOUExceptionFactory.FileWriteErr;
    private static final String FileCloseErr =
        IOUExceptionFactory.FileCloseErr;

    /**
     * Constructor which gets its values from the system configuration.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @throws ConfigException if there is a configuration error.
     * @throws IOUException if an error occurs finding or opening the file.
     */
    public OutputDataFile()
        throws ConfigException, IOUException
    {
        configure(new OutputDataCfg());
    }

    /**
     * Constructor which allows overridding default values with a configuration
     * object.
     * @assumes Nothing
     * @effects Nothing
     * @param config The configuration class.
     * @throws ConfigException if there is a configuration error.
     * @throws IOUException if an error occurs finding or opening the file.
     */
    public OutputDataFile(OutputDataCfg config)
        throws ConfigException, IOUException
    {
        configure(config);
    }

    /**
     * Constructor which allows specifying the filename at runtime and would
     * override any specified filename value from the configuration.
     * @assumes Nothing
     * @effects Nothing
     * @param filename The name of the output file to write to.
     * @throws ConfigException if there is a configuration error.
     * @throws IOUException if an error occurs finding or opening the file.
     */
    public OutputDataFile(String filename)
        throws ConfigException, IOUException
    {
        if (this.filename == null)
            throw (IOUException)exceptionFactory.getException(NullFilename);
        this.filename = filename;
        configure(new OutputDataCfg());
    }

    /**
     * Sets the name of the output file to write to.
     * @assumes Nothing
     * @effects Nothing
     * @param pFilename The name of the output file to write to.
     * @return Nothing
     * @throws Nothing
     */
    public void setFilename(String pFilename)
    {
        this.filename = pFilename;
    }

    /**
     * Sets the delimited to be used between fields in the output file.
     * @assumes Nothing
     * @effects Nothing
     * @param pDelimiter The delimited to be used between fields in the output file.
     * @return Nothing
     * @throws Nothing
     */
    public void setDelimiter(String pDelimiter)
    {
        this.delimiter = pDelimiter;
    }

    /**
     * Gets the name of the output file to write to.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The name of the output file to write to.
     * @throws Nothing
     */
    public String getFilename()
    {
        return this.filename;
    }

    /**
     * Gets the delimited to be used between fields in the output file.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return The delimited to be used between fields in the output file.
     * @throws Nothing
     */
    public String getDelimiter()
    {
        return this.delimiter;
    }

    /**
     * Configure the instance variables.
     * @assumes Nothing
     * @effects Nothing
     * @param pConfig The configuration object from which to configure this
     *                object.
     * @return Nothing
     * @throws ConfigException if there is a configuration error.
     * @throws IOUException if an error occurs finding or opening the file.
     */
    private void configure(OutputDataCfg pConfig)
      throws IOUException, ConfigException
    {
        this.filename = pConfig.getOutputFileName();
        this.delimiter = pConfig.getDelimiter();

        LogCfg cfg = new LogCfg();
        LoggerFactory factory = cfg.getLoggerFactory();
        this.logger = factory.getLogger();

        // The filename may not have been defined through the constructor.
        // Get it from the configuration.
        //
        if (this.filename == null)
            this.filename = pConfig.getOutputFileName();

        // If no filename was defined through the constructor or configuration,
        // thrown an exception.
        //
        if (this.filename == null)
            throw (IOUException)exceptionFactory.getException(NullFilename);

        // Open the output file.
        //
        try
        {
            writer = new FileWriter(filename);
        }
        catch (IOException e)
        {
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileOpenErr, e);
            throw ke;
        }
    }

    /**
     * Write a vector of strings to the output file.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return Nothing
     * @throws IOUException if an error occurs finding or opening the file.
     */
    public void write(Collection list)
        throws IOUException
    {
        // If the vector is empty, just return.
        //
        if (list.size() == 0)
            return;

        // Write each string in the vector to the output file.
        //
        boolean firstItem = true;
        for (Iterator i = list.iterator(); i.hasNext();)
        {
            try
            {
                if (!firstItem)
                    writer.write(delimiter);
                writer.write((String)i.next());
                firstItem = false;
            }
            catch (IOException e)
            {
                IOUException ke = (IOUException)
                    exceptionFactory.getException(FileWriteErr, e);
                throw ke;
            }
        }

        // Write a newline character to the output file.
        //
        try
        {
            writer.write(CRT);
        }
        catch (IOException e)
        {
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileWriteErr, e);
            throw ke;
        }
    }

    /**
     * Write a vector of strings to the output file.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return Nothing
     * @throws IOUException if an error occurs finding or opening the file.
     */
    public void write(OutputFormat formatable)
        throws IOUException
    {
        String s = formatable.format();

        // Write a newline character to the output file.
        //
        try
        {
            writer.write(s);
            writer.write(CRT);
        }
        catch (IOException e)
        {
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileWriteErr, e);
            throw ke;
        }
    }


    /**
     * Close the output file.
     * @assumes Nothing
     * @effects Nothing
     * @param None
     * @return Nothing
     * @throws IOUException if an error occurs finding or opening the file.
     */
    public void close()
        throws IOUException
    {
        try
        {
            writer.close();
        }
        catch (IOException e)
        {
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileCloseErr, e);
            throw ke;
        }
    }
}


//  $Log$
//  Revision 1.1.2.1  2005/01/05 14:26:56  dbm
//  New
//
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
* Copyright \251 1996, 1999, 2002, 2004 by The Jackson Laboratory
*
* All Rights Reserved
*
**************************************************************************/
