package org.jax.mgi.shr.unitTest;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;

import org.jax.mgi.shr.dbutils.ConnectionManager;



/**
 * <p>IS: An object for creating database tables used in java unit
 * testing.</p>
 * <p>HAS: an database connection and methods for creating database tables.
 * A couple generic table definitions are provided. One is called DBTypes which
 * has a column for each data type expected to be encountered in an application.
 * The other is called TEST_DBgeneric and is meant to represent a generic table
 * encountered in applications which consist of the common fields createdBy,
 * modifiedBy, creation_date and modification_date. Additionally a method is
 * provided that will execute any drop table statement and create table
 * statement provided as parameters.</p>
 * <p>DOES: executes drop/create table commands for the purpose
 * of creating tables on the fly to support java unit testing programs.</p>
 * <p>Company: Jackson Laboratory</p>
 * @author M Walker
 * @version 1.0
 */

public class TableCreator {

  private Connection conn = null;

  private String DEFAULT_CONNECTION_MANAGER =
                "org.jax.mgi.shr.dbutils.MGIDriverManager";

  /**
   * default constructor
   * @throws KnownException
   */
  public TableCreator(String url, String database,
                      String user, String password)
      throws Exception {
    conn =
        getConnection(url, database, user, password, DEFAULT_CONNECTION_MANAGER);
  }


        /**
         * default constructor
         * @throws KnownException
         */
        public TableCreator(String url, String database,
                                                                                        String user, String password,
                                                                                        String connectionManagerClass)
                        throws Exception {
                conn =
                        getConnection(url, database, user, password, connectionManagerClass);
        }


