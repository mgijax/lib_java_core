// $Header$
// $Name$

package org.jax.mgi.shr.dbutils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Vector;
import java.util.Iterator;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;

/**
 * @is An object that runs DDL commands from the dbschema
 * product which include create table, create index, drop index, etc.
 * @has A SQLDataManager object for running sql and obtaining the
 * dbschema installation directory.
 * @does Opens files from the dbshema product and locates the sql
 * commands for creating tables, creating and dropping indexes, etc and
 * executes them through JDBC.
 *
 * @company Jackson Laboratory
 * @author M Walker
 * @version 1.0
 */

public class DBSchema {

  private SQLDataManager sqlmanager = null;
  /**
   * regular expression for locating drop index commands in the dbschema
   * files
   */
  private static final String REGEX_DROP_INDEX =
      "^[dD][rR][oO][pP] .*";
  /**
   * regular expression for locating create index commands in the dbschema
   * files
   */
  private static final String REGEX_CREATE_INDEX =
      "^[cC][rR][eE][aA][tT][eE] .*";
  /**
   * regular expression for locating create table commands in the dbschema
   * files
   */
  private static final String REGEX_CREATE_TABLE =
      "^[cC][rR][eE][aA][tT][eE] .*";
  /**
   * regular expression for locating isql go commands in the dbschema files
   */
  private static final String REGEX_GO_COMMAND = "^[gG][oO]$";

  /**
   * compiled regex pattern for locating drop index commands in the dbschema
   * files
   */
  private static final Pattern dropIndexPattern =
      Pattern.compile(REGEX_DROP_INDEX);
  /**
   * compiled regex pattern for locating create index commands in the
   * dbschema files
   */
  private static final Pattern createIndexPattern =
      Pattern.compile(REGEX_CREATE_INDEX);
  /**
   * compiled regex pattern for locating create table commands in the
   * dbschema files
   */
  private static final Pattern createTablePattern =
      Pattern.compile(REGEX_CREATE_TABLE);
  /**
   * compiled regex pattern for locating isql go commands in the dbschema
   * files used when locating the end of a create table command.
   */
  private static final Pattern goCommandPattern =
      Pattern.compile(REGEX_GO_COMMAND);
  /*
   * the exception factory for DBScemaExceptions
   */
  private DBSchemaExceptionFactory exceptionFactory =
      new DBSchemaExceptionFactory();

  /*
   * the following constant definitions are exceptions thrown by this class
   */
  private static String FileNotFoundErr =
      DBSchemaExceptionFactory.FileNotFoundErr;
  private static String FileReadErr =
      DBSchemaExceptionFactory.FileReadErr;
  private static String FileCloseErr =
      DBSchemaExceptionFactory.FileCloseErr;
  private static String NoRegexMatch =
      DBSchemaExceptionFactory.NoRegexMatch;
  private static String UnexpectedString =
      DBSchemaExceptionFactory.UnexpectedString;

  /**
   * constructor which sets the database configuartion
   * @param pSqlmanager the database manager object
   */
  public DBSchema(SQLDataManager pSqlmanager) {
    sqlmanager = pSqlmanager;
  }

  /**
   * locates the drop index commands from the dbschema product for the given
   * table and executes them in the database.
   * @assumes nothing
   * @effects indexes will be dropped on the given table
   * @param pTablename table name
   * @throws DBSchemaException
   * @throws DBException
   */
  public void dropIndexes(String pTablename)
      throws DBSchemaException, DBException  {
    Vector sql = getDropIndexCommands(pTablename);
    executeSql(sql);
  }

  /**
   * locates the create index commands from the dbschema product for the
   * given table and executes them in the database.
   * @assumes nothing
   * @effects indexes will be created for the given table
   * @param pTablename table name
   * @throws DBSchemaException
   * @throws DBException
   */
  public void createIndexes(String pTablename)
      throws DBSchemaException, DBException  {
    Vector sql = getCreateIndexCommands(pTablename);
    executeSql(sql);
  }

  /**
   * locates the create index command from the dbschema product for the
   * given table and executes it in the database.
   * @assumes nothing
   * @effects a new table will be created in the database
   * @param pTablename table name
   * @throws DBSchemaException
   * @throws DBException
   */
  public void createTable(String pTablename)
      throws DBSchemaException, DBException {
    String command = getCreateTableCommand(pTablename);
    sqlmanager.executeUpdate(command);
  }

  /**
   * executes the drop table command for the given table name in the
   * database.
   * @assumes nothing
   * @effects a table will be dropped from the database
   * @param pTablename table name
   * @throws DBException
   */
  public void dropTable(String pTablename) throws DBException {
    String command = "drop table " + pTablename;
    Vector v = new Vector();
    v.add(command);
    executeSql(v);
  }

