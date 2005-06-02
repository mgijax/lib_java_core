//  $Header$
//  $Name$

package org.jax.mgi.shr.ioutils;

import java.io.IOException;
import java.io.BufferedWriter;
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

    // The name of the output file to write to.
    //
    //private BufferedWriter writer = null;
    private FileWriter writer = null;

    // A logger for logging messages.
    //
    private Logger logger = null;

    /*
     * option to auto-flush the buffer after each bcp write
     */
    private boolean okToAutoFlush;

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
        if (filename == null)
        {
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            throw (IOUException) exceptionFactory.getException(
                NullFilename);
        }
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

    public void setOKToAutoFlush(boolean pDelimiter)
    {
        this.okToAutoFlush = pDelimiter;
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

    public boolean getOKToAutoFlush()
    {
        return this.okToAutoFlush;
    }


    /**
     * Configure the instance variables.
     * @assumes Nothing
     * @effects Nothing
     * @param pConfig The configuration object from which to configure this
     *                object.
     * @return
     * @throws ConfigException if there is a configuration error.
     * @throws IOUException if an error occurs finding or opening the file.
     */
    private void configure(OutputDataCfg pConfig)
      throws IOUException, ConfigException
    {
        this.okToAutoFlush = pConfig.getOkToAutoFlush().booleanValue();

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
        {
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            throw (IOUException) exceptionFactory.getException(
                NullFilename);
        }

        // Open the output file.
        //
        try
        {
            //writer = new BufferedWriter(new FileWriter(filename));
            writer = new FileWriter(filename);
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileOpenErr, e);
            ke.bind(filename);
            throw ke;
        }
        return;
    }


    /**
     * Write a string to the output file.
     * @assumes a newline is wanted after the write
     * @effects new output will be written to the file
     * @param s the string to write
     * @throws IOUException if an error occurs finding or opening the file.
     */
    public void write(String s)
        throws IOUException
    {
        // Write a newline character to the output file.
        //
        try
        {
            writer.write(s);
            writer.write(CRT);
            if (this.okToAutoFlush)
                writer.flush();
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileWriteErr, e);
            ke.bind(filename);
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
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileCloseErr, e);
            ke.bind(filename);
            throw ke;
        }
    }
}


//  $Log$
//  Revision 1.1.2.5  2005/01/08 00:57:42  mbw
//  added okToAutoFlush param
//
//  Revision 1.1.2.4  2005/01/07 19:15:26  mbw
//  bug fix: no longer throwing exception when calling constructor with a given filename
//
//  Revision 1.1.2.3  2005/01/07 19:00:48  mbw
//  now using a BufferedWriteer and added config param okToAutoFlush
//
//  Revision 1.1.2.2  2005/01/07 17:08:34  mbw
//  created way for OutputDataFile to write any object using the OutputFormat interface
//
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
