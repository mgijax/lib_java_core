// $Header$
// $Name$

package org.jax.mgi.shr.dbutils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.DatabaseMetaData;
import java.util.Vector;
import java.util.Iterator;
import java.io.FileReader;
import java.io.BufferedReader;

import org.jax.mgi.shr.config.DatabaseCfg;
import org.jax.mgi.shr.config.DatabaseConfigurator;
import org.jax.mgi.shr.config.ScriptWriterCfg;
import org.jax.mgi.shr.log.Logger;
import org.jax.mgi.shr.log.LoggerFactory;
import org.jax.mgi.shr.config.ConfigException;


/**
 * @is An object that manages a database connection along with the
 * processing of SQL on the connection.
 * @has A database connection and a Logger.
 * @does Manages a single database connection and provides methods for
 * running SQL statements.
 * @company Jackson Laboratory
 * @author M. Walker
 * @version 1.0
 */

public class SQLDataManager {
  /**
   * a collection of Statements obtained during sql processing which is
   * saved for closing with a call to the closeResources() method
   */
  private Vector statements = new Vector();
  /**
   * a database connection
   */
  private Connection conn = null;
  /**
   * a class that implements the Logger interface
   */
  private Logger logger = null;
  /**
   * a database server name
   */
  private String server = null;
  /**
   * the name of the database
   */
  private String database = null;
  /**
   * the database url
   */
  private String url = null;
  /**
   * the user name of the login account
   */
  private String user = null;
  /**
   * the password for the login account
   */
  private String password = null;
  /**
   * the name of a file which contains the password for the login account
   */
  private String passwordFile = null;
  /**
   * the directory where the dbSchema product is installed
   */
  private String dbSchemaDir = null;
  /**
   * the flag which indicates whether auto-commit is set
   */
  private boolean autoCommit = true;

  /**
   * the setting which controls the scollable nature of the returned
   * query results. One of two values is assumed. They are
   * ResultSet.TYPE_FORWARD_ONLY and ResultSet.TYPE_SCROLL_INSENSITIVE
   */
  private int scrollable = ResultSet.TYPE_FORWARD_ONLY;
  /**
   * the name of the ConnectionManager class used for instantiating a
   * ConnectionManager
   */
  private String connectionManagerClass =
      "org.jax.mgi.shr.dbutils.MGIDriverManager";
  /**
   * the ConnectionManager reference used for obtaining a database
   * connection
   */
  private ConnectionManager connectionManager = null;
  /**
   * the name of the LoggerFactory class used for instantiating a
   * LoggerFactory
   */
  private String loggerFactoryClass = null;


  // the following constant definitions are exceptions thrown by this class
  private static final String JDBCException =
      DBExceptionFactory.JDBCException;
  private static final String PastEndOfResultSet =
      DBExceptionFactory.PastEndOfResultSet;
  private static final String CloseErr =
      DBExceptionFactory.CloseErr;
  private static final String PasswordNotRetrieved =
      DBExceptionFactory.PasswordNotRetrieved;
  private static final String ConnectErr =
      DBExceptionFactory.ConnectErr;
  private static final String ConnectionClosed =
      DBExceptionFactory.ConnectionClosed;
  private static final String StoredProcedureErr =
      DBExceptionFactory.StoredProcedureErr;
  private static final String ClassFornameErr =
        DBExceptionFactory.ClassFornameErr;

  /**
   * constructer which uses a DatabaseCfg object for obtaining
   * configuration parameters with which to configure the connection
   * @throws ConfigException thrown if there is an error configuring the
   * connection
   * @throws DBException thrown if there is and error making a connection
   */
  public SQLDataManager()
      throws ConfigException, DBException {
    configureExtended(new DatabaseCfg());
    connect();
  }

  /**
   * constructer which uses given DatabaseCfg object for obtaining
   * configuration parameters with which to configure the connection.
   * @param config the database configuration object
   * @throws ConfigException thrown if there is an error configuring the
   * connection
   * @throws DBException thrown if there is and error making a connection
   */
  public SQLDataManager(DatabaseCfg config)
      throws ConfigException, DBException {
    configureExtended(config);
    connect();
  }



