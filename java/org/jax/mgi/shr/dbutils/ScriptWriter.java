package org.jax.mgi.shr.dbutils;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;

import org.jax.mgi.shr.config.ScriptWriterCfg;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.unix.RunCommand;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.log.LoggerFactory;
import org.jax.mgi.shr.config.LogCfg;

/**
 * A class which creates and executes sql scripts within the Sybase
 * database
 * @has a configuration object for configuring
 * @does provides methods for creating and executing sql scripts
 * @company The Jackson Laboratory
 * @author M Walker
 */

public class ScriptWriter {

  /**
   * SQLDataManager to execute the script against
   */
 private SQLDataManager sqlMgr = null;
  /**
   * the name of the script filename
   */
  private String filename = null;
  /**
   * the suffix of the script filename
   */
  private String suffix = null;
  /**
   * the name of the script output filename
   */
  private String outfilename = null;
  /**
   * the suffix of the script output filename
   */
  private String outsuffix = null;
  /*
   * the path where the script files are stored
   */
  private String path = null;
  /*
   * option to not run the script file on execute
   */
  private boolean preventExecute;
  /*
   * option to overwrite existing script files
   */
  private boolean okToOverwrite;
  /*
   * option to use temp file when creating the script file
   */
  private boolean useTempFile;
  /*
   * option to remove the script file after executing
   */
  private boolean removeAfterExecute;
  /**
   * a class that implements the Logger interface
   */
  private Logger logger = null;
  /**
   * the File object for the script
   */
  private File scriptFile = null;
  /*
   * the java file writer object used for writing to the script file
   */
  private FileWriter fileWriter = null;
  /*
   * a java buffered writer for buffering the writes
   */
  private BufferedWriter bufferedWriter = null;
  /**
   * indicator as to whether or not the script file has been closed
   */
  private boolean closed = false;


  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String TempFileCreateErr =
      ScriptExceptionFactory.TempFileCreateErr;
  private static String FileCreateErr =
      ScriptExceptionFactory.FileCreateErr;
  private static String FileCloseErr =
      ScriptExceptionFactory.FileCloseErr;
  private static String FileWriteErr =
      ScriptExceptionFactory.FileWriteErr;
  private static String InterruptErr =
      ScriptExceptionFactory.InterruptErr;
  private static String ScriptIOErr =
      ScriptExceptionFactory.ScriptIOErr;
  private static String NonZeroErr =
      ScriptExceptionFactory.NonZeroErr;


  /**
   * default constructor which reads the configuration from a
   * default configuration object
   * @param sqlMgr the SQLDataManager to execute the script against
   * @throws ConfigException thrown if a configuration error occurs
   * @throws ScriptException thrown if there was an error creating the
   * script file
   */
  public ScriptWriter(SQLDataManager sqlMgr) throws ConfigException, ScriptException
  {
    this.sqlMgr = sqlMgr;
    configure(new ScriptWriterCfg());
    createScriptFile();
  }


  /**
   * constructer which uses a given configuration object for obtaining
   * configuration parameters which is normally used for designating a
   * parameter prefix which is prepended to all parameter names on lookup
   * @param config the configuration object from which to configure the
   * ScriptWriter
   * @param sqlMgr the SQLDataManager to execute the script against
   * @throws ConfigException thrown if there is an error obtaining
   * configuration parameters
   * @throws ScriptException thrown if there was an error creating the
   * script file
   */
  public ScriptWriter(ScriptWriterCfg config, SQLDataManager sqlMgr)
      throws ConfigException, ScriptException
  {
    this.sqlMgr = sqlMgr;
    configure(config);
    createScriptFile();
  }

  /**
   * set the Logger instance
   * @assumes nothing
   * @effects the internal reference to a Logger will be set
   * @param logger the Logger
   */
  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  /**
   * write a line to the script
   * @param s the line to write
   * @throws ScriptException thown if there is an error writing to the script
   * file
   */
  public void write(String s) throws ScriptException
  {
    try
    {
      bufferedWriter.write(s + "\n");
    }
    catch (IOException e)
    {
      ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
      ScriptException e2 = (ScriptException)
          eFactory.getException(FileWriteErr, e);
      e2.bind(scriptFile.getName());
      throw e2;
    }
  }

  /**
   * write a line to the script and include the command termination string
   * ('go' in the case of the Sybase database)
   * @param s the line to write along with the command termination string
   * @throws ScriptException thown if there is an error writing to the script
   * file
   */
  public void writeGo(String s) throws ScriptException
  {
    try
    {
        bufferedWriter.write(s + "\n");
        go();
    }
    catch (IOException e)
    {
      ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
      ScriptException e2 = (ScriptException)
          eFactory.getException(FileWriteErr, e);
      e2.bind(scriptFile.getName());
      throw e2;
    }
  }


  /**
   * write the command termination string to the script file ('go' in the case
   * of the Sybase database)
   * @throws ScriptException thrown if there is an error writing to the script
   * file
   */
  public void go() throws ScriptException
  {
    try
    {
      bufferedWriter.write("go\n");
    }
    catch (IOException e)
    {
      ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
      ScriptException e2 = (ScriptException)
          eFactory.getException(FileWriteErr, e);
      e2.bind(scriptFile.getName());
      throw e2;
    }
  }


