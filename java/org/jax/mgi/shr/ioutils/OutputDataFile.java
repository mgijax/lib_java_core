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
 * @is An object that represents an output file.
 * @has Configurable parameters
 * @does Provides the ability to write to the output file.
 * @company The Jackson Laboratory
 * @author mbw
 */

public class OutputDataFile
{
    // String constants.
    //
    public static final String TAB = "\t";
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

    private ArrayList formatters = new ArrayList();

    private ArrayList formatFiles = new ArrayList();

    private ArrayList formatFilenames = new ArrayList();

    private OutputFormatter[] configuredFormatters = null;

    private boolean okToPreventRawout = false;

    /*
     * option to auto-flush the buffer after each bcp write
     */
    private boolean okToAutoFlush = false;

    private boolean okToPreventFormatting = false;

    /*
     * sort command line options
     */
    private String sortopts = null;

    private boolean isOpen = false;

    private OutputDataCfg config = null;

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


    public OutputDataFile()
    throws IOUException, ConfigException
    {
        configure(new OutputDataCfg());
    }

    public OutputDataFile(OutputDataCfg cfg)
    throws IOUException, ConfigException
    {
        configure(cfg);
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
        this.filename = filename;
        configure(new OutputDataCfg());
    }

    public OutputDataFile(String filename, OutputDataCfg cfg)
        throws ConfigException, IOUException
    {
        this.filename = filename;
        configure(cfg);
    }

    public void addFormatter(OutputFormatter formatter)
    throws IOUException, ConfigException
    {
        addFormatter(formatter, this.deriveFormattedFilename(formatter));
    }



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
        try
        {
            file.write(formatter.getHeader() + this.CRT);
        }
        catch (IOException e)
        {
            IOUExceptionFactory exceptionFactory = new IOUExceptionFactory();
            IOUException ke = (IOUException)
                exceptionFactory.getException(FileWriteErr, e);
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
     * @effects nothing
     * @param bool true if the buffer should be flushed, false otherwise
     */
    public void setOKToAutoFlush(boolean bool)
    {
        this.okToAutoFlush = bool;
    }

    public void setOkToPreventRawOutput(boolean bool)
    {
        this.okToPreventRawout = bool;
    }

    public void setLogger(Logger logger)
    {
        this.logger = logger;
    }

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

    public String getSortOptions()
    {
        return this.sortopts;
    }

    public boolean getOkToPreventRawOutput()
    {
        return this.okToPreventRawout;
    }

    public Logger getLogger()
    {
        return this.logger;
    }


    /**
     * Write a string to the output file.
     * @assumes a newline is not wanted after the write
     * @effects new output will be written to the file
     * @params the string to write
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
     * Write a string to the output file.
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
     * Close the output file.
     * @assumes Nothing
     * @effects Nothing
     * @throws IOUException if an error occurs finding or opening the file.
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