  /**
   * constructer which uses given DatabaseConfigurator object for obtaining
   * configuration parameters with which to configure the connection.
   * @param config the database configuration object
   * @throws ConfigException thrown if there is an error configuring the
   * connection
   * @throws DBException thrown if there is and error making a connection
   */
  public SQLDataManager(DatabaseConfigurator config)
      throws ConfigException, DBException {
    configureBase(config);
    connect();
  }


  /**
   * constructer which takes all the parameters necessary for getting a
   * connection. Any of these parameters can be null, in which case, they
   * will use default values.
   * @param pDatabase database name
   * @param pUser user name.
   * @param pPasswordFile password file name
   * @param pUrl database url
   * @throws ConfigException thrown if there is an error obtainning
   * configuration info
   * @throws DBException if there is an error getting a connection
   */
  public SQLDataManager(String pServer, String pDatabase, String pUser,
                        String pPasswordFile, String pUrl)
      throws ConfigException, DBException {
    configureExtended(new DatabaseCfg());
    if (pServer != null) server = pServer;
    if (pDatabase != null) database = pDatabase;
    if (pUrl != null) url = pUrl;
    if (pUser != null) user = pUser;
    if (pPasswordFile != null) passwordFile = pPasswordFile;
    connect();
  }


  /**
   * set the name of the directory where the dbschema product is installed.
   * @assumes nothing
   * @effects the internal dbSchemaDir value will be set
   * @param dbSchemaDirParm dbschema installation directory
   */
  public void setDBSchemaDir(String dbSchemaDirParm) {
    dbSchemaDir = dbSchemaDirParm;
  }

  /**
   * set the Logger instance
   * @assumes nothing
   * @effects the internal reference to a Logger will be set
   * @param pLogger the Logger
   */
  public void setLogger(Logger pLogger) {
    logger = pLogger;
  }

  /**
   * changes the internal setting to control whether or not to create
   * future query results as scrollable cursers. By default this value is
   * false, and would need to be set to true to make query results
   * scrollable.
   * @param pBool
   */
  public void setScrollable(boolean pBool) {
    if (pBool == true)
      scrollable = ResultSet.TYPE_SCROLL_INSENSITIVE;
    else
      scrollable = ResultSet.TYPE_FORWARD_ONLY;
  }


  /**
   * set whether to use auto commit when executing sql on the current
   * connection or any future connections. The default is on.
   * @assumes nothing
   * @effects the internal value for the autoCommit will be set
   * @param pBool true if auto commit should be on, false otherwise
   * @throws DBException thrown if a JDBC exception is thrown from the
   * database
   */
  public void setAutoCommit(boolean pBool) throws DBException {
    autoCommit = pBool;
    if (conn != null) {
      try {
        conn.setAutoCommit(autoCommit);
      }
      catch (SQLException e) {

      }
    }
  }

  /**
   * get a DBSchema object for executing DDL
   * @assumes nothing
   * @effects nothing
   * @return new DBSchema object
   */
  public DBSchema getDBSchema() {
    return new DBSchema(this);
  }


  /**
   * get the server name that hosts the database. The name of the
   * configuration parameter is DBSERVER. The default value if not set is
   * DEV_MGI.
   * @assumes nothing
   * @effects nothing
   * @return server name
   */
  public String getServer() {
    return server;
  }

  /**
   * get the name of the database. The name of the configuration
   * @assumes nothing
   * @effects nothing
   * parameter is DBNAME. The default value if not set is mgd.
   * @return database name
   */
  public String getDatabase() {
    return database;
  }

  /**
   * get the login user name for the database connection. The name of the
   * configuration parameter is DBUSER. The default value if not set is
   * mgd_dbo.
   * @assumes nothing
   * @effects nothing
   * @return login user
   */
  public String getUser() {
    return user;
  }

  /**
   * get the login password for the database connection. The name of the
   * configuration parameter is DBPASSWORD. There is no default value for
   * this parameter. It is recommended to use DBPASSWORDFILE parameter
   * instead
   * @assumes nothing
   * @effects nothing
   * @return login password
   */
  public String getPassword() {
    return password;
  }

