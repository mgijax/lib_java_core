package org.jax.mgi.shr.dbutils;

import junit.framework.*;
import java.util.*;
import org.jax.mgi.shr.config.DatabaseCfg;

public class TestDBSchema
    extends TestCase {
  private DBSchema dBSchema = null;
  private DatabaseCfg dbConfig = null;
  private SQLDataManager sqlManager = null;

  public TestDBSchema(String name) {
    super(name);
  }

  protected void setUp() throws Exception {
    super.setUp();
    sqlManager = new SQLDataManager();
    sqlManager.setDBSchemaDir("/net/rohan/usr/local/mgi/dbutils/radar/radardbschema");
    dBSchema = sqlManager.getDBSchema();
  }

  protected void tearDown() throws Exception {
    dBSchema = null;
    dbConfig = null;
    super.tearDown();
  }


  public void testCreateIndexCommand() throws Exception {
    String filename = "/net/rohan/usr/local/mgi/dbutils/radar/radardbschema/index/MGI_CloneLoad_Clone_create.object";
    Vector returnVector = dBSchema.getCreateIndexCommands("MGI_CloneLoad_Clone");
    String expectedReturn = "create unique clustered index idx_Clone_key on MGI_CloneLoad_Clone (_Clone_key)";
    assertEquals(expectedReturn, returnVector.get(0));
    expectedReturn = "create nonclustered index idx_cloneName on MGI_CloneLoad_Clone (cloneName)";
    assertEquals(expectedReturn, returnVector.get(1));
    expectedReturn = "create nonclustered index idx_jnumID on MGI_CloneLoad_Clone (jnumID)";
    assertEquals(expectedReturn, returnVector.get(2));
  }

  public void testDropIndexCommand() throws Exception {
    String filename = "/net/rohan/usr/local/mgi/dbutils/radar/radardbschema/index/MGI_CloneLoad_Clone_drop.object";
    Vector returnVector = dBSchema.getDropIndexCommands("MGI_CloneLoad_Clone");
    String expectedReturn = "drop index MGI_CloneLoad_Clone.idx_Clone_key";
    assertEquals(expectedReturn, returnVector.get(0));
    expectedReturn = "drop index MGI_CloneLoad_Clone.idx_cloneName";
    assertEquals(expectedReturn, returnVector.get(1));
    expectedReturn = "drop index MGI_CloneLoad_Clone.idx_jnumID";
    assertEquals(expectedReturn, returnVector.get(2));
  }

  public void testCreateTableCommand() throws Exception {
    String filename = "/net/rohan/usr/local/mgi/dbutils/radar/radardbschema/table/MGI_CloneLoad_Clone_create.object";
    String actualReturn = dBSchema.getCreateTableCommand("MGI_CloneLoad_Clone");
    String expectedReturn = "create table MGI_CloneLoad_Clone ( _Clone_key                     int             not null, cloneName                      varchar(40)     not null, cloneLibrary                   varchar(255)    null, jnumID                         varchar(30)     not null, _JobStream_key                 int             not null, creation_date                  datetime        not null )";
    //System.out.println(expectedReturn);
    //System.out.println(actualReturn);
    assertEquals(expectedReturn, actualReturn);
  }

  public void testCreateTable() throws Exception {
    SQLDataManager sqlman = new SQLDataManager();
    String tablename = "MGI_CloneLoad_Accession";
    try {
      sqlman.executeUpdate("drop table " + tablename);
    }
    catch (DBException e) {} // ignored
    try {
      dBSchema.createTable(tablename);
      dBSchema.createIndexes(tablename);
      dBSchema.dropIndexes(tablename);
      dBSchema.createIndexes(tablename);
      dBSchema.dropIndexes(tablename);
      String sql = "INSERT INTO MGI_CloneLoad_Accession VALUES " +
          "(1, '2', 'logicaldb', 0, 1, '2003-07-04')";
      sqlman.executeUpdate(sql);
      dBSchema.truncateTable(tablename);
      int count = sqlman.executeUpdate("delete from " + tablename);
      assertEquals(count, 0);
      dBSchema.truncateLog();
      dBSchema.dropTable(tablename);
    }
    catch (Exception e) {
      assertTrue(false);
    }
    assertTrue(true);
  }




}
