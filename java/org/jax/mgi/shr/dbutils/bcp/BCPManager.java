// $Header$
// $Name$

package org.jax.mgi.shr.dbutils.bcp;

import java.io.File;
import java.io.IOException;
import java.util.Vector;
import java.util.Iterator;

import org.jax.mgi.shr.unix.RunCommand;
import org.jax.mgi.shr.config.BCPManagerCfg;
import org.jax.mgi.shr.config.BCPWriterCfg;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.config.ConfigException;
import org.jax.mgi.shr.dbutils.DBException;
import org.jax.mgi.shr.dbutils.DBSchema;
import org.jax.mgi.shr.dbutils.DBSchemaException;
import org.jax.mgi.shr.dbutils.SQLDataManager;
import org.jax.mgi.shr.dbutils.Table;


/**
 * @is an object for executing the Sybase bcp command against multiple
 * tables in the database.
 * @has <br>
 * <UL>
 * <LI>a set of configuration parameters and a BCPManagerCfg object
 * for reading these parameters from the configuration files and system
 * properties.
 * <LI>A SQLDatabaseManager for handling DDL database calls.
 * <LI>A BCPWriter class for each table it is to execute against.
 * <LI>A logger for logging informational and debug messages
 * </UL>
 * @does obtains BCPWriters for creating bcp files and execute the bcp
 * command for each file.
 * @company Jackson Laboratory
 * @author M. Walker
 * @version 1.0
 */



public class BCPManager {

  /*
   * the SQLDataManager used for executing DDL commands before and after bcp
   */
  private SQLDataManager sqlmanager = null;
  /*
   * the logger instance
   */
  private Logger logger = null;
  /*
   * the vector of all BCPWriters. Each Writer is associated with one table
   */
  private Vector allWriters = new Vector();
  /*
   * the path where the bcp files are stored
   */
  private String path = null;
  /*
   * the delimiter of the bcp file\
   */
  private String delimiter = null;
  /*
   * option to not run the bcp file on execute
   */
  private boolean preventExecute;
  /*
   * option to overwrite existing bcp files
   */
  private boolean okToOverwrite;
  /*
   * option to use temp file when creating bcp file
   */
  private boolean useTempFile;
  /*
   * option to remove the bcp file after executing
   */
  private boolean removeAfterExecute;
  /*
   * option to auto-fill the user and date columns of a table
   */
  private boolean okToRecordStamp;
  /*
   * option to auto-flush the buffer after each bcp write
   */
  private boolean okToAutoFlush;
  /*
   * option to truncate logs after executing bcp
   */
  private boolean okToTruncateLog;
  /*
   * option to drop indexes before executing bcp
   */
  private boolean okToDropIndexes;
  /*
   * option to truncate table before executing bcp
   */
  private boolean okToTruncateTable;
  /*
   * bcp exception factory used to store and obtain bcp exceptions
   */
  private BCPExceptionFactory exceptionFactory = new BCPExceptionFactory();

  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String InterruptErr =
      BCPExceptionFactory.InterruptErr;
  private static String IOErr =
      BCPExceptionFactory.IOErr;
  private static String NonZeroErr =
      BCPExceptionFactory.NonZeroErr;
  private static String InvalidDelimiter =
      BCPExceptionFactory.InvalidDelimiter;