  /**
   * get the database url. The name of the configuration parameter is DBURL.
   * The default value if not set is rohan.informatics.jax.org:4100.
   * @assumes nothing
   * @effects nothing
   * @return database url
   */
  public String getUrl() {
    return url;
  }

  /**
   * get the name of a file that holds the login password for the
   * database connection. The name of the configuration
   * parameter is DBPASSWORDFILE. The default value if not set is
   * /usr/local/mgi/dbutils/mgidbutilities/.mgd_dbo_password
   * @assumes nothing
   * @effects nothing
   * @return password file name
   */
  public String getPasswordFile() {
    return passwordFile;
  }

  /**
   * get the directory name for the installation of the database schema
   * product.
   * The name of the configuration parameter is DBSCHEMADIR. The default
   * value if not set is
   * /usr/local/mgi/dbutils/mgidbutilities/mgd/mgddbschema.
   * @assumes nothing
   * @effects nothing
   * @return database schema product installation directory
   */
  public String getDBSchemaDir() {
    return dbSchemaDir;
  }

  /**
   * get the logger this instance is using
   * @assumes nothing
   * @effects nothing
   * @return the logger
   */
  public Logger getLogger() {
    return logger;
  }

  /**
   * gets the boolean value for whether future query results will be
   * created with scrollable cursers. By default this is false, but can
   * be changed by calling the setScollable() method.
   * @return whether the future query results will be created as scrollable
   */
  public boolean getScollable() {
    if (scrollable == ResultSet.TYPE_SCROLL_INSENSITIVE)
      return true;
    else
      return false;
  }

  /**
   * get the name of the ConnectionManager class this instance is using
   * @assumes nothing
   * @effects nothing
   */
  public String getConnectionManagerClass() {
    return connectionManagerClass;
  }


  /**
   * get the state of the auto commit indicator set through the
   * setAutoCommit() method.
   * @assumes nothing
   * @effects nothing
   * @return true if auto commit is on, false otherwise
   */
  public boolean getAutoCommit() {
    return autoCommit;
  }

  public BatchProcessor getBatchProcessor() throws DBException {
    Statement s = null;
    try {
      s = this.conn.createStatement();
    }
    catch (SQLException e) {
      throw this.getJDBCException("create a Statement object", e);
    }
    this.statements.add(s);
    return new BatchProcessor(s);
  }

  /**
   * commit the current transaction
   * @assumes nothing
   * @effects the current transaction will be commited and a new transaction
   * will be started
   * @throws DBException thrown if the connection is closed or if there is
   * a JDBC exception thrown from the database
   */
  public void commit() throws DBException {
    this.checkConnection("commit transaction");
    try {
      conn.commit();
    }
    catch (SQLException e) {
      throw this.getJDBCException("commit transaction", e);
    }
  }

  /**
   * rollback the current transaction
   * @assumes nothing
   * @effects the current transaction will be rollbacked and a new
   * transaction will be started
   * @throws DBException thrown if the connection is closed or if there is
   * a JDBC exception thrown from the database
   */
  public void rollback() throws DBException {
    this.checkConnection("rollback transaction");
    try {
      conn.rollback();
    }
    catch (SQLException e) {
      throw this.getJDBCException("rollback transaction", e);
    }
  }



  /**
   * close the database connection and JDBC resources. All results
   * previously obtained would become invalid.
   * @assumes nothing
   * @effects the connection and all Statement classes will be closed
   * @throws DBException thrown if the database connection could not be
   * closed
   */
  public void closeResources() throws DBException {

    if (statements != null) {
      for (Iterator it = statements.iterator(); it.hasNext(); ) {
        Statement s  = (Statement)it.next();
        try {
          s.close();
        }
        catch (SQLException e) {
          throw this.getJDBCException("close a Statement object", e);
        }
      }
      statements = null;
    }
    if (conn != null) {
      try {
        conn.close();
      }
      catch (SQLException e) {
        DBExceptionFactory eFactory = new DBExceptionFactory();
        DBException e2 = (DBException)
            eFactory.getException(CloseErr, e);
        throw e2;
      }
      conn = null;
    }

  }