  /**
   * truncates the transaction log for the configured database.
   * @assumes nothing
   * @effects a table will be truncated
   * @param pTablename table name
   * @throws DBException
   */
  public void truncateTable(String pTablename) throws DBException {
    String command = "truncate table " + pTablename;
    Vector v = new Vector();
    v.add(command);
    executeSql(v);
  }

  /**
   * truncates the transaction log for the configured database.
   * @assumes nothing
   * @effects the database log will be truncated
   * @throws DBException thrown if an error occurs with the database
   */
  public void truncateLog() throws DBException {
    String dbname = sqlmanager.getDatabase();
    String command = "dump transaction " + dbname + " with truncate_only";
    Vector v = new Vector();
    v.add(command);
    executeSql(v);
  }

  /**
   * get the sql commands for creating indexes for the given table
   * @assumes nothing
   * @effects nothing
   * @param pTablename the given table name
   * @return a vector of sql commands from the dbschema file
   * @throws DBSchemaException thrown if the create index commands could
   * not be obtained fron the dbschema product
   */
  public Vector getCreateIndexCommands(String pTablename)
      throws DBSchemaException {
    String filename = calculateFilename("index", "create", pTablename);
    Vector v1 = getCommands(filename, createIndexPattern);
    // do text substition for segment names
    Vector v2 = new Vector();
    String s = null;
    for (int i = 0; i < v1.size(); i++) {
      s = ((String)v1.get(i)).replaceAll("\\$\\{DBCLUSTIDXSEG\\}", "seg0");
      s = (s.replaceAll("\\$\\{DBNONCLUSTIDXSEG\\}", "seg1"));
      v2.add(s);
    }
    return v2;
  }

  /**
   * get the sql commands for dropping indexes for the given table
   * @assumes nothing
   * @effects nothing
   * @param pTablename the given table name
   * @return a vector of sql commands from the dbschema file
   * @throws DBSchemaException thrown if the drop index commands could
   * not be obtained fron the dbschema product
   */
  public Vector getDropIndexCommands(String pTablename)
      throws DBSchemaException {
    String filename = calculateFilename("index", "drop", pTablename);
    return getCommands(filename, dropIndexPattern);
  }

  /**
   * search the given file and extract the create table command form it
   * @assumes nothing
   * @effects nothing
   * @param pTablename the name of the table
   * @return the create table command
   * @throws DBSchemaException thrown if the create table command could
   * not be obtained fron the dbschema product.
   * @throws DBException thrown if there is an error with the database
   */
  public String getCreateTableCommand(String pTablename)
      throws DBSchemaException {
    String filename = calculateFilename("table", "create", pTablename);
    String line = null;
    StringBuffer command = new StringBuffer();
    Pattern commandPattern = createTablePattern;
    Pattern goPattern = goCommandPattern;
    Matcher commandMatcher = null;
    Matcher goMatcher = null;
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader(filename));
    }
    catch (FileNotFoundException e) {
      DBSchemaException e2 = (DBSchemaException)
          exceptionFactory.getException(FileNotFoundErr, e);
      e2.bind(filename);
      throw e2;
    }
    try {
      while ( (line = in.readLine()) != null) {
        line = line.trim();
        // search for the create table command
        commandMatcher = commandPattern.matcher(line);
        if (commandMatcher.matches()) {
          command.append(line);
          boolean foundGoCommand = false;
          // found create table command, now look for go command
          while ( (line = in.readLine()) != null) {
            line = line.trim();
            goMatcher = goPattern.matcher(line);
            if (!goMatcher.matches()) {
              // disregarding the use of table segments
              if (line.indexOf("DBTABLESEGMENT") != -1)
                continue;
              command.append(" " + line);
            }
            else {
              foundGoCommand = true;
              break;
            }
          }
          if (!foundGoCommand) {
            DBSchemaException e2 = (DBSchemaException)
                exceptionFactory.getException(UnexpectedString);
            e2.bind(new String(command));
            e2.bind(filename);
            throw e2;
          }
          break;
        }
      }
    }
    catch (IOException e) {
      DBSchemaException e2 = (DBSchemaException)
          exceptionFactory.getException(FileReadErr, e);
      e2.bind(filename);
      throw e2;
    }
    if (command.length() == 0) {
      DBSchemaException e2 = (DBSchemaException)
          exceptionFactory.getException(NoRegexMatch);
      e2.bind(REGEX_CREATE_TABLE);
      e2.bind(filename);
      throw e2;
    }
    try {
      in.close();
    }
    catch (IOException e) {
      DBSchemaException e2 = (DBSchemaException)
          exceptionFactory.getException(FileCloseErr, e);
      e2.bind(filename);
      throw e2;
    }
    return new String(command);
  }

  /**
   * searches a file and extracts lines which match the given regular
   * expression
   * @assumes nothing
   * @effects nothing
   * @param pFilename the file to search
   * @param pRegex the regular expression to match on
   * @return the vector of lines which matched the regular expression
   * @throws DBSchemaException thrown if ddl commands could
   * not be obtained fron the dbschema product
   */
  private Vector getCommands(String pFilename, Pattern pRegex)
      throws DBSchemaException {
    String filename = pFilename;
    Vector sql = new Vector();
    String line = null;
    Pattern commandPattern = pRegex;
    Matcher commandMatcher = null;
    BufferedReader in = null;
    try {
      in = new BufferedReader(new FileReader(filename));
    }
    catch (FileNotFoundException e) {
      DBSchemaException e2 = (DBSchemaException)
          exceptionFactory.getException(FileNotFoundErr, e);
      e2.bind(filename);
      throw e2;
    }
    try {
      while ( (line = in.readLine()) != null) {
        line = line.trim();
        commandMatcher = commandPattern.matcher(line);
        if (commandMatcher.matches())
          sql.add(line);
      }
    }
    catch (IOException e) {
      DBSchemaException e2 = (DBSchemaException)
          exceptionFactory.getException(FileReadErr, e);
      e2.bind(filename);
      throw e2;
    }
    try {
      in.close();
    }
    catch (IOException e) {
      DBSchemaException e2 = (DBSchemaException)
          exceptionFactory.getException(FileCloseErr, e);
      e2.bind(filename);
      throw e2;
    }
    return sql;
  }

  /**
   * calculate the file name for a file in the dbschema product given the
   * command noun, like table, index, etc, and the command verb, like create
   * or drop, and the table name. The configuration parameter
   * DBSCHEMA_INSTALLDIR is used as the directory name in which the
   * dbschema product is installed.
   * @assumes nothing
   * @effects nothing
   * @param pCommandNoun the sql command noun as in table or index
   * @param pCommandVerb the sql command verb as in create or delete
   * @param tablename the table
   * @return the name of the file from the dbschem product represented by
   * the given parameters
   */
  private String calculateFilename(String pCommandNoun,
                                     String pCommandVerb,
                                     String tablename) {
    String root = sqlmanager.getDBSchemaDir();
    String system = System.getProperties().getProperty("os.name");
	String delimiter = null;
    if (system.equals("Windows 2000"))
		delimiter = "\\";	else
		delimiter = "/";
    String filename = sqlmanager.getDBSchemaDir() + delimiter + pCommandNoun + delimiter +
        tablename + "_" + pCommandVerb + ".object";
    return filename;
  }

  /**
   * exeutes the sql commands found within the given vector
   * @assumes nothing
   * @effects an sql statement will be executed within the database
   * @param pCommands a vector of sql commands
   * @throws DBException
   */
  protected void executeSql(Vector pCommands) throws DBException {
    for (Iterator it = pCommands.iterator(); it.hasNext(); ) {
      String command = (String)it.next();
      int rtn = sqlmanager.executeUpdate(command);
    }
  }

}