  /**
   * default constructor which reads the configuration from a
   * default configuration object
   * @throws ConfigException thrown if a configuration error occurs
   * @throws DBException thrown if an error occurs while trying to
   * access the database
   */
  public BCPManager() throws ConfigException, DBException {
    configure(new BCPManagerCfg());
  }
  /**
   * constructer which uses a given configuration object for obtaining
   * configuration parameters normally used for selecting a certain
   * set of configuration variables that have been prefixed
   * @param config the configuration object from which to configure the
   * BCPManager by
   * @throws ConfigException thrown if there is an error obtaining
   * configuration parameters
   * @throws DBException thrown if there is an error accessing the
   * database
   */
  public BCPManager(BCPManagerCfg config)
      throws ConfigException, DBException {
    configure(config);
  }
  /**
   * set the SQLDataManager used for record validation and DDL calls.
   * @assumes nothing
   * @effects the internal reference to the SQLDAtaManager will be set.
   * @param pSqlmanager the SQLDataManager object through which this
   * object should access the database
   */
  public void setSQLDataManager(SQLDataManager pSqlmanager) {
    sqlmanager = pSqlmanager;
  }
  /**
   * set the Logger instance
   * @assumes nothing
   * @effects the internal reference to the Logger will be set.
   * @param pLogger the Logger
   */
  public void setLogger(Logger pLogger) {
    logger = pLogger;
  }
  /**
   * set the name of the path where bcp files should be created, overriding
   * the value found in the configuration file or system properties.
   * @assumes nothing
   * @effects the internal value of the pathname will be set.
   * @param pathParam the path name
   */
  public void setPathname(String pathParam) {
    path = pathParam;
  }
  /**
   * set the delimiter used as a field seperator in bcp files, overriding the
   * value found in the configuration file or system properties.
   * @assumes nothing
   * @effects the interval value of the delimiter will be set.
   * @param delimiterParam bcp field delimiter
   */
  public void setDelimiter(String delimiterParam) {
    delimiter = delimiterParam;
  }
  /**
   * set the option to prevent executing the bcp command when called,
   * overriding the value found in the configuration file or system
   * properties.
   * @assumes nothing
   * @effects the internal value of the preventExecute flag will be set.
   * @param boolParam true or false
   */
  public void setPreventExecute(boolean boolParam) {
    preventExecute = boolParam;
  }
  /**
   * set the option which allows existing files to be overwritten by new
   * bcp files with the same name, overriding the value found in the
   * configuration file or system properties.
   * @assumes nothing
   * @effects the internal value of the okToOverwrite flag will be set.
   * @param boolParam true or false
   */
  public void setOkToOverwrite(boolean boolParam) {
    okToOverwrite = boolParam;
  }
  /**
   * set the option which designates whether to use temporary files when
   * creating bcp files, overriding the value found in the configuration file
   * or system properties. The location of the temporary directory is
   * determined by the java system property java.io.tempdir.
   * @assumes nothing
   * @effects the internal value of the useTempFile flag will be set.
   * @param boolParam true or false
   */
  public void setUseTempFile(boolean boolParam) {
    useTempFile = boolParam;
  }
  /**
   * set the option which designates that the bcp files will be removed
   * from the system after they are executed, overriding the value found
   * in the configuration file or system properties. This option will be
   * ignored if the
   * preventExecute option is set to true.
   * @assumes nothing
   * @effects the internal value of the removeAfterExecute flag will be
   * set.
   * @param boolParam true or false
   */
  public void setRemoveAfterExecute(boolean boolParam) {
    removeAfterExecute = boolParam;
  }
  /**
   * set the option which designates whether to automatically
   * add the record stamping fields, overriding the value found
   * in the configuration file or system properties.
   * @assumes nothing
   * @effects the internal value of the okToRecordStamp flag will be set.
   * @param boolParam true or false
   */
  public void setOkToRecordStamp(boolean boolParam) {
    okToRecordStamp = boolParam;
  }
  /**
   * set the option which designates whether to automatically
   * flush a buffer after each bcp write, overriding the value found
   * in the configuration file or system properties.
   * @assumes nothing
   * @effects the internal value of the okToAutoFlush flag will be set.
   * @param boolParam true or false
   */
  public void setOkToAutoFlush(boolean boolParam) {
    okToAutoFlush = boolParam;
  }
  /**
   * set the option which designates whether to automatically
   * truncate the log after doing a bcp, overriding the value found in the
   * configuration file or system properties.
   * @assumes nothing
   * @effects the internal value of the okToTruncateLog flag will be set.
   * @param boolParam true or false
   */
  public void setOkToTruncateLog(boolean boolParam) {
    okToTruncateLog = boolParam;
  }
  /**
   * get the SQLDataManager instance set for this object.
   * @assumes nothing
   * @effects nothing
   * @return the SQLDataManager.
   */
  public SQLDataManager getSQLDataManager()
      throws DBException, ConfigException {
    // if the SQLDataManager instance is null, then create a default one
    SQLDataManager sqlMgrRef = sqlmanager;
    if (sqlMgrRef == null)
    {
      sqlMgrRef = new SQLDataManager();
    }
    return sqlMgrRef;
  }
  /**
   * get the field delimiter for the BCP file.
   * @assumes nothing
   * @effects nothing
   * @return the field delimiter.
   */
  public String getDelimiter() {
    return delimiter;
  }
  /**
   * get the path name where bcp files are created.
   * @assumes nothing
   * @effects nothing
   * @return path name.
   */
  public String getPathname() {
    return path;
  }
  /**
   * get the value of the attribute which designates whether to use temporary
   * files when creating bcp files.
   * @assumes nothing
   * @effects nothing
   * @return true or false
   */
  public boolean getUseTempFile() {
    return useTempFile;
  }
  /**
   * get the value of the attribute which designates whether it is ok to
   * overwrite an existing bcp file.
   * @assumes nothing
   * @effects nothing
   * @return true or false
   */
  public boolean getOkToOverwrite() {
    return okToOverwrite;
  }
  /**
   * get the value of the option which designates whether to prevent
   * executing the bcp command when called.
   * @assumes nothing
   * @effects nothing
   * @return true or false
   */
  public boolean getPreventExecute() {
    return preventExecute;
  }
  /**
   * get the value of the option which designates whether to remove the bcp
   * files after executing.
   * @assumes nothing
   * @effects nothing
   * @return true or false
   */
  public boolean getRemoveAfterExecute() {
    return removeAfterExecute;
  }
  /**
   * get the value of the option which designates whether to automatically
   * truncate the log after running bcp.
   * @assumes nothing
   * @effects nothing
   * @return true or false
   */
  public boolean getOkToTruncateLog() {
    return okToTruncateLog;
  }
  /**
   * get the value of the option which designates whether to automatically
   * add the fields createdBy, modifiedBy and the the fields
   * modification_date and creation_date when doing a bcp write.
   * @assumes nothing
   * @effects nothing
   * @return true or false
   */
  public boolean getOkToRecordStamp() {
    return okToRecordStamp;
  }
  /**
   * get the value of the option which designates whether to automatically
   * flush a buffer after each bcp write.
   * @assumes nothing
   * @effects nothing
   * @return true or false
   */
  public boolean getOkToAutoFlush() {
    return okToAutoFlush;
  }
  /**
   * get a BCPWriter object for a given table
   * @assumes nothing
   * @effects a bcp file will be opened for writing
   * @param pTable the table object
   * @return the BCPWriter for the given table
   * @throws BCPException thrown if the bcp file cannot be created
   * @throws DBException thrown if database metadata cannot be obtained for
   * the table
   * @throws ConfigException thrown if there is an error during
   * configuration
   */
  public BCPWriter getBCPWriter(Table pTable)
      throws BCPException, DBException, ConfigException {
    BCPWriterCfg bcpCfg = new BCPWriterCfg();
    bcpCfg.setDefaultOkToRecordStamp(okToRecordStamp);
    bcpCfg.setDefaultOkToAutoFlush(okToAutoFlush);
    return getBCPWriter(pTable, bcpCfg);
  }
  /**
   * get a BCPWriter object through which a client class can create
   * bcp files.
   * @assumes nothing
   * @effects a bcp file will be opened for writing
   * @param pTable the table object to bcp against.
   * @param pCfg the configuration object through which to configure the
   * BCPWriter
   * @return a BCPWriter for the given table
   * @throws BCPException thrown if the bcp file cannot be created
   * @throws DBException if database meta data cannot be obtained for
   * the table
   * @throws ConfigException thrown if there is an error when trying to
   * configure the BCPWriter object
   *
   */
  public BCPWriter getBCPWriter(Table pTable, BCPWriterCfg pCfg)
      throws BCPException, DBException, ConfigException {
    pCfg.setDefaultOkToRecordStamp(okToRecordStamp);
    pCfg.setDefaultOkToAutoFlush(okToAutoFlush);
    pCfg.setDefaultOkToDropIndexes(this.okToDropIndexes);
    pCfg.setDefaultOkToTruncateTable(this.okToTruncateTable);
    BCPWriter writer = new BCPWriter(pTable, this, logger, pCfg);
    allWriters.add(writer);
    return writer;
  }
  /**
   * get a BCPWriter object for a given table
   * @assumes nothing
   * @effects a bcp file will be opened for writing
   * @param pTable the table name
   * @return the BCPWriter for the given table
   * @throws BCPException thrown if the bcp file cannot be created
   * @throws DBException thrown if database metadata cannot be obtained for
   * the table
   * @throws ConfigException thrown if there is an error during
   * configuration
   */
  public BCPWriter getBCPWriter(String pTable)
      throws BCPException, DBException, ConfigException {
    BCPWriterCfg bcpCfg = new BCPWriterCfg();
    bcpCfg.setDefaultOkToRecordStamp(okToRecordStamp);
    bcpCfg.setDefaultOkToAutoFlush(okToAutoFlush);
    return getBCPWriter(pTable, bcpCfg);
  }
  /**
   * get a BCPWriter object through which a client class can create
   * bcp files.
   * @assumes nothing
   * @effects a bcp file will be opened for writing
   * @param pTable the table name to bcp against.
   * @param pCfg the configuration object through which to configure the
   * BCPWriter
   * @return a BCPWriter for the given table
   * @throws BCPException thrown if the bcp file cannot be created
   * @throws DBException if database meta data cannot be obtained for
   * the table
   * @throws ConfigException thrown if there is an error when trying to
   * configure the BCPWriter object
   *
   */
  public BCPWriter getBCPWriter(String pTable, BCPWriterCfg pCfg)
      throws BCPException, DBException, ConfigException {
    pCfg.setDefaultOkToRecordStamp(this.okToRecordStamp);
    pCfg.setDefaultOkToAutoFlush(this.okToAutoFlush);
    pCfg.setDefaultOkToDropIndexes(this.okToDropIndexes);
    pCfg.setDefaultOkToTruncateTable(this.okToTruncateTable);
    BCPWriter writer = new BCPWriter(pTable, this, logger, pCfg);
    allWriters.add(writer);
    return writer;
  }
  /**
   * execute all the existing bcp files
   * @throws BCPException thrown if there is some error handling the bcp
   * file or executing the bcp command
   * @throws DBException thrown if there is an error with the database
   * @throws DBSchemaException thrown if there is an error obtainning DDL
   * commands, for example commands for dropping and creating indexes
   */
  public void executeBCP()
      throws BCPException, DBException, DBSchemaException {
    BCPWriter writer = null;
    for (Iterator it = allWriters.iterator(); it.hasNext(); ) {
      writer = (BCPWriter)it.next();
      writer.close();
    }
    for (Iterator it = allWriters.iterator(); it.hasNext(); ) {
      writer = (BCPWriter) it.next();
      executeBCPFile(writer);
      // invalidate the writer
      writer.setIsValid(false);
    }
    // clear all writers
    allWriters = new Vector();
  }
  /**
   * sets all the attributes from the configuration object
   * @assumes nothing
   * @effects all internal values will be set
   * @param pConfig the configuration object
   * @throws ConfigException throws if there is a configuration error
   */
  private void configure(BCPManagerCfg pConfig) throws ConfigException {
    this.path = pConfig.getPathname();
    this.delimiter = pConfig.getDelimiter();
    this.preventExecute = pConfig.getPreventExecute().booleanValue();
    this.okToOverwrite = pConfig.getOkToOverwrite().booleanValue();
    this.useTempFile = pConfig.getUseTempFile().booleanValue();
    this.removeAfterExecute = pConfig.getRemoveAfterExecute().booleanValue();
    this.okToRecordStamp = pConfig.getOkToRecordStamp().booleanValue();
    this.okToAutoFlush = pConfig.getOkToAutoFlush().booleanValue();
    this.okToTruncateLog = pConfig.getOkToTruncateLog().booleanValue();
    this.okToDropIndexes = pConfig.getOkToDropIndexes().booleanValue();
    this.okToTruncateTable = pConfig.getOkToTruncateTable().booleanValue();
  }
  /**
   * executes the associated bcp file for the given BCPWriter.
   * @assumes nothing
   * @effects data will be loaded into the database
   * @param bcpWriter the BCPWiter object
   * @throws BCPException throws if BCPWriters cannot be closed or if bcp
   * command fails or if an sql exception is thrown while running pre sql or
   * post sql.
   * @throws DBException thrown if there is a failure when trying to run
   * sql commands
   * @throws DBSchemaException thrown if there is an error running DDL
   * commands using the DBSchema product
   */
  private void executeBCPFile(BCPWriter bcpWriter)
       throws BCPException, DBException, DBSchemaException {
    SQLDataManager sqlmanager = bcpWriter.getTable().getSQLDataManager();
    String server = sqlmanager.getServer();
    String db = sqlmanager.getDatabase();
    String user = sqlmanager.getUser();
    String pwFile = sqlmanager.getPasswordFile();
    DBSchema dbSchema = new DBSchema(sqlmanager);
    String file = bcpWriter.getFilename();
    String table = bcpWriter.getTablename();
    String cmd = "cat " + pwFile + " | bcp " + db + ".." + table +
                 " in " + file + " -c -S " + server + " -U " + user +
                 " -t " + convertDelimiter(delimiter);
    int exitCode = 0;
    // check attributes to see if the bcp command should not be performed
    if (preventExecute)
      return;
    // execute any sql pre bcp as designated in the bcpWriter
    if (logger != null)
      logger.logInfo(table + ": Execute any configured pre-SQL statements");
    executeSql(bcpWriter.getPreSql(),
               bcpWriter.getTable().getSQLDataManager());
    if (bcpWriter.getOkToTruncateTable()) {
      if (logger != null)
        logger.logInfo(table + ": Truncate table");
      dbSchema.truncateTable(table);
    }
    // Only drop indexes, run bcp and re-create indexes if the writer has
    // records to load.
    if (bcpWriter.hasRecords()) {
      // drop indexes on table if configured to do so
      if (bcpWriter.getOkToDropIndexes()) {
        if (logger != null)
          logger.logInfo(table + ": Drop indexes on table");
        dbSchema.dropIndexes(table);
      }
      RunCommand runner = new RunCommand();
      try {
        if (logger != null)
          logger.logInfo(table + ": Execute the bcp command: " + cmd);
        runner.setCommand(cmd);
        exitCode = runner.run();
      }
      // if execeptions are thrown from the RunCommand class,
      // try and recreate the indexes if they were dropped and
      // throw a new BCPException
      catch (InterruptedException e) {
        // need to recreate indexes if they were dropped
        if (bcpWriter.getOkToDropIndexes()) {
          if (logger != null)
            logger.logInfo(table + ": Create indexes on table");
          dbSchema.createIndexes(table);
        }
        BCPException e2 = (BCPException)
            exceptionFactory.getException(InterruptErr, e);
        e2.bind(cmd);
        throw e2;
      }
      catch (IOException e) {
        BCPException e2 = (BCPException)
            exceptionFactory.getException(IOErr, e);
        e2.bind(cmd);
        // need to recreate indexes if they were dropped
        if (bcpWriter.getOkToDropIndexes()) {
          if (logger != null)
            logger.logInfo(table + ": Create indexes on table");
          dbSchema.createIndexes(table);
        }
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
      // exit code of non-zero indicates an error occurred while running bcp.
      // recreate indexes if they were dropped and throw a BCPException
      if (exitCode != 0) {
       BCPException e2 = (BCPException)
            exceptionFactory.getException(NonZeroErr);
        e2.bind(cmd);
        // need to recreate indexes if they were dropped
        if (bcpWriter.getOkToDropIndexes()) {
          if (logger != null)
            logger.logInfo(table + ": Create indexes on table");
          dbSchema.createIndexes(table);
        }
        throw e2;
      }
      // All is OK...The bcp command ran with zero exit code.
      //
      // recreate indexes if they were dropped in advance of running bcp
      if (bcpWriter.getOkToDropIndexes()) {
        if (logger != null)
          logger.logInfo(table + ": Create indexes on table");
        dbSchema.createIndexes(table);
      }
    }
    // execute any sql post bcp as designated in the bcpWriter
    if (logger != null)
      logger.logInfo(table + ": Execute any configured post-SQL statements");
    executeSql(bcpWriter.getPostSql(),
               bcpWriter.getTable().getSQLDataManager());
    // truncate the log if configured to do so
    if (okToTruncateLog) {
      if (logger != null) logger.logInfo("Truncate transaction log");
      dbSchema.truncateLog();
    }
    // see if bcp file should be removed
    if (removeAfterExecute) {
      File f = new File(file);
      f.delete();
    }
  }
  /**
   * executes a vector of sql strings
   * @assumes nothing
   * @effects an sql statment will executed in the database
   * @param v the vector of sql strings
   * @throws DBException thrown if there is an sql exception
   */
  private void executeSql(Vector v, SQLDataManager sqlmanager)
      throws DBException {
    if (v != null) {
      for (Iterator it = v.iterator(); it.hasNext(); ) {
        String sql = (String)it.next();
        sqlmanager.executeUpdate(sql);
      }
    }
  }
  /**
   * converts a string representing the delimiter to a string suitable
   * for writing to the command line
   * @assumes mothing
   * @effects nothing
   * @param s the string to convert
   * @return the converted value
   * @throws BCPException
   */
  private String convertDelimiter(String s) throws BCPException {
    if (s == null || s.equals("")) {
      BCPExceptionFactory eFactory = new BCPExceptionFactory();
      BCPException e = (BCPException)
          eFactory.getException(InvalidDelimiter);
      e.bind("null");
      throw e;
    }
    if (s.toUpperCase().equals("SPACE") || s.equals(" "))
      return new String("' '");
    else if (s.toUpperCase().equals("TAB") || s.equals("\t"))
      return new String("'\t'");
    else {
      return new String("'" + s + "'");
    }
  }
}
// $Log$
// Revision 1.2  2003/12/09 22:49:18  mbw
// merged jsam branch onto the trunk
//
// Revision 1.1.2.10  2003/11/26 11:22:05  dbm
// Write the bcp command to the diagnostic log
//
// Revision 1.1.2.9  2003/11/19 14:43:51  dbm
// Only skip indexes and bcp if there are no records to load
//
// Revision 1.1.2.8  2003/10/23 17:43:14  dbm
// Set defaults for drop index and truncate table in getWriter(Table,BCPWriterCfg)
//
// Revision 1.1.2.7  2003/10/23 13:38:25  mbw
// added new parameters settings for okTODropIndexes and okToTruncateTable
//
// Revision 1.1.2.6  2003/10/14 13:40:40  dbm
// Close all bcp files before attempting to execute them.
//
// Revision 1.1.2.5  2003/10/02 19:04:13  mbw
// added new calls to the setDefault methods on the incoming BCPWriterCfg in the getBCPWriter method
//
// Revision 1.1.2.4  2003/10/02 16:25:19  mbw
// *** empty log message ***
//
// Revision 1.1.2.3  2003/10/02 16:23:35  mbw
// bug fix: now calling setDefault methods on BCPWriterCfg in getWriter() method
//
// Revision 1.1.2.2  2003/10/01 18:33:38  dbm
// Add auto flush capability the bcp writers
//
// Revision 1.1.2.1  2003/09/24 14:24:47  mbw
// moved to new package
//
// Revision 1.5.2.33  2003/09/18 19:22:50  mbw
// added a SQLDataManager reference as a new instance variable
// and now have dual methods for getBCPWriter(): one which takes a String and another which takes a Table object
//
// Revision 1.5.2.32  2003/09/10 21:23:52  mbw
// now using a Table class directly instead of the name of the table and it no longer needs a SQLDataManager since it can get one from the Table class
//
// Revision 1.5.2.31  2003/09/08 15:23:37  mbw
// initial version
//
// Revision 1.5.2.30  2003/06/04 18:29:51  mbw
// javadoc edits
//
// Revision 1.5.2.29  2003/06/04 14:51:47  mbw
// javadoc edits
//
// Revision 1.5.2.28  2003/05/22 15:56:16  mbw
// javadocs edits
//
// Revision 1.5.2.27  2003/05/16 15:11:19  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.5.2.26  2003/05/15 00:50:07  mbw
// impacted from changes to unitTest package
//
// Revision 1.5.2.25  2003/05/14 15:10:12  dbm
// Include the table name on bcp log messages
//
// Revision 1.5.2.24  2003/05/13 18:21:15  mbw
// modified logging messages
//
// Revision 1.5.2.23  2003/05/08 19:40:53  mbw
// changed to suit addition of new class BCPWriterCfg
//
// Revision 1.5.2.22  2003/05/08 01:56:09  mbw
// incorporated changes from code review
//
// Revision 1.5.2.21  2003/04/29 21:22:47  mbw
// changed to reflect changes made to the BCPManagerCfg class
//
// Revision 1.5.2.20  2003/04/28 21:18:32  mbw
// creating an instance member for the BCPExceptionFactory
//
// Revision 1.5.2.19  2003/04/25 17:11:57  mbw
// updated to reflect changes in design of exception handling
//
// Revision 1.5.2.18  2003/04/15 17:39:37  dbm
// Log any pre/post bcp SQL statements
//
// Revision 1.5.2.17  2003/04/15 17:07:40  dbm
// Check for null vector of SQL commands before trying to execute
//
// Revision 1.5.2.16  2003/04/15 12:02:29  mbw
// javadoc edits
//
// Revision 1.5.2.15  2003/04/11 15:27:13  mbw
// added code to execute pre sql and post sql when running bcp
//
// Revision 1.5.2.14  2003/04/10 17:22:50  mbw
// getBCPWriter now takes string as a parameter vs Table
//
// Revision 1.5.2.13  2003/04/09 21:20:36  mbw
// added setter methods (no longer uses configuration objects for storing attribute values); moved executeBCP code from BCPWriter to this class;
//
// Revision 1.5.2.12  2003/04/04 00:13:31  mbw
// moved bcp implementation to BCPWriter (see rev revision 1.5.2.11)
//
// Revision 1.5.2.11  2003/04/04 00:09:03  mbw
// removed extraneous EOF character
//
// Revision 1.5.2.10  2003/03/21 16:52:48  mbw
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