 /**
  * executes the script and closes resources
  * @assumes nothing
  * @effects the script file will be executed and closed
  * @throws ScriptException thrown if there is an error executing the script
  */
  public void execute() throws ScriptException
  {
    String server = sqlMgr.getServer();
    String db = sqlMgr.getDatabase();
    String user = sqlMgr.getUser();
    String pwFile = sqlMgr.getPasswordFile();
    String cmd = "cat " + pwFile + " | isql -U" + user + " -S" + server +
                 " -D" + db + " -i " + path + File.separator + filename + "." +
                 suffix + " -o " + path + File.separator + outfilename + "." +
                 outsuffix + " -e";
    try
    {
      close();
    }
    catch (IOException e)
    {
      ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
      ScriptException e2 = (ScriptException)
          eFactory.getException(FileCloseErr, e);
      e2.bind(scriptFile.getName());
      throw e2;
    }
    // check attributes to see if script execution should not be performed
    if (preventExecute)
      return;
    if (scriptFile.length() == 0)
      return;
    RunCommand runner = new RunCommand();
    runner.setCommand(cmd);
    int exitCode = 0;
    try
    {
      exitCode = runner.run();
    }
    catch (InterruptedException e)
    {
      ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
      ScriptException e2 = (ScriptException)
          eFactory.getException(InterruptErr, e);
      e2.bind(cmd);
      throw e2;
    }
    catch (IOException e)
    {
      ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
      ScriptException e2 = (ScriptException)
          eFactory.getException(ScriptIOErr, e);
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
    if ((msgOut = runner.getStdOut()) != null) {
      if (logger != null)
        logger.logInfo(msgOut);
    }
    // throw a ScriptException on non-zero exit code
    if (exitCode != 0) {
      ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
      ScriptException e = (ScriptException)
          eFactory.getException(NonZeroErr);
      e.bind(cmd);
      throw e;
    }
    if (this.removeAfterExecute)
      scriptFile.delete();


  }

  /**
   * aborts the script and will close resources without executing the script
   * file
   * @assumes nothing
   * @effects the script file will be closed
   * @throws ScriptException thrown if there is an error executing the script
   */
  public void abort() throws ScriptException
  {
    try
    {
      close();
    }
    catch (IOException e)
    {
      ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
      ScriptException e2 = (ScriptException)
          eFactory.getException(FileCloseErr, e);
      e2.bind(scriptFile.getName());
      throw e2;
    }
    if (this.removeAfterExecute)
      scriptFile.delete();
  }

  /**
   * sets all the attributes from the configuration object
   * @assumes nothing
   * @effects all internal values will be set
   * @param config the configuration object
   * @throws ConfigException throws if there is a configuration error
   */
  private void configure(ScriptWriterCfg config) throws ConfigException
  {
    path = config.getPathname();
    filename = config.getFilename();
    suffix = config.getSuffix();
    outfilename = config.getOutFilename();
    outsuffix = config.getOutSuffix();
    preventExecute = config.getPreventExecute().booleanValue();
    okToOverwrite = config.getOkToOverwrite().booleanValue();
    useTempFile = config.getUseTempFile().booleanValue();
    removeAfterExecute = config.getRemoveAfterExecute().booleanValue();
    LogCfg cfg = new LogCfg();
    LoggerFactory logFactory = cfg.getLogerFactory();
    this.logger = logFactory.getLogger();
  }

  /**
   * create the script file
   * @assumes nothing
   * @effects a new script file will be created
   * @throws ScriptException thrown if there is an error creating the
   * script file
   */
  private void createScriptFile() throws ScriptException
  {
    if (useTempFile == true)
    { // create a temp file
      try
      {
        scriptFile = File.createTempFile(filename, suffix);
        scriptFile.deleteOnExit();
      }
      catch (IOException e)
      {
        ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
        ScriptException e2 = (ScriptException)
            eFactory.getException(TempFileCreateErr, e);
        e2.bind(scriptFile.getName());
        throw e2;
      }
    }
    else { // use named file
      scriptFile = new File(path + File.separator +
                            filename + "." + suffix);
      if (!okToOverwrite)
      {
          String basename = filename;
        int count = 0;
        while (scriptFile.exists())
        {
          count = count + 1;
          filename = basename + "_" + String.valueOf(count);
          outfilename = outfilename + "_" + String.valueOf(count);
          scriptFile = new File(path + File.separator +
                                filename + "." + suffix);
        }
      }
    }
    try {
      fileWriter = new FileWriter(scriptFile);
      bufferedWriter = new BufferedWriter(fileWriter);
    }
    catch (IOException e) {
      ScriptExceptionFactory eFactory = new ScriptExceptionFactory();
      ScriptException e2 = (ScriptException)
          eFactory.getException(FileCreateErr, e);
      e2.bind(scriptFile.getName());
      throw e2;
    }
  }

  /**
   * close the script file
   * @assumes nothing
   * @effects the scriptFile will be closed
   * @throws IOException thrown if there is an error closing the script file
   */
  private void close() throws IOException
  {
    if (!closed)
    {
      bufferedWriter.close();
      fileWriter.close();
      closed = true;
    }
  }
}