  /**
   * reconnect to the database if you have previously run the
   * closeResources() method. This will have no effect if the connection
   * is already open.
   * @assumes nothing
   * @effects a new connection to the database will be established
   * @throws DBException thrown if there is a database access error
   */
  public void reconnect() throws DBException {
    if (conn == null) {
      connect();
    }
  }

  public SQLDataManager tempDB() throws ConfigException, DBException {
  	return new SQLDataManager(this.getServer(), "tempdb", this.getUser(),
  	                          this.getPasswordFile(), this.getUrl());
  }

  /**
   * execute the query statement
   * @assumes nothing
   * @effects a query will be executed against the database
   * @param sql the query statement
   * @return a ResultsNavigator for the query results
   * @throws DBException
   */
  public ResultsNavigator executeQuery(String sql) throws DBException {
    ResultSet rs = null;
    if (logger != null)
      logger.logDebug(sql);
    this.checkConnection("execute query");
    try {
      Statement statement = conn.createStatement(scrollable,
                                                 ResultSet.CONCUR_READ_ONLY);
      statements.add(statement);
      rs = statement.executeQuery(sql);
    }
    catch (SQLException e) {
      String msg = "execute query on the following sql string\n" + sql;
      throw this.getJDBCException(msg, e);
    }
    ResultsNavigator iterator = new ResultsNavigator(rs);
    return (ResultsNavigator)iterator;
  }

	/**
	 * execute the InterpretedQuery class
	 * @assumes nothing
	 * @effects a query will be executed against the database
	 * @param the InterpretedQuery class
	 * @return a ResultsNavigator for the query results with the
	 * InterpretedQuery object plugged into it as a RowDataInterpreter
	 * @throws DBException
	 */
	public ResultsNavigator executeQuery(InterpretedQuery query)
		throws DBException {
		String sql = query.getQuery();
		ResultsNavigator nav = executeQuery(sql);
		nav.setInterpreter(query);
		return nav;
	}

  /**
   * execute the update, delete or insert statement
   * @assumes nothing
   * @effects an sql statement will be executed against the database
   * @param sql the update, delete or insert statement
   * @return the number of rows updated, deleted or inserted.
   * @throws DBException thrown if there is a database error
   */
  public int executeUpdate(String sql) throws DBException {
    int results = 0;
    if (logger != null)
      logger.logDebug(sql);
    this.checkConnection("execute update");
    try {
      Statement statement = conn.createStatement();
      statements.add(statement);
      results = statement.executeUpdate(sql);
    }
    catch (SQLException e) {
      String msg = "execute update on the following sql string\n" + sql;
      throw this.getJDBCException(msg, e);
    }
    return results;
  }

  /**
   * execute the sql statement which may be a stored procedure which can
   * return multiple results
   * @assumes nothing
   * @effects an sql statement will be executed against the database
   * @param sql the query statement
   * @return a Vector of update counts and/or ResultsNavigators
   * @throws DBException
   */
  public MultipleResults execute(String sql) throws DBException {
    if (logger != null)
      logger.logDebug(sql);
    this.checkConnection("execute sql");
    try {
      Statement statement = conn.createStatement();
      // add statement to the global list for batch post statement closings
      statements.add(statement);
      // execute which may return multiple results
      boolean isResultSet = statement.execute(sql);
      return new MultipleResults(statement, isResultSet, sql);
    }
    catch (SQLException e) {
      String msg = "execute the following sql string\n" + sql;
      throw this.getJDBCException(msg, e);
    }
  }

  /**
   * executes a stored procedure which only provides a return value of
   * int and does not provide access to out parameters
   * @assumes nothing
   * @effects a stored procedure will be executed against the database
   * @param pSPCallString the stored procedure call string to execute
   * @return the return int value of the strored procedure
   * @throws DBException if the stored procedure raises an error or an
   * error occurs accessing the database
   */
  public int executeSimpleProc(String pSPCallString) throws DBException {
    int returnValue;
    try {
      this.checkConnection("execute a stored procedure");
      String sql = "{? = call " + pSPCallString + "}";
      CallableStatement cs = conn.prepareCall(sql);
      cs.registerOutParameter(1, java.sql.Types.INTEGER);
      cs.execute();
      returnValue = cs.getInt(1);
    }
    catch (SQLException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(StoredProcedureErr, e);
      e2.bind(pSPCallString);
      throw e2;
    }
    return returnValue;
  }