// $Log$
// Revision 1.1  2003/12/30 16:50:24  mbw
// imported into this product
//
// Revision 1.2  2003/12/09 22:48:55  mbw
// merged jsam branch onto the trunk
//
// Revision 1.1.4.9  2003/10/23 19:35:26  mbw
// bug fixed previous committed changes
//
// Revision 1.1.4.8  2003/10/23 18:51:36  mbw
// added use of segments for the create table commands
//
// Revision 1.1.4.7  2003/09/22 20:18:35  mbw
// made file seperator symbol more portable
//
// Revision 1.1.4.6  2003/08/26 15:24:57  mbw
// fixed bug so that proper DBSchemaException is thrown not DBException and added final to static constant definitions
//
// Revision 1.1.4.5  2003/06/17 20:26:48  mbw
// removed the use of this because of static context
//
// Revision 1.1.4.4  2003/06/04 18:29:55  mbw
// javadoc edits
//
// Revision 1.1.4.3  2003/06/03 19:50:50  mbw
// removed the use of table segments from the create table method
//
// Revision 1.1.4.2  2003/05/22 15:56:20  mbw
// javadocs edits
//
// Revision 1.1.4.1  2003/05/20 18:29:10  mbw
// merged from branch lib_java_shrdbutils-1-0-7-jsam
//
// Revision 1.1.2.11  2003/05/16 15:11:22  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.1.2.10  2003/05/13 18:22:24  mbw
// fixed bug in truncateLog which preventing execution of command
//
// Revision 1.1.2.9  2003/05/08 01:56:12  mbw
// incorporated changes from code review
//
// Revision 1.1.2.8  2003/04/28 21:13:48  mbw
// switched use from DBExceptionFactory to DBSchemaExceptionFactory
//
// Revision 1.1.2.7  2003/04/25 17:12:00  mbw
// updated to reflect changes in design of exception handling
//
// Revision 1.1.2.6  2003/04/15 12:07:09  mbw
// javadoc edits
//
// Revision 1.1.2.5  2003/04/09 21:25:18  mbw
// replaced attribute DatabaseCfg with SQLDataManager
//
// Revision 1.1.2.4  2003/03/21 16:52:55  mbw
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