  /**
   * creates a table that consisting of all expected data types with the
   * following definition:
   * <pre>
   * CREATE TABLE TEST_DBtypes
   * columnA     varchar(30)   not null
   * columnB     datetime      not null
   * columnC     int           not null
   * columnD     char(1)       null
   * columnE     text          null
   * columnF     float(8)      null
   * columnG     bit           not null
   * </pre>
   *
   * @throws KnownException
   */
  public void createDBtypes() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBtypes";
    String sqlCreate =
        "CREATE TABLE TEST_DBtypes ("
        + "columnA     varchar(30)   null,"
        + "columnB     datetime      null,"
        + "columnC     int           null,"
        + "columnD     char(1)       null,"
        + "columnE     text          null,"
        + "columnF     float(8)      null,"
        + "columnG     bit           not null)";
    createTable(sqlDrop, sqlCreate);
  }

  /**
   * drop the table TEST_DBtypes
   * @throws KnownException
   */
  public void dropDBtypes() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBTypes";
    dropTable(sqlDrop);
  }

        /**
         * creates a table with the following definition:
         * <pre>
         * CREATE TABLE TEST_DBsimple
         * columnA           int         not null
         * columnB           varchar(30) null
         * </pre>
         * @throws KnownException
         */
        public void createDBsimple() throws Exception {
                String sqlDrop = "DROP TABLE TEST_DBsimple";
                String sqlCreate =
                                "CREATE TABLE TEST_DBsimple ("
                                + "columnA           int         not null,"
                                + "columnB           varchar(30) null)";
                createTable(sqlDrop, sqlCreate);
        }


        /**
         * drop the table TEST_DBsimple
         * @throws KnownException
         */
        public void dropDBsimple() throws Exception {
                String sqlDrop = "DROP TABLE TEST_DBsimple";
                dropTable(sqlDrop);
        }

        /**
         * creates a table with a primary key column of type int and
         * with the following definition:
         * <pre>
         * CREATE TABLE TEST_DBkeyedInt
         * columnA           int         not null primary key
         * columnB           varchar(30) null
         * <br>
         * </pre>
         * @throws KnownException
         */
        public void createDBkeyedInt() throws Exception {
                String sqlDrop = "DROP TABLE TEST_DBkeyedInt";
                String sqlCreate =
                                "CREATE TABLE TEST_DBkeyedInt ("
                                + "columnA           int         primary key,"
                                + "columnB           varchar(30) null)";
                createTable(sqlDrop, sqlCreate);
        }


        /**
         * drop the table TEST_DBKeyedInt
         * @throws KnownException
         */
        public void dropDBkeyedInt() throws Exception {
                String sqlDrop = "DROP TABLE TEST_DBkeyedInt";
                dropTable(sqlDrop);
        }

  /**
   * creates a table which models original tables in MGD which have record
   * stamping fields createdBy, modifiedBy, creation_date
   * and modification_date
   * <pre>
   * CREATE TABLE TEST_DBstamped_MGDOrg
   * columnA           varchar(30) not null
   * columnB           datetime    not null
   * columnC           int         not null
   * columnD           float       null
   * createdBy         varchar(30) not null
   * modifiedBy        varchar(30) not null
   * creation_date     datetime    not null
   * modification_date datetime    not null
   * </pre>
   * @throws KnownException
   */
  public void createDBstamped_MGDOrg() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_MGDOrg";
    String sqlCreate =
        "CREATE TABLE TEST_DBstamped_MGDOrg ("
        + "columnA           varchar(30) not null,"
        + "columnB           datetime    not null,"
        + "columnC           int         not null,"
        + "columnD           float       null,"
        + "createdBy         varchar(30) not null,"
        + "modifiedBy        varchar(30) not null,"
        + "creation_date     datetime    not null,"
        + "modification_date datetime not null)";
    createTable(sqlDrop, sqlCreate);
  }

  /**
   * drop the table TEST_DBstamped_MGDOrg
   * @throws KnownException
   */
  public void dropDBstamped_MGDOrg() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_MGDOrg";
    dropTable(sqlDrop);
  }




  /**
   * creates a table which models tables in MGD which have record
   * stamping fields _createdBy_key, _modifiedBy_ky, creation_date
   * and modification_date
   * <pre>
   * CREATE TABLE TEST_DBstamped_MGD
   * columnA           int         not null
   * columnB           String      not null
   * _createdBy_key    int         not null
   * _modifiedBy_key   int         not null
   * creation_date     datetime    not null
   * modification_date datetime    not null
   * </pre>
   * @throws KnownException
   */
  public void createDBstamped_MGD() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_MGD";
    String sqlCreate =
        "CREATE TABLE TEST_DBstamped_MGD ("
        + "columnA           int         not null,"
        + "columnB           varchar(30) not null,"
        + "_createdBy_key    int         not null,"
        + "_modifiedBy_key   int         not null,"
        + "creation_date     datetime    not null,"
        + "modification_date datetime not null)";
    createTable(sqlDrop, sqlCreate);
  }

  /**
   * drop the table TEST_DBstamped_MGD
   * @throws KnownException
   */
  public void dropDBstamped_MGD() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_MGD";
    dropTable(sqlDrop);
  }

  /**
   * creates a table which models tables in RADAR which have record
   * stamping fields _jobstream_key and creation_date
   * <pre>
   * CREATE TABLE TEST_DBstamped_RADAR
   * columnA           int         not null
   * columnB           String      not null
   * _jobstream_key    int         not null
   * creation_date     datetime    not null
   * </pre>
   * @throws KnownException
   */
  public void createDBstamped_RADAR() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_RADAR";
    String sqlCreate =
        "CREATE TABLE TEST_DBstamped_RADAR ("
        + "columnA           int         not null,"
        + "columnB           varchar(30) not null,"
        + "_jobstream_key    int         not null,"
        + "creation_date     datetime    not null)";
    createTable(sqlDrop, sqlCreate);
  }

  /**
   * drop the table TEST_DBstamped_RADAR
   * @throws KnownException
   */
  public void dropDBstamped_RADAR() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_RADAR";
    dropTable(sqlDrop);
  }

  /**
   * creates a table which models tables in MGD which have record
   * stamping fields creation_date and modification_datye
   * <pre>
   * CREATE TABLE TEST_DBstamped_MGDDate
   * columnA           int         not null
   * columnB           String      not null
   * creation_date     datetime    not null
   * modification_date datetime    not null
   * </pre>
   */

  public void createDBstamped_MGDDate() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_MGDDate";
    String sqlCreate =
        "CREATE TABLE TEST_DBstamped_MGDDate ("
        + "columnA           int         not null,"
        + "columnB           varchar(30) not null,"
        + "creation_date     datetime    not null,"
        + "modification_date datetime    not null)";
    createTable(sqlDrop, sqlCreate);
  }

  /**
   * drop the table TEST_DBstamped_MGDDate
   * @throws KnownException
   */
  public void dropDBstamped_MGDDate() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_MGDDate";
    dropTable(sqlDrop);
  }

  /**
   * creates a table which models tables in MGD which have record
   * stamping fields creation_date, modification_date and
   * release_date
   * <pre>
   * CREATE TABLE TEST_DBstamped_MGDRelease
   * columnA           int         not null
   * columnB           String      not null
   * creation_date     datetime    not null
   * modification_date datetime    not null
   * release_date      datetime    not null
   * </pre>
   */

  public void createDBstamped_MGDRelease() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_MGDRelease";
    String sqlCreate =
        "CREATE TABLE TEST_DBstamped_MGDRelease ("
        + "columnA           int         not null,"
        + "columnB           varchar(30) not null,"
        + "creation_date     datetime    not null,"
        + "modification_date datetime    not null,"
        + "release_date      datetime    not null)";
    createTable(sqlDrop, sqlCreate);
  }

  /**
   * drop the table TEST_DBstamped_MGDRelease
   * @throws KnownException
   */
  public void dropDBstamped_MGDRelease() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBstamped_MGDRelease";
    dropTable(sqlDrop);
  }

  /**
   * creates a table with the following definition:
   * <pre>
   * CREATE TABLE TEST_DBkeyed
   * columnA           int         not null
   * columnB           varchar(30) null
   * </pre>
   * add applies a primary key definition to columnA
   * @throws KnownException
   */
  public void createDBkeyed() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBkeyed";
    String sqlCreate =
        "CREATE TABLE TEST_DBkeyed ("
        + "columnA           int         not null,"
        + "columnB           varchar(30) null)";
    createTable(sqlDrop, sqlCreate);
    Statement statement = conn.createStatement();
    statement.execute("sp_primarykey TEST_DBkeyed, columnA");
  }

  /**
   * drop the table TEST_DBkeyed
   * @throws KnownException
   */
  public void dropDBkeyed() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBkeyed";
    dropTable(sqlDrop);
  }

  /**
   * creates a table with the following definition:
   * <pre>
   * CREATE TABLE TEST_DBmultikeyed
   * columnA           int         not null
   * columnB           varchar(30) null
   * </pre>
   * add applies a primary key definition to columnA
   * @throws KnownException
   */
  public void createDBmultikeyed() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBmultikeyed";
    String sqlCreate =
        "CREATE TABLE TEST_DBmultikeyed ("
        + "columnA           int         not null,"
        + "columnB           varchar(30) null)";
    createTable(sqlDrop, sqlCreate);
    Statement statement = conn.createStatement();
    statement.execute("sp_primarykey TEST_DBmultikeyed, columnA, columnB");
  }

  /**
   * drop the table TEST_DBmultikeyed
   * @throws KnownException
   */
  public void dropDBmultikeyed() throws Exception {
    String sqlDrop = "DROP TABLE TEST_DBmultikeyed";
    dropTable(sqlDrop);
  }



  /**
   * execute the given drop table and create table commands. The drop command is
   * run in advance of create to compensate for the scenario where the table
   * already exists in the database.
   * @param drop drop table command
   * @param create create table command
   * @throws KnownException
   */
  private void createTable(String drop, String create) throws Exception {
    try {
      Statement statement = conn.createStatement();
      statement.executeUpdate(drop);
    }
    catch (Exception e) {
      // ignore this since probably triggered from the table not existing
    }
    Statement statement = conn.createStatement();
    statement.executeUpdate(create);
  }

  /**
   * will execute the given drop table command to be used when cleaning up at the
   * completion of a unit test run.
   * @param drop the drop table command
   * @throws KnownException
   */
  private void dropTable(String drop) throws Exception {
    Statement statement = conn.createStatement();
    statement.executeUpdate(drop);
  }

  private Connection getConnection(String url, String database,
                                   String user, String password,
                                   String connectionManagerClass)
    throws SQLException {
                ConnectionManager connectionManager = null;
                Connection conn = null;
                try {
                        Class c = Class.forName(connectionManagerClass);
                        connectionManager = (ConnectionManager) c.newInstance();
                        conn = connectionManager.getConnection(database, user, password, url);
                }
                catch (ClassNotFoundException e) {
                        throw new SQLException(e.getMessage());
                }
                catch (IllegalAccessException e) {
                        throw new SQLException(e.getMessage());
                }
                catch (InstantiationException e) {
                        throw new SQLException(e.getMessage());
                }
                return conn;
  }


}