  /**
   * get the meta data for the Sybase DBMS.
   * @assumes nothing
   * @effects nothing
   * @return the meta data
   * @throws DBException
   */
  public DatabaseMetaData getMetaData() throws DBException {
    this.checkConnection("access meta data");
    try {
      return conn.getMetaData();
    }
    catch (SQLException e) {
      String msg = "get metadata from database " + database +
          " on server " + server;
      throw this.getJDBCException(msg, e);
    }
  }

  /**
   * return a ResultsNavigator of Table objects from the current connection
   * @effects nothing
   * @assumes nothing
   * @return a ResultsNavigator of Table objects
   * @throws DBException thrown if there is an error in accessing database
   */
  public ResultsNavigator getTables() throws DBException {
    DatabaseMetaData meta = this.getMetaData();
    ResultSet rs = null;
    try {
      rs = meta.getTables(null, null, "%", null);
    }
    catch (SQLException e) {
      String msg = "get table metadata from database " + database +
          " on server " + server;
      throw this.getJDBCException(msg, e);
    }
    ResultsNavigator nav = this.getResultsNavigator(rs);
    class Interpreter
        implements RowDataInterpreter {
      private SQLDataManager sqlMgr = null;
      public Interpreter(SQLDataManager pSqlMgr) {
        sqlMgr = pSqlMgr;
      }

      public Object interpret(RowReference row) throws DBException {
        return Table.getInstance(row.getString("TABLE_NAME"), sqlMgr);
      }
    }

    nav.setInterpreter(new Interpreter(this));
    return nav;
  }

  /**
   * get a BindableStatement for the given sql.
   * @assumes nothing
   * @effects nothing
   * @param sql the sql string which can be a query, update, insert or
   * delete
   * @return the BindableStatement object which is a wrapper class for the
   * JDBC PreaperdStatement class.
   * @throws DBException thrown if there is a database error
   */
  public BindableStatement getBindableStatement(String sql)
  throws DBException
  {
    this.checkConnection("obtain a new BindableStatement object");
    PreparedStatement p = null;
    try {
      p = conn.prepareStatement(sql, scrollable, ResultSet.CONCUR_READ_ONLY);
      statements.add(p);
    }
    catch (SQLException e) {
      String msg =
          "call prepareStatement method with the following sql string\n"
          + sql;
      throw this.getJDBCException(msg, e);
    }
    BindableStatement s = new BindableStatement(this, p, sql);
    return s;
  }

  /**
   * get a ScriptWriter
   * @assumes nothing
   * @effects a new ScriptWriter class will be created
   * @return the ScriptWriter
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   * @throws ScriptException is there is an error creating a new file
   */
  public ScriptWriter getScriptWriter()
      throws ConfigException, ScriptException
  {
    ScriptWriter writer = new ScriptWriter(this);
    writer.setLogger(this.logger);
    return writer;
  }

  /**
   * get a ScriptWriter
   * @assumes nothing
   * @effects a new ScriptWriter class will be created
   * @param config the ScriptWriter configuration object
   * @return the ScriptWriter
   * @throws ConfigException thrown if there is an error accessing the
   * configuration file
   * @throws ScriptException is there is an error creating a new file
   */
  public ScriptWriter getScriptWriter(ScriptWriterCfg config)
  throws ConfigException, ScriptException
  {
    ScriptWriter writer = new ScriptWriter(config, this);
    writer.setLogger(this.logger);
    return writer;
  }

  /**
   * return a RowDataIterator for the given ResultSet
   * @assumes nothing
   * @effects nothing
   * @param rs the ResultSet
   * @return a corresponding RowDataIterator
   * @throws DBException
   */
  protected ResultsNavigator getResultsNavigator(ResultSet rs)
      throws DBException {
    return new ResultsNavigator(rs);
  }


