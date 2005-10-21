//  $Header$
//  $Name$

package org.jax.mgi.shr.ioutils;

import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;

import org.jax.mgi.shr.ioutils.InputDataFile;
import org.jax.mgi.shr.ioutils.RecordDataIterator;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.config.LogCfg;
import org.jax.mgi.shr.config.OutputDataCfg;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.log.LoggerFactory;
import org.jax.mgi.shr.log.ConsoleLogger;
import org.jax.mgi.shr.unix.RunCommand;
import org.jax.mgi.shr.exception.MGIException;

/**
 * Is an object that represents an output data file. It is configurable using
 * the org.jax.mgi.shr.config package
 * @has OutputDataCfg object for configuration and a collection of
 * OutputFormatter objects
 * @does Provides the ability to write raw data to an output file and format
 * the data in various ways as configured to do in the configuration settings.
 * @company The Jackson Laboratory
 * @author M Walker
 */

public class OutputDataFile
{
    /**
     * a system constant for representing the tab character
     */
    public static final String TAB = "\t";
    /**
     * a system constant for representing the carriage return character
     */
    public static final String CRT = System.getProperty("line.separator");

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

    // an arraylist of OutputFormatter objects
    private ArrayList formatters = new ArrayList();

    // an arraylist of FileWriters corresponding to each OutputFormatter
    // object
    private ArrayList formatFiles = new ArrayList();

    // an arraylist of filenames corresponding to each formatted
    // output file
    private ArrayList formatFilenames = new ArrayList();

    // an array of OutputFormatters obtained from the configuration file
    private OutputFormatter[] configuredFormatters = null;

    // boolean indicator to determine whether or not it is ok to prevent
    // the raw output from being saved and only save formatted output
    private boolean okToPreventRawout = false;

    // option to auto-flush the buffer after each bcp write
    private boolean okToAutoFlush = false;

    // boolean indicator to determine whether it is ok or not to prevent files
    // form being formatted
    private boolean okToPreventFormatting = false;

    // sort command line options
    private String sortopts = null;

    // boolean indicator to determine if the file has been closed already
    private boolean isOpen = false;

    // the configuration object
    private OutputDataCfg config = null;

    // a HashMap to keep track of the number of times a particular suffix has
    // been used when creating new formatted output. This count is used in
    // the naming of the formatted output files if there are more than one for
    // any given filename suffix
    private HashMap formatCounts = new HashMap();

    // The following are the exceptions that are thrown.
    //
    private static final String NullOutFilename =
        IOUExceptionFactory.NullOutFilename;
    private static final String FileOpenErr =
        IOUExceptionFactory.FileOpenErr;
    private static final String FileWriteErr =
        IOUExceptionFactory.FileWriteErr;
    private static final String FileReadErr =
        IOUExceptionFactory.FileReadErr;
    private static final String FileCloseErr =
        IOUExceptionFactory.FileCloseErr;
    private static String SortInterrupt =
        IOUExceptionFactory.SortInterrupt;
    private static String SortIOErr =
        IOUExceptionFactory.SortIOErr;
    private static String SortNonZero =
        IOUExceptionFactory.SortNonZero;
    private static String FormatterErr =
        IOUExceptionFactory.FormatterErr;
    private static String FormatUnClosed =
        IOUExceptionFactory.FormatUnClosed;
    private static String PostFormatErr =
        IOUExceptionFactory.PostFormatErr;


    /**
     * default constructor
     * @throws IOUException thrown if there is a problem accessing the
     * database
     * @throws ConfigException thrown if there is a problem accessing the
     * configuration
     */
    public OutputDataFile()
    throws IOUException, ConfigException
    {
        configure(new OutputDataCfg());
    }

    /**
     * constructor which accepts a configuration object as input
     * @param cfg the configuration object from which to configure this
     * instance
     * @throws IOUException thrown if there is a problem creating the file
     * @throws ConfigException thrown if there is a problem accessing the
     * configuration
     */
    public OutputDataFile(OutputDataCfg cfg)
    throws IOUException, ConfigException
    {
        configure(cfg);
    }