  /**
   * get the connection used by this instance
   * @assumes nothing
   * @effects nothing
   * @return the connection
   * @throws DBException if a new connection was required and an error
   * occured while trying to establish one
   */
  protected Connection getConnection() throws DBException {
    checkConnection("return Connection to caller");
    return conn;
  }


  /**
   * sets all the attributes from the base configuration which
   * include database, url, user, password or password filename
   * @assumes nothing
   * @effects internal values will be set
   * @param pConfig the configuration object
   * @throws ConfigException throws if there is a configuration error
   */
  private void configureBase(DatabaseConfigurator pConfig)
      throws ConfigException {
    server = pConfig.getServer();
    database = pConfig.getDatabase();
    url = pConfig.getUrl();
    user = pConfig.getUser();
    password = pConfig.getPassword();
    passwordFile = pConfig.getPasswordFile();
  }

  /**
   * sets all the attributes from the base configuration plus
   * the extended configuration which inludes dbSchema directory
   * and the ConnectionManager class name
   * @assumes nothing
   * @effects internal values will be set
   * @param pConfig the configuration object
   * @throws ConfigException throws if there is a configuration error
   */
  private void configureExtended(DatabaseCfg pConfig)
      throws ConfigException, DBException {
    configureBase(pConfig);
    dbSchemaDir = pConfig.getDBSchemaDir();
    connectionManagerClass = pConfig.getConnectionManagerClass();
    loggerFactoryClass = pConfig.getLoggerFactoryClass();
    if (loggerFactoryClass != null) {
      logger = instantiateLogger(loggerFactoryClass);
    }
  }


  /**
   * get a database connection
   * @assumes nothing
   * @effects a new connection will be made to the database
   * @throws DBException
   */
  private void connect() throws DBException {
    try {
      if (conn != null && !conn.isClosed())
        return;
    }
    catch (SQLException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(JDBCException, e);
      e2.bind("open database connection to " + database +
              " on server " + server);
      throw e2;
    }
    if (password == null) { // open password file and read password from it
      if (passwordFile != null) {
        try {
          BufferedReader reader =
              new BufferedReader(new FileReader(passwordFile));
          password = reader.readLine();
          reader.close();
        }
        catch (Exception e) {
          DBExceptionFactory eFactory = new DBExceptionFactory();
          DBException e2 = (DBException)
              eFactory.getException(PasswordNotRetrieved, e);
          e2.bind(passwordFile);
          throw e2;
        }
      }
    }
    try {
      if (connectionManager == null) {
        Class c = Class.forName(connectionManagerClass);
        connectionManager = (ConnectionManager) c.newInstance();
      }
      conn = connectionManager.getConnection(database, user, password, url);
    }
    catch (SQLException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(ConnectErr, e);
      throw e2;
    }
    catch (ClassNotFoundException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(ClassFornameErr, e);
      e2.bind(connectionManagerClass);
      throw e2;
    }
    catch (IllegalAccessException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(ClassFornameErr, e);
      e2.bind(connectionManagerClass);
      throw e2;
    }
    catch (InstantiationException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(ClassFornameErr, e);
      e2.bind(connectionManagerClass);
      throw e2;
    }
    try {
      conn.setAutoCommit(autoCommit);
    }
    catch (SQLException e) {
      throw this.getJDBCException("set auto commit", e);
    }
  }