    /**
     * constructor which allows specifying the filename at runtime and would
     * override any specified filename value from the configuration.
     * @param filename The name of the output file to write to.
     * @throws ConfigException if there is a configuration error.
     * @throws IOUException thrown if there is a problem creating the file.
     */
    public OutputDataFile(String filename)
        throws ConfigException, IOUException
    {
        this.filename = filename;
        configure(new OutputDataCfg());
    }

    /**
     * constructor which takes both a configuration object and a filename
     * @param filename The name of the output file to write to.
     * @param cfg The configuration object
     * @throws ConfigException if there is a configuration error.
     * @throws IOUException thrown if there is a problem creating the file.
     */
    public OutputDataFile(String filename, OutputDataCfg cfg)
        throws ConfigException, IOUException
    {
        this.filename = filename;
        configure(cfg);
    }

    /**
     * add an OutputFormatter for this instance. All formatted output is
     * created when the close() method
     * @param formatter the OutputFormatter to add
     * @throws IOUException thrown if a new file could not be created
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     */
    public void addFormatter(OutputFormatter formatter)
    throws IOUException, ConfigException
    {
        addFormatter(formatter, this.deriveFormattedFilename(formatter));
    }

    /**
     * add an OutputFormatter for this instance while designating the name of
     * the formatted filename
     * @param formatter the OutputFormatter object to add
     * @param filename the name of the formatted output file
     * @throws IOUException thrown if a new file could not be created
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     */
    public void addFormatter(OutputFormatter formatter,
                             String filename)
    throws IOUException, ConfigException
    {
        FileWriter file = null;
        try
        {
            file = new FileWriter(filename);
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileOpenErr, e);
            ke.bind(filename);
            throw ke;

        }
        this.formatters.add(formatter);
        this.formatFiles.add(file);
        this.formatFilenames.add(filename);
    }


    /**
     * sets whether or not to flush the output buffer on each write
     * @assumes nothing
     * @effects alters the indicator value
     * @param bool true if the buffer should be flushed, false otherwise
     */
    public void setOKToAutoFlush(boolean bool)
    {
        this.okToAutoFlush = bool;
    }

    /**
     * sets whether or not to prevent the saving of the raw output and only
     * save formatted output
     * @assumes nothing
     * @effects alters the indicator value
     * @param bool true if raw data should not be written, false otherwise
     */
    public void setOkToPreventRawOutput(boolean bool)
    {
        this.okToPreventRawout = bool;
    }

    /**
     * set the Logger for this instance.
     * @assumes nothing
     * @effects alters the logger being used
     * @param logger the Logger object to use for logging
     */
    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * sets the unix sort command options for this instance
     * @assumes nothing
     * @effects effects the way the raw output is sorted
     * @param opts the unix sort command options
     */
    public void setSortOptions(String opts)
    {
        this.sortopts = opts;
    }


    /**
     * Gets the name of the output file to write to.
     * @assumes Nothing
     * @effects Nothing
     * @return The name of the output file to write to.
     */
    public String getFilename()
    {
        return this.filename;
    }

    /**
     * gets the current configured value for whether or not to flush the
     * output buffer on every write
     * @assumes Nothing
     * @effects Nothing
     * @return true if the buffer is set to be flushed, false otherwise
     */
    public boolean getOKToAutoFlush()
    {
        return this.okToAutoFlush;
    }

    /**
     * gets the current configured value for the unix command sort options
     * @assumes Nothing
     * @effects Nothing
     * @return current configured value for the unix command sort options
     */
    public String getSortOptions()
    {
        return this.sortopts;
    }

    /**
     * gets the current configured value for whether or not to prevent
     * raw output from being saved
     * @assumes Nothing
     * @effects Nothing
     * @return true if the raw output should be saved, false otherwise
     */
    public boolean getOkToPreventRawOutput()
    {
        return this.okToPreventRawout;
    }

    /**
     * get the instance of the current Logger
     * @assumes Nothing
     * @effects Nothing
     * @return the instance of the current Logger
     */
    public Logger getLogger()
    {
        return this.logger;
    }


    /**
     * write a string to the output file.
     * @assumes a newline is not wanted after the write
     * @effects new output will be written to the file
     * @param s the string to write
     * @throws IOUException if an error occurs finding or opening the file.
     */
    public void write(String s)
        throws IOUException
    {
        if (!this.isOpen)
            this.openWriter();
        // Write a newline character to the output file.
        //
        try
        {
            writer.write(s);
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
     * write a string to the output file followed by a newline character.
     * @assumes a newline is wanted after the write
     * @effects new output will be written to the file
     * @param s the string to write
     * @throws IOUException if an error occurs finding or opening the file.
     */
    public void writeln(String s)
        throws IOUException
    {
        write(s + this.CRT);
    }




    /**
     * close the output file, sort the raw data and run the formatters
     * @assumes Nothing
     * @effects the raw data will be saved along with all formatted files
     * @throws IOUException if an error occurs finding or opening the file.
     * @throws ConfigException thrown if there is an error accessing the
     * configuration for formatting the output file
     */
    public void close()
        throws IOUException, ConfigException
    {
        try
        {
            if (this.isOpen)
                this.writer.close();
            else
            {
                // create empty file for a file that was never opened
                FileUtility.createFile(this.filename);
            }
            this.isOpen = false;
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileCloseErr, e);
            ke.bind(filename);
            throw ke;
        }
        if (!this.okToPreventFormatting)
            formatFile();
        if (this.okToPreventRawout == true)
        {
            File file = new File(this.filename);
            boolean b = file.delete();
        }

    }

    /**
     * just run the format methods of OutputFormatters on existing raw data
     * instead of creating new raw data
     * @assumes nothing
     * @effects formatted output files will be created
     * @throws IOUException thrown if there is an error accessing the
     * file system
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     */
    public void postFormat()
    throws IOUException, ConfigException
    {
        if (this.isOpen)
        {
            IOUExceptionFactory eFactory = new IOUExceptionFactory();
            IOUException e = (IOUException)
                eFactory.getException(FormatUnClosed);
            e.bind(this.filename);
            throw e;
        }
        formatFile();
    }

    /**
     * sort and format the raw output
     * @assumes nothing
     * @effects the formatters will be run and new formatted output files
     * will be created
     * @throws IOUException thrown if there is an error accessing the file
     * system
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     */
    protected void formatFile()
    throws IOUException, ConfigException
    {
        File file = new File(this.filename);
        if (!file.exists())
        {
            return;
        }
        String tempfile = this.filename + "temporary";
        if (this.sortopts != null)
        {
            int exitCode = 0;
            String cmd =
                "sort -t\"$TAB\" " + sortopts + " " + this.filename +
                " -o " + tempfile;
            RunCommand runner = new RunCommand();
            String[] env = {"TAB=\t"};
            try
            {
                runner.setCommand(cmd);
                runner.setEnv(env);
                exitCode = runner.run();
            }
            catch (InterruptedException e)
            {
                IOUExceptionFactory exceptionFactory =
                    new IOUExceptionFactory();
                IOUException e2 = (IOUException)
                    exceptionFactory.getException(SortInterrupt, e);
                e2.bind(cmd);
                throw e2;
            }
            catch (IOException e)
            {
                IOUExceptionFactory exceptionFactory =
                    new IOUExceptionFactory();
                IOUException e2 = (IOUException)
                    exceptionFactory.getException(SortIOErr, e);
                e2.bind(cmd);
                throw e2;
            }
            // The RunCommand executed without exception although the exit code
            // may still indicate an error has occurred. Log the contents of
            // standard out and standard error
            String msgErr = null;
            String msgOut = null;
            if ((msgErr = runner.getStdErr()) != null)
                logger.logInfo(msgErr);
            if ((msgOut = runner.getStdOut()) != null)
            {
                if (logger != null)
                    logger.logInfo(msgOut);
            }
            // exit code of non-zero indicates an error occurred while running
            // sort command.
            if (exitCode != 0)
            {
                FileUtility.deleteFile(tempfile);
                IOUExceptionFactory exceptionFactory =
                    new IOUExceptionFactory();
                IOUException e = (IOUException)
                    exceptionFactory.getException(SortNonZero);
                e.bind(cmd);
                e.bind(msgErr);
                throw e;
            }
            FileUtility.copyFile(tempfile, filename);
            FileUtility.deleteFile(tempfile);
        }
        callAllFormatters();
    }

    /**
     * derive the name of a formatted file based upon properties of
     * the given OutputFormatter
     * @assumes nothing
     * @effects nothing
     * @param formatter the OutputFormatter object on which to base the
     * derived file name
     * @return the derived file name
     * @throws ConfigException thrown if there is an error accessing the
     * configuration
     * @throws IOUException thrown if there is an error accessing the file
     * system
     */
    protected String deriveFormattedFilename(OutputFormatter formatter)
    throws ConfigException, IOUException
    {
        String newfilename = null;
        int suffixPos = this.filename.lastIndexOf(".");
        String fileSuffix = null;
        if (suffixPos > 0)
            fileSuffix = this.filename.substring(suffixPos + 1);

        String newSuffix = formatter.getFileSuffix();

        if (newSuffix == null || newSuffix.equals(""))
            newSuffix = "formatted";

        // keep track of how many times this suffix has been used
        Integer count = (Integer)this.formatCounts.get(newSuffix);
        String countString = null;
        if (count == null)
        {
            this.formatCounts.put(newSuffix, new Integer(1));
            countString = "";
        }
        else
        {
            int intCount = count.intValue();
            this.formatCounts.put(newSuffix, new Integer(++intCount));
            countString = count.toString();
        }


        if (fileSuffix != null)  // filename has a suffix
        {
            newfilename =
                this.filename.substring(0, suffixPos) + countString +
                "." + newSuffix;
        }
        else // filename has no suffix
        {
            newfilename = this.filename + countString +
                "." + newSuffix;
        }
        return newfilename;
    }



    /**
     * flush the buffered output to the output file
     * @assumes nothing
     * @effects buffer content will be written to the file
     * @throws IOUException thrown if there is an error accessing the file
     * system
     */
    protected void flush()
    throws IOUException
    {
        try
        {
            if (!this.okToPreventRawout)
                this.writer.flush();
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory =
                new IOUExceptionFactory();
            IOUException e2 = (IOUException)
                exceptionFactory.getException(FileWriteErr, e);
            e2.bind(this.filename);
            throw e2;
        }
    }

    private void configure(OutputDataCfg cfg)
    throws IOUException, ConfigException
    {
        this.config = cfg;
        if (this.filename == null)
            this.filename = cfg.getOutputFileName();
        if (this.filename == null)
        {
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            throw
                (IOUException)exceptionFactory.getException(NullOutFilename);
        }
        this.logger = new ConsoleLogger();
        this.okToAutoFlush = cfg.getOkToAutoFlush().booleanValue();
        this.okToPreventRawout = cfg.getOkToPreventRawOutput().booleanValue();
        this.okToPreventFormatting =
            cfg.getOkToPreventFormatting().booleanValue();
        this.configuredFormatters = cfg.gerFormatters();
        this.sortopts = cfg.getSortDef();
        if (this.configuredFormatters != null)
            for (int i = 0; i < this.configuredFormatters.length; i++)
            {
                OutputFormatter formatter = this.configuredFormatters[i];
                this.addFormatter(formatter);
            }
    }

    private void openWriter()
    throws IOUException
    {
        try
        {
            writer = new FileWriter(this.filename);
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            IOUException e2 = (IOUException)
                exceptionFactory.getException(FileOpenErr, e);
            e2.bind(this.filename);
            throw e2;
        }
        this.isOpen = true;

    }


    private void callAllFormatters()
    throws IOUException, ConfigException
    {
        for (int i = 0; i < this.formatters.size(); i++)
        {
            OutputFormatter formatter =
                (OutputFormatter)this.formatters.get(i);
            formatter.preprocess();
            FileWriter file = (FileWriter)this.formatFiles.get(i);
            try
            {
                file.write(formatter.getHeader() + this.CRT);
            }
            catch (IOException e)
            {
                IOUExceptionFactory exceptionFactory =
                    new IOUExceptionFactory();
                IOUException ke = (IOUException)
                    exceptionFactory.getException(FileWriteErr, e);
                ke.bind(filename);
                throw ke;
            }

            InputDataFile infile = new InputDataFile(this.filename);
            RecordDataIterator it = infile.getIterator();
            while (it.hasNext())
            {
                String s = null;
                try
                {
                    s = (String)it.next();
                }
                catch (MGIException e)
                {
                    IOUExceptionFactory exceptionFactory = new
                       IOUExceptionFactory();
                   IOUException e2 = (IOUException)
                       exceptionFactory.getException(FileReadErr, e);
                   e2.bind((String)this.formatFilenames.get(i));
                   throw e2;
                }
                try
                {
                    file.write(formatter.format(s));
                }
                catch (IOException e)
                {
                    IOUExceptionFactory exceptionFactory = new
                        IOUExceptionFactory();
                    IOUException ke = (IOUException)
                        exceptionFactory.getException(FileWriteErr, e);
                    ke.bind((String)this.formatFilenames.get(i));
                    throw ke;
                }
                catch (RuntimeException e)
                {
                    IOUExceptionFactory exceptionFactory = new
                        IOUExceptionFactory();
                    IOUException ke = (IOUException)
                        exceptionFactory.getException(FormatterErr, e);
                    ke.bind((String)this.formatFilenames.get(i));
                    ke.bind(formatter.getClass().getName());
                    throw ke;

                }
            }
            try
            {
                it.close();
            }
            catch (MGIException e)
            {
                IOUExceptionFactory exceptionFactory = new
                    IOUExceptionFactory();
                IOUException ke = (IOUException)
                    exceptionFactory.getException(FileCloseErr, e);
                ke.bind(this.filename);
                throw ke;
            }
            String trailer =
                    ((OutputFormatter)this.formatters.get(i)).getTrailer();
            try
            {
                file.write(this.CRT + trailer);
            }
            catch (IOException e)
            {
                IOUExceptionFactory exceptionFactory = new
                    IOUExceptionFactory();
                IOUException ke = (IOUException)
                    exceptionFactory.getException(FileWriteErr, e);
                ke.bind((String)this.formatFilenames.get(i));
                throw ke;
            }
            try
            {
                file.close();
            }
            catch (IOException e)
            {
                IOUExceptionFactory exceptionFactory = new
                    IOUExceptionFactory();
                IOUException ke = (IOUException)
                    exceptionFactory.getException(FileCloseErr, e);
                ke.bind((String)this.formatFilenames.get(i));
                throw ke;
            }
            formatter.postprocess();

        }
    }



}