  /**
   * throw a DBException if the database connection is closed. This method
   * is called by all methods that require database access
   * @assumes nothing
   * @effects a exception will raised if the connection is closed
   * @param msg the string naming the operation being performed
   * when calling this method. It will be used within the final
   * exception message.
   * @throws DBException thrown if the connection is closed
   */
  private void checkConnection(String msg) throws DBException {
    if (conn == null) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e = (DBException)
          eFactory.getException(ConnectionClosed);
      e.bind(msg);
      throw e;
    }
  }

  /**
   * get a DBException object defined by the name
   * ExceptionFactory.JDBCException
   * @assumes nothing
   * @effects nothing
   * @param pBind string to concatenate to exception message describing
   * what was being attempted when the JDBC Exception was thrown
   * @param pException the JDBC exception that was thrown
   * @return DBException the new exception with a completed message and
   * the parent exception set to the given exception
   */
  private DBException getJDBCException(String pBind, Exception pException) {
    DBExceptionFactory eFactory = new DBExceptionFactory();
    DBException e = (DBException)
        eFactory.getException(JDBCException, pException);
    e.bind(pBind);
    return e;
  }

  /**
   * instantiate a Logger instance from a class name
   * @param pClassName
   * @return
   */
  private Logger instantiateLogger(String pClassName)
      throws DBException {
    LoggerFactory factory = null;
    try {
      Class c = Class.forName(pClassName);
      factory = (LoggerFactory) c.newInstance();
    }
    catch (ClassNotFoundException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(ClassFornameErr, e);
      e2.bind(pClassName);
      throw e2;
    }
    catch (IllegalAccessException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(ClassFornameErr, e);
      e2.bind(pClassName);
      throw e2;
    }
    catch (InstantiationException e) {
      DBExceptionFactory eFactory = new DBExceptionFactory();
      DBException e2 = (DBException)
          eFactory.getException(ClassFornameErr, e);
      e2.bind(pClassName);
      throw e2;
    }

    Logger logger = factory.getLogger();
    return logger;
  }


}

// $Log$
// Revision 1.4  2003/12/09 22:49:09  mbw
// merged jsam branch onto the trunk
//
// Revision 1.3.2.26  2003/10/10 15:41:00  mbw
// added getScriptWriter method
//
// Revision 1.3.2.25  2003/10/02 15:23:16  mbw
// added a new TempDB method and fixed impact of changes to Table constructor
//
// Revision 1.3.2.24  2003/09/24 17:35:03  mbw
// added comments
//
// Revision 1.3.2.23  2003/09/24 14:10:30  mbw
// added new method getTables and executeQuery(InterpretedQuery)
//
// Revision 1.3.2.22  2003/08/28 20:58:47  mbw
// added new method getBatchProcessor
//
// Revision 1.3.2.21  2003/07/23 21:46:25  mbw
// made default value of scrollable variable to be false
//
// Revision 1.3.2.20  2003/07/23 21:40:09  mbw
// added capability for scrollable resultsets
//
// Revision 1.3.2.19  2003/06/25 17:32:13  mbw
// added a new constructor which accepts a DatabaseCfg object
//
// Revision 1.3.2.18  2003/06/17 20:29:09  mbw
// made the Logger and the ConnectManager classes that can be dynamically discovered and instantiated
//
// Revision 1.3.2.17  2003/06/04 18:29:57  mbw
// javadoc edits
//
// Revision 1.3.2.16  2003/06/04 14:51:50  mbw
// javadoc edits
//
// Revision 1.3.2.15  2003/06/03 21:59:35  mbw
// added support for transactions, stored procedures and the JDBC execute() method
//
// Revision 1.3.2.14  2003/05/22 16:15:54  mbw
// now uses DatabaseConfigurator interface in the constructor
//
// Revision 1.3.2.13  2003/05/22 15:54:30  mbw
// removed inner class Navigator to make into out class and added new execute() method
//
// Revision 1.3.2.12  2003/05/16 15:11:24  mbw
// fixed javadocs to be in sync with code
//
// Revision 1.3.2.11  2003/05/15 00:50:08  mbw
// impacted from changes to unitTest package
//
// Revision 1.3.2.10  2003/05/09 14:23:20  mbw
// added a protected getConnection method
//
// Revision 1.3.2.9  2003/05/08 01:56:15  mbw
// incorporated changes from code review
//
// Revision 1.3.2.8  2003/04/25 17:12:03  mbw
// updated to reflect changes in design of exception handling
//
// Revision 1.3.2.7  2003/04/15 12:04:41  mbw
// added extended error messages and edits to javadocs
//
// Revision 1.3.2.6  2003/04/09 21:22:22  mbw
// added new attributes and setter methods (attribute values were previously stored in DatabaseCfg)
//
// Revision 1.3.2.5  2003/03/21 16:53:03  mbw
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