//  $Log$
//  Revision 1.4  2005/10/13 11:35:01  sc
//  tr7108 branch merge to trunk
//
//  Revision 1.3.10.1  2005/10/06 20:44:45  mbw
//  header is now being written after the call to preprocess()
//
//  Revision 1.3  2005/08/09 19:48:23  mbw
//  javadocs only
//
//  Revision 1.2  2005/08/05 16:30:11  mbw
//  merged code from branch lib_java_core-tr6427-1
//
//  Revision 1.1.4.8  2005/08/01 19:22:32  mbw
//  javadocs only
//
//  Revision 1.1.4.7  2005/08/01 19:20:26  mbw
//  javadocs only
//
//  Revision 1.1.4.6  2005/08/01 19:17:21  mbw
//  javadocs only
//
//  Revision 1.1.4.5  2005/08/01 19:14:14  mbw
//  javadocs only
//
//  Revision 1.1.4.4  2005/08/01 18:22:39  mbw
//  fixed compile error
//
//  Revision 1.1.4.3  2005/08/01 15:55:32  mbw
//  added post formatting functionality
//
//  Revision 1.1.4.2  2005/06/08 18:30:55  mbw
//  javadocs only
//
//  Revision 1.1.4.1  2005/06/02 16:58:51  mbw
//  initial version
//
//  Revision 1.1.2.5  2005/01/08 00:57:42  mbw
//  added okToAutoFlush param
//
//  Revision 1.1.2.4  2005/01/07 19:15:26  mbw
//  bug fix: no longer throwing exception when calling constructor with a given filename
//
//  Revision 1.1.2.3  2005/01/07 19:00:48  mbw
//  now using a BufferedWriter and added config param okToAutoFlush
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
